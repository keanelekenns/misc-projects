#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>

#ifndef __devices_h__
#define __devices_h__

uint8_t devicesGiveInput (uint8_t device);
uint8_t devicesGetOutput (uint8_t device, uint8_t output);

#endif
