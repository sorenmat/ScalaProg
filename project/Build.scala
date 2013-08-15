import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "ScalaBlogEngine"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.mongodb" % "mongo-java-driver" % "2.11.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.2.2",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.2",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:experimental.macros"),
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

}
