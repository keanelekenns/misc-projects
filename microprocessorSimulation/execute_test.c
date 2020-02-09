#include "memory.h"
#include "decode.h"
#include "devices.h"
#include "ALU.h"
#include "execute.h"
#include <stdio.h>

DecodeControl control = {0};
uint32_t number_tstates_executed = 0;

// Reset memory, registers, address pointer, etc
void reset_memory() {
    for (int i = 0; i < 16384; mem.memory[i] = 0, i++);
    for (int i = 0; i < 7; mem.scratch_pad[i] = 0, i++);
    for (int i = 0; i < 8; mem.address_stack[i] = 0, i++);

    mem.program_counter = 0;
    mem.mem_low = 0;
    mem.mem_high = 0;
    mem.reg_a = 0;
    mem.reg_b = 0;
    mem.flip_flops = 0;
    mem.instruction_reg = 0;
}

void test_t1_execute() {
    // Test PCL_OUT
    puts("Testing T1_execute().");
    control = init_decode_control(control);
    reset_memory();

    // Set current instruction to index 5 in memory
    mem.address_stack[0] = 5;

    // Execute T1 to get low byte of instruction address
    T1_execute(control.t1_control[0]);
    if (mem.mem_low != 5) {
        puts("PCL_OUT test failed.");
        exit(0);
    }

    // Test REGL_OUT
    // Set control signal and provide test value for register H
    control.t1_control[0] = REGL_OUT;
    mem.scratch_pad[L] = 17;
    // Execute T1 to get byte from register H
    T1_execute(control.t1_control[0]);
    if (mem.mem_low != 17) {
        puts("REGL_OUT test failed.");
        exit(0);
    }
    puts("All T1_execute() tests passed.");
}

void test_t2_execute() {
    // Test PCH_OUT
    puts("Testing T2_execute().");
    control = init_decode_control(control);
    reset_memory();

    // Set current instruction to index 1153 in memory
    mem.address_stack[0] = 1153;

    // Execute T2 to get high byte of instruction address
    T2_execute(control.t2_control[0]);
    if (mem.mem_high != 4) {
        puts("PCH_OUT test failed.");
        exit(0);
    }

    // Test REGH_OUT
    // Set control signal and provide test value for register H
    control.t2_control[0] = REGH_OUT;
    mem.scratch_pad[H] = 246;
    // Execute T1 to get byte from register H
    T2_execute(control.t2_control[0]);
    if (mem.mem_high != 246) {
        puts("REGL_OUT test failed.");
        exit(0);
    }

    puts("All T2_execute() tests passed.");
}

void test_t3_execute() {
    puts("Testing T3_excute().");
    control = init_decode_control(control);
    reset_memory();

    // Test instruction fetch/decode
    control.t3_control[0] = FETCH;
    // Setup data to load instruction
    mem.memory[5] = 196;
    mem.address_stack[0] = 5;
    mem.mem_low = 5;
    mem.mem_high = 0;
    // Run test
    T3_execute(control.t3_control[0]);
    if (mem.reg_b != 196 || mem.instruction_reg != 196 || mem.address_stack[0] != 6) {
        puts("FETCH test failed.");
        exit(0);
    }

    // Test REGB_TO_OUT
    control.t3_control[0] = REGB_TO_OUT;
    mem.memory[1797] = 73;
    mem.mem_low = 5;
    mem.mem_high = 7;
    mem.reg_b = 7;
    T3_execute(control.t3_control[0]);
    if (mem.memory[1797] != 7) {
        puts("REGB_TO_OUT test failed.");
        exit(0);
    }

    // Test DATA_TO_REGB and LOW_ADDR_TO_REGB
    // Complete the same instruction and only differ by control signal.
    // This test is for behaviour, so can just test with one signal
    control.t3_control[0] = DATA_TO_REGB;
    mem.reg_b = 5;
    T3_execute(control.t3_control[0]);
    if (mem.reg_b != 7) {
        puts("DATA_TO_REGB/LOW_ADDR_TO_REGB test failed.");
        exit(0);
    }

    // Test HIGH_ADDR_TO_REGA and HIGH_ADDR_TO_REGA_COND
    // As with test for DATA_TO_REGB and LOW_ADDR_TO_REGB can just test one
    control.t3_control[0] = HIGH_ADDR_TO_REGA;
    mem.reg_a = 5;
    T3_execute(control.t3_control[0]);
    if (mem.reg_a != 7) {
        puts("HIGH_ADDR_TO_REGA/HIGH_ADDR_TO_REGA_COND test failed.");
        exit(0);
    }

    puts("All T3_execute() tests passed.");
}

void test_t4_execute() {
    puts("Testing T4_excute().");
    control = init_decode_control(control);
    reset_memory();

    // Test SSS_TO_REGB
    control.t4_control[0] = SSS_TO_REGB;
    control.source_register = 5;
    mem.scratch_pad[5] = 3;
    T4_execute(control.t4_control[0]);

    if (mem.reg_b != 3) {
        puts("SSS_TO_REGB test failed.");
        exit(0);
    }

    // Test REG_A_TO_PCH
    mem.reg_a = 2;
    control.t4_control[0] = REG_A_TO_PCH;
    T4_execute(control.t4_control[0]);

    if (mem.address_stack[mem.program_counter] != (2 << 8)) {
        puts("REG_A_TO_PCH test failed.");
        exit(0);
    }

    puts("All T4_execute() tests passed.");
}

void test_t5_execute() {
    puts("Testing T5_excute().");
    control = init_decode_control(control);
    reset_memory();

    // Test REGB_TO_DDD
    control.t5_control[0] = REGB_TO_DDD;
    control.destination_register = 4;
    mem.reg_b = 1;
    T5_execute(control.t5_control[0]);

    if (mem.scratch_pad[4] != 1) {
        puts("SSS_TO_REGB test failed.");
        exit(0);
    }

    // Test REGB_TO_PCL
    control.t5_control[0] = REGB_TO_PCL;
    mem.reg_b = 3;
    T5_execute(control.t5_control[0]);

    if ((mem.address_stack[0] & 0x00FF) != 3) {
        puts("REGB_TO_PCL test failed.");
        exit(0);
    }

    puts("All T5_execute() tests passed.");
}

int main () {
    test_t1_execute();
    puts("");
    test_t2_execute();
    puts("");
    test_t3_execute();
    puts("");
    test_t4_execute();
    puts("");
    test_t5_execute();

    return 0;
}
