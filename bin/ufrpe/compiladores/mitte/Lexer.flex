package ufrpe.compiladores.mitte;

%%

%class MitteLexer
%public

%function nextToken
%type Token

whiteSpace=([ \n\t\f\r]+)
letra=[a-zA-Z]
literalchars="\\n"|"\\t"|" "|","|"("|")"|":"
numero=[0-9]
inteiro={numero}+
real = {inteiro}\.{inteiro}
char  = {letra}|"\\n"|"\\t"|" "
comentario = "(*" [^*] ~"*)" | "(*" "*"+ ")"

%%

{whiteSpace} { 
	// Caracteres ignorados.
	// Nenhum token é retornado.
}

"==" { return new Token(TokenType.OP_IGUAL); }
"!=" { return new Token(TokenType.OP_DIFERENTE); }
">" { return new Token(TokenType.OP_MAIOR_QUE); }
"<" { return new Token(TokenType.OP_MENOR_QUE); }
">=" { return new Token(TokenType.OP_MAIOR_OU_IGUAL); }
"<=" { return new Token(TokenType.OP_MENOR_OU_IGUAL); }


"+" { return new Token(TokenType.SOMA); }
"-" { return new Token(TokenType.SUB); }
"*" { return new Token(TokenType.MULT); }
"/" { return new Token(TokenType.DIV); }
"%" { return new Token(TokenType.RESTO); }
"and" { return new Token(TokenType.AND); }
"or" { return new Token(TokenType.OR); }
"not" { return new Token(TokenType.NOT); }


"(" { return new Token(TokenType.ABRE_PAR); }
")" { return new Token(TokenType.FECHA_PAR); }
"{" { return new Token(TokenType.ABRE_CHAVE); }
"}" { return new Token(TokenType.FECHA_CHAVE); }
"," { return new Token(TokenType.VIRGULA); }
";" { return new Token(TokenType.PONTO_VIRGULA); }
":" { return new Token(TokenType.DOIS_PONTOS); }

"=" { return new Token(TokenType.ATRIBUICAO); }

"if" { return new Token(TokenType.KEY_IF); }
"else" { return new Token(TokenType.KEY_ELSE); }
"while" { return new Token(TokenType.KEY_WHILE); }
"return" { return new Token(TokenType.KEY_RETURN); }
"int" { return new Token(TokenType.KEY_INT); }
"float" { return new Token(TokenType.KEY_FLOAT); }
"char" { return new Token(TokenType.KEY_CHAR); }
"string" { return new Token(TokenType.KEY_STRING); }
"call" { return new Token(TokenType.KEY_CALL); }
"def"      { return new Token(TokenType.KEY_DEF); }
"print"      { return new Token(TokenType.KEY_PRINT); }


{inteiro}  { return new Token(TokenType.NUMERO_INT, yytext()); }
{real} { return new Token(TokenType.NUMERO_REAL, yytext()); }
"\'"{char}"\'" { return new Token(TokenType.NUMERO_CHAR, yytext()); }
"\""({numero}|{letra}|{literalchars})*"\""   { return new Token(TokenType.STRING_LITERAL, yytext()); }

{numero}*{letra}({numero}|{letra})*   { return new Token(TokenType.IDENTIFICADOR, yytext()); }

"##"[^\n]* {
	// Comentários de linha.
	// Não retorna token.
}

{comentario} {
	// Comentários de linha.
	// Não retorna token.
}

. { 
    // Casa com qualquer caracter que não casar com as expressoes acima.
    System.err.println("Illegal character : " + yytext());
}

<<EOF>> {
	// Casa com o fim do arquivo apenas.
	return new Token(TokenType.EOF);
}
