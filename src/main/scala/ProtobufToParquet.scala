/** Unmarshal Dataset[PROTOBUF] to save into Parquet.
 *
 *  PROTOBUF refers to serialized Array[Byte] data.
 *
 *  Usage example:
 *    spark-submit --class ProtobufToParquet \
 *      target/scala-2.11/protobuf-to-parquet-assembly-1.0.0.jar
 */
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

// The ScalaPB plugin combines the contents of src/main/protobuf/ into
//   target/scala-2.11/src_managed/.
import helloworld.helloworld.HelloRequest


object ProtobufToParquet {

  def main(args: Array[String]): Unit = {
    val appName = "ProtobufToParquet"

    val conf = new SparkConf().setAppName(appName)
    val spark = SparkSession
      .builder
      .appName(appName)
      .config(conf)
      .getOrCreate
    val sc = spark.sparkContext

    // Don't import spark.implicits._, ONLY import this.
    import scalapb.spark.Implicits._

    // Create Seq[Helloworld].
    val testData = Seq(
      HelloRequest().withName("Alice"),
      HelloRequest().withName("Bob")
    )

    // This gives us a Dataset[PROTOBUF], which is the same thing that would
    // come from something like a Spark Streaming Kafka reader.
    val d1 = spark.createDataset(testData.map(_.toByteArray))
    d1.show()

    // This unmarshals PROTOBUF back into a Scala-native object of the
    // intended Protocol Buffer message.
    val d2 = d1.map(HelloRequest.parseFrom(_))
    d2.write.parquet("hello-request.parquet")
    d2.show()

    // Read back the Parquet to ensure this is what we wanted.
    val d3 = spark.read.parquet("hello-request.parquet")
    d3.show()

    spark.stop
  } // def main

} // object ProtobufToParquet
