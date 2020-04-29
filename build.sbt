name := "protobuf-to-parquet"
version := "1.0.0"
scalaVersion := "2.11.12"

val sparkVersion = "2.4.4"

PB.targets in Compile := Seq(
  scalapb.gen(grpc=true) -> (sourceManaged in Compile).value
)

// This is required for Protocol Buffer specifications that include gRPC
// definitions.
libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion % Provided,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion % Provided
)

libraryDependencies += "com.thesamet.scalapb" %% "sparksql-scalapb" % "0.9.0"

// According to scalapb.github.io:
//   Spark ships with an old version of Google’s Protocol Buffers runtime that
//   is not compatible with the current version. Therefore, we need to shade
//   our copy of the Protocol Buffer runtime. Add this to your build.sbt:
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll
)

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % Provided
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % Provided
