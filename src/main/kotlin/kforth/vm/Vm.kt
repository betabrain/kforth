package kforth.vm

import java.math.BigInteger

class Vm {
    private var nextThreadId = BigInteger.ZERO
    private val threads: MutableMap<BigInteger, VmThread> = mutableMapOf()
    private val running: MutableList<BigInteger> = mutableListOf()
    private val stopping: MutableList<BigInteger> = mutableListOf()
    var debug = false

    fun send(recipient: BigInteger, value: BigInteger) {
        threads[recipient]?.message(value)
    }

    fun broadcast(sender: BigInteger, value: BigInteger) {
        running.filter { it != sender }.forEach { threads[it]?.message(value) }
    }

    fun run(code: Code, id: BigInteger? = null, steps: Int = 50): BigInteger {
        val tid = if (id == null) {
            nextThreadId += BigInteger.ONE; nextThreadId
        } else {
            id
        }
        threads[tid] = VmThread(this, tid, code, steps)
        running.add(tid)
        return tid
    }

    fun step() {
        println("-- vm step")
        running.forEach { threads[it]?.run() }
        running.removeAll(stopping)
        stopping.clear()
    }

    fun stop(thread: BigInteger) = stopping.add(thread)
}