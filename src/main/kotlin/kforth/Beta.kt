package kforth

class Beta {
  data class StackFrame(var ip: Int = 0, val locals: MutableList<Int> = mutableListOf())

  fun execute(vararg code: Int) {
    println(code.map { it.toString(36) }.joinToString("."))

    val returnStack = mutableListOf(StackFrame())
    val dataStack = mutableListOf<Int>()

    while (true) {
      println("before: ${returnStack.last()} - $dataStack")

      when (code[returnStack.last().ip]) {
        0 -> { //0	0x00	0	FLOW CONTROL	HALT	0	0 -- 0	0 -- 0	Exits the VM.
          return
        }
        1 -> { //1	0x01	1	FLOW CONTROL	GOTO	1	0 -- 0	0 -- 0	Unconditional jump to another location in code
          returnStack.last().ip = code[returnStack.last().ip + 1]
        }
        2 -> { //2	0x02	2	FLOW CONTROL	BRANCH?	1	1 -- 0	0 -- 0	Conditional jump to another location in code, if TOS = -1
          if (dataStack.removeLast() == -1) {
            returnStack.last().ip = code[returnStack.last().ip + 1]
          } else {
            returnStack.last().ip += 2
          }
        }
        //3	0x03	3	FLOW CONTROL	CALL	2	0 -- 0	0 -- 1?
        //4	0x04	4	FLOW CONTROL	DYNCALL	1	1 -- 0	0 -- 1?
        //5	0x05	5	FLOW CONTROL	RETURN	0	0 -- 0	1 -- 0
        //6	0x06	6	FLOW CONTROL	SAVE_STATE	0	0 -- 1	?
        //7	0x07	7	FLOW CONTROL	LOAD_STATE	0	1 -- 0	?
        //8	0x08	8	LOCALS	LOAD	1	0 -- 1	 0 -- 0	Load value from stack frame.
        //9	0x09	9	LOCALS	STORE	1	1 -- 0	0 -- 0	Store value to stack frame.
        10 -> { //10	0x0A	A	DATA STACK	VALUE	1	0	1	Pushes a value onto the stack
          dataStack.add(code[returnStack.last().ip + 1])
          returnStack.last().ip += 2
        }
        //11	0x0B	B	DATA STACK	DUP	0	1	2	Duplicates the top of stack
        11 -> {
          dataStack.add(dataStack.last())
          returnStack.last().ip += 1
        }
        //12	0x0C	C	DATA STACK	EMPTY?	0	0	1	Indicates whether the stack is empty
        //13	0x0D	D	DATA STACK	DROP	0	1	0	Drops the top of stack
        //14	0x0E	E	LOGIC	LESS	0	2	1
        //15	0x0F	F	LOGIC	GREATER	0	2	1
        //16	0x10	G	LOGIC	NOT	0	1	1
        //17	0x11	H	LOGIC	AND	0	2	1
        //18	0x12	I	LOGIC	OR	0	2	1
        //19	0x13	J	NUMBERS	PLUS		2	1
        20 -> { //20	0x14	K	NUMBERS	MINUS		2	1
          val tmp = dataStack.removeLast()
          dataStack.add(dataStack.removeLast() - tmp)
          returnStack.last().ip += 1
        }
        //21	0x15	L	NUMBERS	TIMES		2	1
        //22	0x16	M	NUMBERS	INVERSE		2	1
        //23	0x17	N	NUMBERS	DIVREM		2	1
        //24	0x18	O	NUMBERS	POWER		2	1
        //25	0x19	P	TABLES	NEW	0	0	1
        //26	0x1A	Q	TABLES	WRITE	0	3	0
        //27	0x1B	R	TABLES	READ	0	2	1
        //28	0x1C	S	TABLES	ADD LEFT
        //29	0x1D	T	TABLES	POP LEFT
        //30	0x1E	U	TABLES	ADD RIGHT
        //31	0x1F	V	TABLES	POP RIGHT
        //32	0x20	W
        //33	0x21	X	TABLES	FIRST
        //34	0x22	Y	TABLES	NEXT
        //35	0x23	Z	TABLES	LAST
        else -> TODO()
      }

      println("after: ${returnStack.last()} - $dataStack")
    }
  }
}

fun main() {
  val test = Beta()
  test.execute(0)
  test.execute(1, 2, 0)
  test.execute(
    // value 5
    10, 5,
    // dup
    11,
    // branch? 9
    2, 10,
    // value 1
    10, 1,
    // minus
    20,
    // goto
    1, 2,
    //halt
    0
  )
}
