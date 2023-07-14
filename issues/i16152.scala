final case class Foo(name: String)

object Foo {
  object Foos {
    val Name1 = Foo("Apple")
    val Name2 = Foo("Banana")
    val Name3 = Foo("Cherry")
    // Using explicit `new`s prevents the issue
    // val Name1 = new Foo("Apple")
    // val Name2 = new Foo("Banana")
    // val Name3 = new Foo("Cherry")
  }

  val allFoos: Seq[Foo] = {
    import Foos._
    Seq(Name1, Name2, Name3)
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    println(Foo.Foos.Name1)
    println(Foo.allFoos)
    // Accessing in the reverse order (or not accessing Name1 at all) prevents the issue
    // println(Foo.allFoos)
    // println(Foo.Foos.Name1)
  }
}
