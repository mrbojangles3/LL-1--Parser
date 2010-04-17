#Makefile for CS3240 Project 1
#LL(1) scanner parser
# Authors: James McCarty
# 	Logan Blyth
# 	Rob Rayborn

JC = javac
CC = gcc
CFLAGS = -ansi -pedantic -Werror -Wall -O2
JFILES = EpsilonRule.java NonterminalRule.java Parser.java ParserGenerator.java GrammarRule.java ParseTable.java ParserDriver.java TerminalRule.java

all: scanner parser

scanner: scanner.c
	$(CC) $(CFLAGS) scanner.c -o scanner

parser: $(JFILES)
	$(JC) $(JFILES)

tar:
	tar zcfv CS3240-ll1.tar.gz README Makefile scanner.c tiny.txt *.t *.g run.sh $(JFILES)

clean:
	\rm scanner *.class
