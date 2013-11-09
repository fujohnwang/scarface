package com.github.fujohnwang.scarface.batch

import com.microsoft.sqlserver.jdbc.SQLServerDataSource
import org.springframework.jdbc.core.{RowCallbackHandler, JdbcTemplate}
import java.sql.ResultSet

object KaiFangRecordTranslator {
  def main(args: Array[String]) {
//    println(System.getProperty("java.library.path"))
//    System.loadLibrary("sqljdbc_auth.dll")

    val dataSource = new SQLServerDataSource
    dataSource.setServerName("10.72.87.96")
    dataSource.setUser("afoo.me")
    dataSource.setPassword("111111")
    dataSource.setDatabaseName("shifenzheng")
    // this need to load dll, so disable it since I don't care security issue under LAN
//    dataSource.setIntegratedSecurity(true)


    val jt = new JdbcTemplate(dataSource)

    jt.query("select top 10 * from cdsgus", new RowCallbackHandler {
      def processRow(rs: ResultSet) {
        println(rs.getString("Name"))
      }
    })
  }
}