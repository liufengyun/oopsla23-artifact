object Names:
  class Name(val start: Int, val length: Int)
  var chrs: Array[Char] = new Array[Char](0x20000)
  def name(s: String): Name = Name(0, chrs.length)
  val StdNames = new StdNames

class StdNames:
  val List: Names.Name = Names.name("List")