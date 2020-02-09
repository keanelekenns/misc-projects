#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include "ALU.h"

int main(){
    uint8_t a = 55;
	uint8_t b = 200;
	uint8_t c;
	c = ADD(a,b);
	printf("%d + %d = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = ADD_with_carry(a,b);
	printf("%d + %d + carry = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 40;
	c = SUBTRACT(a,b);
	printf("%d - %d = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = SUBTRACT_with_borrow(a,b);
	printf("%d - %d - carry = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = AND(a,b);
	printf("%d & %d = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = OR(a,b);
	printf("%d | %d = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = EXCLUSIVE_OR(a,b);
	printf("%d ^ %d = %d\n",a,b,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	COMPARE(a,b);
	printf("compare %d to %d\n", a, b);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = INCREMENT(a);
	printf("inc %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = DECREMENT(a);
	printf("dec %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = RLC(a);
	printf("Rotate left through carry %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = RRC(a);
	printf("Rotate right through carry %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = RAL(a);
	printf("Rotate left around carry %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
	a = 55;
	b = 200;
	c = RAR(a);
	printf("Rotate right around carry %d = %d\n",a,c);
	printf("%x\n", get_flip_flops());
	
    return 0;
}