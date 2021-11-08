package kforth.support

class Assembler {
}

fun main() {
    val asm = Assembler()

    val main = asm.label()
        .value(5)
        .value(2)
        .goto(sum)
        .halt()

    val sum = asm.label()
        .add()
        .goto(main)

    println(asm)
}
