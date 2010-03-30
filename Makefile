#Makefile for CS3240 Project 1
#LL(1) scanner parser
# Authors: James McCarty
# 	Logan Blyth
# 	Rob Rayborn

CC = gcc
CFLAGS = -ansi -pedantic -Werror -Wall -O2

scanner: scanner.c
	$(CC) $(CFLAGS) scanner.c -o scanner

tar:
	tar zcfv CS3240-ll1.tar.gz Makefile scanner.c tiny.txt *.t

clean:
	\rm scanner
