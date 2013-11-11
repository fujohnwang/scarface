package com.github.fujohnwang.scarface.batch

import com.microsoft.sqlserver.jdbc.SQLServerDataSource
import org.springframework.jdbc.core.{RowCallbackHandler, JdbcTemplate}
import java.sql.ResultSet
import org.elasticsearch.node.NodeBuilder._
import java.util.{HashMap => JMap}

object HotelCRMRecordTranslator {
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
    val node = nodeBuilder().clusterName("afoosearch").client(true).node()
    val client = node.client()
    try {
      jt.query("select * from cdsgus", new RowCallbackHandler {
        def processRow(rs: ResultSet) {
          val map = new JMap[String, Object]

          map.put("Name", rs.getString("Name"))
          map.put("CardNo", rs.getString("CardNo"))
          map.put("Descriot", rs.getString("Descriot"))
          map.put("CtfTp", rs.getString("CtfTp"))
          map.put("CtfId", rs.getString("CtfId"))
          map.put("Gender", rs.getString("Gender"))
          map.put("Birthday", rs.getString("Birthday"))
          map.put("Address", rs.getString("Address"))
          map.put("Zip", rs.getString("Zip"))
          map.put("Dirty", rs.getString("Dirty"))
          map.put("District1", rs.getString("District1"))
          map.put("District2", rs.getString("District2"))
          map.put("District3", rs.getString("District3"))
          map.put("District4", rs.getString("District4"))
          map.put("District5", rs.getString("District5"))
          map.put("District6", rs.getString("District6"))
          map.put("FirstNm", rs.getString("FirstNm"))
          map.put("LastNm", rs.getString("LastNm"))
          map.put("Duty", rs.getString("Duty"))
          map.put("Mobile", rs.getString("Mobile"))
          map.put("Tel", rs.getString("Tel"))
          map.put("Fax", rs.getString("Fax"))
          map.put("EMail", rs.getString("EMail"))
          map.put("Nation", rs.getString("Nation"))
          map.put("Taste", rs.getString("Taste"))
          map.put("Education", rs.getString("Education"))
          map.put("Company", rs.getString("Company"))
          map.put("CTel", rs.getString("CTel"))
          map.put("CAddress", rs.getString("CAddress"))
          map.put("CZip", rs.getString("CZip"))
          map.put("Family", rs.getString("Family"))
          map.put("Version", rs.getString("Version"))
          map.put("id", rs.getString("id"))

          val response = client.prepareIndex("customers", "customer").setSource(map).execute().actionGet()
          println(s"${response.getId} - ${response.getIndex} - ${response.getType} - ${response.getVersion}")
        }
      })
    } finally {
      node.close()
    }

  }
}