package com.github.fujohnwang.scarface.fastjson
//
//import com.alibaba.fastjson.JSON
//
//case class Group(id: Int = 0, name: String = "")
//
//object FastJsonDemo {
//  def main(args: Array[String]) {
//    val group = new Group(111, "genius")
//
//    val jsonString = JSON.toJSONString(group, Nil: _*)
//    println(jsonString)
//
//    val g = JSON.parseObject(jsonString, classOf[Group])
//    println(s"group: id=${g.id}, name=${g.name}")
//
//  }
//}