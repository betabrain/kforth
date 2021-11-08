package kforth.vm

import java.math.BigInteger

class Vm {
    private val running: MutableList<VmThread> = mutableListOf()
    private val stopping: MutableList<VmThread> = mutableListOf()

    fun bcast(value: BigInteger) {
        running.forEach { it.send(value) }
    }

    fun run(code: Code) {
        running.add(VmThread(this, code))
    }

    fun step() {
        println("-- vm step")
        running.forEach { it.step() }
        running.removeAll(stopping)
        stopping.clear()
    }

    fun stop(thread: VmThread) = stopping.add(thread)
}