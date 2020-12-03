package minhash

import scala.util.Random

object JaccardSimilarity {
  
  val randgen= new Random
  
  /*
   * 
   * Calculate the Jaccard Distance of two Sets
   * 
   */
  def calculateJaccardDistanceSet[T](set1:Set[T], set2:Set[T]):Double = {
    val intersect_size = ( set1 intersect set2 ) size
    val j_index = intersect_size.toDouble / (set1.size + set2.size - intersect_size)
    val j_distance = 1 - j_index
    j_distance
  }
  
   /*
 	 * 
   * Calculate the Jaccard Distance of two Bags
   * 
   */
  def calculateJaccardDistanceBag[T](bag1:Iterable[T], bag2:Iterable[T]):Double = {
    val numberOfSharedWords      =  ( bag1.toList   intersect  bag2.toList ).size.toDouble
    val totalNumberOfUniqueWords =  ( bag1.toList     union    bag2.toList ).size.toDouble

    numberOfSharedWords / totalNumberOfUniqueWords
  }


  /*
   * 
   * Calculates an Array of Hash Functions
   * (size corresponds to the number of Hash-Functions respectively the size
   * of the array)
   * 
   * Each function of the array should have the following structure
   * h(x)= (m*x + b) mod c, where 
   *    
   *    m is random integer 
   *    b is a random integer
   *    c is the parameter size, that is passed in the signature of the method
   */
  def createHashFuntions(size:Integer, nrHashFuns: Int):Array[(Int=>Int)]= {
    val rand = new scala.util.Random(size)
    (
      for {
        i <- 0 until size
        (m,b) = {
          var x = rand.nextInt
          var y = rand.nextInt
          while ( x<2 || y<2 ) {
            x = rand.nextInt
            y = rand.nextInt
          }
          (x,y)
        }
      }
        yield (x:Int) => (m*x+b)%size
      ).toArray
  }

  /*
   * Implement the MinHash algorithm presented in the lecture
   * 
   * Input:
   * matrix: Document vectors (each column should corresponds to one document)
   * hFuns: Array of Hash-Functions
   * 
   * Output:
   * Signature Matrix:
   * columns: Each column corresponds to one document
   * rows: Each row corresponds to one hash function
   */
  
  def minHash[T](matrix:Array[Array[T]],hFuns:Array[Int=>Int]):Array[Array[Int]] = {

    // create signature matrix filled with -1
    val signatureMatrix:Array[Array[Int]] = Array.fill(hFuns.length)(Array.fill(matrix(0).size)(-1))

    for { // iterate over matrix
      i <- 0 until matrix.size
      j <- 0 until matrix(i).size

      if (matrix(i)(j) == 1) // if non zero element detected

    } yield {

      val hashedVals:Array[Int] = hFuns.map(_(i)) // calculate hashes

      for (k <- 0 until hashedVals.size) {
        val hash:Int = hashedVals(k)
        if (signatureMatrix(k)(j) < 0) signatureMatrix(k)(j) = hash // any value if first
        else if (signatureMatrix(k)(j) > hash) signatureMatrix(k)(j) = hash // min values else
      }
    }
    signatureMatrix

    /**
     * The test fails reliable on two accounts: for the big set the fun calculateJaccardDistanceSet assert failed
     * If you comment the assert (Line 74 ) in ./test/JaccardSimilarityTest.scala
     * the test fails on a second account. the difference between two distances is to large
     * TODO fix calculateJaccardDistanceSet
     * Todo fix minhash
     */

  }
  
  /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   * 
   * Helper functions that are used in the tests
   * 
   * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */
  
  def printMultipleSets(data:Array[Array[Int]]):Unit= {
    data.foreach(x=>println(x.mkString(" ")))
  }
  
  def createRandomSetAsArray(nrElements:Int):Array[Int]= {
    val res= Array.tabulate(nrElements)(_=>0)
    (for (i <- 0 to nrElements-1) {
      
      if (randgen.nextFloat<0.3) res(randgen.nextInt(nrElements-1))=1
    })
    res
  }
  
  def transformArrayIntoSet(data:Array[Int]):Set[Int]={
    
    (for (i <- 0 to data.size-1 if (data(i)==1)) yield i).toSet 
   
  }
  
  def findNextPrim(x:Int):Int={

    def isPrim(X:Int, i:Int, Max:Int):Boolean = {
       if (i>=Max) true
          else if (X % i == 0) false
            else isPrim(X,i+1,Max)
    }
          
    if (isPrim(x,2,math.sqrt(x).toInt+1)) x 
      else findNextPrim(x+1)
  }
  
   def compareSignatures(sig1: Array[Int], sig2:Array[Int]):Int={
     
     var res=0
     for (i <- 0 to sig1.size-1) {if (sig1(i)==sig2(i)) res=res+1}
     res
   }
    
}