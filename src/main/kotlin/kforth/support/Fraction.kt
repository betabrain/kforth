package kforth.support

import java.math.BigInteger

/**
 * Fraction class.
 */
class Fraction(numerator: BigInteger, denominator: BigInteger): Number(), Comparable<Fraction> {

    private val numerator = if(denominator.signum() == -1) { - numerator } else { numerator }
    private val denominator = denominator.abs()

    private val cachedGcd: BigInteger by lazy { this.numerator.gcd(this.denominator) }

    companion object {
        /**
         * Factory method to create fractions from strings in "base#number" format.
         */
        @JvmStatic
        fun valueOf(string: String): Fraction {
            val (base, number) =
                (Parsers.ALL.values.map { it -> it(string) }.firstOrNull { it is Success }
                    ?: throw IllegalArgumentException("No valid parsers for $string")) as Success
            return valueOf(number, base.radix)
        }

        /**
         * Factory method to create fractions from number representation and radix.
         */
        @JvmStatic
        fun valueOf(string: String, radix: Int): Fraction {
            val partitions = string.replace("_", "").split('.')
            val(scale, unscaled) =
                when(partitions.size) {
                    1 -> Pair(0, partitions[0])
                    2 -> Pair(partitions[1].length, partitions.joinToString(""))
                    else -> throw IllegalArgumentException("Cannot have more than 1 dot in number string $string")
                }
            return Fraction(BigInteger(unscaled, radix), BigInteger.valueOf(radix.toLong()).pow(scale))
        }
    }

    /**
     * Reduce the fraction to the normalized version.
     */
    fun normalize(): Fraction {
        return Fraction(numerator / cachedGcd, denominator / cachedGcd)
    }

    /**
     * Get the inverse of this fraction such that this * this.inverse() = 1
     */
    fun inverse(): Fraction {
        return Fraction(denominator, numerator)
    }

    /**
     * Round up this fraction.
     */
    fun ceil(): Fraction {
        return Fraction( denominator * ((numerator / denominator) + BigInteger.ONE), denominator)
    }

    /**
     * Round down this fraction.
     */
    fun floor(): Fraction {
        return Fraction( denominator * (numerator / denominator), denominator)
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
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

    fun toString(radix: Int): String {
        return "$radix#${numerator.toString(radix)}/$radix#${denominator.toString(radix)}"
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

    override fun toDouble(): Double {
        return cachedDecimal.toDouble()
    }

    override fun toFloat(): Float {
        return cachedDecimal.toFloat()
    }

    override fun toInt(): Int {
        return cachedDecimal.toInt()
    }

    override fun toLong(): Long {
        return cachedDecimal.toLong()
    }

    override fun toShort(): Short {
        return cachedDecimal.toShort()
    }

    override fun toByte(): Byte {
        return toInt().toByte()
    }

    override fun toChar(): Char {
        return toInt().toChar()
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

}

fun main() {
    val f = Fraction(5, 7)
    println(f)
}
