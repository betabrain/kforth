package kforth.vm

import java.math.BigInteger

class VmThread(
    private val vm: Vm,
    private val id: BigInteger,
    private val code: Code,
    private val steps: Int = 100
) {
    // control flow
    private var ip: Int = 0
    private val returnStack = mutableListOf<BigInteger>()

    // locals & constants
    private val dataStack = mutableListOf<BigInteger>()
    private var a = BigInteger.ZERO
    private var b = BigInteger.ZERO

    // ipc
    private val mbox = mutableListOf<BigInteger>()
    fun message(value: BigInteger) = mbox.add(value)

    fun run() {
        var count = 0
        while (count < steps) {
            count += 1
            val instruction = code.op(ip)
            println("[$id] $count/$steps: ip=$ip a=$a b=$b stack=$dataStack rstack=$returnStack  --  $instruction")
            when (instruction) {
                null -> {
                    vm.stop(id)
                    ip = -1
                    return
                }
                Asm.JMP -> {
                    ip = code.address(ip + 1)
                }
                Asm.ZJP -> {
                    ip = if (dataStack.removeLast() == BigInteger.ZERO) {
                        code.address(ip + 1)
                    } else {
                        ip + 2
                    }
                }
                Asm.CALL -> {
                    returnStack.add(ip.toBigInteger())
                    returnStack.add(a)
                    returnStack.add(b)
                    ip = code.address(ip + 1)
                }
                Asm.RET -> {
                    b = returnStack.removeLast()
                    a = returnStack.removeLast()
                    ip = returnStack.removeLast().toInt()
                }
                Asm.CONST -> {
                    dataStack.add(code.value(ip + 1))
                    ip += 2
                }
                Asm.DUP -> {
                    dataStack.add(dataStack.last())
                    ip += 1
                }
                Asm.DROP -> {
                    dataStack.removeLast()
                    ip += 1
                }
                Asm.LDA -> {
                    dataStack.add(a)
                    ip += 1
                }
                Asm.STA -> {
                    a = dataStack.removeLast()
                    ip += 1
                }
                Asm.LDB -> {
                    dataStack.add(b)
                    ip += 1
                }
                Asm.STB -> {
                    b = dataStack.removeLast()
                    ip += 1
                }
                Asm.READ -> {
                    if (mbox.isNotEmpty()) {
                        dataStack.add(mbox.removeFirst())
                        ip += 1
                    } else {
                        return
                    }
                }
                Asm.SEND -> {
                    val msg = dataStack.removeLast()
                    vm.send(dataStack.removeLast(), msg)
                    ip += 1
                }
                Asm.BROADCAST -> {
                    vm.broadcast(id, dataStack.removeLast())
                    ip += 1
                }
                Asm.ADD -> {
                    dataStack.add(dataStack.removeLast() + dataStack.removeLast())
                    ip += 1
                }
            }
        }
    }
}
