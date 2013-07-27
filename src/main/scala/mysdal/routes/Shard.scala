package mysdal.routes

import javax.sql.DataSource

/**
 * under one logical shard, it can have multiple replicas;
 *
 * but the replica feature will NOT be opened for the time being, as I don't know whether or not it's proper to do LB in our DAL.
 * most of the time, in production, VIP and replication structures will be setup separately.
 */
case class Shard(id: String, dataSource: DataSource, replicas: Set[DataSource], description: String = "")


