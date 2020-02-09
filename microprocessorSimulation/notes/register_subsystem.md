# Register File, Program Counter and Stack

## Scratch Pad/Index Registers (AKA the Register File)

Scratch Pad/Index Registers: 7 8-bit registers. They are all the same except some have pre-designated special uses.

```

Reg Number: Bits:			Name and Explanation:
	
			7 6 5...      0
		0	_ _ _ _ _ _ _ _ Accumulator: Normal reg used to store results of Arithmetic instructions.
		1	_ _ _ _ _ _ _ _ B
		2	_ _ _ _ _ _ _ _ C
		3	_ _ _ _ _ _ _ _ D
		4	_ _ _ _ _ _ _ _ E
		5	X X _ _ _ _ _ _ H: First 6 bits used in indirect addressing.
		6	_ _ _ _ _ _ _ _ L: All 8 bits used in indirect addressing.


```

### Index Registers vs Memory Registers

Index registers are the 7 8-bit registers in the Scratch Pad (AKA register file). Memory Registers aren't really registers; they're memory locations that we access with indirect addressing with register H and L

### Indirect Addressing (AKA loading from memory)

Say we want to load a value from memory into a register. One instruction to do so is the LrM instruction:

```
| 1 | 1 | D | D | D | 1 | 1 | 1 |
```

Where D D D specifies the destination register. Now, how do we specify what 14 bit memory location to load from though?


We combine the first 6 bits of the H register with all 8 bits of the L register to create a 14 bit address and we load from that.


## Address Stack

```

Address:    Bits: 									

			13 12 11 10 ...						1  0

0 0 0		_  _  _  _  _  _  _  _  _  _  _  _  _  _			3-bit Address Counter stores 
0 0 1		_  _  _  _  _  _  _  _  _  _  _  _  _  _			current program counter
0 1 0		_  _  _  _  _  _  _  _  _  _  _  _  _  _			(8 possible locations)
.			_  _  _  _  _  _  _  _  _  _  _  _  _  _			
.			_  _  _  _  _  _  _  _  _  _  _  _  _  _
.			_  _  _  _  _  _  _  _  _  _  _  _  _  _				| _ | _ | _ |
1 1 0		_  _  _  _  _  _  _  _  _  _  _  _  _  _
1 1 1		_  _  _  _  _  _  _  _  _  _  _  _  _  _


```



## Required Instructions for Memory Systems
CALL: 
	- Increments Address Counter
	- Copies old PC to new address counter location (can wrap around)

RETURN:
	- Decrements Address Counter


