package textanalyse

import org.apache.spark.rdd.RDD

import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast

class EntityResolution (sc:SparkContext, dat1:String, dat2:String, stopwordsFile:String, goldStandardFile:String){

  val amazonRDD:RDD[(String, String)]= Utils.getData(dat1, sc)
  val googleRDD:RDD[(String, String)]= Utils.getData(dat2, sc)
  val stopWords:Set[String]= Utils.getStopWords(stopwordsFile)
  val goldStandard:RDD[(String,String)]= Utils.getGoldStandard(goldStandardFile, sc)

  var amazonTokens:RDD[(String, List[String])]= _
  var googleTokens:RDD[(String, List[String])]= _
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

    data.map{ case (key,tokenlist) => (key, tokenlist.length) }.fold("",0)((acc,head)=>{ ("", acc._2 + head._2) })._2
  }
  
  def findBiggestRecord(data:RDD[(String,List[String])]):(String,List[String])={
    
    /*
     * Findet den Datensatz mit den meisten Tokens
     */
    data map { case (key:String,tokenList:List[String]) => (key,tokenList) } reduce ((acc,head) => { if (acc._2.length >= head._2.length) acc else head } )
  }

  def createCorpus={
    /*
     * Erstellt die Tokenmenge für die Amazon und die Google-Produkte
     * (amazonRDD und googleRDD), vereinigt diese und speichert das
     * Ergebnis in corpusRDD
     */
    corpusRDD = getTokens(googleRDD) ++ getTokens(amazonRDD);
  }
  
  
  def calculateIDF={ // takes about 4-5 minutes as cartesian is very expensive, as is distinct
    
    /*
     * Berechnung des IDF-Dictionaries auf Basis des erzeugten Korpus
     * Speichern des Dictionaries in die Variable idfDict
     */
    val CORPUS:Double = corpusRDD.count.toDouble
    
    val allTokens:RDD[String] = corpusRDD.flatMap { case (key,tokenList) => tokenList }

    val allDocuments:RDD[List[String]] = corpusRDD.map(_._2)

    // very expensive cartesian join with distinct takes 4-5 minutes
//    idfDict = allTokens.cartesian(allDocuments).
//      distinct.
//      filter { case (word,list) => list.contains(word) }.
//      groupBy(_._1).
//      map(e => (e._1,CORPUS/e._2.size.toDouble)).
//      collect.
//      toMap

    // Using broadcast to join with distinct takes 1.44 minutes
    val bc = sc.broadcast(allTokens.collect)

    val joined = allDocuments.mapPartitions({ iter =>
      val y = bc.value
      for {
        (x) <- iter
      } yield (x, y)
    }, preservesPartitioning = true)

    val grouped = joined.flatMap(x => x._2.map(y => (y,x._1))).distinct

    idfDict = grouped.filter { case (word,list) => list.contains(word) }.
      groupBy(_._1).
      map( e => (e._1,CORPUS/e._2.size.toDouble) ).
      collect.
      toMap
  }

 
  def simpleSimimilarityCalculation:RDD[(String,String,Double)]={
    
    /*
     * Berechnung der Document-Similarity für alle möglichen 
     * Produktkombinationen aus dem amazonRDD und dem googleRDD
     * Ergebnis ist ein RDD aus Tripeln bei dem an erster Stelle die AmazonID
     * steht, an zweiter die GoogleID und an dritter der Wert
     */
    ???
  }
  
  def findSimilarity(vendorID1:String,vendorID2:String,sim:RDD[(String,String,Double)]):Double={
    
    /*
     * Funktion zum Finden des Similarity-Werts für zwei ProduktIDs
     */
    ???
  }
  
 def simpleSimimilarityCalculationWithBroadcast:RDD[(String,String,Double)]={
   
    ???
  }
 
 /*
  * 
  * 	Gold Standard Evaluation
  */
 
  def evaluateModel(goldStandard:RDD[(String, String)]):(Long, Double, Double)={
    
    /*
     * Berechnen Sie die folgenden Kennzahlen:
     * 
     * Anzahl der Duplikate im Sample
     * Durchschnittliche Consinus Similaritaet der Duplikate
     * Durchschnittliche Consinus Similaritaet der Nicht-Duplikate
     * 
     * 
     * Ergebnis-Tripel:
     * (AnzDuplikate, avgCosinus-SimilaritätDuplikate,avgCosinus-SimilaritätNicht-Duplikate)
     */
    
    
    ???
  }
}

