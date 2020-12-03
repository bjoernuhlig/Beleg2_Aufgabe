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
- [x] [tokenize](./src/main/scala/textanalyse/EntityResolution.scala#L153) 
- [x] [getTokens](./src/main/scala/textanalyse/EntityResolution.scala#L23) 
- [x] [countTokens](./src/main/scala/textanalyse/EntityResolution.scala#L34) 
- [x] [findBiggestRecord](./src/main/scala/textanalyse/EntityResolution.scala#L44)
- [x] [calculateTF_IDF](./src/main/scala/textanalyse/EntityResolution.scala#L185)
- [x] [computeSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L175)
- [x] [calculateDotProduct](./src/main/scala/textanalyse/EntityResolution.scala#L195)
- [x] [calculateNorm](./src/main/scala/textanalyse/EntityResolution.scala#L203)
- [x] [calculateCosinusSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L211)
- [x] [calculateDocumentSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L228)
- [x] [computeSimilarityWithBroadcast](./src/main/scala/textanalyse/EntityResolution.scala#L240)
- [x] [getTermFrequencies](./src/main/scala/textanalyse/EntityResolution.scala#L165)
- [x] [createCorpus](./src/main/scala/textanalyse/EntityResolution.scala#L52)
- [x] [calculateIDF](./src/main/scala/textanalyse/EntityResolution.scala#L62)
- [x] [simpleSimimilarityCalculation](./src/main/scala/textanalyse/EntityResolution.scala#L103)
- [x] [findSimilarity](./src/main/scala/textanalyse/EntityResolution.scala#L114)
- [x] [simpleSimimilarityCalculationWithBroadcast](./src/main/scala/textanalyse/EntityResolution.scala#L122)
- [ ] [evaluateModel](./src/main/scala/textanalyse/EntityResolution.scala#L132)

### Task B

#### Min Hashing
Implement functions in: `src/main/scala/minhash/JaccardSimilarity.scala`
- [x] [calculateJaccardDistanceSet](./src/main/scala/minhash/JaccardSimilarity.scala#L14) 
- [x] [calculateJaccardDistanceBag](./src/main/scala/minhash/JaccardSimilarity.scala#L26) 
- [x] [createHashFuntions](./src/main/scala/minhash/JaccardSimilarity.scala#L47) 
- [ ] [minHash](./src/main/scala/minhash/JaccardSimilarity.scala#L79)

#### Local Sensitivity Hashing 

#### Scalable Entity Resolution ( Text Analysis )
Implement functions in: `src/main/scala/textanalyse/ScalableEntityResolution.scala`
- [ ] [buildInverseIndex](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L66)
- [ ] [determineCommonTokens](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L80)
- [ ] [calculateSimilaritiesFullDataset](./src/main/scala/textanalyse/ScalableEntityResolution.scala#L90)