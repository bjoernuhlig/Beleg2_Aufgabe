
import textanalyse.{EntityResolution, Utils}
import textanalyse.Utils.split_regex

val stopws="stopwords.txt"
val stopwords= Utils.getStopWords(stopws)

def tokenizeString(s:String):List[String]={

  val words= s.toLowerCase.split(split_regex).toList
  words.filter(_!="")
}

val s="Being at the top of the pops!"
val words_s= List("pops","top")
s
val r= EntityResolution.tokenize(s,stopwords)
r
assert(r.size==2)
assert(r.sorted==words_s)

// list.diff(list) removes only the first occurence of an object. filterNot with set removes all occurences of the set in the list