%Tokens INTNUM LEFTPAR RIGHTPAR PLUS MULTIPLY 
%Non-terminals <E> <T> <F>
%Start <E>
%Rules
<E> : <E> PLUS <T> | <T>
<T> : <T> MULTIPLY <F> | <F> 
<F> : LEFTPAR <E> RIGHTPAR | INTNUM
