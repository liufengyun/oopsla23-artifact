object A {             // error
  val a: Int = B.b
}

object B {
  val b: Int = A.a     // error
}