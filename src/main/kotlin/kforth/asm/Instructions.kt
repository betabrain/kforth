package kforth.asm

enum class Instructions(vararg val args: String) {
    HALT,
    CONSTANT("value"),
    DUP,
    DROP,
    LOAD_A,
    STORE_A,
    LOAD_B,
    STORE_B,
    LOAD_C,
    STORE_C,
    LOAD_D,
    STORE_D,
    JUMP,
    BRANCH("target"),
    CALL("target"),
    RETURN,
    PLUS,
    NEGATE,
    MULTIPLY,
    DIVIDE,
    MODULO,
}

