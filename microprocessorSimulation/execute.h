#include <stdint.h>

#define PCL_MASK 0xFF
#define L 6
#define H 5

void T1_execute(uint8_t t1_control);

void T2_execute(uint8_t t2_control);

void T3_execute(uint8_t t3_control);

void T4_execute(uint8_t t4_control);

void T5_execute(uint8_t t5_control);

void execute_alu_operation();
