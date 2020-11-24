val dict = List("a","b","c","d","e","f","g","h","i")
val corpus = List(
  List("a","b"),
  List("b","b"),
  List("b","c"),
  List("e","f"),
  List("f","g"),
  List("a","i"),
  List("h","b"),
  List("h","i"),
  List("i","c"),
)
val len = corpus.size.toDouble

dict
  .flatMap(word => corpus.flatMap {
  case tokenList if tokenList.contains(word) => Some(word, 1)
  case _ => None
}).groupBy(_._1).map(e => (e._1,len/e._2.size.toDouble))