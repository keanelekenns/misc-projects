#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>
#include "stonylake/memory.h"


// These are variables that simulate the 'devices' returning values they are just
// simple binary values to test that each device correctly sends a response
uint8_t deviceZero  = 0b0000000;
uint8_t deviceOne   = 0b0000001;
uint8_t deviceTwo   = 0b0000010;
uint8_t deviceThree = 0b0000100;
uint8_t deviceFour  = 0b0001000;
uint8_t deviceFive  = 0b0010000;
uint8_t deviceSix   = 0b0100000;
uint8_t deviceSeven = 0b1000000;

/*
 * Name: devicesGiveInput
 * Description: This asks one of the eight connected devices for input
 * @params
 * uint8_t device = the binary value 0-7 of which device to get input from
 * @returns
 * uint8_t = a binary value that the 'device' inputs
 */
uint8_t devicesGiveInput(uint8_t device){
    uint16_t address = 0x0000;
    address = address + mem.mem_high;
//    printf("address1: %x\n", address);
    address = address << 10;
    address = address >> 2;
//    printf("address2: %x\n", address);
    address = address + mem.mem_low;
//    printf("address3: %x\n", address);

    return mem.memory[address];
}

/*
 * Name: devicesGetOutput
 * Description: Pushes output to a selected device, we are simulating the 8008 not the devices
 *              so it just returns a dummy value and doesn't actually do anything
 * @params
 * uint8_t device = the binary value 0-7 of which device to give the output to
 * uint8_t output = the output to send to the device
 * @returns
 * uint8_t = 0xff to say that it is correctly done
 */
void devicesGetOutput(uint8_t device, uint8_t output){
    uint16_t address = 0x0000;
    address = address + mem.mem_high;
//    printf("address1: %x\n", address);
    address = address << 10;
    address = address >> 2;
//    printf("address2: %x\n", address);
    address = address + mem.mem_low;
//    printf("address3: %x\n", address);
    mem.memory[address] = output;
    return;
}
