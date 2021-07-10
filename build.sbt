val ScalatraVersion = "2.7.1"

ThisBuild / scalaVersion := "2.12.12"
ThisBuild / organization := "bao.ho"

lazy val hello = (project in file("."))
  .settings(
    name := "My Scalatra Web App",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra"      %% "scalatra"           % ScalatraVersion,
      "ch.qos.logback"    % "logback-classic"     % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp"        % "9.4.35.v20201120" % "container;compile",
      "javax.servlet"     % "javax.servlet-api"   % "3.1.0" % "provided",
      "org.scalatra"      %% "scalatra-json"      % "2.7.0",
      "org.json4s"        %% "json4s-jackson"     % "3.5.2",
      "org.typelevel"     %% "cats-core"          % "2.3.0",
      "org.typelevel"     %% "cats-effect"        % "2.3.0",
      "org.scalatra"      %% "scalatra-scalatest" % ScalatraVersion % Test,
      "org.scalatest"     %% "scalatest"          % "3.0.3" % Test
    )
  )

enablePlugins(JettyPlugin)
