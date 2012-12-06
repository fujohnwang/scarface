package com.github.fujohnwang.scarface

import concurrent.Future
import beans.BeanProperty

case class Order(id: Long, price: Double, amount: Int)

case class MarketData()

case class OrderExecution(originalOrderId: Long)

case class AccountInfo(accountId: Long)


trait Gateway {
  @BeanProperty
  var marketDataConsumer: MarketData => Unit = _

  // TODO order placement interfaces

}


trait ForexService {

  def getSpotMarketDataOf(instrumentId: Long): Future[Option[MarketData]]

  def getAccountInfoOf(accountId: Long): Future[Option[AccountInfo]]

  def placeOrder(order: Order): Future[Seq[Long]]

  def placeClosingOrder(closeOrder: Order): Future[Long]

  def getOrderExecutionsOf(orderId: Long): Future[Seq[OrderExecution]]
}