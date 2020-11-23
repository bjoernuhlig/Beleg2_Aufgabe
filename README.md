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

### Task A: Create basic functions for textanalysis and entity resolution 
See Chapter 3 (pp. 73-103) of [Mining Massive Datasets](http://infolab.stanford.edu/~ullman/mmds/book.pdf) for min hashing and local sensitivity hashing.


#### Entity Resolution
Implement functions in: `src/main/scala/textanalyse/EntityResolution.scala`

- [x] [tokenize](./src/main/scala/textanalyse/EntityResolution.scala#L121) 
- [x] [getTokens](./src/main/scala/textanalyse/EntityResolution.scala#L22) 
- [x] [countTokens](./src/main/scala/textanalyse/EntityResolution.scala#L33) 
- [x] [findBiggestRecord](./src/main/scala/textanalyse/EntityResolution.scala#L43)
- [ ] calculateTF_IDF
- [ ] computeSimilarity
- [ ] calculateDotProduct
- [ ] calculateNorm
- [ ] calculateCosinusSimilarity
- [ ] calculateDocumentSimilarity
- [ ] computeSimilarityWithBroadcast

Implement TF-IDF 
- [x] [getTermFrequencies](./src/main/scala/textanalyse/EntityResolution.scala#L133)
- [ ] createCorpus
- [ ] calculateIDF
- [ ] simpleSimimilarityCalculation
- [ ] findSimilarity
- [ ] simpleSimimilarityCalculationWithBroadcast
- [ ] evaluateModel

### Task B

#### Min Hashing

#### Local Sensitivity Hashing 

