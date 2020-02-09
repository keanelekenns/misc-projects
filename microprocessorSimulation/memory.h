#include <stdint.h>

#ifndef __MEMORY_STRUCTS__
#define __MEMORY_STRUCTS__

struct mem{
	
	// scratchpad[0]: accumulator
	// scratchpad[1]: B
	// scratchpad[2]: C
	// scratchpad[3]: D
	// scratchpad[4]: E
	// scratchpad[5]: H
	// scratchpad[6]: L
	uint8_t scratch_pad[7];

	// Stores current program counter
	uint8_t program_counter;

	// 8 14-bit slots to store addresses; we only use 14 bits of each of these.
	uint16_t address_stack[8];

	// External memory array
	uint8_t memory[16384];
	uint8_t mem_high;
	uint8_t mem_low;

	uint8_t reg_a;
	uint8_t reg_b;

	uint8_t instruction_reg;

}mem;

void init_memory(struct mem Memory);

#endif

