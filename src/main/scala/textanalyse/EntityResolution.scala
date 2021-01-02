package textanalyse

import org.apache.spark.rdd.RDD

import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast

class EntityResolution (sc:SparkContext, dat1:String, dat2:String, stopwordsFile:String, goldStandardFile:String){

  val amazonRDD:RDD[(String, String)]= Utils.getData(dat1, sc)
  val googleRDD:RDD[(String, String)]= Utils.getData(dat2, sc)
  val stopWords:Set[String]= Utils.getStopWords(stopwordsFile)
  val goldStandard:RDD[(String,String)]= Utils.getGoldStandard(goldStandardFile, sc)

  var amazonTokens:RDD[(String, List[String])]= getTokens(amazonRDD)
  var googleTokens:RDD[(String, List[String])]= getTokens(googleRDD)
  var corpusRDD:RDD[(String, List[String])]= _
  var idfDict:Map[String, Double]= _
  
  var idfBroadcast:Broadcast[Map[String,Double]]= _
  var similarities:RDD[(String, String, Double)]= _
  
  def getTokens(data:RDD[(String,String)]):RDD[(String,List[String])]={

    /*
     * getTokens soll die Funktion tokenize auf das gesamte RDD anwenden
     * und aus allen Produktdaten eines RDDs die Tokens extrahieren.
     */
    val sw = stopWords // params cannot be passed directly => not serializable
    data.map({ case (key, productDescription ) => (key, EntityResolution.tokenize(productDescription,sw) )})

  }

  def countTokens(data:RDD[(String,List[String])]):Long={

    /*
     * Zählt alle Tokens innerhalb eines RDDs
     * Duplikate sollen dabei nicht eliminiert werden
     */

//    data.map{ case (key,tokenlist) => (key, tokenlist.length) }.fold("",0)((acc,head)=>{ ("", acc._2 + head._2) })._2

    //  Shorter:
    data.map(x => (x._2.size)).sum.toLong
  }
  
  def findBiggestRecord(data:RDD[(String,List[String])]):(String,List[String])={
    
    /*
     * Findet den Datensatz mit den meisten Tokens
     */
    //data map { case (key:String,tokenList:List[String]) => (key,tokenList) } reduce ((acc,head) => { if (acc._2.length >= head._2.length) acc else head } )

    //Shorter:
    data.sortBy(x => -x._2.size).take(1)(0)
  }

  def createCorpus={
    /*
     * Erstellt die Tokenmenge für die Amazon und die Google-Produkte
     * (amazonRDD und googleRDD), vereinigt diese und speichert das
     * Ergebnis in corpusRDD
     */
    corpusRDD = getTokens(googleRDD) ++ getTokens(amazonRDD)
  }
  
  
  def calculateIDF={ // takes about 3-5 minutes as cartesian is very expensive, as is distinct
    
    /*
     * Berechnung des IDF-Dictionaries auf Basis des erzeugten Korpus
     * Speichern des Dictionaries in die Variable idfDict
     */
    val CORPUS:Double = corpusRDD.count.toDouble
    
    val allTokens:RDD[String] = corpusRDD.values flatMap { case tokenList => tokenList.toSet } //distinct
    allTokens.cache()

    val idfRDD = allTokens groupBy identity map {
      case (word, list) => (word, CORPUS / list.size.toDouble)
    }
    idfDict = idfRDD.collectAsMap.toMap

  }

 
  def simpleSimilarityCalculation:RDD[(String,String,Double)]={
    /*
     * Berechnung der Document-Similarity für alle möglichen 
     * Produktkombinationen aus dem amazonRDD und dem googleRDD
     * Ergebnis ist ein RDD aus Tripeln bei dem an erster Stelle die AmazonID
     * steht, an zweiter die GoogleID und an dritter der Wert
     */
    val ama = amazonRDD;
    val goo = googleRDD;
    val idfDic= idfDict;
    val sWords = stopWords;
    val car = ama.cartesian(goo).distinct

    car.map(EntityResolution.computeSimilarity(_,idfDic,sWords))
  }
  
  def findSimilarity(vendorID1:String,vendorID2:String,sim:RDD[(String,String,Double)]):Double={ // sim is simpleSimilarityCalculation,
    
    /*
     * Funktion zum Finden des Similarity-Werts für zwei ProduktIDs
     */
    sim.filter {
      record => record match {
        case (id1,id2,_) => if (vendorID1==id1 && vendorID2==id2) true else false
      }
    }.distinct.collect.toList.lift(0).get._3
  }
  
 def simpleSimilarityCalculationWithBroadcast:RDD[(String,String,Double)]={
   val ama = amazonRDD;
   val goo = googleRDD;
   val idfDicBC = sc.broadcast(idfDict);
   val sWords = stopWords;
   val car = ama.cartesian(goo).distinct

   car.map(EntityResolution.computeSimilarityWithBroadcast(_,idfDicBC,sWords))

  }
 
 /*
  * 
  * 	Gold Standard Evaluation
  */
 
  def evaluateModel(goldStandard:RDD[(String, String)]):(Long, Double, Double)={
    
    /*
     * Berechnen Sie die folgenden Kennzahlen:
     * 
     *LONG Anzahl der Duplikate im Sample
     *DOUBLE Durchschnittliche Consinus Similaritaet der Duplikate
     *DOUBLE Durchschnittliche Consinus Similaritaet der Nicht-Duplikate
     * 
     * 
     * Ergebnis-Tripel:
     * (AnzDuplikate, avgCosinus-SimilaritätDuplikate,avgCosinus-SimilaritätNicht-Duplikate)
     */

    val sim = simpleSimilarityCalculationWithBroadcast.map {
      case (google, amazon, similarityValue) => (google + " " + amazon, similarityValue)
    }
    val AnzDuplikate = sim.join(goldStandard)
    val AnzNonDuplikate = sim.subtractByKey(AnzDuplikate)

    (
      AnzDuplikate.count,
      AnzDuplikate.map(e => e._2._1).sum / AnzDuplikate.count,
      AnzNonDuplikate.map(e => e._2).sum / AnzNonDuplikate.count
    )
  }

}

