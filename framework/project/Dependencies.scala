/*
 * Copyright (C) 2009-2017 Lightbend Inc. <https://www.lightbend.com>
 */
import sbt._
import buildinfo.BuildInfo

object Dependencies {

  val akkaVersion = "2.4.17"
  val akkaHttpVersion = "10.0.5"
  val playJsonVersion = "2.6.0-M5"

  val specsVersion = "3.8.9"
  val specsBuild = Seq(
    "specs2-core",
    "specs2-junit",
    "specs2-mock"
  ).map("org.specs2" %% _ % specsVersion)

  val specsMatcherExtra = "org.specs2" %% "specs2-matcher-extra" % specsVersion

  val specsSbt = specsBuild

  val jacksons = Seq(
    "com.fasterxml.jackson.core" % "jackson-core",
    "com.fasterxml.jackson.core" % "jackson-annotations",
    "com.fasterxml.jackson.core" % "jackson-databind",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"
  ).map(_ % "2.8.7")

  val playJson = "com.typesafe.play" %% "play-json" % playJsonVersion

  val slf4j = Seq("slf4j-api", "jul-to-slf4j", "jcl-over-slf4j").map("org.slf4j" % _ % "1.7.25")
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.2"

  val guava = "com.google.guava" % "guava" % "21.0"
  val findBugs = "com.google.code.findbugs" % "jsr305" % "3.0.1" // Needed by guava
  val mockitoAll = "org.mockito" % "mockito-all" % "1.10.19"

  val h2database = "com.h2database" % "h2" % "1.4.194"
  val derbyDatabase = "org.apache.derby" % "derby" % "10.13.1.1"

  val acolyteVersion = "1.0.43-j7p"
  val acolyte = "org.eu.acolyte" % "jdbc-driver" % acolyteVersion

  val jdbcDeps = Seq(
    "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
    "com.zaxxer" % "HikariCP" % "2.6.1",
    "com.googlecode.usc" % "jdbcdslog" % "1.0.6.2",
    h2database % Test,
    acolyte % Test,
    logback % Test,
    "tyrex" % "tyrex" % "1.0.1") ++ specsBuild.map(_ % Test)

  val jpaDeps = Seq(
    "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",
    "org.hibernate" % "hibernate-entitymanager" % "5.2.6.Final" % "test"
  )

