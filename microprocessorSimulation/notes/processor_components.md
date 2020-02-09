# 8008 Processor Components

The following is a summary of 8008UM.pdf pages 6-7.

### External memory
- 2^14 of memory (could be a big array). 14 bit addresses.

## Instruction Register and Control (Control Unit)

- 8-bit instruction register

## Internal Processor Memory

Consists of:

1) Address Stack

- 8 14-bit registers. First is Program Counter and other seven are for subroutines.

2) Scatch Pad (AKA the Register Bank)

- 8-bit accumulator (A register).
- 6 8-bit data registers (B,C,D,E,H,L registers).
- Accumulator used in all arithmetic instructions. Indirect addressing provided by H&L.

## ALU

- 2 8-bit temporary registers (a&b) used as input to ALU. Accumulator always stored in register a and used as one input.
- 4 flag bits (Same as a Current Processor Status Register) globally accessible by rest of processor.

## I/O Buffer

8 bi-directional output buffers.

