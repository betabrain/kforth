package kforth.mu

class Mu {
  enum class Ops {
    // System
    BYE, RX, TX,
    // Inner Interpreters
    LIT, GOTO, BRANCH, SELECT, EXECUTE, RETURN,
    // Memory Access
    // LOAD, STORE,
    // Return stack
    // PUSH, POP, RAT,
    // Data stack
    COUNT, DROP, DUP, SWAP, OVER,
    // Logic
    AND, OR, XOR,
    // Arithmetic
    ADD,
  }

  private val code = mutableListOf<Int>()
  private val dataStack = mutableListOf<Int>()
  private val returnStack = mutableListOf<Int>()
  private var wp = 0
  private var ip = 0

  fun run() {
    while (wp + ip < code.size) {
      var op1 = code[wp + ip]
      var op2 = Ops.values().firstOrNull { it.ordinal == op1 }
      println("$wp:$ip $op2 ($op1) - $dataStack $returnStack")
      when (code[wp + ip]) {
        Ops.BYE.ordinal -> { return }
        Ops.RX.ordinal -> { print("RX: "); dataStack.add((readLine() ?: "0").toInt()); ip += 1 }
        Ops.TX.ordinal -> { println("TX: " + dataStack.removeLast()); ip += 1 }
        Ops.LIT.ordinal -> { dataStack.add(code[wp + ip + 1]); ip += 2 }
        Ops.GOTO.ordinal -> { ip = code[wp + ip + 1] }
        Ops.BRANCH.ordinal -> { ip = if (dataStack.removeLast() < 0) { code[wp + ip + 1] } else { ip + 2 }  }
        Ops.SELECT.ordinal -> { dataStack.add(code[wp + ip + if (dataStack.removeLast() < 0) { 1 } else { 2 }]); ip += 3 }
        Ops.EXECUTE.ordinal -> { returnStack.add(wp); returnStack.add(ip); wp = dataStack.removeLast(); ip = 0; }
        Ops.RETURN.ordinal -> { ip = returnStack.removeLast() + 1; wp = returnStack.removeLast() }
        Ops.COUNT.ordinal -> { dataStack.add(dataStack.size); ip += 1 }
        Ops.DROP.ordinal -> { dataStack.removeLast(); ip += 1 }
        Ops.DUP.ordinal -> { dataStack.add(dataStack.last()); ip += 1 }
        Ops.SWAP.ordinal -> { dataStack.add(dataStack.size - 2, dataStack.removeLast()); ip += 1 }
        Ops.OVER.ordinal -> { dataStack.add(dataStack.elementAt(dataStack.size - 2)); ip += 1 }
        Ops.ADD.ordinal -> { dataStack.add(dataStack.removeLast() + dataStack.removeLast()); ip += 1 }
        else -> {}
      }
    }
  }

  fun assemble(op: Ops, vararg args: Int): Mu {
    code.add(op.ordinal)
    code.addAll(args.toList())
    return this
  }
}

fun main() {
  val m = Mu()
    .assemble(Mu.Ops.LIT, 5) // 0
    .assemble(Mu.Ops.DUP) // 2
    .assemble(Mu.Ops.BRANCH, 12) // 3
    .assemble(Mu.Ops.DUP) // 5
    .assemble(Mu.Ops.TX) // 6
    .assemble(Mu.Ops.LIT, -1) // 7
    .assemble(Mu.Ops.ADD) // 9
    .assemble(Mu.Ops.GOTO, 2) // 10
    .assemble(Mu.Ops.BYE) // 12
    .run()
}