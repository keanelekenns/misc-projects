/*
UVIC Spring 2019 CSC350 Project 2: Team Stony Lake - Intel 8008 microprocessor Simulation
This is the header file for the Arithmetic Logical Unit (ALU) component.

Each operational function (everything but get_flip_flops) returns the result of the operation
except for COMPARE(), which sets flag bits accordingly.

Filename: ALU.h
Created by: Keanelek Enns
Last Edited: April 4, 2019
*/

#ifndef __ALU__
#define __ALU__
/*
The flip_flops byte contains the carry, zero, sign, and parity
flags in its lower 4 bits. Carry = bit 0, zero = bit 1, sign = bit 2,
and parity = bit 3. flip_flops = 0b0000PSZC
*/
uint8_t get_flip_flops();

/* ALU OPERATIONS

All of the ALU operations do work on the accumulator,
this is why the first argument of each function is
named accumulator (it is necessary for the
accumulator value to be passed in as the first argument).
Note that arg2 could be the value of another index register,
an immediate value, or a value from memory.

*/

uint8_t ADD(uint8_t accumulator, uint8_t arg2);

uint8_t ADD_with_carry(uint8_t accumulator, uint8_t arg2);

uint8_t SUBTRACT(uint8_t accumulator, uint8_t arg2);

uint8_t SUBTRACT_with_borrow(uint8_t accumulator, uint8_t arg2);

uint8_t AND(uint8_t accumulator, uint8_t arg2);

uint8_t EXCLUSIVE_OR(uint8_t accumulator, uint8_t arg2);

uint8_t OR(uint8_t accumulator, uint8_t arg2);

// Compare accumulator (accumulator) with other value (arg2)
// If accumulator == arg2, zero flag is set (unset otherwise)
// If accumulator < arg2, carry flag is set (unset otherwise)
void COMPARE(uint8_t accumulator, uint8_t arg2);

//The documentation says the carry flip flop is not set by INCREMENT or DECREMENT (not entirely sure why)
//@param reg - any register that is not the accumulator
uint8_t INCREMENT(uint8_t reg);

uint8_t DECREMENT(uint8_t reg);

/*
The rotate instructions are not technically part of the ALU, but if we want
the ALU to be the only modifier of the flip_flops, then they must be here.
Note also that the rotate instructions are only supposed to work on the
Accumulator, but since I do not know how the ALU will access it, I am just
listing the accumulator as an argument (we can change this once we know).
*/
uint8_t RLC(uint8_t accumulator);

uint8_t RRC(uint8_t accumulator);

uint8_t RAL(uint8_t accumulator);

uint8_t RAR(uint8_t accumulator);

#endif
