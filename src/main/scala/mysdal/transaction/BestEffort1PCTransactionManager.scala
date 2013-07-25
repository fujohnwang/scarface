package mysdal.transaction

import org.springframework.transaction.support.{DefaultTransactionStatus, AbstractPlatformTransactionManager}
import org.springframework.transaction.{TransactionException, TransactionStatus, TransactionDefinition}
import mysdal.routes.Shard
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import scala.collection.mutable.ListBuffer

class BestEffort1PCTransactionManager(shards: Seq[Shard]) extends AbstractPlatformTransactionManager {

  val transactionManagers: Seq[AbstractPlatformTransactionManager] = shards.map(_.dataSource).map(new DataSourceTransactionManager(_))

  def doGetTransaction(): AnyRef = new ListBuffer[DefaultTransactionStatus]

  def doBegin(transaction: Any, definition: TransactionDefinition) {
    transactionManagers.foreach(txManager => transaction.asInstanceOf[ListBuffer[TransactionStatus]].append(txManager.getTransaction(definition)))
  }

  def doCommit(status: DefaultTransactionStatus) {
    val transactionExceptions = transactionManagers.zip(status.getTransaction.asInstanceOf[ListBuffer[TransactionStatus]]).foldRight(Seq[TransactionException]()) {
      (tuple, exceptions) => try {
        tuple._1.commit(tuple._2); exceptions
      } catch {
        case ex: TransactionException => exceptions :+ ex
      }
    }
    if (!transactionExceptions.isEmpty) throw transactionExceptions.head
  }

  def doRollback(status: DefaultTransactionStatus) {
    val transactionExceptions = transactionManagers.zip(status.getTransaction.asInstanceOf[ListBuffer[TransactionStatus]]).foldRight(Seq[TransactionException]()) {
      (t, exceptions) => try {
        t._1.rollback(t._2); exceptions
      } catch {
        case ex: TransactionException => exceptions :+ ex
      }
    }
    if (!transactionExceptions.isEmpty) throw transactionExceptions.head
  }
}