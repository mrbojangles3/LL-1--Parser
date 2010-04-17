%Tokens BEGIN END SEMICOLON COMMA INTNUM PLUS MINUS MULTIPLY MODULO ID PRINT READ ASSIGN LEFTPAR RIGHTPAR 
%Non-terminals <Tiny-program> <statement-list> <statement> <id-list> <exp-list> <exp> <bin-op>
%Start <Tiny-program>
%Rules
<Tiny-program> : BEGIN <statement-list> END
<statement-list> : <statement-list> <statement> | <statement>
<statement> : PRINT LEFTPAR <exp-list> RIGHTPAR SEMICOLON
<statement> : ID ASSIGN <exp> SEMICOLON
<statement> : READ LEFTPAR <id-list> RIGHTPAR SEMICOLON
<id-list> : <id-list> COMMA ID | ID
<exp-list> : <exp-list> COMMA <exp> | <exp>
<exp> : ID | INTNUM | LEFTPAR <exp> RIGHTPAR
<exp> : <exp> <bin-op> <exp>
<bin-op> : PLUS | MINUS | MULTIPLY | MODULO
