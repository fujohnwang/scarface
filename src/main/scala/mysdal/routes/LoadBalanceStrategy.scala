package mysdal.routes

import javax.sql.DataSource

trait LoadBalanceStrategy {
  def loadBalanceAmong(replicas: Set[DataSource]): Option[DataSource]
}

class AlwaysFirstReplicaLoadBalanceStrategy extends LoadBalanceStrategy {
  def loadBalanceAmong(replicas: Set[DataSource]): Option[DataSource] = if (replicas == null || replicas.isEmpty) None else replicas.headOption
}




