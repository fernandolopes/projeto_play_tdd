name := """server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test" )
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.6"
libraryDependencies += specs2 % Test
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.9"