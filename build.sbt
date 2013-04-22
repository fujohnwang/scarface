name := "scarface"

organization := "com.github.fujohnwang"

version := "0.0.1-SNAPSHOT"

publishMavenStyle := true

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

scalacOptions := Seq("-deprecation", "-unchecked","-optimise")

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalaVersion := "2.10.0"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Akka.IO Repo" at "http://akka.io/repository"

resolvers += "Local Maven Repo" at "file://"+Path.userHome+"/.m2/repository"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.0.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.0"

libraryDependencies += "org.slf4j" % "jcl-over-slf4j" %"1.6.2"

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" %"1.6.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"

libraryDependencies += "org.springframework" % "spring-context" % "3.2.0.RELEASE"

libraryDependencies += "com.yammer.metrics" % "metrics-core" % "2.1.1"

libraryDependencies += "io.netty" % "netty" % "3.6.3.Final"

libraryDependencies +=  "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "com.tristanhunt" %% "knockoff" % "0.8.1"

libraryDependencies += "com.google.guava" % "guava" % "14.0"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

libraryDependencies += "org.parboiled" %% "parboiled-scala" % "1.1.4"

libraryDependencies += "com.ning" % "async-http-client" % "1.7.11"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "2.3.1.201302201838-r"

net.virtualvoid.sbt.graph.Plugin.graphSettings