object EntityResolution{
  
  def tokenize(s:String, stopws:Set[String]):List[String]= {
    /*
   	* Tokenize splittet einen String in die einzelnen Wörter auf
   	* und entfernt dabei alle Stopwords.
   	* Verwenden Sie zum Aufsplitten die Methode Utils.tokenizeString
   	*/

    Utils
      .tokenizeString(s) // list.diff(list) removes only the first occurrence of an object. filterNot with Set removes all elements of the Set from the list
      .filterNot(stopws)
   }

  def getTermFrequencies(tokens:List[String]):Map[String,Double]={
    
    /*
     * Berechnet die Relative Haeufigkeit eine Wortes in Bezug zur
     * Menge aller Wörter innerhalb eines Dokuments 
     */
    
    tokens groupBy identity map {
      case (word, frequency) => (word,frequency.length / tokens.length.toDouble)
    }
  }
   
  def computeSimilarity(record:((String, String),(String, String)), idfDictionary:Map[String,Double], stopWords:Set[String]):(String, String,Double)={
    /*
     * Bererechnung der Document-Similarity einer Produkt-Kombination
     * Rufen Sie in dieser Funktion calculateDocumentSimilarity auf, in dem 
     * Sie die erforderlichen Parameter extrahieren
     */
    val idfDict = idfDictionary
    val sWords = stopWords

    record match {
      case ((id1,text1),(id2,text2)) => (
        id1, id2, calculateDocumentSimilarity(text1,text2,idfDict,sWords)
      )
    }
  }
  
  def calculateTF_IDF(terms:List[String], idfDictionary:Map[String,Double]):Map[String,Double]={
    
    /* 
     * Berechnung von TF-IDF Wert für eine Liste von Wörtern
     * Ergebnis ist eine Mapm die auf jedes Wort den zugehörigen TF-IDF-Wert mapped
     */ //TF * IDF
    val dict = idfDictionary

    getTermFrequencies(terms) map {
      case (term:String,tf:Double) => (term, tf * dict(term))
    }
  }
  
  def calculateDotProduct(v1:Map[String,Double], v2:Map[String,Double]):Double={
    
    /*
     * Berechnung des Dot-Products von zwei Vectoren - use keyset to remove sparse elements without corresponding keys
     */
    // if zip doesnt work use _.view.zip ... but loose performance
    v1.filterKeys(v2.keySet) zip v2.filterKeys(v1.keySet) map {
      case ((_,u),(_,v)) => u*v
    } sum
  }

  def calculateNorm(vec:Map[String,Double]):Double={
    
    /*
     * Berechnung der Norm eines Vectors
     */
    Math.sqrt(((for (el <- vec.values) yield el*el).sum))
  }
  
  def calculateCosinusSimilarity(doc1:Map[String,Double], doc2:Map[String,Double]):Double={

    /* Berechnung der Cosinus-Similarity für zwei Vectoren
     *
     * Required:
     * 2 Vectors (Maps) with identical sorted keys
     *
     * Given:
     * 2 Vectors (Maps, doc1 and doc2) with different unsorted keysets
     *
     * Solution:
     * Create a corpus from all given documents

        ( doc1.keys ++ doc2.keys ).toList

     * Get all terms from out of the corpus which are missing in the doc and assign them to the doc.

        doc1.toSeq ++ corpus.diff(doc1.keys.toList).

     * These terms do not appear and thus have a frequency of 0.0

        map( s => (s, 0.0) )

     * convert to Array and throw into a SortedMap or TreeMap which are always sorted by Key

        toArray:_*

     * use special type ascription _* to pass the array directly to the map constructor
     */

    val corpus = ( doc1.keys ++ doc2.keys ).toList
    import scala.collection.immutable.SortedMap
    val n1:Map[String,Double] = SortedMap(( doc1.toSeq ++ corpus.diff(doc1.keys.toList).map( s => (s, 0.0)) ).toArray:_*)
    val n2:Map[String,Double] = SortedMap(( doc2.toSeq ++ corpus.diff(doc2.keys.toList).map( s => (s, 0.0)) ).toArray:_*)

    calculateDotProduct(n1,n2) / ( calculateNorm(n1) * calculateNorm(n2) )
  }
  
  def calculateDocumentSimilarity(doc1:String, doc2:String,idfDictionary:Map[String,Double],stopWords:Set[String]):Double={
   
    /*
     * Berechnung der Document-Similarity für ein Dokument
     */
    val idfDict = idfDictionary; val sWords = stopWords

    calculateCosinusSimilarity(
      calculateTF_IDF(tokenize(doc1,sWords),idfDict),
      calculateTF_IDF(tokenize(doc2,sWords),idfDict)
    )
  }
  def computeSimilarityWithBroadcast(record:((String, String),(String, String)),idfBroadcast:Broadcast[Map[String,Double]], stopWords:Set[String]):(String, String,Double)={
    
    /*
     * Bererechnung der Document-Similarity einer Produkt-Kombination
     * Rufen Sie in dieser Funktion calculateDocumentSimilarity auf, in dem 
     * Sie die erforderlichen Parameter extrahieren
     * Verwenden Sie die Broadcast-Variable.
     */
    val bc = idfBroadcast.value
    val sWords = stopWords

    record match {
      case ((id1,text1),(id2,text2)) => (id1,id2,calculateDocumentSimilarity(text1,text2,bc,sWords))
    }
  }
}
