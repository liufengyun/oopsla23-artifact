class A(val a: A)
object B:
  val a: A = loop()
  def loop(): A = new A(loop())