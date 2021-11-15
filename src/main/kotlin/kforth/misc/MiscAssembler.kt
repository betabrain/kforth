package kforth.misc

class MiscAssembler {
    private val code = mutableListOf<MiscCode>()

    private fun assemble(vararg v: Int): MiscAssembler {
        v.forEach { code.add(MiscCode.Value(it)) }
        return this
    }

    //0	CONST	+1	Push value onto data stack.
    fun const(v: Int): MiscAssembler = assemble(0, v)
    //1	PC	+1	Push the program counter onto the data stack.
    fun lpc(): MiscAssembler = assemble(1)
    //2	PC!	-1	Set program counter to TOS of data stack.
    fun spc(): MiscAssembler = assemble(2)
    //3	SELECT	-3 +1	Apply <0 to value and leave one of two branch addresses on the data stack.
    fun select(): MiscAssembler = assemble(3)
    //4	PUSH	-1	Pop TOS of data stack and push onto return stack.
    //5	POP	+1	Pop TOS of return stack and push onto data stack.
    //6	INV	-1 +1	Bit-wise invert the number at TOS.
    //7	AND	-2 +1	Bit-wise and the top two number on the data stack.
    //8	XOR	-2 +1	Bit-wise xor the top two numbers on the data stack.
    //9	ADD	-2 +1	Add the top two numbers on the data stack.
    //10	DUP	-1 +2	Duplicate the top of stack.
    //11	DROP	-1	Drop the top of stack.
    //12	OVER	+1	Push next on data stack on top of TOS.
    //13	LOAD	-1 +1	Pop address from data stack and push value onto the data stack.
    //14	STORE	-2	Store value at NEXT to address in TOS.
    //15	RET	--	Return from call.
    //16-255	??	??	Call to one of 240 user defined words.

}