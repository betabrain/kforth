package kforth.omega

interface Word {
  val name: String
  val token: Int

  fun handles(idiom: String): Boolean = name == idiom
  fun compile(omega: Omega, idiom: String): MutableList<Int> = mutableListOf(token)
  fun execute(omega: Omega, compiled: MutableList<Int>) {
    // do nothing
  }
}

object Number : Word {
  override val name: String = "<number>"
  override val token: Int = 0

  override fun handles(idiom: String): Boolean {
    return idiom.toIntOrNull() != null
  }

  override fun compile(omega: Omega, idiom: String): MutableList<Int> = mutableListOf(token, idiom.toInt())

  override fun execute(omega: Omega, compiled: MutableList<Int>) {
    omega.stack.add(compiled.removeFirst())
  }
}

class NativeWord(
  override val name: String,
  override val token: Int,
  private val block: Omega.() -> Unit
) : Word {
  override fun execute(omega: Omega, compiled: MutableList<Int>) {
    block.invoke(omega)
  }
}

class ImmediateWord(
  override val name: String,
  override val token: Int,
  private val block: Omega.() -> Unit
) : Word {
  override fun compile(omega: Omega, idiom: String): MutableList<Int> {
    block.invoke(omega)
    return mutableListOf()
  }
}

data class ColonWord(
  override val name: String,
  override val token: Int,
  val tokens: MutableList<Int> = mutableListOf()
) : Word {
  override fun execute(omega: Omega, compiled: MutableList<Int>) {
    omega.innerLoop(tokens.toMutableList())
  }
}

class Omega {
  private val idioms = mutableListOf<String>()
  val dictionary = mutableListOf<Word>()
  val stack = mutableListOf<Int>()
  var compiling = false

  init {
    // data types
    dictionary.add(Number)

    // math
    native("+") { stack.add(stack.removeLast() + stack.removeLast()) }
    native("-") { val tmp = stack.removeLast(); stack.add(stack.removeLast() - tmp) }
    native("*") { stack.add(stack.removeLast() * stack.removeLast()) }
    native("/") { val tmp = stack.removeLast(); stack.add(stack.removeLast() / tmp) }

    // stack
    native("dup") { stack.add(stack.last()) }
    native("swap") { stack.add(stack.size - 2, stack.removeLast()) }

    // definitions
    native(":") { dictionary.add(ColonWord(nextIdiom(), dictionary.size)); compiling = true }
    immediate(";") { compiling = false }
  }

  private fun native(name: String, block: Omega.() -> Unit) {
    dictionary.add(NativeWord(name, dictionary.size, block))
  }

  private fun immediate(name: String, block: Omega.() -> Unit) {
    dictionary.add(ImmediateWord(name, dictionary.size, block))
  }

  fun run() {
    while (true) {
      val idiom = this.nextIdiom()
      val found = this.lookup(idiom)

      if (found == null) {
        println("error: $idiom unknown")
        stack.clear()
        continue
      }

      val compiled = found.compile(this, idiom)

      if (compiling) {
        (dictionary.last() as ColonWord).tokens.addAll(compiled)
      } else {
        innerLoop(compiled)
      }
    }
  }

  fun innerLoop(compiled: MutableList<Int>) {
    while (compiled.isNotEmpty()) {
      val token = compiled.removeFirst()
      val word = lookup(token)
      if (word == null) {
        println("error: illegal token $token")
        stack.clear()
        return
      }
      word.execute(this, compiled)
    }
  }

  private fun lookup(idiom: String): Word? {
    return dictionary.findLast { it.handles(idiom) }
  }

  private fun lookup(token: Int): Word? {
    return dictionary.getOrNull(token)
  }

  private fun nextIdiom(): String {
    while (idioms.isEmpty()) {
      print("> ")
      readLine()?.split(Regex("\\s+"))?.apply { idioms.addAll(this) }
    }
    return idioms.removeFirst()
  }
}

fun main() {
  Omega().run()
}
