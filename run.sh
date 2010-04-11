#!/bin/bash
# Shell script for scanner/parser project
# James McCarty, Logan Blyth, Rob Rayborn
# CS3240 -- Spring 2010

# Update project files build
make all > /dev/null 2>&1 || (echo "Errors occured in the build..."; echo "Run 'make all' to debug")

# Check for valid input

if [ -z "$1" -o -z "$2" ] ; then
	echo "Usage: $0 tiny_file.t input_grammar.g"
	exit
fi	
if [ ! -f $1 -o ! -f $2 ] ; then
	echo "Usage: $0 tiny_file.t input_grammar.g"
	echo "Invalid filename was given."
	exit
fi

# Run our scanner on our tiny file and pipe the output.

./scanner $1 > $1.tok || (echo "Error in scanning $1:" && cat $1.tok && rm $1.tok)

if [ ! -f $1.tok ] ; then
	exit
fi

# Run our parser the grammar file, and feed it our tokenized input
