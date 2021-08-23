package kforth

import java.util.*

class KForth {
  // Words
  private val dict: MutableList<Pair<String, Int>> = mutableListOf()
  private val builtins: MutableMap<Int, () -> Unit> = mutableMapOf()
  private val immediates: MutableSet<Int> = mutableSetOf()

  // State
  private val stack: MutableList<Int> = mutableListOf()
  private val rstack: MutableList<Int> = mutableListOf()
  private val heap: MutableList<Int> = mutableListOf()
  private var ip: Int = 0
  private var base: Int = 10
  private var compiling: Boolean = false
  private var scanner: Scanner? = null

  // Stop Word
  private var stop: Int = -1

  init {
    builtin("stop") {}
    stop = lookupWord("stop")!!

    // Utilities
    builtin(".s", immediate = true) { println("stack: $stack") }
    builtin(".r", immediate = true) { println("rstack: $rstack") }
    builtin(".c", immediate = true) { println("compiling: $compiling") }
    builtin("\\", immediate = true) {
      val delimiter = scanner!!.delimiter()
      scanner!!.useDelimiter("\n")
      scanner!!.next()
      scanner!!.useDelimiter(delimiter)
    }
    builtin("words") {
      for (word in dict.sortedBy { it.first }) {
        println("${word.first} ${word.second}")
      }
    }
    builtin("dump") {
      val labels: MutableMap<Int, String> = mutableMapOf()
      val segments: MutableSet<Int> = mutableSetOf(0, heap.size)
      for (word in dict.filter { it.second >= 0 }) {
        labels[word.second] = word.first
        segments.add(word.second)
        segments.add(word.second + heap.slice(word.second until heap.size).takeWhile { it != stop }.size + 1)
      }
      for (segment in segments.sorted().zipWithNext()) {
        var start = segment.first
        var label = labels[start] ?: "<data>"
        for (chunk in heap.subList(segment.first, segment.second).chunked(4)) {
          val bytes = chunk.joinToString(" | ") { "%08x  %+-12d".format(it, it) }
          println("%16s | %06d-%06d | $bytes".format(label, start, start + chunk.size - 1))
          start += chunk.size
          label = ""
        }
      }
    }

    // Colon Words
    builtin(":") { dict.add(scanner?.next()!! to heap.size); compiling = true }
    builtin(";", immediate = true) { heap.add(stop); compiling = false }
    builtin("lit") { stack.add(heap[ip++]) }
    builtin("immediate", immediate = true) { immediates.add(dict.last().second) }
    builtin("jmp#f") { ip += if (stack.removeLast() == 1) 1 else heap[ip] }
    builtin("jmp") { ip += heap[ip] }
    builtin("variable") {
      dict.add(scanner?.next()!! to heap.size)
      heap.addAll(listOf(lookupWord("lit")!!, heap.size + 3, stop, 0))
    }
    builtin("constant") {
      dict.add(scanner?.next()!! to heap.size)
      heap.addAll(listOf(lookupWord("lit")!!, stack.removeLast(), stop))
    }
    builtin("'") { stack.add(lookupWord(scanner?.next()!!)!!) }
    builtin("exec") { execute(stack.removeLast()) }
    builtin("[", immediate = true) { compiling = false }
    builtin("]") { compiling = true }
    builtin("mark") { dict.add(scanner?.next()!! to heap.size) }

    // Stack Manipulation
    builtin("drop") { stack.removeLast() }
    builtin("dup") { stack.add(stack.last()) }
    builtin("swap") {
      val a = stack.removeLast()
      val b = stack.removeLast(); stack.add(a); stack.add(b)
    }
    builtin("clear") { stack.clear() }
    builtin(">r") { rstack.add(stack.removeLast()) }
    builtin("r>") { stack.add(rstack.removeLast()) }
    builtin("i") { stack.add(rstack.last()) }
    builtin("j") { stack.add(rstack[rstack.size - 2]) }
    eval(": nip swap drop ;")
    eval(": over >r dup r> swap ;")
    eval(": 2dup over over ;")
    eval(": 2drop drop drop ;")
    eval(": rot >r swap r> swap ;")
    eval(": -rot swap >r swap r> ;")
    eval(": tuck swap over ;")

    // Math
    builtin("base!") { base = stack.removeLast() }
    builtin("base@") { stack.add(base) }
    builtin("+") { stack.add(stack.removeLast() + stack.removeLast()) }
    builtin("-") { val top = stack.removeLast(); stack.add(stack.removeLast() - top) }
    builtin("*") { stack.add(stack.removeLast() * stack.removeLast()) }
    builtin("/") { val top = stack.removeLast(); stack.add(stack.removeLast() / top) }
    eval(": oct 8 base! ;")
    eval(": dec 10 base! ;")
    eval(": hex 16 base! ;")
    eval(": 1+ 1 + ;")
    eval(": 1- 1 - ;")

    // Logic
    builtin("and") { stack.add(if (stack.removeLast() == 1 && stack.removeLast() == 1) 1 else 0) }
    builtin("or") { stack.add(if (stack.removeLast() == 1 || stack.removeLast() == 1) 1 else 0) }
    builtin("not") { stack.add(if (stack.removeLast() == 1) 0 else 1) }
    builtin("=") { stack.add(if (stack.removeLast() == stack.removeLast()) 1 else 0) }
    builtin("<") { stack.add(if (stack.removeLast() > stack.removeLast()) 1 else 0) }
    builtin("true") { stack.add(1) }
    builtin("false") { stack.add(0) }
    eval(": != = not ;")
    eval(": >= < not ;")
    eval(": <= swap < not ;")
    eval(": > swap < ;")

    // Heap Manipulation
    builtin("here") { stack.add(heap.size) }
    builtin("!") { heap[stack.removeLast()] = stack.removeLast() }
    builtin("@") { stack.add(heap[stack.removeLast()]) }
    builtin(",") { heap.add(stack.removeLast()) }
    builtin("allot") {
      for (i in 0 until stack.removeLast()) {
        heap.add(0)
      }
    }

    // Input / Output
    builtin(".") { println(stack.removeLast().toString(base)) }
    builtin("emit") { print(stack.removeLast().toChar()) }
    builtin("key", immediate = true) { stack.add(scanner!!.next().codePointAt(0)) }

    // Control Flow
    eval(": if immediate lit jmp#f , here 0 , ;")
    eval(": else immediate lit jmp , here 0 , swap here over - swap ! ;")
    eval(": then immediate here over - swap ! ;")
    eval(": begin immediate here ;")
    eval(": while immediate lit jmp#f , here 0 , ;")
    eval(": repeat immediate swap lit jmp , here - ,  here over - swap ! ;")
    eval(": until immediate lit jmp#f , here - , ;")
    eval(": do immediate lit swap , lit >r , lit >r , 0 here ;")
    eval(": unloop r> r> r> 2drop >r ;")
    eval(": loop immediate")
    eval("    lit r> , lit 1+ , lit >r ,")
    eval("    lit i , lit j , lit >= ,")
    eval("    lit jmp#f , here - ,")
    eval("    here over - swap !")
    eval("    lit unloop , ;")

    // Convenience
    eval(": min 2dup < if drop else nip then ;")
    eval(": max 2dup < if nip else drop then ;")
    eval(": +! tuck @ + swap ! ;")

    // Test
    //    eval("variable example-val")
    //    eval(": example-do 0 do i example-val +! loop ;")
    //    eval(": example-until begin dup example-val +! 1 - dup 0 < until drop ;")
    //    eval(": example-while begin dup 0 >= while dup example-val +! 1 - repeat drop ;")
    //    eval("55 example-do 35 example-until 15 example-while")
  }

