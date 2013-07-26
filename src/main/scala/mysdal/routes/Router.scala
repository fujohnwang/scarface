package mysdal

import mysdal.routes.RouteRepository
import mysdal.routes.Route
import mysdal.routes.Shard

trait Router {
  def route(sqlmap: String, argument: Any): Option[Set[Shard]]
}


/**
 * SimpleRouter will NOT take care of load balance concern after routing.
 *
 * If users want to compose routing and load balancing together as per their database topology,
 * they can implement another totally-new Router or by reusing some logic in SimpleRouter.
 *
 * BUT, remember, if another Router is provided, transaction facility should be synchronized: either involve all of the data sources or use different PlatformTransactionManager as per each scenario.
 *
 */
class SimpleRouter(routeRepository: RouteRepository) extends Router {
  /**
   *
   * 整个路由规则的查找匹配将分四步走：
   * 1. 进行sqlmap级别的路由匹配，需要对路由表达式进行evaluate；
   * 2. 如果所有路由表达式都没有匹配，则看是否存在没有指定路由表达式的sqlmap级别的路由存在，如果有，则完成查找返回；否则，进入下一步；
   * 3. 进行namespace级别的路由查找，依然先进行表达式匹配查找，如果匹配到，则返回；否则，进入第四步；
   * 4. 查找是否存在没有指定表达式的namespace级别的路由，如果有，则返回，否则，抛出异常或者返回None.
   */
  def route(sqlmap: String, argument: Any): Option[Set[Shard]] = {
    val shardSet = matchRouteSetSequentially(sqlmap, argument, Seq(routeRepository.sqlmapExprRoutes, routeRepository.sqlmapRoutes))
    if (!shardSet.isEmpty) shardSet else matchRouteSetSequentially(sqlmap.substring(0, sqlmap.lastIndexOf(".")), argument, Seq(routeRepository.namespaceExprRoutes, routeRepository.namespaceRoutes))
  }

  def matchRouteSetSequentially(sqlmap: String, argument: Any, routes: Seq[Map[String, Set[Route]]]): Option[Set[Shard]] = {
    routes.foreach {
      routeCtx => {
        val routeOption = matchRoute(sqlmap, argument, routeCtx)
        if (!routeOption.isDefined) return routeOption.map(_.shards)
      }
    }
    None
  }

  def matchRoute(path: String, argument: Any, routes: Map[String, Set[Route]]): Option[Route] = {
    if (routes == null || routes.isEmpty) return None
    if (!routes.contains(path)) return None
    routes.get(path).get.find(route => {
      if (!(route.expression == null || route.expressionEngine == null || null == argument)) route.expressionEngine.eval(path, argument) else false
    })
  }
}





