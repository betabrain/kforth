package kforth.assembler

import kforth.vm.Asm
import java.math.BigInteger

sealed interface Value {
    data class Op(val op: Asm) : Value
    data class Const(val const: BigInteger) : Value
    data class Hole(val label: String) : Value
}