object EntityResolution{
  
  def tokenize(s:String, stopws:Set[String]):List[String]= {
    /*
   	* Tokenize splittet einen String in die einzelnen Wörter auf
   	* und entfernt dabei alle Stopwords.
   	* Verwenden Sie zum Aufsplitten die Methode Utils.tokenizeString
   	*/

    Utils.tokenizeString(s) // list.diff(list) removes only the first occurrence of an object. filterNot with Set removes all elements of the Set from the list
      .filterNot(stopws)

   }

  def getTermFrequencies(tokens:List[String]):Map[String,Double]={
    
    /*
     * Berechnet die Relative Haeufigkeit eine Wortes in Bezug zur
     * Menge aller Wörter innerhalb eines Dokuments 
     */
    
    tokens.groupBy(identity).map { case (word, frequency) => (word,frequency.length.toDouble / tokens.length.toDouble) }
  }
   
  def computeSimilarity(record:((String, String),(String, String)), idfDictionary:Map[String,Double], stopWords:Set[String]):(String, String,Double)={
    
    /*
     * Bererechnung der Document-Similarity einer Produkt-Kombination
     * Rufen Sie in dieser Funktion calculateDocumentSimilarity auf, in dem 
     * Sie die erforderlichen Parameter extrahieren
     */
     ???  
  }
  
  def calculateTF_IDF(terms:List[String], idfDictionary:Map[String,Double]):Map[String,Double]={
    
    /* 
     * Berechnung von TF-IDF Wert für eine Liste von Wörtern
     * Ergebnis ist eine Mapm die auf jedes Wort den zugehörigen TF-IDF-Wert mapped
     */ //TF * IDF
    val dict = idfDictionary
    getTermFrequencies(terms).map { case (term:String,tf:Double) => (term, tf * dict(term)) }
  }
  
  def calculateDotProduct(v1:Map[String,Double], v2:Map[String,Double]):Double={
    
    /*
     * Berechnung des Dot-Products von zwei Vectoren - use keyset to remove sparse elements without corresponding keys
     */
    v1.filterKeys(v2.keySet).view zip v2.filterKeys(v1.keySet) map {case ((_,u),(_,v)) => u*v} sum
  }

  def calculateNorm(vec:Map[String,Double]):Double={
    
    /*
     * Berechnung der Norm eines Vectors
     */
    Math.sqrt(((for (el <- vec.values) yield el*el).sum))
  }
  
  def calculateCosinusSimilarity(doc1:Map[String,Double], doc2:Map[String,Double]):Double={

    /* 
     * Berechnung der Cosinus-Similarity für zwei Vectoren
     */
    val corpus = ( doc1.keys ++ doc2.keys ).toList

    // We need a sorted (doc) map the size of the corpus which contains zeros for words missing from the corpus
    import scala.collection.immutable.SortedMap

    // Find missing words from corpus / fill with zeros / convert to Array to throw into a SortedMap or TreeMap
    val n1:Map[String,Double] = SortedMap( (doc1.toSeq ++ corpus.diff(doc1.keys.toList)  .map( s => (s,0.0)))  .toArray:_* )
    val n2:Map[String,Double] = SortedMap( (doc2.toSeq ++ corpus.diff(doc2.keys.toList)  .map( s => (s,0.0)))  .toArray:_* )

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
    ???
  }
}
