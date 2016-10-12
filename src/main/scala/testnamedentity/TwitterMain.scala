package testnamedentity
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.twitter.TwitterUtils

object Main {

	def main(args: Array[String]): Unit = {
			Logger.getLogger("org").setLevel(Level.OFF);
			Logger.getLogger("akka").setLevel(Level.OFF);

			val config = new SparkConf().setMaster("local[4]").setAppName("spark-twitter-stream")
			val sc = new SparkContext(config)

			val ssc = new StreamingContext(sc, Seconds(1))

			System.setProperty("twitter4j.oauth.consumerKey", "")
			System.setProperty("twitter4j.oauth.consumerSecret", "")
			System.setProperty("twitter4j.oauth.accessToken", "")
			System.setProperty("twitter4j.oauth.accessTokenSecret", "")

			val filters = Companies.load().toArray

			val stream = TwitterUtils.createStream(ssc, None, filters=filters)

			val enStream = stream.filter { x => x.getLang=="en" }
				
			val enTextStream = enStream.map(_.getText).map(NETransform.extractNamedEntities)
			
			enTextStream.foreachRDD(_.foreach(println))
			
			ssc.start()
			ssc.awaitTermination()
	}

}