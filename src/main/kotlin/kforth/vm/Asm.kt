package kforth.vm

enum class Asm {
    JMP,
    ZJP,
    CALL,
    RET,
    CONST,
    DUP,
    DROP,
    LDA,
    STA,
    LDB,
    STB,
    SPAWN,
    READ,
    SEND,
    BROADCAST,
    ADD,
    NEGATE,
    MULTIPLY,
    MODULO,
    ;
}