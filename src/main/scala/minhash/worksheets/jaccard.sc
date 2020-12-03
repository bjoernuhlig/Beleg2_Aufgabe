val bag1=List("this","is", "a", "test","this","is", "a", "test","hello", "world")
val bag2=List("this","is", "a", "test","this","is", "a", "test","cat","dog")

def calculateJaccardDistanceBag[T](bag1:Iterable[T], bag2:Iterable[T]):Double = {
  def helper[T](b1:Iterable[T], b2:Iterable[T]):Double = {
    val intersect_size = b1.toList intersect b2.toList size // number of unique words in both bags
    val j_index = intersect_size.toDouble / (b1.toList.distinct.size + b2.toList.distinct.size - intersect_size) toDouble
    val j_distance = 1 - j_index
    j_distance
  }
  helper(bag1,bag2)
}


/**
Doc1: “Word2”, “Word3”, “Word4”, “Word2”.
Doc2: “Word1”, “Word5”, “Word4”, “Word2”.
Doc3: “Word1”.
The output is:

Jaccard: 0.4
Jaccard: 1
Jaccard: 0

For the first result, the total number of unique words (union) in “Doc1” and “Doc2” is 5, and the number of shared words (intersection) is 2.
This gives the Jaccard Similarity of 2.0 / 5.0 = 0.4.

The second result shows that a document compared to itself gives a value of 1 (completely similar) and

the third test shows a value of 0 for completely dissimilar documents.
 */

val totalNumberOfUniqueWords = bag1.union(bag2)
totalNumberOfUniqueWords
totalNumberOfUniqueWords.size
val numberOfSharedWords = bag1.intersect(bag2)
numberOfSharedWords
numberOfSharedWords.size

numberOfSharedWords.size.toDouble / totalNumberOfUniqueWords.size
