#include "print_contents.c"

#ifndef __PRINT_CONTENTS__
#define __PRINT_CONTENTS__

void print_memory_chunk(int beginning, int end, int ascii);

void print_program_counter();

void print_scratch_pad();

void print_control_bits();

void print_instruction_reg();

void print_memory();

void print_misc_values();

/*
This function is used to print out the contents of memory in a concise manner
that lends itself to the demonstration of the software. 
*/
void print_all_contents();

#endif