#!/bin/bash
make
echo -e "\n\n=============================================================================================\n\n"
./test01
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST01:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"
./test02
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST02:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"
./test03
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST03:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"
./test04
echo -e "\n\n=============================================================================================\n\n"
./test05
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST05:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"
./test06
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST06:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"
./test07
echo -e "\n\n=============================================================================================\n\n"
echo -e "CONTENTS OF VDISK AFTER TEST07:\n"
hexdump -C ../disk/vdisk
echo -e "\n\n=============================================================================================\n\n"