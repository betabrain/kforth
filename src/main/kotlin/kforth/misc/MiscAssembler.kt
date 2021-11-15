package kforth.misc

class MiscAssembler {
    private val code = mutableListOf<MiscCode>()
    private val labels = mutableMapOf<String, Int>()

    private fun assemble(vararg v: Int): MiscAssembler {
        v.forEach { code.add(MiscCode.Value(it)) }
        return this
    }

    fun label(s: String): MiscAssembler {
        labels[s] = code.size
        return this
    }

    fun target(s: String): MiscAssembler {
        code.add(MiscCode.Value(0))
        code.add(MiscCode.Target(s))
        return this
    }

    fun assemble(): List<Int> = code.map {
        when (it) {
            is MiscCode.Target -> labels[it.name]!!
            is MiscCode.Value -> it.value
        }
    }

    override fun toString(): String = assemble().joinToString()

    //0	CONST	+1	Push value onto data stack.
    fun const(v: Int): MiscAssembler = assemble(0, v)
    //1	PC	+1	Push the program counter onto the data stack.
    fun lpc(): MiscAssembler = assemble(1)
    //2	PC!	-1	Set program counter to TOS of data stack.
    fun spc(): MiscAssembler = assemble(2)
    //3	SELECT	-3 +1	Apply <0 to value and leave one of two branch addresses on the data stack.
    fun select(): MiscAssembler = assemble(3)
    //4	PUSH	-1	Pop TOS of data stack and push onto return stack.
    fun push(): MiscAssembler = assemble(4)
    //5	POP	+1	Pop TOS of return stack and push onto data stack.
    fun pop(): MiscAssembler = assemble(5)
    //6	INV	-1 +1	Bit-wise invert the number at TOS.
    fun inv(): MiscAssembler = assemble(6)
    //7	AND	-2 +1	Bit-wise and the top two number on the data stack.
    fun and(): MiscAssembler = assemble(7)
    //8	XOR	-2 +1	Bit-wise xor the top two numbers on the data stack.
    fun xor(): MiscAssembler = assemble(8)
    //9	ADD	-2 +1	Add the top two numbers on the data stack.
    fun add(): MiscAssembler = assemble(9)
    //10	DUP	-1 +2	Duplicate the top of stack.
    fun dup(): MiscAssembler = assemble(10)
    //11	DROP	-1	Drop the top of stack.
    fun drop(): MiscAssembler = assemble(11)
    //12	OVER	+1	Push next on data stack on top of TOS.
    fun over(): MiscAssembler = assemble(12)
    //13	LOAD	-1 +1	Pop address from data stack and push value onto the data stack.
    fun load(): MiscAssembler = assemble(13)
    //14	STORE	-2	Store value at NEXT to address in TOS.
    fun store(): MiscAssembler = assemble(14)
    //15	RET	--	Return from call.
    fun ret(): MiscAssembler = assemble(15)
    //16-255	??	??	Call to one of 240 user defined words.
    fun udf(v: Int): MiscAssembler = assemble(v)

}