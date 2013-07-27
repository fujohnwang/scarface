package mysdal

import org.springframework.orm.ibatis.{SqlMapClientCallback, SqlMapClientTemplate}
import java.util
import com.ibatis.sqlmap.client.event.RowHandler
import com.ibatis.sqlmap.client.SqlMapClient
import java.util.concurrent.{TimeUnit, Callable, ExecutorService}
import mysdal.routes.Shard
import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.Connection
import javax.sql.DataSource
import scala.beans.BeanProperty
import org.slf4j.LoggerFactory
import scala.util.Try
import scala.collection.mutable.ListBuffer

/**
 * ThreadPool for global scope, and tune as per situation. it seems ThreadPool-per-DataSource is not necessary.
 *
 * CAUTION:
 * 1. how to propagate the transaction synchronizations to executor threads?!
 * -In old days, I just get the connection for current transaction and pass it to another thread for execution, but if I want to reuse SqlmapClientTemplate's facility,
 * I have to find another way.
 *
 * 2. how to reflect the transaction status code properly?
 * - ICBU colleagues ever submit a bug on this concern, I should recall what it was and fix it this time!!!
 * - TransactionSynchronization.UNKNOWN
 *
 *
 * @param router
 */
class MysdalSqlmapClientTemplate(router: Router, shards: Set[Shard], executor: ExecutorService) extends SqlMapClientTemplate {


  @BeanProperty
  var timeout: Int = 5 * 1000

  override def afterPropertiesSet() {
    super.afterPropertiesSet()
    if (router == null) throw new IllegalStateException("A router must be provided to do distributed data access")
    if (shards == null || shards.isEmpty) throw new IllegalArgumentException("At least one shard should be provided.")
    if (executor == null) throw new IllegalArgumentException("U have to assigned a ExecutorService as per your scenario.")
  }

  override def queryForObject(statementName: String): AnyRef = this.queryForObject(statementName, null)

  override def queryForObject(statementName: String, parameterObject: Any): AnyRef = {
    super.queryForObject(statementName, parameterObject)
  }

  override def queryForObject(statementName: String, parameterObject: Any, resultObject: Any): AnyRef = super.queryForObject(statementName, parameterObject, resultObject)

  override def queryForList(statementName: String): util.List[_] = super.queryForList(statementName)

  override def queryForList(statementName: String, parameterObject: Any): util.List[_] = super.queryForList(statementName, parameterObject)

  override def queryForList(statementName: String, skipResults: Int, maxResults: Int): util.List[_] = super.queryForList(statementName, skipResults, maxResults)

  override def queryForList(statementName: String, parameterObject: Any, skipResults: Int, maxResults: Int): util.List[_] = super.queryForList(statementName, parameterObject, skipResults, maxResults)

  override def queryWithRowHandler(statementName: String, rowHandler: RowHandler) {
    super.queryWithRowHandler(statementName, rowHandler)
  }

  override def queryWithRowHandler(statementName: String, parameterObject: Any, rowHandler: RowHandler) {
    super.queryWithRowHandler(statementName, parameterObject, rowHandler)
  }

  override def queryForMap(statementName: String, parameterObject: Any, keyProperty: String): util.Map[_, _] = super.queryForMap(statementName, parameterObject, keyProperty)

  override def queryForMap(statementName: String, parameterObject: Any, keyProperty: String, valueProperty: String): util.Map[_, _] = super.queryForMap(statementName, parameterObject, keyProperty, valueProperty)

  override def insert(statementName: String): AnyRef = super.insert(statementName)

  override def insert(statementName: String, parameterObject: Any): AnyRef = super.insert(statementName, parameterObject)

  override def update(statementName: String): Int = super.update(statementName)

  override def update(statementName: String, parameterObject: Any): Int = super.update(statementName, parameterObject)

  override def update(statementName: String, parameterObject: Any, requiredRowsAffected: Int) {
    super.update(statementName, parameterObject, requiredRowsAffected)
  }

  override def delete(statementName: String): Int = super.delete(statementName)

  override def delete(statementName: String, parameterObject: Any): Int = super.delete(statementName, parameterObject)

  override def delete(statementName: String, parameterObject: Any, requiredRowsAffected: Int) {
    super.delete(statementName, parameterObject, requiredRowsAffected)
  }

}

object MysdalSqlmapClientTemplate {

  val logger = LoggerFactory.getLogger(classOf[MysdalSqlmapClientTemplate])

  val cleanUpContinuation: (Connection, DataSource) => Unit = (connection, dataSource) => DataSourceUtils.releaseConnection(connection, dataSource)

  /**
   * Several things should be taken care of:
   * 1. the connection should be pre-fetched in transaction thread and be closed in transaction thread too.
   * 2. SQLException will be thrown out to caller, so the SQLException-to-Spring-DataAccessException translation should be taken care of by caller.
   */
  def asynClosure[T](connection: Connection, sqlmapClient: SqlMapClient, callback: SqlMapClientCallback[T]): T = {
    val session = sqlmapClient.openSession()
    try {
      session.setUserConnection(connection)
      callback.doInSqlMapClient(session)
    } finally {
      session.close()
    }
  }

  def executeConcurrently[T](shards: Set[Shard], executor: ExecutorService, sqlmapClient: SqlMapClient, callback: SqlMapClientCallback[T], timeout: Int = 5 * 1000): Seq[T] = {
    val results = ListBuffer[T]()
    val exceptions = ListBuffer[Throwable]()

    for {
      node <- shards.map(shard => Try {
        val connection = DataSourceUtils.getConnection(shard.dataSource)
        val future = executor.submit(new Callable[T]() {
          def call(): T = asynClosure(connection, sqlmapClient, callback)
        })
        (connection, shard.dataSource, future)
      }).map(t => t.flatMap(arg => Try {
        try {
          arg._3.get(timeout, TimeUnit.MILLISECONDS)
        } finally {
          DataSourceUtils.releaseConnection(arg._1, arg._2)
        }
      }))
    } {
      if (node.isSuccess) results.append(node.get) else exceptions.append(node.failed.get)
    }

    if (!exceptions.isEmpty) throw new Exception(exceptions.map(_.getStackTraceString).mkString("\r\n"))

    results
  }

}