package kforth.vm

import java.math.BigInteger

class Vm {
    private var nextThreadId = 0
    private val threads: MutableMap<Int, VmThread> = mutableMapOf()
    private val running: MutableList<Int> = mutableListOf()
    private val stopping: MutableList<Int> = mutableListOf()

    fun send(recipient: Int, value: BigInteger) {
        threads[recipient]?.message(value)
    }

    fun broadcast(sender: Int, value: BigInteger) {
        running.filter { it != sender }.forEach { threads[it]?.message(value) }
    }

    fun run(code: Code, steps: Int = 50) {
        threads[nextThreadId] = VmThread(this, nextThreadId, code, steps)
        running.add(nextThreadId)
        nextThreadId += 1
    }

    fun step() {
        println("-- vm step")
        running.forEach { threads[it]?.run() }
        running.removeAll(stopping)
        stopping.clear()
    }

    fun stop(thread: Int) = stopping.add(thread)
}