  val scalaJava8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0"
  def scalaParserCombinators(scalaVersion: String) = CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, major)) if major >= 11 => Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5")
    case _ => Nil
  }

  val springFrameworkVersion = "4.3.7.RELEASE"

  val javaDeps = Seq(
    scalaJava8Compat,

    ("org.reflections" % "reflections" % "0.9.11")
      .exclude("com.google.code.findbugs", "annotations")
      .classifier(""),

    // Used by the Java routing DSL
    "net.jodah" % "typetools" % "0.4.9",

    logback % Test
  ) ++ specsBuild.map(_ % Test)

  val javaFormsDeps = Seq(

    "org.hibernate" % "hibernate-validator" % "5.2.4.Final",

    ("org.springframework" % "spring-context" % springFrameworkVersion)
      .exclude("org.springframework", "spring-aop")
      .exclude("org.springframework", "spring-beans")
      .exclude("org.springframework", "spring-core")
      .exclude("org.springframework", "spring-expression")
      .exclude("org.springframework", "spring-asm"),

    ("org.springframework" % "spring-core" % springFrameworkVersion)
      .exclude("org.springframework", "spring-asm")
      .exclude("commons-logging", "commons-logging"),

    ("org.springframework" % "spring-beans" % springFrameworkVersion)
      .exclude("org.springframework", "spring-core")

  ) ++ specsBuild.map(_ % Test)

  val junitInterface = "com.novocode" % "junit-interface" % "0.11"
  val junit = "junit" % "junit" % "4.12"

  val javaTestDeps = Seq(
    junit,
    junitInterface,
    "org.easytesting" % "fest-assert"     % "1.4",
    mockitoAll
  ).map(_ % Test)

  val guiceVersion = "4.1.0"
  val guiceDeps = Seq(
    "com.google.inject" % "guice" % guiceVersion,
    "com.google.inject.extensions" % "guice-assistedinject" % guiceVersion
  )

  def runtime(scalaVersion: String) =
    slf4j ++
    Seq("akka-actor", "akka-slf4j").map("com.typesafe.akka" %% _ % akkaVersion) ++
    Seq("akka-testkit").map("com.typesafe.akka" %% _ % akkaVersion % Test) ++
    jacksons ++
    Seq(
      "commons-codec" % "commons-codec" % "1.10",

      playJson,

      guava,

      "org.apache.commons" % "commons-lang3" % "3.5",

      "javax.transaction" % "jta" % "1.1",
      "javax.inject" % "javax.inject" % "1",

      logback % Test,

      "org.scala-lang" % "scala-reflect" % scalaVersion,
      scalaJava8Compat
    ) ++ scalaParserCombinators(scalaVersion) ++
    specsBuild.map(_ % Test) ++
    javaTestDeps

  val nettyVersion = "4.1.8.Final"

  val netty = Seq(
    "com.typesafe.netty" % "netty-reactive-streams-http" % "2.0.0-M1",
    "io.netty" % "netty-transport-native-epoll" % nettyVersion classifier "linux-x86_64",
    logback % Test
  ) ++ specsBuild.map(_ % Test)

  val nettyUtilsDependencies = slf4j

  val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
  )

  def routesCompilerDependencies(scalaVersion: String) = Seq(
    "commons-io" % "commons-io" % "2.5",
    specsMatcherExtra % Test,
    logback % Test
  ) ++ specsBuild.map(_ % Test) ++ scalaParserCombinators(scalaVersion)

  private def sbtPluginDep(sbtVersion: String, scalaVersion: String, moduleId: ModuleID) = {
    moduleId.extra(
      "sbtVersion" -> CrossVersion.binarySbtVersion(sbtVersion),
      "scalaVersion" -> CrossVersion.binaryScalaVersion(scalaVersion)
    )
  }

  def runSupportDependencies(sbtVersion: String, scalaVersion: String) = Seq(
    sbtIO(sbtVersion, scalaVersion),
    "com.lightbend.play" %% "play-file-watch" % "1.0.0",
    logback % Test
  ) ++ specsBuild.map(_ % Test)

  // use partial version so that non-standard scala binary versions from dbuild also work
  def sbtIO(sbtVersion: String, scalaVersion: String): ModuleID = CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, major)) if major >= 11 => "org.scala-sbt" %% "io" % "0.13.13" % "provided"
    case _ => "org.scala-sbt" % "io" % sbtVersion % "provided"
  }

  val jnotify = "net.contentobjects.jnotify" % "jnotify" % "0.94-play-1"

  val typesafeConfig = "com.typesafe" % "config" % "1.3.1"

  def sbtDependencies(sbtVersion: String, scalaVersion: String) = {
    def sbtDep(moduleId: ModuleID) = sbtPluginDep(sbtVersion, scalaVersion, moduleId)

    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion % "provided",
      typesafeConfig,

      jnotify,

      sbtDep("com.typesafe.sbt" % "sbt-twirl" % BuildInfo.sbtTwirlVersion),

      sbtDep("com.typesafe.sbt" % "sbt-native-packager" % BuildInfo.sbtNativePackagerVersion),

      sbtDep("com.typesafe.sbt" % "sbt-web" % "1.3.0"),
      sbtDep("com.typesafe.sbt" % "sbt-js-engine" % "1.1.3"),

      logback % Test
    ) ++ specsBuild.map(_ % Test)
  }

  val playdocWebjarDependencies = Seq(
    "org.webjars" % "jquery"   % "2.2.4"    % "webjars",
    "org.webjars" % "prettify" % "4-Mar-2013" % "webjars"
  )

  val playDocVersion = "1.8.0"
  val playDocsDependencies = Seq(
    "com.typesafe.play" %% "play-doc" % playDocVersion
  ) ++ playdocWebjarDependencies

  val streamsDependencies = Seq(
    "org.reactivestreams" % "reactive-streams" % "1.0.0",
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    scalaJava8Compat,
    logback % Test
  ) ++ specsBuild.map(_ % "test") ++ javaTestDeps



  val scalacheckDependencies = Seq(
    "org.specs2"     %% "specs2-scalacheck" % specsVersion % Test,
    "org.scalacheck" %% "scalacheck"        % "1.13.4"     % Test
  )

  val playServerDependencies = Seq(
    guava % Test,
    logback % Test
  ) ++ specsBuild.map(_ % Test)

  val testDependencies = Seq(junit) ++ specsBuild.map(_ % Test) ++ Seq(
    junitInterface,
    guava,
    findBugs,
    ("org.fluentlenium" % "fluentlenium-core" % "3.1.1")
      .exclude("org.jboss.netty", "netty"),
    "net.sourceforge.htmlunit" % "htmlunit" % "2.23",
    "org.seleniumhq.selenium" % "htmlunit-driver" % "2.23.2",
    "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.0.1",
    "org.seleniumhq.selenium" % "selenium-support" % "3.0.1",
    logback % Test
  ) ++ guiceDeps

  val ehcacheVersion = "2.6.11"
  val playCacheDeps = Seq(
      "net.sf.ehcache" % "ehcache-core" % ehcacheVersion,
      logback % Test
    ) ++ specsBuild.map(_ % Test)

  val playWsStandaloneVersion = "1.0.0-M4"
  val playWsDeps = Seq(
    "com.typesafe.play" %% "play-ws-standalone" % playWsStandaloneVersion,
    logback % Test
  ) ++
    (specsBuild :+ specsMatcherExtra).map(_ % Test) :+
    mockitoAll % Test

  val playAhcWsDeps = Seq(
    "com.typesafe.play" %% "play-ahc-ws-standalone" % playWsStandaloneVersion
  )

  val playDocsSbtPluginDependencies = Seq(
    "com.typesafe.play" %% "play-doc" % playDocVersion
  )

}
