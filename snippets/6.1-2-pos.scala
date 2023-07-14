// This snippet is 6.1-1-neg.scala modified with the fix from the code in the paper
object Names:
  class Name(val start: Int, val length: Int)
  var chrs: Array[Char] = new Array[Char](0x20000)
  def name(s: String): Name = Name(0, chrs.length)
  val StdNames = new StdNames

class StdNames:
  val AnyRef: Names.Name = Names.name("AnyRef")
  val Array: Names.Name = Names.name("Array")
  val List: Names.Name = Names.name("List")