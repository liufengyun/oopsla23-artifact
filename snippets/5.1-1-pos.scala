class C(var x: Int)
object A:
  var f: C = foo()
  @annotation.nowarn("msg=Infinite recursive call") def foo(): C = foo()