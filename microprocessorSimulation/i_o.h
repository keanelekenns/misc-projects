#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>

#ifndef __i_o_h__
#define __i_o_h__

uint8_t i_oGetInput(uint8_t fromDeviceNum);
void i_oGiveOutput(uint8_t toDeviceNum, uint8_t output);
void loadLow(uint8_t lowBits);
void loadHigh(uint8_t highBits);

#endif
