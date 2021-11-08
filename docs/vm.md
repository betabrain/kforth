# kForth VM

## Principles
Code consists of a list of numbers.
These numbers are either instructions, jump locations, or data values.

Code is usually displayed as a "." separated string of base 36 encoded values.
This is because the instruction set aims to contain less than 36 instructions.

## Instructions

| Code Hex | Base 36 | Name    | Args | Stack | Meaning |
| -------- | ------- | ------- | ---- | ----- | ------- |
| 0x00     | 0       | HALT    | 0    |       | Stop VM |
| 0x01     | 1       | CONST   | 1    | +1    | Push a constant value |
| 0x02     | 2       | DUP     | 0    | -1 +2 | Duplicate TOS |
| 0x03     | 3       | DROP    | 0    | -1    | Drop TOS |
| 0x04     | 4       | STACK?  |      | +1    | Checks whether stack is empty and pushes result |
| 0x05     | 5       | LOAD    | 1    | +1    | Push the value of local variable |
| 0x06     | 6       | STORE   | 1    | -1    | Store TOS into a local variable |
| 0x07     | 7       | GOTO    | 1    |       | Jump to location |
| 0x08     | 8       | BRANCH? | 1    |       | Jump if TOS is positive |
| 0x09     | 9       | STATIC  | 1    |       | Call location |
| 0x0A     | A       | DYNAMIC | 0    | -1    | Call TOS | 
| 0x0B     | B       | RETURN  | 0    |       | Pop return stack | 
| 0x0C     | C       | PLUS    | 0    | -2 +1 | Sum two values | 
| 0x0D     | D       | NEGATE  | 0    | -1 +1 | Negate the sign of a value | 
| 0x0E     | E       | TIMES   | 0    | -2 +1 | Multiply two values | 
| 0x0F     | F       | INVERSE | 0    | -1 +1 | Inverse numerator and denominator |
| 0x10     | G       | NUM     | 0    | -1 +1 | Push the numerator |
| 0x11     | H       | DENOM   | 0    | -1 +1 | Push the denominator | 
| 0x12     | I       | NORM    | 0    | -1 +1 | Normalize TOS | 
| 0x13     | J       | GT      | 0    | -2 +1 | Push result of greater comparison |
| 0x14     | K       | NEW     | 0    | +1    | Push an empty table |
| 0x15     | L       | ADDL    | 0    | -2 +1 | Add value to left of table |
| 0x16     | M       | POPL    | 0    | -1 +1 | Pop value from the left of table |
| 0x17     | N       | ADDR    | 0    | -2 +1 | Add value to right of table |
| 0x18     | O       | POPR    | 0    | -1 +1 | Pop value from the right of table |
| 0x19     | P       | SET     | 0    | -3 +1 | Set value for key in table |
| 0x1A     | Q       | GET     | 0    | -2 +1 | Get value for key in table |
| 0x1B     | R       | FIRST   | 0    | -1 +1 | Set first key from table |
| 0x1C     | S       | NEXT    | 0    | -2 +1 | Get next key for table and key |
| 0x1D     | T       | LAST    | 0    | -1 +1 | Get last key from table |
| 0x1E     | U       | RES     | 0    |       | Reserved |
| 0x1F     | V       | RES     | 0    |       | Reserved |
| 0x20     | W       | RES     | 0    |       | Reserved |
| 0x21     | X       | RES     | 0    |       | Reserved |
| 0x22     | Y       | RES     | 0    |       | Reserved |
| 0x23     | Z       | RES     | 0    |       | Reserved |
