package mysdal

import mysdal.routes.{Route, RouteRepository, Shard}

trait Router {
  def route(sqlmap: String, argument: AnyRef): Option[Set[Shard]]
}


class SimpleRouter(routeRepository: RouteRepository) extends Router {
  /**
   *
   * 整个路由规则的查找匹配将分四步走：
   * 1. 进行sqlmap级别的路由匹配，需要对路由表达式进行evaluate；
   * 2. 如果所有路由表达式都没有匹配，则看是否存在没有指定路由表达式的sqlmap级别的路由存在，如果有，则完成查找返回；否则，进入下一步；
   * 3. 进行namespace级别的路由查找，依然先进行表达式匹配查找，如果匹配到，则返回；否则，进入第四步；
   * 4. 查找是否存在没有指定表达式的namespace级别的路由，如果有，则返回，否则，抛出异常或者返回None.
   */
  def route(sqlmap: String, argument: AnyRef): Option[Set[Shard]] = {
    Seq(routeRepository.sqlmapExprRoutes, routeRepository.sqlmapRoutes, routeRepository.namespaceExprRoutes, routeRepository.namespaceRoutes).foreach(routeCtx => {
      val routeOption = matchRoute(sqlmap, argument, routeCtx)
      if (!routeOption.isDefined) return routeOption.map(_.shards)
    })
    None
  }

  def matchRoute(path: String, argument: AnyRef, routes: Map[String, Set[Route]]): Option[Route] = {
    if (routes == null || routes.isEmpty) return None
    if (!routes.contains(path)) return None
    routes.get(path).get.find(route => {
      if (!(route.expression == null || route.expressionEngine == null)) route.expressionEngine.eval(path, argument) else false
    })
  }
}





