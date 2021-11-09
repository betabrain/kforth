package kforth.vm

import java.math.BigInteger

class VmThread(
    private val vm: Vm,
    private val id: BigInteger,
    private val code: Code,
    private var ip: Int = 0,
    private val steps: Int = 100,
) {
    // control flow
    private val returnStack = mutableListOf<Frame>()

    // locals & constants
    private val dataStack = mutableListOf<BigInteger>()
    private var a = BigInteger.ZERO
    private var b = BigInteger.ZERO

    // ipc
    private val mbox = mutableListOf<BigInteger>()
    fun message(value: BigInteger) = mbox.add(value)

    fun run(): Int {
        var count = 0
        while (count < steps) {
            count += 1
            val instruction = code.op(ip)
            if (vm.debug)
                println("[$id] $count/$steps: ip=$ip a=$a b=$b stack=$dataStack rstack=$returnStack  --  $instruction")
            when (instruction) {
                null -> {
                    vm.stop(id)
                    ip = -1
                    return count
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
                    returnStack.add(Frame((ip + 2), a, b))
                    ip = code.address(ip + 1)
                }
                Asm.RET -> {
                    val frame = returnStack.removeLast()
                    ip = frame.ip
                    a = frame.a
                    b = frame.b
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
                Asm.SPAWN -> {
                    dataStack.add(vm.run(code, code.address(ip + 1)))
                    ip += 2
                }
                Asm.READ -> {
                    if (mbox.isNotEmpty()) {
                        dataStack.add(mbox.removeFirst())
                        ip += 1
                    } else {
                        return count
                    }
                }
                Asm.SEND -> {
                    val msg = dataStack.removeLast()
                    vm.send(id, dataStack.removeLast(), msg)
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
                Asm.NEGATE -> {
                    dataStack.add(-dataStack.removeLast())
                    ip += 1
                }
                Asm.MULTIPLY -> {
                    dataStack.add(dataStack.removeLast() * dataStack.removeLast())
                    ip += 1
                }
                Asm.MODULO -> {
                    val tmp = dataStack.removeLast()
                    dataStack.add(dataStack.removeLast().mod(tmp))
                    ip += 1
                }
            }
        }
        return count
    }
}
