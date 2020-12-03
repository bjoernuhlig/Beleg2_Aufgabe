val funs:Array[Int=>Int]= Array(((x:Int)=>(x+1)%5),((x:Int)=>(3*x+1)%5))
val matrix= Array(Array(1,0,0,1),Array(0,0,1,0),Array(0,1,0,1),Array(1,0,1,1), Array(0,0,1,0))

//     r  c
matrix(0)(0)
matrix(0)(1)
matrix(0)(2)
matrix(0)(3)


//create array of zeros
val sigMat = Array.fill(funs.length)(Array.fill(matrix(0).size)(-1))
// Array.ofDim[Int](funs.length,matrix.length) // zeros array


//start with row 0 and calc hash values

val hashes = (for {
  i <- 0 until matrix.size
  j <- 0 until funs.size

} yield funs(j)(i)).
  grouped(funs.length).
  toArray.
  map(_.toArray)


hashes
hashes.map(_.min)

/** min hash alg:
 * replace the row/col with a row/fun mat
 * replace the row/fun mat with a fun/col mat
 * put the hash value into the fun/col idx ONLY...
 * if row/fun idx is not zero
 * if fun/col idx is not smaller than the hash value
 *
 * hashes.rows = matrix.rows
 * sigMat.rows = hashes.cols
 * sigMat.cols = matrix.cols
 *
 *          r c
 *  sigMat [2,4] size: 2
 *  hashes [5,2] size: 5
 *  matrix [5,4] size: 5
 *          i j
 *
 */
funs.size
sigMat.size
sigMat(0).size
hashes.size
hashes(0).size
matrix.size
matrix(0).size

//mat: take the hash value for the row/fun
//sigMat check in the fun/doc
//

var h = "HASHES"
hashes(0)(0)
hashes(0)(1)

hashes(1)(0)
hashes(1)(1)

hashes(2)(0)
hashes(2)(1)

hashes(3)(0)
hashes(3)(1)

hashes(4)(0)
hashes(4)(1)

var s = "SIGNATUREMATRIX"
sigMat(0)(0)
sigMat(0)(1)
sigMat(0)(2)
sigMat(0)(3)

sigMat(1)(0)
sigMat(1)(1)
sigMat(1)(2)
sigMat(1)(3)

var m = "MATRIX"
matrix(0)(0)
matrix(0)(1)
matrix(0)(2)
matrix(0)(3)

matrix(1)(0)
matrix(1)(1)
matrix(1)(2)
matrix(1)(3)

matrix(2)(0)
matrix(2)(1)
matrix(2)(2)
matrix(2)(3)

matrix(3)(0)
matrix(3)(1)
matrix(3)(2)
matrix(3)(3)

matrix(4)(0)
matrix(4)(1)
matrix(4)(2)
matrix(4)(3)

// iter über die rows der matrix, 0-5
// steht eine 1 drin? hol die hashfuncs.

for {
  i <- 0 until matrix.size
  j <- 0 until matrix(i).size

  if matrix(i)(j) == 1

} yield for {

  k <- 0 until hashes(0).size
  if sigMat(k)(j) > hashes(i)(k)
} sigMat(k)(j) = hashes(i)(k)