#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>
#include "stonylake/devices.h"
#include "stonylake/memory.h"


/*
 * Name: i_oGetInput
 * Description: This takes an eight bit value of to determine which device to get
 *              input from
 * @params
 * uint8_t device = a binary value 0-7 of which device to get input from
 * @returns
 * uint8_t = a binary value that the 'device' inputs
 */
uint8_t i_oGetInput(uint8_t fromDeviceNum){
    uint8_t ret = devicesGiveInput(fromDeviceNum);
    if(ret == 0xff) printf("Bad value for device number\n");
    return ret;
};


/*
 * Name: i_oGiveOutput
 * Description: This gives one of the eight devices output
 * @params
 * uint8_t device = the binary value 0-7 of which device to give the output to
 * uint8_t output = output to give selected device
 * @returns
 * uint8_t = a binary value that the 'device' inputs
 */
void i_oGiveOutput(uint8_t toDeviceNum, uint8_t output){
    uint8_t rt = devicesGetOutput(toDeviceNum, output);
};

void loadHigh(uint8_t highBits) {
    mem.mem_high = highBits;
}

void loadLow(uint8_t lowBits) {
    mem.mem_low = lowBits;
}

/*
 * TESTER CODE
 */
int main(){
    uint8_t testDevice = 0b111;
    uint8_t testGiveOutput = 0b10001000;
    uint8_t testGetInput;
    loadHigh(0xff);
    loadLow(0x11);

    i_oGiveOutput(testDevice, testGiveOutput);
    testGetInput = i_oGetInput(testDevice);
    printf("testGiveOutput %x\n", testGiveOutput);
    printf("testGetInput %x\n", testGetInput);

    return 0;
}
