package kforth.asm

fun main() {
    for (instruction in Instructions.values()) {
        println("${instruction.ordinal.toString(36)} ${instruction.name} ${instruction.args.joinToString()}")
    }
}