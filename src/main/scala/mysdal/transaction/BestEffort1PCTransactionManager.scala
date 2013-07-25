package mysdal.transaction

import org.springframework.transaction.support.{DefaultTransactionStatus, AbstractPlatformTransactionManager}
import org.springframework.transaction.TransactionDefinition

class BestEffort1PCTransactionManager extends AbstractPlatformTransactionManager {
  def doGetTransaction(): AnyRef = ???

  def doBegin(transaction: Any, definition: TransactionDefinition) {}

  def doCommit(status: DefaultTransactionStatus) {}

  def doRollback(status: DefaultTransactionStatus) {}
}