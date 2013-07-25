package mysdal.routes

import javax.sql.DataSource

/**
 * under one logical shard, it can have multiple replicas;
 */
case class Shard(id: String, dataSource: DataSource, replicas: Set[DataSource], description: String = "")


