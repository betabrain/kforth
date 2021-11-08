package kforth.vm

import java.math.BigInteger

class VmThread(private val vm: Vm, private val code: Code) {
    // control flow
    private var ip: Int = 0
    private val returnStack = mutableListOf<BigInteger>()
    private fun halt() { vm.stop(this) }
    private fun jmp(next: Int) { ip = next }
    private fun zjp(next: Int) { ip = if (dataStack.removeLast() == BigInteger.ZERO) { next } else { ip + 1 } }
    private fun call(next: Int) {
        returnStack.add(ip.toBigInteger())
        returnStack.add(a)
        returnStack.add(b)
        ip = next
    }
    private fun ret() {
        b = returnStack.removeLast()
        a = returnStack.removeLast()
        ip = returnStack.removeLast().toInt()
    }

    // locals & constants
    private val dataStack = mutableListOf<BigInteger>()
    private var a = BigInteger.ZERO
    private var b = BigInteger.ZERO
    private fun const(value: BigInteger) { dataStack.add(value); ip += 2 }
    private fun lda() { dataStack.add(a); ip += 1 }
    private fun sta() { a = dataStack.removeLast(); ip += 1 }
    private fun ldb() { dataStack.add(b); ip += 1 }
    private fun stb() { b = dataStack.removeLast(); ip += 1 }

    // ipc
    private val mbox = mutableListOf<BigInteger>()
    fun send(value: BigInteger) = mbox.add(value)
    private fun read() {
        if (mbox.isNotEmpty()) {
            dataStack.add(mbox.removeFirst())
            ip += 1
        }
    }
    private fun bcast() { vm.bcast(dataStack.removeLast()) }

    // numbers
    private fun add() { dataStack.add(dataStack.removeLast() + dataStack.removeLast()); ip += 1 }

    fun step() {
        println("[$this] step: ip=$ip a=$a b=$b stack=$dataStack --  ${code[ip]}")
        when (code[ip].toInt()) {
            0 -> halt()
            1 -> jmp(code[ip + 1].toInt())
            2 -> zjp(code[ip + 1].toInt())
            3 -> call(code[ip + 1].toInt())
            4 -> ret()
            5 -> const(code[ip + 1])
            6 -> lda()
            7 -> sta()
            8 -> ldb()
            9 -> stb()
            10 -> read()
            11 -> bcast()
            12 -> add()
            else -> TODO()
        }
    }
}