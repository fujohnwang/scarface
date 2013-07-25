package mysdal.routes

trait LoadBalanceStrategy {
  def loadBalanceAmong(replicas: Set[Replica]): Option[Replica]
}

class AlwaysFirstReplicaLoadBalanceStrategy extends LoadBalanceStrategy {
  def loadBalanceAmong(replicas: Set[Replica]): Option[Replica] = if (replicas == null || replicas.isEmpty) None else replicas.headOption
}




