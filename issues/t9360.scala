class BaseClass(s: String) {
  def print: Unit = ()
}

object Obj {                           // error
  val s: String = "hello"

  object AObj extends BaseClass(s)     // error

  object BObj extends BaseClass(s)

  val list = List(AObj, BObj)

  def print = {
    println(list)
  }
}

object ObjectInit {
  def main(args: Array[String]) = {
    Obj.AObj.print
    Obj.BObj.print
    Obj.print
  }
}
