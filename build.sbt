ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "2.13.9"

lazy val artifacts = new {
  // artifacts version
  val akkaVersion      = "2.6.20"
  val akkaHttpVersion  = "10.2.9"
  val logbackVersion   = "1.4.7"
  val scalaTestVersion = "3.2.15"

  //artifacts

  val akkaTyped = Seq("com.typesafe.akka" %% "akka-actor-typed" % akkaVersion)

  val akkaClassic = Seq("com.typesafe.akka" %% "akka-actor" % akkaVersion)

  val akkaTest = Seq("com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test)

  val scalaLogging = Seq("ch.qos.logback" % "logback-classic" % logbackVersion)

  val scalaTest = Seq("org.scalatest" %% "scalatest" % scalaTestVersion % Test)
}

lazy val sharedSettings = Seq(
  organization := "com.akkaInAction",
  scalacOptions ++= Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-unused:imports,patvars,privates,locals,explicits,implicits"
  ),
  libraryDependencies ++= artifacts.akkaTyped ++ artifacts.akkaTest ++ artifacts.scalaTest
)

lazy val chapter02 = project
  .in(file("chapter02"))
  .settings(sharedSettings, libraryDependencies ++= artifacts.akkaClassic)
