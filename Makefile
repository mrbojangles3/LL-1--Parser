#Makefile for CS3240 Project 1
#LL(1) scanner parser
# Authors: James McCarty
# 	Logan Blyth
# 	Rob Rayborn

JC = javac
CC = gcc
CFLAGS = -ansi -pedantic -Werror -Wall -O2

all: scanner parser

scanner: scanner.c
	$(CC) $(CFLAGS) scanner.c -o scanner

parser: GrammarRule.java NonterminalRule.java Parser.java RulesHash.java TerminalRule.java Test.java
	$(JC) *.java

tar:
	tar zcfv CS3240-ll1.tar.gz Makefile scanner.c tiny.txt *.t *.java run.sh

clean:
	\rm scanner *.class
