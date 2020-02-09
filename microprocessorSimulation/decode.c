#include "decode.h"
#include <stdlib.h>
#include <stdio.h>

// Initialize control to defaults for first first cycle
DecodeControl init_decode_control(DecodeControl decode_control) {
    decode_control.current_cycle = 0;
    decode_control.cycle_length = 1;
    decode_control.alu_operation = 0;
    decode_control.source_register = 0;
    decode_control.destination_register = 0;
    decode_control.condition = 0;
    decode_control.jump_test = 0;

    for (int i = 0; i < 3; i++) {
        decode_control.increment_pc[i] = 1;
    }

    // Set first cycle to default memory fetch
    decode_control.t1_control[0] = PCL_OUT;
    decode_control.t2_control[0] = PCH_OUT;
    decode_control.t3_control[0] = FETCH;
    decode_control.t4_control[0] = IDLE;
    decode_control.t5_control[0] = IDLE;

    // Set remaining cycles to no operation
    for (uint8_t i = 1; i < 3; i++) {
        decode_control.t1_control[i] = 0;
        decode_control.t2_control[i] = 0;
        decode_control.t3_control[i] = 0;
        decode_control.t4_control[i] = 0;
        decode_control.t5_control[i] = 0;
    }

    return decode_control;
}

// Set control for ALU operations on an scratch pad register
DecodeControl set_control_scratch_pad(DecodeControl decode_control) {
    decode_control.t4_control[0] = SSS_TO_REGB;
    decode_control.t5_control[0] = ALU_OP;
    return decode_control;
}

// Set control for an ALU operation on a value stored in memory
DecodeControl set_control_memory(DecodeControl decode_control) {
    decode_control.cycle_length = 2;
    decode_control.increment_pc[1] = 0;
    decode_control.t1_control[1] = REGL_OUT;
    decode_control.t2_control[1] = REGH_OUT;
    decode_control.t3_control[1] = DATA_TO_REGB;
    decode_control.t4_control[1] = IDLE;
    decode_control.t5_control[1] = ALU_OP;
    return decode_control;
}

// Set control for an ALU operation on a value stored in the next instruction byte
DecodeControl set_control_immediate(DecodeControl decode_control) {
    decode_control.cycle_length = 2;
    decode_control.t1_control[1] = PCL_OUT;
    decode_control.t2_control[1] = PCH_OUT;
    decode_control.t3_control[1] = DATA_TO_REGB;
    decode_control.t4_control[1] = IDLE;
    decode_control.t5_control[1] = ALU_OP;
    return decode_control;
}

// Set control for an accumulator rotate operation
DecodeControl set_control_rotate(DecodeControl decode_control) {
    decode_control.cycle_length = 1;
    decode_control.t1_control[0] = PCL_OUT;
    decode_control.t2_control[0] = PCH_OUT;
    decode_control.t3_control[0] = FETCH;
    decode_control.t4_control[0] = IDLE;
    decode_control.t5_control[0] = ALU_OP;
    return decode_control;
}

uint8_t check_in_sequence(uint8_t value, uint8_t start, uint8_t end, uint8_t increment) {
    for (; start <= end; start += increment) {
        if (value == start) {
            return 1;
        }
    }

    return 0;
}

