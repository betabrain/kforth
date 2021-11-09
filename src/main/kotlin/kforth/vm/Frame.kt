package kforth.vm

import java.math.BigInteger

data class Frame(val ip: Int, val a: BigInteger, val b: BigInteger)
