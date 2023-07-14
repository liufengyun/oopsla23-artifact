object A:
  val a: Int = B.b + 10
object B:
  def foo(): Int = A.a * 2
  val b: Int = foo()