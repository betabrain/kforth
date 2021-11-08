package kforth.vm

import java.math.BigInteger

class VmThread {
    // control flow
    private var ip: Int = 0
    fun jmp(next: Int) { ip = next }

    // locals & constants
    private val dataStack = mutableListOf<BigInteger>()
    private var a = BigInteger.ZERO
    private var b = BigInteger.ZERO
    fun const(value: Int) { dataStack.add(value.toBigInteger()) }
    fun lda() { dataStack.add(a) }
    fun sta() { a = dataStack.removeLast() }
    fun ldb() { dataStack.add(b) }
    fun stb() { b = dataStack.removeLast() }

    // numbers
    fun add() { dataStack.add(dataStack.removeLast() + dataStack.removeLast()) }

    // return stack stuff
    private val returnStack = mutableListOf<BigInteger>()
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
}