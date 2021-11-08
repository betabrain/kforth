# kForth VM

| Code Hex | Base 36 | Name    | Args | Stack | Meaning |
| -------- | ------- | ------- | ---- | ----- | ------- |
| 0x00     | 0       | HALT    | 0    |       | Stop VM |
| 0x01     | 1       | CONST   | 1    | +1    | Push a constant value |
| 0x02     | 2       | DUP     | 0    | -1 +2 | Duplicate TOS |
| 0x03     | 3       | DROP    | 0    | -1    | Drop TOS |
| 0x04     | 4       | EMPTY?  |      | +1    | Checks whether stack is empty and pushes result |
| 0x05     | 5       | LOAD    | 1    | +1    | Push the value of local variable |
| 0x06     | 6       | STORE   | 1    | -1    | Store TOS into a local variable |
| 0x07     | 7       | GOTO    | 1    |       | Jump to location |
| 0x08     | 8       | BRANCH? | 1    |       | Jump if TOS is zero |
| 0x09     | 9       | STATIC  | 1    |       | Call location |
| 0x0A     | A       | DYNAMIC | 0    | -1    | Call TOS | 
| 0x0B     | B       | RETURN  | 0    |       | Pop return stack | 
| 0x0C     | C       | PLUS    | 0    | -2 +1 | Sum two values | 
| 0x0D     | D       | NEGATE  | 0    | -1 +1 | Negate the sign of a value | 
| 0x0E     | E       | TIMES   | 0    | -2 +1 | Multiply two values | 
| 0x0F     | F       | INVERSE | 0    | -1 +1 | Inverse numerator and denominator | 
