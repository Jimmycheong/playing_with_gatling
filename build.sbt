enablePlugins(GatlingPlugin)

name := "playing_with_gatling"

version := "0.1"

scalaVersion := "2.12.7"

val gatlingVersion = "3.0.0"

libraryDependencies ++= Seq() ++gatlingDeps ++ akkaDeps

val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11"
)

val gatlingDeps = Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test",
  "io.gatling" % "gatling-test-framework" % gatlingVersion % "test"
)
