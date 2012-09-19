
import AssemblyKeys._

assemblySettings

name := "BigIce"

version := "1.0"

scalaVersion := "2.9.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.3.17"
