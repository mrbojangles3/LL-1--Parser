%Tokens BEGIN END SEMICOLON COMMA INTNUM PLUS MINUS MULTIPLY MODULO ID PRINT READ ASSIGN LEFTPAR RIGHTPAR 
%Non-terminals <Tiny-program> <statement-list> <statement> <id-list> <exp-list> <exp> <op> <mult-op> <add-op> <mod-op> <term>
%Start <Tiny-program>
%Rules
<Tiny-program> : BEGIN <statement-list> END
<statement-list> : <statement-list> <statement> | <statement>
<statement> : PRINT LEFTPAR <exp-list> RIGHTPAR SEMICOLON
<statement> : ID ASSIGN <exp> SEMICOLON
<statement> : READ LEFTPAR <id-list> RIGHTPAR SEMICOLON
<id-list> : <id-list> COMMA ID | ID
<exp-list> : <exp-list> COMMA <exp> | <exp>
<exp> : <exp> <mod-op> <term>
<exp> : <exp> <mult-op> <term>
<exp> : <exp> <add-op> <term>
<exp> : <term>
<term> : LEFTPAR <exp> RIGHTPAR | ID | INTNUM
<mult-op> : MULTIPLY
<add-op> : MINUS | PLUS
<mod-op> : MODULO
