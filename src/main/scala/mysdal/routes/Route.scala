package mysdal.routes

import java.util

case class Route(path: String, expression: String, shards: Set[Shard], loadBalanceStrategy: LoadBalanceStrategy = new AlwaysFirstReplicaLoadBalanceStrategy, expressionEngine: ExpressionEngine = new MVELExpressionEngine(new util.HashMap[String, AnyRef]()))

case class RouteRepository(namespaceRoutes: Map[String, Set[Route]], namespaceExprRoutes: Map[String, Set[Route]], sqlmapRoutes: Map[String, Set[Route]], sqlmapExprRoutes: Map[String, Set[Route]])

/**
 * Bag Data Structure is better for such scenario.
 *
 * namespace和sqlmap作为匹配用的key是没有问题的，但是， 参数其实是变动很频繁的东西，所以，把参数作为hash的因素考虑进快速查找其实是不合适的。
 *
 * 所以，如果要做hash的话，同一个key下，可能需要挂多个values。
 *
 * 在RouteRepository初始化的时候，需要做一个类似于indexing的工作，将结构化不强的route集合， 组织成一个层次化的结构。
 *
 * 这同时要求，表达式的evaluation性能最好是比较强悍一些，因为需要对同一个key匹配下的动态路由进行O(n)的匹配运算。
 *
 * 在做route便利的时候，需要顺道将默认的route提取出来，当所有表达式evaluation完成后依然没有找到对应的route的时候，需要使用到fallback用的默认route。
 *
 */
trait RouteRepositoryBuilder {
  def build(): RouteRepository
}



