package kforth

import kforth.vm.Code
import kforth.vm.Vm

fun main() {
    val code = Code()

    val vm = Vm()
    vm.load(code)
}
