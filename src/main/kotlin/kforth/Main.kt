package kforth

import kforth.vm.Asm
import kforth.vm.Code
import kforth.vm.Vm

fun main() {
    val code = Code()
        .add(Asm.CONST, 5)
        .add(Asm.DUP)
        .add(Asm.ADD)

    val vm = Vm()
    println(code)
    vm.run(code)
    vm.run(code)

    val c2 = Code()

    c2
        .add(Asm.CONST, 1)
        .add(Asm.CONST, 2)
        .label("loop")
        .add(Asm.STA)
        .add(Asm.STB)
        .add(Asm.LDA)
        .add(Asm.LDB)
        .add(Asm.JMP, c2.resolve("loop"))

    println(c2)
    vm.run(c2)


    vm.step()
    vm.step()
    vm.step()
}
