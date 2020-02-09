
#ifndef __READ_FILE__
#define __READ_FILE__

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
int read_file(char* filename);

#endif