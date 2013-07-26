package mysdal

import org.springframework.orm.ibatis.{SqlMapClientCallback, SqlMapClientTemplate}
import java.util
import com.ibatis.sqlmap.client.event.RowHandler
import com.ibatis.sqlmap.client.SqlMapExecutor
import java.util.concurrent.{Callable, ForkJoinPool, ExecutorService}
import scala.beans.BeanProperty
import mysdal.routes.Shard

/**
 * for query, we will select route first, and then scatter the query to selected data sources in parallel and join when all the query is done or timeout/exception;
 * for insert, there should be one destination data source for it, so just execute it in current thread;
 * for update, see insert;
 * for delete, see insert;
 *
 * ThreadPool for global scope, and tune as per situation. it seems ThreadPool-per-DataSource is not necessary.
 *
 * @param router
 */
class MysdalSqlmapClientTemplate(router: Router, shards: Set[Shard]) extends SqlMapClientTemplate {
  @BeanProperty
  var executor: ExecutorService = new ForkJoinPool(shards.size * 5)

  override def queryForObject(statementName: String): AnyRef = this.queryForObject(statementName, null)

  override def queryForObject(statementName: String, parameterObject: Any): AnyRef = {
    val callback = new SqlMapClientCallback[Any]() {
      def doInSqlMapClient(executor: SqlMapExecutor): Any = executor.queryForObject(statementName, parameterObject)
    }

    router.route(statementName, parameterObject) match {
      case None => super.queryForObject(statementName, parameterObject)
      case Some(shards) => {
        shards.foreach(shard => executor.submit(new Callable[AnyRef] {
          def call(): AnyRef = {
            ???
          }
        }))
        null
      }
    }

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