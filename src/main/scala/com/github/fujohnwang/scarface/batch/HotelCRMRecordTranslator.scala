package com.github.fujohnwang.scarface.batch

import com.microsoft.sqlserver.jdbc.SQLServerDataSource
import org.springframework.jdbc.core.{RowCallbackHandler, JdbcTemplate}
import java.sql.ResultSet

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

    jt.query("select top 10 * from cdsgus", new RowCallbackHandler {
      def processRow(rs: ResultSet) {
        val name = rs.getString("Name")
        val cardNo = rs.getString("CardNo")
        val descriot = rs.getString("Descriot")
        val ctftp = rs.getString("CtfTp")
        val ctfid = rs.getString("CtfId")
        val gender = rs.getString("Gender")
        val birthday = rs.getString("Birthday")
        val address = rs.getString("Address")
        val zip = rs.getString("Zip")
        val dirty = rs.getString("Dirty")
        val district1 = rs.getString("District1")
        val district2 = rs.getString("District2")
        val district3 = rs.getString("District3")
        val district4 = rs.getString("District4")
        val district5 = rs.getString("District5")
        val district6 = rs.getString("District6")
        val firstName = rs.getString("FirstNm")
        val lastName = rs.getString("LastNm")
        val duty = rs.getString("Duty")
        val mobile = rs.getString("Mobile")
        val tel = rs.getString("Tel")
        val fex = rs.getString("Fax")
        val email = rs.getString("EMail")
        val nation = rs.getString("Nation")
        val taste = rs.getString("Taste")
        val edu = rs.getString("Education")
        val com = rs.getString("Company")
        val ctel = rs.getString("CTel")
        val caddress = rs.getString("CAddress")
        val czip = rs.getString("CZip")
        val family = rs.getString("Family")
        val version = rs.getString("Version")
        val id = rs.getString("id")
        println(s"$name\t$ctfid\t$address\t$zip\t$birthday\t$email\t$mobile\t$tel")
      }
    })
  }
}