name := "ConsoleAkka"

version := "0.1"

scalaVersion := "2.11.12"

//resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.16",
  "org.scala-lang" % "scala-actors" % "2.11.12"
)
