# B44.1 WT Content Management, Such- und Texttechnologien (SL) - 3. Zug- WiSe2020/21

###### Credits
Original owner of project/code 
- Prof. Dr. Hendrik Gärtner

Student Contributers: 
- Björn Uhlig  
- Lennart Döring

## Intro
Combine two essentially connected fields:  
- (A) duplicate recognition using entity resolution
- (B) data reduction using min hashing algorithm 

### Avoid Not Serializable Errors

- function params cannot be passed directly: 
```Scala
import Utils.tokenize
def myFunc(s:String,param:Any) = { Utils.tokenize(s,param) } // does not work

def myFunc(s:String,param:Any) = { val mp = param; Utils.tokenize(s,mp) } // does work
```
 => not serializable

### Task A: Create basic functions for text analysis (entity resolution) 
See Chapter 3 (pp. 73-103) of [Mining Massive Datasets](http://infolab.stanford.edu/~ullman/mmds/book.pdf) for min hashing and local sensitivity hashing.


#### Entity Resolution ( Text Analysis )
Implement functions in: `src/main/scala/textanalyse/EntityResolution.scala`
Implement TF-IDF: 
```
TFt,d is the number of occurrences of t in document d.
DFt is the number of documents containing the term t.
N is the total number of documents in the corpus.

    Wt,d = TFt,d log (N/DFt)
```
- [x] [tokenize](./src/main/scala/textanalyse/EntityResolution.scala#L162) 
- [x] [getTokens](./src/main/scala/textanalyse/EntityResolution.scala#L23) 
- [x] [countTokens](./src/main/scala/textanalyse/EntityResolution.scala#L34) 
- [x] [findBiggestRecord](./src/main/scala/textanalyse/EntityResolution.scala#L47)
- [x] [calculateTF_IDF](./src/main/scala/textanalyse/EntityResolution.scala#L202)
- [x] [computeSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L186)
- [x] [calculateDotProduct](./src/main/scala/textanalyse/EntityResolution.scala#L215)
- [x] [calculateNorm](./src/main/scala/textanalyse/EntityResolution.scala#L226)
- [x] [calculateCosinusSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L211)
- [x] [calculateDocumentSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L272)
- [x] [computeSimilarityWithBroadcast](./src/main/scala/textanalyse/EntityResolution.scala#L284)
- [x] [getTermFrequencies](./src/main/scala/textanalyse/EntityResolution.scala#L174)
- [x] [createCorpus](./src/main/scala/textanalyse/EntityResolution.scala#L58)
- [x] [calculateIDF](./src/main/scala/textanalyse/EntityResolution.scala#L68)
- [x] [simpleSimilarityCalculation](./src/main/scala/textanalyse/EntityResolution.scala#L87)
- [x] [findSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L103)
- [x] [simpleSimilarityCalculationWithBroadcast](./src/main/scala/textanalyse/EntityResolution.scala#L115)
- [x] [evaluateModel](./src/main/scala/textanalyse/EntityResolution.scala#L131)

### Task B

#### Min Hashing
Implement functions in: `src/main/scala/minhash/JaccardSimilarity.scala`
- [x] [calculateJaccardDistanceSet](./src/main/scala/minhash/JaccardSimilarity.scala#L14) 
- [x] [calculateJaccardDistanceBag](./src/main/scala/minhash/JaccardSimilarity.scala#L26) 
- [x] [createHashFuntions](./src/main/scala/minhash/JaccardSimilarity.scala#L47) 
- [x] [minHash](./src/main/scala/minhash/JaccardSimilarity.scala#L103)

#### Local Sensitivity Hashing 

#### Scalable Entity Resolution ( Text Analysis )
Implement functions in: `src/main/scala/textanalyse/ScalableEntityResolution.scala`
- [x] [buildInverseIndex](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L66)
- [x] [determineCommonTokens](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L81)
- [x] [calculateSimilaritiesFullDataset](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L105)
- [x] [fastCosinusSimilarity](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L282)