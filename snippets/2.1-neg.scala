class X {
  def foo(): Int = 10
}
object A {
  var a: X = new X()
}
object B {
  val b: Int = A.a.foo()
}