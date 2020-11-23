import textanalyse.EntityResolution

val v1:Map[String,Double] = Map(("a"-> 4), ("c"-> 5), ("b"-> 7))
val v2:Map[String,Double] = Map(("a"-> 5), ("c"-> 2), ("e"-> 7))
//assert(Math.abs(res-0.35805743701971)<0.000001)

val corpus = v1.keySet ++ v2.keySet
corpus.filter(v1.keySet)
corpus.filter(v2.keySet)
v1.filterKeys(v2.keySet)
v2.filterKeys(v1.keySet)

//whats not in v2
v1.keys.toList.diff(v2.keys.toList)

//whats not in v1
v2.keys.toList.diff(v1.keys.toList)

//whats not in doc2
val addToV2:List[String] = v1.keys.toList.diff(v2.keys.toList)

addToV2.map(s => (s,0)).toMap

val pdense = List(4,5,7,0)
val qdense = List(5,2,0,7)

val psparse = List(4,5)
val qsparse = List(5,2)


val dotdense = pdense zip qdense map {case (u,v) => u*v} sum
val norm_pdense = Math.sqrt(((for (el <- pdense) yield el*el).sum))
val norm_qdense = Math.sqrt(((for (el <- qdense) yield el*el).sum))

dotdense / (norm_pdense * norm_qdense)

val dotsparse = psparse zip qsparse map {case (u,v) => u*v} sum
val norm_psparse = Math.sqrt(((for (el <- psparse) yield el*el).sum))
val norm_qsparse = Math.sqrt(((for (el <- qsparse) yield el*el).sum))

dotsparse / (norm_psparse * norm_qsparse)

v1.filterKeys(v2.keySet).view zip v2.filterKeys(v1.keySet) map {case ((_,u),(_,v)) => u*v} sum

EntityResolution.calculateDotProduct(v1,v2)
EntityResolution.calculateNorm(v1.filterKeys(v2.keySet))
EntityResolution.calculateNorm(v2.filterKeys(v1.keySet))

EntityResolution.calculateDotProduct(v1,v2) / (
EntityResolution.calculateNorm(v1.filterKeys(v2.keySet)) *
EntityResolution.calculateNorm(v2.filterKeys(v1.keySet)) )

30 / 9*8
val l = List(3,8,7,5,2,9)

Math.sqrt (l.foldLeft(0.0)(_ + Math.pow(_, 2)))


val d1 = v1.filterKeys(v2.keySet)
val d2 = v2.filterKeys(v1.keySet)

1 - ( d1.view zip d2 map {case ((_,u),(_,v)) => u*v} sum ) / ( Math.sqrt(((for (el <- d1.values) yield el*el).sum)) *  Math.sqrt(((for (el <- d2.values) yield el*el).sum)))




val res = EntityResolution.calculateCosinusSimilarity(v1,v2)
//assert(Math.abs(res-0.35805743701971)<0.000001)

val x = List(1,2,3)
val y = List(2,3,5)

// cosine(x, y)
// 0.0028235350472619603


// new test


val a1:Map[String,Double] = Map(("a"-> 4), ("c"-> 5), ("b"-> 7),("e"-> 2))
val a2:Map[String,Double] = Map(("a"-> 5), ("c"-> 2), ("b"-> 3),("e"-> 7))

EntityResolution.calculateDotProduct(a1,a2) /
  (EntityResolution.calculateNorm(a1) *
  EntityResolution.calculateNorm(a2))


(4*5+5*2+7*3+2*7) / ( Math.sqrt(4*4+5*5+7*7+2*2)*Math.sqrt(5*5+2*2+3*3+7*7) )