name := """booktopia-api"""
organization := "it.renny"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.7.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "it.renny.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "it.renny.binders._"

val slickVersion = "3.6.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.2.11",
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.2.11",

  "com.typesafe.slick" %% "slick"         % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,

  "org.playframework" %% "play-slick" % "6.2.0",
  "org.playframework" %% "play-slick-evolutions" % "6.2.0",
  "com.typesafe.play" %% "play-json" % "2.10.6",

  "org.postgresql" % "postgresql" % "42.7.7",
  "org.mindrot" % "jbcrypt" % "0.4",

  "software.amazon.awssdk" % "s3" % "2.31.73",

  "com.github.jwt-scala" %% "jwt-core" % "11.0.0",
  "com.github.jwt-scala" %% "jwt-play-json" % "11.0.0"
)
