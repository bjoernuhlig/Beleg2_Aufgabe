import textanalyse.EntityResolution

val v1:Map[String,Double] = Map(("a"-> 4), ("c"-> 5), ("b"-> 7))
val v2:Map[String,Double] = Map(("a"-> 5), ("c"-> 2), ("e"-> 7))
//assert(Math.abs(res-0.35805743701971)<0.000001)

//whats not in doc2
val addTodoc2:List[String] = v1.keys.toList.diff(v2.keys.toList)

//whats not in doc1
val addTodoc1:List[String] = v2.keys.toList.diff(v1.keys.toList)


val d1:Map[String,Double] = ( v1.toSeq ++ addTodoc1.map(s => (s,0.0)) ).toMap
val d2:Map[String,Double] = ( v2.toSeq ++ addTodoc2.map(s => (s,0.0)) ).toMap

collection.immutable.SortedMap(d1.toArray:_*)

val n1 = collection.mutable.LinkedHashMap( d1.filterKeys(d2.keySet).toSeq.sortBy(_._1):_* ).toMap
val n2 = collection.mutable.LinkedHashMap( d2.filterKeys(d1.keySet).toSeq.sortBy(_._1):_* ).toMap

val dot= n1 zip n2 map {case (u,v) => u._2*v._2} sum

val norm_n1 = Math.sqrt(((for (el <- n1) yield el._2*el._2).sum))
val norm_n2 = Math.sqrt(((for (el <- n2) yield el._2*el._2).sum))

dot / (norm_n1 * norm_n2)


val corpus = v1.keys ++ v2.keys

v1.keys.toList.intersect(corpus.toList)
corpus.toList.diff(v1.keys.toList)