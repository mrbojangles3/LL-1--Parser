%Tokens ID LEFTPAR RIGHTPAR PLUS 
%Non-terminals <S> <F>
%Start <S>
%Rules
<S> : <F>
<S> : LEFTPAR <S> PLUS <F> RIGHTPAR
<F> : ID
