#include <stdint.h>
#define SSS_MASK 0x07
#define DDD_MASK 0x38
#define DDD_SHIFT 3
#define MEM 0x07

// Indicates T-state should be skipped
#define SKIP 0x00
// Indicates T-state should take time but do nothing
#define IDLE 0xFF

// Mask for conditional jumps
#define COND_FLIP_FLOPS 0x18

// Control values for T1
#define PCL_OUT 0x01
#define REGL_OUT 0x02

// Control values for T2
#define PCH_OUT  0x01
#define REGH_OUT 0x02

// Control values for T3
#define FETCH 0x01
#define REGB_TO_OUT 0x02
#define DATA_TO_REGB 0x03
#define LOW_ADDR_TO_REGB 0x04
#define HIGH_ADDR_TO_REGA 0x05
#define HIGH_ADDR_TO_REGA_COND 0x06

// Control values for T4
#define SSS_TO_REGB 0x01
#define REG_A_TO_PCH 0x02

// Control values for T5
#define REGB_TO_DDD 0x01
#define ALU_OP 0x02
#define REGB_TO_PCL 0x03

// ALU Operations
#define ADD_OP 0x01
#define ADD_C 0x02
#define SUB_OP 0x03
#define SUB_B 0x04
#define L_AND 0x05
#define L_XOR 0x06
#define L_OR 0x07
#define CMP 0x08
#define INC 0x09
#define DEC 0x0A
#define RLC_OP 0x0B
#define RRC_OP 0x0C
#define RAL_OP 0x0D
#define RAR_OP 0x0E

// Contains control signals for instruction execution
typedef struct DecodeControl {
    uint8_t current_cycle;
    uint8_t cycle_length;
    uint8_t source_register;
    uint8_t destination_register;
    uint8_t t1_control[3];
    uint8_t t2_control[3];
    uint8_t t3_control[3];
    uint8_t t4_control[3];
    uint8_t t5_control[3];
    uint8_t alu_operation;
    uint8_t condition;
    uint8_t jump_test;
    uint8_t increment_pc[3];
} DecodeControl;

// Decodes based on opcode
DecodeControl decode(DecodeControl decode_control, uint8_t opcode);

DecodeControl init_decode_control(DecodeControl decode_control);

// Set control for ALU operations on an scratch pad register
DecodeControl set_control_scratch_pad(DecodeControl decode_control);

// Set control for an ALU operation on a value stored in memory
DecodeControl set_control_memory(DecodeControl decode_control);

// Set control for an ALU operation on a value stored in the next instruction byte
DecodeControl set_control_immediate(DecodeControl decode_control);

// Set control for an accumulator rotate operation
DecodeControl set_control_rotate(DecodeControl decode_control);

uint8_t check_in_sequence(uint8_t value, uint8_t start, uint8_t end, uint8_t increment);
