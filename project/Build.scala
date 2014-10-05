import sbt._
import Keys._

object build extends Build {

  val projectName = "playground-scala-selenium"

  lazy val baseSettings = Seq(
    name := projectName,
    organization := "me.guymer",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.2",
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature"),
    conflictManager := ConflictManager.strict,

    parallelExecution in Test := true
  )

  object Version {
    val selenium = "2.43.1"
  }

  lazy val project = Project(projectName, file("."))
    .settings(baseSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.2" % "test",
        "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0" % "test"
          exclude ("org.seleniumhq.selenium", "selenium-java")
          exclude ("org.seleniumhq.selenium", "selenium-remote-driver")
          exclude ("org.seleniumhq.selenium", "selenium-server"),
        "org.seleniumhq.selenium" % "selenium-java" % Version.selenium % "test",
        "org.seleniumhq.selenium" % "selenium-remote-driver" % Version.selenium % "test",
        "org.seleniumhq.selenium" % "selenium-server" % Version.selenium % "test"
      )
    )

}