// Decode opcode and generate control
DecodeControl decode(DecodeControl decode_control, uint8_t opcode) {
    decode_control = init_decode_control(decode_control);

    // Decode and control generation for instructions
    if (opcode <= 0xFE && opcode >= 0xC0) {
        // Lr1r2, LrM, LMr
        // Byte format
        // 11 DDD SSS
        // Covers all values of DDD SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.destination_register = (DDD_MASK & opcode) >> DDD_SHIFT;

        if (decode_control.source_register == MEM) {
            // LrM
            decode_control.cycle_length = 2;
            decode_control.t1_control[1] = REGL_OUT;
            decode_control.t2_control[1] = REGH_OUT;
            decode_control.t3_control[1] = DATA_TO_REGB;
            decode_control.t4_control[1] = IDLE;
            decode_control.t5_control[1] = REGB_TO_DDD;
            decode_control.increment_pc[1] = 0;
        } else if (decode_control.destination_register == MEM) {
            // LMr
            decode_control.cycle_length = 2;
            decode_control.t4_control[0] = SSS_TO_REGB;
            decode_control.t1_control[1] = REGL_OUT;
            decode_control.t2_control[1] = REGH_OUT;
            decode_control.t3_control[1] = REGB_TO_OUT;
            decode_control.increment_pc[1] = 0;
        } else {
            // Lr1r2
            decode_control.t4_control[0] = SSS_TO_REGB;
            decode_control.t5_control[0] = REGB_TO_DDD;
        }
    } else if (check_in_sequence(opcode, 0x06, 0x3E, 0x08)) {
        // LrI/LMI
        // 2 byte format
        // 00 DDD 110
        // BB BBB BBB
        decode_control.destination_register = (DDD_MASK & opcode) >> DDD_SHIFT;
        decode_control.t1_control[1] = PCL_OUT;
        decode_control.t2_control[1] = PCH_OUT;
        decode_control.t3_control[1] = DATA_TO_REGB;

        if (decode_control.destination_register < MEM) {
            // LrI
            decode_control.t4_control[1] = IDLE;
            decode_control.t5_control[1] = REGB_TO_DDD;
            decode_control.cycle_length = 2;
        } else {
            // LMI
            decode_control.cycle_length = 3;
            decode_control.increment_pc[2] = 0;

            decode_control.t1_control[2] = REGL_OUT;
            decode_control.t2_control[2] = REGH_OUT;
            decode_control.t3_control[2] = REGB_TO_OUT;
        }
    } else if (check_in_sequence(opcode, 0x08, 0x30, 0x08)) {
        // INr
        // Byte format
        // 00 DDD 000
        // DDD != 000
        // Opcode starts at 00 001 000 and increments by 8 until DDD = 110
        decode_control.destination_register = (DDD_MASK & opcode) >> DDD_SHIFT;
        decode_control.t5_control[0] = ALU_OP;
        decode_control.alu_operation = INC;
    } else if (check_in_sequence(opcode, 0x09, 0x31, 0x08)) {
        // DCr
        // Same as INr except starts at 00 001 001
        decode_control.destination_register = (DDD_MASK & opcode) >> DDD_SHIFT;
        decode_control.t5_control[0] = ALU_OP;
        decode_control.alu_operation = DEC;
    } else if (opcode <= 0x87 && opcode >= 0x80) {
        // ADr/ADM
        // Byte format
        // 10 000 SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = ADD_OP;
        if (decode_control.source_register == MEM) {
            //ADM
            decode_control = set_control_memory(decode_control);
        } else {
            // ADr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode <= 0x8F && opcode >= 0x88) {
        // ACr/ACM
        // Byte format
        // 10 001 SSS
        decode_control.source_register = opcode & SSS_MASK;
        if (decode_control.source_register == MEM) {
            //ADM
            decode_control.alu_operation = ADD_OP;
            decode_control = set_control_memory(decode_control);
        } else {
            // ACr
            decode_control.alu_operation = ADD_C;
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode == 0x04) {
        // ADI
        // 2 byte format
        // 00 000 100
        // BB BBB BBB
        decode_control.alu_operation = ADD_OP;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode == 0x0C) {
        // ACI
        // 2 byte format
        // 00 001 100
        // BB BBB BBB
        decode_control.alu_operation = ADD_C;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode >= 0x90 && opcode <= 0x97) {
        // SUr/SUM
        // Byte format
        // 10 010 SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = SUB_OP;
        if (decode_control.source_register == MEM) {
            // SUM
            decode_control = set_control_memory(decode_control);
        } else {
            // SUr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode >= 0x98 && opcode <= 0x9F) {
        // SBr/SBM
        // Byte format
        // 10 011 SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = SUB_B;
        if (decode_control.source_register == MEM) {
            // SBM
            decode_control = set_control_memory(decode_control);
        } else {
            // SBr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode == 0b00010100) {
        // SUI
        decode_control.alu_operation = SUB_OP;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode == 0b00011100) {
        // SBI
        decode_control.alu_operation = SUB_B;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode >= 0xA0 && opcode <= 0xA7) {
        // NDr/NDM
        // Byte format
        // 10 100 SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = L_AND;
        if (decode_control.source_register == MEM) {
            // NMD
            decode_control = set_control_memory(decode_control);
        } else {
            //NDr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode >= 0xA8 && opcode <= 0xAF) {
        // XRr/XRM
        // Byte format
        // 10 101 SSS
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = L_XOR;
        if (decode_control.source_register == MEM) {
            // XRM
            decode_control = set_control_memory(decode_control);
        } else {
            // XRr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode == 0b00100100) {
        // NDI
        decode_control.alu_operation = L_AND;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode == 0b00101100) {
        // XRI
        decode_control.alu_operation = L_XOR;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode >= 0xB0 && opcode <= 0xB7) {
        // ORr/ORM
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = L_OR;
        if (decode_control.source_register == MEM) {
            // ORM
            decode_control = set_control_memory(decode_control);
        } else {
            //ODr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode >= 0xB8 && opcode <= 0xBF) {
        // CPr/CPM
        decode_control.source_register = opcode & SSS_MASK;
        decode_control.alu_operation = CMP;
        if (decode_control.source_register == MEM) {
            // CPM
            decode_control = set_control_memory(decode_control);
        } else {
            //CPr
            decode_control = set_control_scratch_pad(decode_control);
        }
    } else if (opcode == 0b00110100) {
        // ORI
        decode_control.alu_operation = L_OR;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode == 0b00111100) {
        // CPI
        decode_control.alu_operation = CMP;
        decode_control = set_control_immediate(decode_control);
    } else if (opcode == 0b00000010) {
        // RLC
        decode_control.alu_operation = RLC_OP;
        decode_control = set_control_rotate(decode_control);
    } else if (opcode == 0b00001010) {
        // RRC
        decode_control.alu_operation = RRC_OP;
        decode_control = set_control_rotate(decode_control);
    } else if (opcode == 0b00010010) {
        // RAL
        decode_control.alu_operation = RAL_OP;
        decode_control = set_control_rotate(decode_control);
    } else if (opcode == 0b00011010) {
        // RAR
        decode_control.alu_operation = RAR_OP;
        decode_control = set_control_rotate(decode_control);
    } else if ((opcode & 0b11000111) == 0b01000100) {
        // JMP
        // First byte format
        // 01 XXX 100, where X are don't cares
        decode_control.cycle_length = 3;
        decode_control.t1_control[1] = PCL_OUT;
        decode_control.t2_control[1] = PCH_OUT;
        decode_control.t3_control[1] = LOW_ADDR_TO_REGB;

        decode_control.increment_pc[2] = 0;
        decode_control.t1_control[2] = PCL_OUT;
        decode_control.t2_control[2] = PCH_OUT;
        decode_control.t3_control[2] = HIGH_ADDR_TO_REGA;
        decode_control.t4_control[2] = REG_A_TO_PCH;
        decode_control.t5_control[2] = REGB_TO_PCL;
    } else if (opcode == 0x40 || opcode == 0x48 || opcode == 0x50 || opcode == 0x58) {
        // JFc
        // First byte format
        // 01 0CC 100, where CC are the flip flops bit
        decode_control.condition = 0x01 << ((opcode & COND_FLIP_FLOPS) >> 3);
        decode_control.cycle_length = 3;
        decode_control.t1_control[1] = PCL_OUT;
        decode_control.t2_control[1] = PCH_OUT;
        decode_control.t3_control[1] = LOW_ADDR_TO_REGB;
        decode_control.t1_control[2] = PCL_OUT;
        decode_control.t2_control[2] = PCH_OUT;
        decode_control.t3_control[2] = HIGH_ADDR_TO_REGA_COND;
        decode_control.t4_control[2] = REG_A_TO_PCH;
        decode_control.t5_control[2] = REGB_TO_PCL;
    } else if (opcode == 0x60 || opcode == 0x68 || opcode == 0x70 || opcode == 0x78) {
        // JTc
        // First byte format
        // 01 1CC 100, where CC are the flip flops bit
        decode_control.jump_test = 1;
        decode_control.condition = 0x01 << ((opcode & COND_FLIP_FLOPS) >> 3);
        decode_control.cycle_length = 3;
        decode_control.t1_control[1] = PCL_OUT;
        decode_control.t2_control[1] = PCH_OUT;
        decode_control.t3_control[1] = LOW_ADDR_TO_REGB;
        decode_control.t1_control[2] = PCL_OUT;
        decode_control.t2_control[2] = PCH_OUT;
        decode_control.t3_control[2] = HIGH_ADDR_TO_REGA_COND;
        decode_control.t4_control[2] = REG_A_TO_PCH;
        decode_control.t5_control[2] = REGB_TO_PCL;
    } else {
        // An unrecognized instruction was reached
        printf("Opcode = %x is not recognized as an instruction.\n", opcode);
        exit(0);
    }

    return decode_control;
}
