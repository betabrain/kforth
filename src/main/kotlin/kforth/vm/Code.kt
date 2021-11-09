package kforth.vm

import java.math.BigInteger

class Code(private val byteCode: List<BigInteger>) {
    fun address(ip: Int): Int = byteCode[ip].toInt()

    fun op(ip: Int): Asm? = byteCode.getOrNull(ip)?.let { Asm.values().getOrNull(it.toInt()) }

    fun value(ip: Int): BigInteger = byteCode[ip]

    override fun toString(): String =
        byteCode
            .joinToString("+") { it.toString(36) }
            .replace("+-", "-")
}
