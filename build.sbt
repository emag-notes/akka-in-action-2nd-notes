val ScalaVersion = "2.13.10"
val AkkaVersion = "2.7.0"
val LogbackVersion = "1.4.5"
val ScalaTest = "3.2.15"

lazy val chapter02 = project
  .in(file("chapter02"))
  .settings(
    scalaVersion := ScalaVersion,
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
        "ch.qos.logback" % "logback-classic" % LogbackVersion))

lazy val chapter03 = project
  .in(file("chapter03"))
  .settings(
    scalaVersion := ScalaVersion,
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion))