import sbtassembly.Plugin._

import AssemblyKeys._

seq(assemblySettings: _*)

jarName in assembly := "big-ice.jar"

name := "BigIce"

version := "1.0"

scalaVersion := "2.9.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.3.17"
