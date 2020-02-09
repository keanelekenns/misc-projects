#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include "memory.h"

/*Function for reading in program file into Memory array
  Returns number of bytes read on success, returns -1 on failure
  Format of file: comments are allowed as long as any sequence of ones and zeros
  are a part of the program bytes (don't put them in comments)
  e.g.
  10101010
  11110001 here is a comment
  11010110
  ...
  11110000
  01010101
*/
int read_file(char* filename){
	
	FILE* fp = fopen(filename, "rb");
	if(fp == NULL){
		printf("File \"%s\" could not be read\n", filename);
		return -1;
	}
	uint8_t byte = 0x00;
	int i, j, c;
	for(i = 0; i <= 0xFF; i++){//program area of memory is from 0x00 up to 0xFF (inclusive)
		for(j = 7; j >= 0; j--){
			c = fgetc(fp);
			while(!((c == 0x30)||( c == 0x31))){
				if(c == EOF){
					j = 0;
					break;//we're done reading, so exit all loops
				}
				c = fgetc(fp);
			}
			if(c == 0x31){
				byte = byte | (1 << j);
			}
		}
		if(c == EOF){
			break;
		}
		mem.memory[i] = byte;
		byte = 0x00;
	}
	fclose(fp);
	return i;
}

/*int main(int argc, char** argv){
	if(argc < 2){
		printf("ERROR:\nA program filename must be included as an argument\n");
	}
    printf("%d %s\n", argc, argv[1]);
	int num_bytes = read_file(argv[1]);
	for(int i = 0; i < num_bytes; i++){
		printf("%x\n", mem.memory[i]);
	}
    return 0;
}*/