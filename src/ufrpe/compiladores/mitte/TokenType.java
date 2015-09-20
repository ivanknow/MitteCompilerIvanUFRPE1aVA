package ufrpe.compiladores.mitte;


/**
 * Esta enumeração guarda os tipos de token
 * que a linguagem suporta.
 * 
 * @author Ivan Rodrigues
 */
public enum TokenType {
	
	IDENTIFICADOR,
	
	OP_IGUAL,
	OP_DIFERENTE,
	OP_MAIOR_QUE,
	OP_MENOR_QUE,
	OP_MAIOR_OU_IGUAL,
	OP_MENOR_OU_IGUAL,
	
	SOMA,
	SUB,
	MULT,
	DIV,
	RESTO,
	AND,
	OR,
	NOT,
	
	ATRIBUICAO,
	
	ABRE_PAR,
	FECHA_PAR,
	ABRE_CHAVE,
	FECHA_CHAVE,
	VIRGULA,
	PONTO_VIRGULA,
	DOIS_PONTOS,
	
	NUMERO_INT,
	NUMERO_REAL,
	NUMERO_CHAR,
	STRING_LITERAL,
	
	KEY_IF,
	KEY_ELSE,
	KEY_WHILE,
	KEY_RETURN,
	KEY_INT,
	KEY_FLOAT,
	KEY_CHAR,
	KEY_STRING,
	KEY_CALL,
	KEY_DEF,
	KEY_PRINT,
	
	EOF
}
