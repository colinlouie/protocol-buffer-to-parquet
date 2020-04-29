# Protocol Buffer to Parquet

Why does this project exist? This is a PoC to illustrate how to work with
Protocol Buffers in Scala, Apache Spark, and saving the unmarshalled data into
Parquet format for querying.

This was tested on:

```
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.4
      /_/

Using Scala version 2.11.12 (OpenJDK 64-Bit Server VM, Java 1.8.0_252)
```

# Building

`$ sbt assembly`

# Running

Note: The `hello-request.parquet/` directory will get dropped by the Apache
Spark job. Ensure that the Apache Spark working directory is writable.

```
$ spark-submit --class ProtobufToParquet \
    target/scala-2.11/protobuf-to-parquet-assembly-1.0.0.jar
```

# Test output


The Dataset containing the serialized Protocol Buffer message should look like
this:

```
+--------------------+
|                  _1|
+--------------------+
|[0A 05 41 6C 69 6...|
|    [0A 03 42 6F 62]|
+--------------------+
```

After the map function call to apply `HelloRequest.parseFrom(_)`, the Dataset
should look like this:

```
+-----+
| name|
+-----+
|Alice|
|  Bob|
+-----+
```
