package kforth.misc

class MiscVm {
    private val ram: Array<Int> = Array(2048) { 0 }

    private var pc: Int
        get() = ram[0]
        set(value) { ram[0] = value }

    private fun dinc() { ram[1] = (ram[1] + 1) % 32 }
    private fun ddec() { ram[1] = (ram[1] - 1) % 32 }
    private fun rinc() { ram[2] = (ram[2] + 1) % 32 }
    private fun rdec() { ram[2] = (ram[2] - 1) % 32 }

    private var dtos: Int
        get() = ram[4 + ram[1]]
        set(value) { ram[4 + ram[1]] = value }

    private var rtos: Int
        get() = ram[36 + ram[2]]
        set(value) { ram[36 + ram[2]] = value }

    private var err: Int
        get() = ram[3]
        set(value) { ram[3] = value }

    fun run() {
        while (err >= 0) {
            when (ram[pc]) {
                0 -> {
                    // const
                    dinc()
                    dtos = ram[pc + 1]
                    pc += 2
                }
                1 -> {
                    pc = ram[pc + 1]
                    pc += 2
                }
                else -> {
                    pc = ram[68 + pc]
                }
            }
        }
    }
}