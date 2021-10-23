package kforth

import java.math.BigInteger

class Beta {
  private val stacks: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
  private val contexts: MutableList<MutableMap<String, (beta: Beta) -> Unit>> = mutableListOf(mutableMapOf())

  private fun find(symbol: String): ((beta: Beta) -> Unit)? {
    for (context in this.contexts.reversed()) {
      if (context.containsKey(symbol)) return context[symbol]
    }
    return null
  }

  fun debug() {
    for (stack in this.stacks) {
      println("     STACK: $stack")
    }
    for (context in this.contexts) {
      println("   CONTEXT: $context")
    }
    println()
  }

  fun define(symbol: String, definition: (beta: Beta) -> Unit) {
    this.contexts.last()[symbol] = definition
  }

  // mess with values on top stack
  fun add(vararg symbols: String) = this.stacks.last().addAll(symbols)

  // mess with stacks
  fun down() = this.stacks.add(mutableListOf())
  fun up() = this.stacks.removeLast()

  // eval
  fun evaluate(symbol: String) {
    this.find(symbol)?.invoke(this)
  }
}

fun main() {
  val test = Beta()
  test.define("+") { beta ->
    var result = BigInteger.ZERO
    beta.up().map { result += it.toBigInteger() }
    beta.down()
    beta.add(result.toString())
  }
  test.define("*") { beta ->
    var result = BigInteger.ONE
    beta.up().map { result *= it.toBigInteger() }
    beta.down()
    beta.add(result.toString())
  }
  test.define("merge") { beta ->
    var s2 = beta.up()
    var s1 = beta.up()
    beta.down()
    s1.map { beta.add(it) }
    s2.map { beta.add(it) }
  }

  test.add("-- first stack --")
  test.debug()

  test.down()
  test.add("12837", "767", "-827348")
  test.debug()

  test.evaluate("+")
  test.debug()

  test.down()
  test.add("12837", "767", "-827348")
  test.debug()

  test.evaluate("+")
  test.debug()

  test.evaluate("merge")
  test.debug()

  test.evaluate("*")
  test.debug()


//  test.add("Hello, World!")
//  test.down()
//  test.add("1245")
//  test.add("5421")
//  test.down()
//  test.add("1245")
//  test.add("5421")
//  test.add("1111")
//  test.down()
//  test.debug()
//  test.evaluate("+")
//  test.debug()
//  test.evaluate("*")
//  test.debug()
}