package kforth.assembler

import kforth.vm.Asm
import kforth.vm.Code
import java.math.BigInteger

class Assembler {
    private val instructions: MutableList<Value> = mutableListOf()
    private val labels: MutableMap<String, Int> = mutableMapOf("_start" to 0, "_stop" to -1)

    fun assemble(): Code = Code(instructions
        .map {
            when (it) {
                is Value.Op -> it.op.ordinal.toBigInteger()
                is Value.Const -> it.const
                is Value.Hole -> labels[it.label]!!.toBigInteger()
            }
        }
    )

    private fun add(op: Asm, vararg values: Value): Assembler {
        instructions.add(Value.Op(op))
        instructions.addAll(values)
        return this
    }

    fun label(name: String): Assembler {
        labels[name] = instructions.size
        return this
    }

    fun jmp(to: String): Assembler = add(Asm.JMP, Value.Hole(to))
    fun zjp(to: String): Assembler = add(Asm.ZJP, Value.Hole(to))
    fun call(to: String): Assembler = add(Asm.CALL, Value.Hole(to))
    fun ret(): Assembler = add(Asm.RET)
    fun const(v: BigInteger): Assembler = add(Asm.CONST, Value.Const(v))
    fun const(v: Int): Assembler = const(v.toBigInteger())
    fun dup(): Assembler = add(Asm.DUP)
    fun drop(): Assembler = add(Asm.DROP)
    fun lda(): Assembler = add(Asm.LDA)
    fun sta(): Assembler = add(Asm.STA)
    fun ldb(): Assembler = add(Asm.LDB)
    fun stb(): Assembler = add(Asm.STB)
    fun spawn(to: String): Assembler = add(Asm.SPAWN, Value.Hole(to))
    fun read(): Assembler = add(Asm.READ)
    fun send(): Assembler = add(Asm.SEND)
    fun broadcast(): Assembler = add(Asm.BROADCAST)
    fun add(): Assembler = add(Asm.ADD)
    fun negate(): Assembler = add(Asm.NEGATE)
    fun multiply(): Assembler = add(Asm.MULTIPLY)
    fun modulo(): Assembler = add(Asm.MODULO)
}