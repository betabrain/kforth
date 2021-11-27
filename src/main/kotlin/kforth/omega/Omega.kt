package kforth.omega

class Omega {
  private val idioms = mutableListOf<String>()
  val dictionary = mutableListOf<Word>()
  val stack = mutableListOf<Int>()
  private var compiling = false

  init {
    dictionary.add(Number)
    dictionary.add(NativeWord("+") { stack.add(stack.removeLast() + stack.removeLast()) })
    dictionary.add(NativeWord("*") { stack.add(stack.removeLast() * stack.removeLast()) })
    dictionary.add(NativeWord("dup") { stack.add(stack.last()) })
    dictionary.add(NativeWord(":") { dictionary.add(ColonWord(nextIdiom())); compiling = true })
    dictionary.add(object : Word {
      override fun handles(idiom: String): Boolean {
        return idiom == ";"
      }

      override fun execute(omega: Omega, idiom: String, token: Int) {
        TODO("Not yet implemented")
      }

      override fun immediate(omega: Omega, idiom: String, token: Int) {
        omega.compiling = false
      }
    })
  }

  fun run() {
    while (true) {
      val idiom = this.nextIdiom()
      val found = this.lookup(idiom)

      if (found != null) {
        println("idioms = $idioms, idiom = $idiom, word = ${found.value}, compiling = $compiling")
        dictionary.forEach(::println)
        if (compiling) {
          found.value.immediate(this, idiom, found.index)
        } else {
          found.value.execute(this, idiom, found.index)
        }
        println("stack = $stack")
      } else {
        println("error: $idiom unknown")
      }
    }
  }

  fun lookup(idiom: String): IndexedValue<Word>? {
    return dictionary.withIndex().findLast { it.value.handles(idiom) }
  }

  fun nextIdiom(): String {
    while (idioms.isEmpty()) {
      print("> ")
      readLine()?.split(Regex("\\s+"))?.apply { idioms.addAll(this) }
    }
    return idioms.removeFirst()
  }
}