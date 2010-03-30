/************************************************ 
 * CS3240 Project
 * LL(1) scanner
 * Authors: James McCarty, Logan Blyth, Rob Rayborn
 *
************************************************/ 
/* Libraries */
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<malloc.h>

/* Defines */
#define TOKENBUF 10
#define UNDERSCORE 95

/* Structures */

/* Function Prototypes */
char* open_file(char*);
void match(char*);
void scan(char*);
int is_digit(char);
int is_letter(char);
int is_whitespace(char);

/* returns 0 if char is a digit */
int is_digit(char sym) {
	if(sym >= 48 && sym <= 57)
		return 0;
	return 1;
}

/* returns 0 if a char is a letter */
int is_letter(char sym) {
	if(	(sym >= 65 && sym <= 90) ||	/* uppercase */
		(sym >= 97 && sym <= 122) )	/* lowercase */
		return 0;
	return 1;
}

int is_control(char sym) {
	if(sym == '+' ||
	   sym == ';' ||
	   sym == '*' ||
	   sym == '(' ||
	   sym == ')' ||
	   sym == '%' ||
	   sym == '-' ||
	   sym == ',' )
	   return 0;
	  return 1;
}

/* returns 0 if a char is a whitespace symbol */
int is_whitespace(char sym) {
	if( (sym == '\n') ||
		(sym == ' ') ||
		(sym == '\t' ))
		return 0;
	return 1;
}

/* take a char* and compare it to list of known strings */
void match(char* var) {

	/* check for known symbols */
	if(!strcmp(var, "begin")) {
		printf("BEGIN");
	}
	else if(!strcmp(var, "end")) {
		printf("END");
	}
	else if(!strcmp(var, "print")) {
		printf("PRINT");
	}
	else if(!strcmp(var, "read")) {
		printf("READ");
	}
	else if(!strcmp(var, ";")) {
		printf("SEMICOLON");
	}
	else if(!strcmp(var, ",")) {
		printf("COMMA");
	}
	else if(!strcmp(var, ":=")) {
		printf("ASSIGN");
	}
	else if(!strcmp(var, "(")) {
		printf("LEFTPAR");
	}
	else if(!strcmp(var, ")")) {
		printf("RIGHTPAR");
	}
	else if(!strcmp(var, "+")) {
		printf("PLUS");
	}
	else if(!strcmp(var, "-")) {
		printf("MINUS");
	}
	else if(!strcmp(var, "*")) {
		printf("MULTIPLY" );
	}
	else if(!strcmp(var, "%")) {
		printf("MODULO");
	}
	else { /* determine if we have ID || INTNUM */
		
		if(var[0] == UNDERSCORE ) { /* valid start of ID */
			printf("ID");
		}
		else if(var[0] == '0') { /* ILLEGAL */
			printf("\nERROR, illegal symbol in token %s\n", var);
			exit(1);
		}
		else if(!is_digit(var[0])) { /* valid start of INTNUM */
			printf("INTNUM");
		}
		else if(!is_letter(var[0])) { /* valid start of ID */
			printf("ID");
		}
		else {
			printf("\nERROR, could not parse token %s\n", var);
			exit(1);
		}
	}
}

/* scan a buffer and match all symbols */
void scan(char* buffer)
{
	char token[TOKENBUF+1];
	int idx = 0;
	long int index = 0;

	/* parse over the entire buffer */
	while(buffer[index] != EOF) {
		
		/* clear token buffer */
		memset(token, 0, TOKENBUF+1);
		/* reset token buffer index */
		idx = 0;
		
		/* ignore whitespace */
		while(!is_whitespace(buffer[index]))
			index++;

		/* determine if we have NUM, ASSIGN, or ID */
		
		if(buffer[index] == ':') { /* assignment */
			token[idx++] = buffer[index++];
			token[idx++] = buffer[index++];
		}
		else if(!is_control(buffer[index])) { /* control char */
			token[idx] = buffer[index++];
		}
		else if(!is_digit(buffer[index])) { /* NUM */
			while(!is_digit(buffer[index])) {
				if(idx == TOKENBUF) {
					printf("\nERROR:Token starting with %s is too long\n", token);
					exit(1);
				}
				token[idx++] = buffer[index++];
			}
		}
		else if(!is_letter(buffer[index]) || buffer[index] == '_') { /* ID */
			while(	!is_letter(buffer[index]) || 
					!is_digit(buffer[index])  ||
					buffer[index] == '_' ) {
					if(idx == TOKENBUF) {
						printf("\nERROR:Token starting with %s is too long\n", token);
						exit(1);
					}
					token[idx++] = buffer[index++];
			}
		}

		/* match what we collected in the buffer */
		if(token[0] != '\0')
			match(token);
			fputc(' ', stdout);
	}
}


/* open a file and return a char buffer containing the file */
char* open_file(char* filename) {
	
	FILE* input;
	char* file_buffer;
	long int filelen, read;
	
	input = fopen(filename, "r"); /* open user given file */
	if(input == NULL) { /* could not find specified file */
		printf("Could not open file %s\n", filename);
		exit(1);
	}

	/* determine size of input file */
	fseek(input, 0, SEEK_END);
	filelen = ftell(input);
	rewind(input);

	/* malloc space for input file */

	file_buffer = (char*) malloc( ( filelen + 1 ) * sizeof(char));
	if(file_buffer == NULL) {
		printf("malloc() failed.\n");
		exit(1);
	}

	read = fread(file_buffer, sizeof(char), filelen, input);
	if(read != filelen) {
		printf("Error reading input file.\n");
		exit(1);
	}

	/* finished with input file */
	fclose(input);

	/* set EOF */
	file_buffer[filelen] = EOF;

	/* return buffer pointer to the user */
	/* don't forget to free it later ! */
	return file_buffer;
}

int main(int argc, char **argv) {

	char* file_buffer;
	
	if(argc != 2) { /* check for correctly given input file */
		printf("Usage: scanner <file>\n");
		return 1;
	}

	/* open user file and save it to file buffer */
	file_buffer = open_file(argv[1]);
	
	/* scan the buffer, output goes to stdout */
	scan(file_buffer);

	/* free any malloced memory */
	free(file_buffer);

	/* clean up output */
	putc('\n', stdout);
	fflush(stdout);

	return 0;
}
