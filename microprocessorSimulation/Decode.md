## Instruction formats

Instructions are 1, 2, or 3 bytes in length. The first byte uniquely identifies the instruction and is referred to as the opcode. Two byte instructions use the second byte is an immediate value, and three byte instructions use the second and third bytes to contain a memory address.

Each instruction takes up to three memory cycles to complete. The first memory cycle always obtains a value from memory and decodes it as an instruction. Additional operations may be executed in the first cycle.

## Control signals

Control signals are used to indicate what an instruction needs to do in each T-state. The DecodeControl struct contains an array of three control values for each T-state. Some instructions require source and destination register indexes. The indexes are stored as variables in the struct. When a control signals indicates an ALU operation should occur what operation to execute is stored as a value in the struct. Conditional jumps require an indication of what the condition and this is stored in a variable in the struct.

For all T-states there are two shared signals
* SKIP
* IDLE
The SKIP signal indicates that the T-state should not be executed, and IDLE indicates the state should execute but result in no changes.

The T1 state sends the lower byte of a memory address to memory. The control signals for this are:
* PCL_OUT - Send low byte from program counter
* REGL_OUT - Send value from scratch pad register L

The T2 state does the same task as T1 but for the high byte of a memory address. The control signals for this are:
* PCH_OUT - Send high byte from program counter
* REGH_OUT - Send value from scratch pad register H

The T3 state reads or writes data from memory using the address sent in T1 and T2. Control signals indicate what is being read or write and any addition operations that are required. The control signals for this are:
* FETCH - Read opcode from memory to IR and register 'b', then generate control signals and increment program counter
* REGB_OUT - Write data from register 'b' to memory at address sent in T1 and T2
* DATA_TO_REGB - Read data from memory at address sent in T1 and T2 to register 'b'
* REGB_TO_OUT - Send contents in register 'b' to be written to memory at address sent in T1 and T2

The T4 state moves data from one register to another. The control signals for this are:
* SSS_TO_REGB - Move data from register SSS in opcode to register 'b'
* REG_A_TO_PCH - Move data from register 'a' to high byte of program counter

The T5 state moves data from one register to another and may perform arithmetic logical unit operation. The control signals for this are:
* REGB_TO_DDD - Move data from register 'b' to register DDD
* ALU_OP - Perform ALU operation indicated by ALU control signal
* REGB_TO_PCL - Send value in register 'b' to low byte of program counter

The arithmetic logical unit has a set of control signals that indicates what operation should be executed. These control signals are:
* ADD - Add value to accumulator ignoring the carry bit
* ADD_C - Add value to accumulator setting the carry bit
* SUB - Subtract value from accumulator ignoring the borrow bit
* SUB_B - Subtract value from accumulator setting borrow bit
* L_AND - Logical and between a value and the accumulator
* L_XOR - Logical exclusive or between a value and the accumulator
* L_OR - Logical or between a value and the accumulator
* CMP - Comparison between a value and the accumulator
* INC - Increment register
* DEC - Decrement register
* RLC, RRC, RAL, and RAR - Rotate the accumulator using the carry bit in a few ways
