name := "edflow"

version := "3.0"

lazy val `edflow` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41"

libraryDependencies += "com.rometools" % "rome" % "1.7.0"

libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.3"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"
