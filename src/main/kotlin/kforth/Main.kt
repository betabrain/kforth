package kforth

import kforth.vm.Code
import kforth.vm.Vm
import java.math.BigInteger

fun main() {
    val code: Code = listOf(
        BigInteger.ZERO
    )

    val vm = Vm()
    vm.run(code)
    vm.run(code)
    vm.run(listOf(5.toBigInteger(), BigInteger.ZERO, BigInteger.ZERO))

    vm.step()
    vm.step()
    vm.step()
}
