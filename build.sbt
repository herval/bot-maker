name := "slack_relay_bot"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-core" % "17.12.0",
  "com.twitter" %% "finagle-http" % "17.12.0"
)
