package kforth.misc

sealed interface MiscCode {
    data class Target(val name: String) : MiscCode
    data class Value(val value: Int) : MiscCode
}
