object Positioned:
  var debug: Boolean = false
  var debugId = Int.MinValue
  var nextId: Int = 0

abstract class Positioned:
  if (Positioned.debug) { // error
    println("do debugging")
  }

object Trees:
  class Tree extends Positioned
  val emptyTree = new Tree