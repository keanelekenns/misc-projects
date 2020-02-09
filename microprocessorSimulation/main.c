#include "main.h"
#include "memory.h"
#include "decode.h"
#include "devices.h"
#include "execute.h"
#include "ALU.h"
#include "print_contents.h"
#include "read_file.h"

DecodeControl control;
uint32_t number_tstates_executed = 0;


int main(int argc, char *argv[]) {

    // Check if machine code file specified on command line
    if (argc == 2){
      printf("Printing initial memory contents.\n");
      print_all_contents();
      read_file(argv[1]);
    } else {
      fprintf(stderr, "Usage: ./main <machine_code_file>\n");
      exit(1);
    }
    printf("Loading test program into memory.\n");
    print_all_contents();


    // Counter to let us track which instruction we're execution
    int instruction_count = 1;


    control = init_decode_control(control);
    uint8_t current_cycle = control.current_cycle;
    // Loop until program execution halts
    for (;;) {
    	T1_execute(control.t1_control[current_cycle]);

    	T2_execute(control.t2_control[current_cycle]);

        T3_execute(control.t3_control[current_cycle]);
        // Conditional jump check
        if (control.t3_control[current_cycle] == HIGH_ADDR_TO_REGA_COND) {
            // Check for JTc
            if (control.jump_test && !(get_flip_flops() & control.condition)) {
                // Reset control and skip T4/T5
                control = init_decode_control(control);

                printf("Finished instruction %d. System state:\n", instruction_count);
                print_all_contents();
                instruction_count++;
                printf("Press enter to continue.\n");
                getchar();
                continue;
            }
            // Check for JFc
            if (!control.jump_test && (get_flip_flops() & control.condition)) {
                // Reset control and skip T4/T5
                control = init_decode_control(control);

                printf("Finished instruction %d. System state:\n", instruction_count);
                print_all_contents();
                instruction_count++;
                printf("Press enter to continue.\n");
                getchar();
                continue;
            }
        }

        T4_execute(control.t4_control[current_cycle]);

        T5_execute(control.t5_control[current_cycle]);

        control.current_cycle++;
        // Instruction complete
        if (control.current_cycle == control.cycle_length) {
            control = init_decode_control(control);
            printf("Finished instruction %d. System state:\n", instruction_count);
            print_all_contents();
            instruction_count++;
            printf("Press enter to continue.\n");
            getchar();
        }
        // Update local copy of current cycle
        current_cycle = control.current_cycle;
    }

	exit(0);
}
