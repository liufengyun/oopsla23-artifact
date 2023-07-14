import scala.annotation.init.region

abstract class Foo { def foo(): Int }
class C(var x: Int) extends Foo { def foo(): Int = 20 }
class D(var y: Int) extends Foo { def foo(): Int = A.m }
class Box(var value: Foo)

object A:
  val box1: Box = region { new Box(new C(5)) }
  val box2: Box = region { new Box(new D(10)) }
  val m: Int = box1.value.foo()                      // ok