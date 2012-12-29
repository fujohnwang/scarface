name := "scarface"

organization := "com.github.fujohnwang"

version := "0.0.1-SNAPSHOT"

publishMavenStyle := true

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

scalacOptions := Seq("-deprecation", "-unchecked","-optimise")

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalaVersion := "2.10.0-RC3"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Akka.IO Repo" at "http://akka.io/repository"

resolvers += "Local Maven Repo" at "file://"+Path.userHome+"/.m2/repository"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.0.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.0"

libraryDependencies += "org.slf4j" % "jcl-over-slf4j" %"1.6.2"

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" %"1.6.2"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.1.0"

libraryDependencies += "com.typesafe.akka" % "akka-agent_2.10" % "2.1.0"

libraryDependencies += "org.springframework" % "spring-context" % "3.1.3.RELEASE"

libraryDependencies += "com.yammer.metrics" % "metrics-core" % "2.1.1"

libraryDependencies +=  "org.scalatest" % "scalatest_2.10.0-RC3" % "1.8-B1" % "test"
