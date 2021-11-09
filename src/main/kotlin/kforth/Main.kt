package kforth

import kforth.vm.Asm
import kforth.vm.Code
import kforth.vm.Vm
import java.math.BigInteger

fun main() {
    val code1 = Code()

    code1
        .add(Asm.READ)
        .label("loop")
        .add(Asm.BROADCAST)
        .add(Asm.READ)
        .add(Asm.CONST, -1)
        .add(Asm.ADD)
        .add(Asm.DUP)
        .add(Asm.ZJP, -1)
        .add(Asm.JMP, code1.resolve("loop"))

    println(code1)

    val code2 = Code()

    code2
        .label("loop")
        .add(Asm.READ)
        .add(Asm.BROADCAST)
        .add(Asm.JMP, code2.resolve("loop"))

    println(code2)

    val code3 = Code()
        .add(Asm.CONST)
        .add(BigInteger("MAIN", 36))
        .add(Asm.CONST, 5)
        .add(Asm.SEND)

    println(code3)

    val code4 = Code()

    code4
        .add(Asm.CONST, 5)
        .label("func")
        .add(Asm.CONST, -1)
        .add(Asm.ADD)
        .add(Asm.DUP)
        .add(Asm.ZJP, -1)
        .add(Asm.CALL, code4.resolve("func"))


    val vm = Vm()
    vm.run(code1, id = BigInteger("MAIN", 36))
    vm.run(code2, id = BigInteger("ECHO", 36))
    vm.run(code3)
    vm.run(code4)

    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
    vm.step()
}
