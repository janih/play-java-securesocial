name := "play-java-securesocial"

version := "1.0-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file(".")).enablePlugins(PlayJava).enablePlugins(SbtWeb)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    javaCore,
    "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
    "org.webjars" % "bootstrap" % "3.2.0"
)

// for less when it will work
includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"
