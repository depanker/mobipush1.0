name := """mobipush"""

version := "1.0"

//lazy val root = (project in file(".")).enabqlePlugins(PlayJava, PlayScala)
//lazy val root = (project in file(".")).enabqlePlugins(PlayScala)


enablePlugins(JavaAppPackaging)

scalaVersion := "2.11.6"



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.akka" % "akka-remote_2.11" % "2.4.0")


libraryDependencies += "net.ruippeixotog" % "scala-scraper_2.11" % "0.1.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.8.0"

libraryDependencies += "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.3"

resolvers += "amateras-repo" at "http://amateras.sourceforge.jp/mvn/"

libraryDependencies += "jp.sf.amateras.solr.scala" %% "solr-scala-client" % "0.0.12"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
libraryDependencies += "com.caucho" % "quercus" % "4.0.45"



