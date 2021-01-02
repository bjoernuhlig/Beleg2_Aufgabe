//import org.apache.spark.mllib.linalg.{Vector, Vectors, DenseVector}
//import breeze.linalg.{DenseVector => BDV, SparseVector => BSV, Vector => BV}

val s1 = List(1,2,3)
val s2 = List(4,5,6)


s1.zip(s2).map {case (u,v) => u*v} sum // works for dense vectors

val v1:Map[String,Double] = Map(("a"-> 4.0), ("c"-> 5), ("b"-> 7))
v1.keySet
val v2:Map[String,Double] = Map(("a"-> 2), ("b"-> 50),("d"->100))
v2.keySet

//sorted hashmap
collection.mutable.LinkedHashMap(v1.toSeq.sortBy(_._1):_*)

2*4 + 7*50

v2.keySet
//use keyset to remove sparse elements without corresponding keys

v1.keySet
v2.keySet

v1.filterKeys(v2.keySet)
v2.filterKeys(v1.keySet)

(v1.filterKeys(v2.keySet) zip v2.filterKeys(v1.keySet) ) map {case ((_,u),(_,v)) => u*v} sum