  fun eval(source: String) {
    scanner = Scanner(source)
    while (scanner!!.hasNext()) {
      val name: String = scanner!!.next()
      val xt: Int? = lookupWord(name)
      try {
        when (compiling) {
          false -> if (xt != null) execute(xt) else stack.add(name.toInt(base))
          true -> if (xt != null) {
            if (xt in immediates) {
              execute(xt)
            } else heap.add(xt)
          } else {
            heap.add(lookupWord("lit")!!)
            heap.add(name.toInt(base))
          }
        }
      } catch (ex: RuntimeException) {
        println("error: $ex")
        ex.printStackTrace()
        stack.clear()
        rstack.clear()
        return
      }
    }
  }

  fun repl() {
    while (true) {
      print("${stack.size}> ")
      eval(readLine() ?: "")
    }
  }

  fun pop(): Int = stack.removeLast()
  fun push(value: Int) = stack.add(value)
  fun stack(): List<Int> = stack.toList()

  private fun lookupWord(name: String): Int? = dict.findLast { it.first == name }?.second

  private fun builtin(name: String, immediate: Boolean = false, func: () -> Unit) {
    val xt = -builtins.size - 1
    dict.add(name to xt)
    builtins[xt] = func
    if (immediate) immediates.add(xt)
  }

  private fun execute(xt: Int) {
    if (xt < 0) {
      builtins[xt]!!.invoke()
    } else {
      rstack.add(ip)
      ip = xt
      var word = heap[ip++]
      while (stop != word) {
        execute(word)
        word = heap[ip++]
      }
      ip = rstack.removeLast()
    }
  }
}

fun main() {
  val kForth = KForth()
  kForth.repl()
}
