package kforth.support

import java.math.BigInteger

/**
 * Fraction class.
 */
class Fraction(numerator: BigInteger = BigInteger.ZERO, denominator: BigInteger = BigInteger.ONE) :
    Comparable<Fraction> {

    constructor(numerator: Int, denominator: Int = 1) : this(
        numerator.toBigInteger(),
        denominator.toBigInteger()
    )

    private val numerator = if (denominator.signum() == -1) {
        -numerator
    } else {
        numerator
    }
    private val denominator = denominator.abs()
    private val cachedGcd: BigInteger by lazy { this.numerator.gcd(this.denominator) }

    companion object {
        @JvmStatic
        fun valueOf(string: String): Fraction {
            val partitions = string.split('/')
            return Fraction(BigInteger(partitions.first()), BigInteger(partitions.last()))
        }
    }

    fun normalize(): Fraction {
        return Fraction(numerator / cachedGcd, denominator / cachedGcd)
    }

    fun inverse(): Fraction {
        return Fraction(denominator, numerator)
    }

    fun ceil(): Fraction {
        return Fraction(denominator * ((numerator / denominator) + BigInteger.ONE), denominator)
    }

    fun floor(): Fraction {
        return Fraction(denominator * (numerator / denominator), denominator)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            is String -> this == valueOf(other)
            is Fraction -> {
                val normalized = this.normalize()
                val o = other.normalize()
                normalized.numerator == o.numerator && normalized.denominator == o.denominator
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

    operator fun plus(other: Fraction): Fraction {
        val cnm = (numerator * other.denominator) + (other.numerator * denominator)
        val cdm = denominator * other.denominator
        return Fraction(cnm, cdm)
    }

    operator fun unaryPlus(): Fraction {
        return this
    }

    operator fun minus(other: Fraction): Fraction {
        val cnm = (numerator * other.denominator) - (other.numerator * denominator)
        val cdm = denominator * other.denominator
        return Fraction(cnm, cdm)
    }

    operator fun unaryMinus(): Fraction {
        return Fraction(-numerator, denominator)
    }

    operator fun times(other: Fraction): Fraction {
        return Fraction(numerator * other.numerator, denominator * other.denominator)
    }

    operator fun div(other: Fraction): Fraction {
        return Fraction(numerator * other.denominator, denominator * other.numerator)
    }

    operator fun rem(other: Fraction): Fraction {
        val q = (this / other).floor()
        val tmp = q * other
        return this - tmp
    }

    override fun compareTo(other: Fraction): Int {
        val nod = numerator.multiply(other.denominator)
        val don = denominator.multiply(other.numerator)
        return when {
            nod > don -> -1
            don > nod -> 1
            else -> 0
        }
    }

    @SuppressWarnings("unused")
    private fun lcm(other: Fraction): BigInteger {
        val gcd = denominator.gcd(other.denominator)
        return (denominator * other.denominator).abs() / gcd
    }

    fun toInt() = (numerator / denominator).toInt()

}

fun main() {
    val f = Fraction(5, 7)
    println((f * f - f + f.inverse()).normalize())
}
