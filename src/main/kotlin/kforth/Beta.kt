package kforth

import kforth.support.Fraction

class Beta {
    data class StackFrame(
        var ip: Int = 0,
        val locals: MutableList<Fraction> = mutableListOf(
            Fraction(),
            Fraction(),
            Fraction(),
            Fraction(),
            Fraction(),
            Fraction()
        )
    )

    fun execute(vararg code: Int) {
        println(code.map { it.toString(36) }.joinToString("."))

        val returnStack = mutableListOf(StackFrame())
        val dataStack = mutableListOf<Fraction>()

        fun code(offset: Int = 0) = code[returnStack.last().ip + offset]
        fun incrIp(n: Int = 1) {
            returnStack.last().ip += n
        }

        while (true) {
            println("before: ${returnStack.last()} - $dataStack")

            when (code()) {
                //| Code Hex | Base 36 | Name    | Args | Stack | Meaning |
                //| -------- | ------- | ------- | ---- | ----- | ------- |
                //| 0x00     | 0       | HALT    | 0    |       | Stop VM |
                0x00 -> {
                    return
                }
                //| 0x01     | 1       | CONST   | 1    | +1    | Push a constant value |
                0x01 -> {
                    dataStack.add(Fraction(code(1))); incrIp(2)
                }
                //| 0x02     | 2       | DUP     | 0    | -1 +2 | Duplicate TOS |
                0x02 -> {
                    dataStack.add(dataStack.last()); incrIp()
                }
                //| 0x03     | 3       | DROP    | 0    | -1    | Drop TOS |
                0x03 -> {
                    dataStack.removeLast(); incrIp()
                }
                //| 0x04     | 4       | STACK?  |      | +1    | Checks whether stack is empty and pushes result |
                0x04 -> {
                    dataStack.add(
                        if (dataStack.isEmpty()) {
                            Fraction(1)
                        } else {
                            Fraction(-1)
                        }
                    ); incrIp()
                }
                //| 0x05     | 5       | LOAD    | 1    | +1    | Push the value of local variable |
                0x05 -> {
                    dataStack.add(returnStack.last().locals[code(1)]); incrIp(2)
                }
                //| 0x06     | 6       | STORE   | 1    | -1    | Store TOS into a local variable |
                0x06 -> {
                    returnStack.last().locals[code(1)] = dataStack.removeLast(); incrIp(2)
                }
                //| 0x07     | 7       | GOTO    | 1    |       | Jump to location |
                0x07 -> {
                    returnStack.last().ip = code(1)
                }
                //| 0x08     | 8       | BRANCH? | 1    |       | Jump if TOS is positive |
                0x08 -> {
                    if (dataStack.last() > Fraction(0)) {
                        returnStack.last().ip = code(1)
                    } else {
                        incrIp(2)
                    }
                }
                //| 0x09     | 9       | STATIC  | 1    |       | Call location |
                0x09 -> {
                    returnStack.add(returnStack.last().copy()); returnStack.last().ip = code(1)
                }
                //| 0x0A     | A       | DYNAMIC | 0    | -1    | Call TOS |
                0x0A -> {
                    returnStack.add(returnStack.last().copy()); returnStack.last().ip =
                        dataStack.removeLast().toInt()
                }
                //| 0x0B     | B       | RETURN  | 0    |       | Pop return stack |
                0x0B -> {
                    returnStack.removeLast()
                }
                //| 0x0C     | C       | PLUS    | 0    | -2 +1 | Sum two values |
                //| 0x0D     | D       | NEGATE  | 0    | -1 +1 | Negate the sign of a value |
                //| 0x0E     | E       | TIMES   | 0    | -2 +1 | Multiply two values |
                //| 0x0F     | F       | INVERSE | 0    | -1 +1 | Inverse numerator and denominator |
                //| 0x10     | G       | NUM     | 0    | -1 +1 | Push the numerator |
                //| 0x11     | H       | DENOM   | 0    | -1 +1 | Push the denominator |
                //| 0x12     | I       | NORM    | 0    | -1 +1 | Normalize TOS |
                //| 0x13     | J       | GT      | 0    | -2 +1 | Push result of greater comparison |
                else -> TODO()
            }

            println("after: ${returnStack.last()} - $dataStack")
        }
    }
}

fun main() {
    val test = Beta()
    test.execute(
        // const 5
        1, 5,
        // const 2
        1, 2,
        // plus
        12
    )
}
