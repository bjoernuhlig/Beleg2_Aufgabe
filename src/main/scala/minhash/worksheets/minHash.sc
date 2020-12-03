val funs:Array[Int=>Int]= Array(((x:Int)=>(x+1)%5),((x:Int)=>(3*x+1)%5))
val matrix= Array(Array(1,0,0,1),Array(0,0,1,0),Array(0,1,0,1),Array(1,0,1,1),
  Array(0,0,1,0))

//     r  c
matrix(0)(0)
matrix(0)(1)
matrix(0)(2)
matrix(0)(3)


// dim of signature matrix
funs.length
matrix.length

//create array of zeros
val sigMat = Array.ofDim[Int](funs.length,matrix.length)

//start with row 0 and calc hash values

for {
  i <- 0 until matrix.size
  j <- 0 until funs.size

} yield funs(j)

val f = funs(0)

f(1)