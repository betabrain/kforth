package kforth

import kforth.vm.Asm
import kforth.vm.Code
import kforth.vm.Vm

fun main() {
    val code1 = Code()

    code1
        .add(Asm.CONST, 9)
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


    val vm = Vm()
    vm.run(code1)
    vm.run(code2)

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
