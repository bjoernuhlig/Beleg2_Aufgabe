import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import org.apache.spark.broadcast.Broadcast

val conf:SparkConf = new SparkConf().setMaster("local[4]").setAppName("Beleg3EntityResolutionApp")
conf.set("spark.executor.memory","6g")
conf.set("spark.storage.memoryFraction","0.8")
conf.set("spark.driver.memory", "4g")

val sc:SparkContext = new SparkContext(conf)

val rdd = sc.parallelize(Seq(Array("one","two","three"), Array("four", "five", "six")))
val map = sc.parallelize(Seq("one" -> 1, "two" -> 2, "three" -> 3, "four" -> 4, "five" -> 5, "six"->6))
val flat = rdd.flatMap(_.toSeq).keyBy(x=>x)

flat.collect.foreach(println)
val res = flat.join(map).map{case (k,v) => v}

res.collect.foreach(println)


val token = sc.parallelize(Seq("one","two","three"))
val docs = sc.parallelize(Seq(List("four", "five", "six"),List("four", "five", "six"),List("four", "five", "six")))

// token.collect.foreach(println)
// docs.collect.foreach(println)



val bc = sc.broadcast(token.collect)

val joined = docs.mapPartitions({ iter =>


  val y = bc.value

  for {


    (x) <- iter


  } yield (x, y)


}, preservesPartitioning = true)


val grouped = joined.flatMap(x => x._2.map(y => (y,x._1))).distinct

grouped.collect.foreach(println)

joined.collect.foreach(println)