package kforth.vm

import java.math.BigInteger

class Code {
    private val byteCode: MutableList<BigInteger> = mutableListOf()
    private val labels: MutableMap<String, Int> = mutableMapOf()

    fun add(op: Asm, vararg args: Int): Code {
        byteCode.add(op.ordinal.toBigInteger())
        args.forEach { byteCode.add(it.toBigInteger()) }
        return this
    }

    fun add(code: BigInteger): Code {
        byteCode.add(code)
        return this
    }

    fun label(name: String): Code {
        labels[name] = byteCode.size
        return this
    }

    fun resolve(name: String): Int = labels[name]!!

    fun address(ip: Int): Int = byteCode[ip].toInt()

    fun op(ip: Int): Asm? = byteCode.getOrNull(ip)?.let { Asm.values().getOrNull(it.toInt()) }

    fun value(ip: Int): BigInteger = byteCode[ip]

    override fun toString(): String = byteCode.joinToString { it.toString() }
}
