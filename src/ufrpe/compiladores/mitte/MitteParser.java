package ufrpe.compiladores.mitte;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ufrpe.compiladores.mitte.exception.MitteParserException;

public class MitteParser {
	private MitteLexer lexer;
	private Token currentToken;
	private FileReader source;
	private ArrayList<TokenType> firstComando = new ArrayList<TokenType>() {
		{
			add(TokenType.IDENTIFICADOR);
			add(TokenType.KEY_WHILE);
			add(TokenType.KEY_IF);
			add(TokenType.KEY_PRINT);
			add(TokenType.KEY_CALL);
			add(TokenType.KEY_RETURN);
			add(TokenType.ABRE_CHAVE);
		}
	};

	public MitteParser(FileReader source) {
		this.source = source;
		lexer = new MitteLexer(source);
	}

	public boolean parse() throws MitteParserException {

		try {
			lexer.yyreset(source);
			setCurrentToken(lexer.nextToken());
			parseProgram();
			acceptToken();//EOF

			return true;
		} catch (IOException e) {

			System.out.println("IOException: erro na leitura do arquivo");
			return false;
		}

	}

	private void parseProgram() throws MitteParserException, IOException {
		while (getCurrentToken().getType() != TokenType.EOF) {
			parseDeclaracaoGlobal();
		}

	}

	private void parseDeclaracaoGlobal() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.KEY_DEF) {
			parseDeclFucao();
		} else if (currentToken.getType() == TokenType.IDENTIFICADOR) {
			parseDeclVariavel();
		} else {
			throw new MitteParserException("");
		}

	}

	private void parseDeclVariavel() throws MitteParserException, IOException {

		parseListaIdent();
		acceptToken(TokenType.DOIS_PONTOS);
		parseTipo();
		acceptToken(TokenType.PONTO_VIRGULA);

	}

	private void parseDeclVariavelLocal() throws MitteParserException, IOException {

		acceptToken();
		parseTipo();
		acceptToken(TokenType.PONTO_VIRGULA);

	}

	private void parseListaIdent() throws MitteParserException, IOException {
		acceptToken();// identificador
		while (currentToken.getType() == TokenType.VIRGULA) {
			acceptToken();
			acceptToken(TokenType.IDENTIFICADOR);
		}

	}

	private void parseTipo() throws MitteParserException, IOException {

		switch (currentToken.getType()) {
		case KEY_CHAR:
		case KEY_FLOAT:
		case KEY_INT:
		case KEY_STRING:
			acceptToken();
			break;
		default:
			new MitteParserException("[parseTipo]");
			break;
		}

	}

	private void parseDeclFucao() throws MitteParserException, IOException {
		parseAssinatura();
		parseBloco();

	}

	private void parseBloco() throws MitteParserException, IOException {
		acceptToken(TokenType.ABRE_CHAVE);
		parseListaComandos();
		acceptToken(TokenType.FECHA_CHAVE);

	}

	private void parseListaComandos() throws MitteParserException, IOException {
		while (firstComando.contains(currentToken.getType())) {
			parseComando();
		}

	}

	private void parseComando() throws MitteParserException, IOException {
		TokenType tp = currentToken.getType();
		switch (tp) {

		case IDENTIFICADOR:// atribuicao ou declaracao de var
			parseListaIdent();
			if (currentToken.getType() == TokenType.DOIS_PONTOS) {
				parseDeclVariavelLocal();
			} else if (currentToken.getType() == TokenType.ATRIBUICAO) {
				parseAtribuicaoLocal();
				
			} else {
				throw new MitteParserException("[PARSE COMANDO]" + currentToken);
			}
			break;
		case KEY_WHILE:// iteracao
			parseIteracao();
			break;
		case KEY_IF:// decisao
			parseDecisao();
			break;
		case KEY_PRINT:// escrita
			parseEscrita();
			break;
		case KEY_CALL:// chamada de funcao cmd
			parseChamadaFuncaoCMD();
			break;
		case KEY_RETURN:// retorno
			parseRetorno();
			break;
		case ABRE_CHAVE:// bloco
			parseBloco();
			break;

		}

	}

	private void parseRetorno() throws MitteParserException, IOException {
		acceptToken();// return
		parseExpressao();
		acceptToken(TokenType.PONTO_VIRGULA);
	}

	private void parseChamadaFuncaoCMD() throws MitteParserException, IOException {
		acceptToken(); //call
		parseChamadaFuncao();
		acceptToken(TokenType.PONTO_VIRGULA);

	}

	private void parseChamadaFuncao() throws MitteParserException, IOException {
		acceptToken(TokenType.IDENTIFICADOR);
		acceptToken(TokenType.ABRE_PAR);

		parseListaExp();
		acceptToken(TokenType.FECHA_PAR);

	}

	private void parseChamadaFuncaoLocal() throws MitteParserException, IOException {

		acceptToken(TokenType.ABRE_PAR);

		parseListaExp();
		acceptToken(TokenType.FECHA_PAR);

	}

	private void parseListaExp() throws MitteParserException, IOException {

		if(currentToken.getType() == TokenType.NUMERO_CHAR || currentToken.getType() == TokenType.NUMERO_INT
				|| currentToken.getType() == TokenType.NUMERO_REAL || currentToken.getType() == TokenType.STRING_LITERAL
				|| currentToken.getType() == TokenType.IDENTIFICADOR) {
			parseExpressao();
			parseRestoExpressao();
		}

	}

	private void parseRestoExpressao() throws MitteParserException, IOException {
		while(currentToken.getType() == TokenType.VIRGULA){
			acceptToken();//virgula
			parseExpressao();
		}
	
	}

	private void parseEscrita() throws MitteParserException, IOException {
		acceptToken();//print
		acceptToken(TokenType.ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.FECHA_PAR);
		acceptToken(TokenType.PONTO_VIRGULA);
	}

	private void parseDecisao() throws MitteParserException, IOException {
		acceptToken();//if
		acceptToken(TokenType.ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();
		parseRestoDecisao();

	}

	private void parseRestoDecisao() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.KEY_ELSE) {
			acceptToken();//else
			parseComando();
		} else {
			// vazio
		}

	}

	private void parseIteracao() throws MitteParserException, IOException {
		acceptToken();//while
		acceptToken(TokenType.ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();

	}

	private void parseAtribuicao() throws MitteParserException, IOException {
		parseListaIdent();
		acceptToken();// igual
		parseExpressao();

	}
	private void parseAtribuicaoLocal() throws MitteParserException, IOException {
		
		acceptToken();// igual
		parseExpressao();
		acceptToken(TokenType.PONTO_VIRGULA);
	}

	private void parseAssinatura() throws MitteParserException, IOException {
		acceptToken();// def
		acceptToken(TokenType.IDENTIFICADOR);
		acceptToken(TokenType.ABRE_PAR);

		parseParamFormais();
		acceptToken(TokenType.FECHA_PAR);
		parseRestoAssinatura();

	}

	private void parseParamFormais() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken();//identificador
			acceptToken(TokenType.DOIS_PONTOS);
			parseTipo();
			parseRestoParamFormais();
		} else {
			// vazio
		}

	}

	private void parseRestoParamFormais() throws MitteParserException, IOException {
		while (currentToken.getType() == TokenType.VIRGULA) {
			acceptToken();//virgula
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.DOIS_PONTOS);
			parseTipo();
		}

	}

	private void parseRestoAssinatura() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.DOIS_PONTOS) {
			acceptToken();//dois pontos
			parseTipo();
		} else {
			// vazio
		}

	}

	private void acceptToken() throws MitteParserException, IOException {
		currentToken = lexer.nextToken();
	}

	private void acceptToken(TokenType tp) throws MitteParserException, IOException {
		if (currentToken.getType() == tp) {
			currentToken = lexer.nextToken();

		} else {
			throw new MitteParserException("Token inesperado: " + "foi lido um \"" + currentToken.getType()
					+ "\", quando o esperado era \"" + tp + "\".");
		}

	}

	public Token getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(Token currentToken) {
		this.currentToken = currentToken;
	}

	private void parseExpressao() throws MitteParserException, IOException {
		parseExpOrAnd();

	}

	private void parseExpOrAnd() throws MitteParserException, IOException {
		parseExpOpComparacao();
		parseRestoExpOrAnd();
	}

	private void parseRestoExpOrAnd() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.OR) {
			acceptToken();//or
			parseExpOrAnd();
		} else if (currentToken.getType() == TokenType.AND) {
			acceptToken();//and
			parseExpOrAnd();
		} else {
			// vazio
		}

	}

	private void parseExpOpComparacao() throws MitteParserException, IOException {
		parseExpMaisMenos();
		parseRestoExpOpComparacao();

	}

	private void parseRestoExpOpComparacao() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.OP_IGUAL) {
			acceptToken();
			parseExpOpComparacao();
		} else if (currentToken.getType() == TokenType.OP_DIFERENTE) {

			acceptToken();
			parseExpOpComparacao();
		} else if (currentToken.getType() == TokenType.OP_MENOR_QUE) {

			acceptToken();
			parseExpOpComparacao();
		} else if (currentToken.getType() == TokenType.OP_MAIOR_QUE) {

			acceptToken();
			parseExpOpComparacao();
		} else if (currentToken.getType() == TokenType.OP_MENOR_OU_IGUAL) {

			acceptToken();
			parseExpOpComparacao();
		} else if (currentToken.getType() == TokenType.OP_MAIOR_OU_IGUAL) {

			acceptToken();
			parseExpOpComparacao();
		} else {
			// vazio
		}

	}

	private void parseExpMaisMenos() throws MitteParserException, IOException {
		parseExpVezesDivResto();
		parseRestoExpMaisMenos();

	}

	private void parseRestoExpMaisMenos() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.SOMA) {
			acceptToken();
			parseExpMaisMenos();
		} else if (currentToken.getType() == TokenType.SUB) {

			acceptToken();
			parseExpMaisMenos();
		} else {
			// vazio
		}

	}

	private void parseExpVezesDivResto() throws MitteParserException, IOException {
		parseExpBasica();
		parseRestoExpVezesDivResto();

	}

	private void parseRestoExpVezesDivResto() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.MULT) {
			acceptToken();
			parseExpVezesDivResto();
		} else if (currentToken.getType() == TokenType.DIV) {

			acceptToken();
			parseExpVezesDivResto();
		} else if (currentToken.getType() == TokenType.RESTO) {

			acceptToken();
			parseExpVezesDivResto();
		} else {
			// vazio
		}

	}

	private void parseExpBasica() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.NUMERO_INT
				|| currentToken.getType() == TokenType.NUMERO_CHAR
				|| currentToken.getType() == TokenType.NUMERO_REAL
				|| currentToken.getType() == TokenType.STRING_LITERAL) {
			acceptToken();

		} else if (currentToken.getType() == TokenType.IDENTIFICADOR) {// identificador
			acceptToken();
			if (currentToken.getType() == TokenType.ABRE_PAR) {//chamada funcao
				parseChamadaFuncaoLocal();
			}

		} else if (currentToken.getType() == TokenType.ABRE_PAR) {
			acceptToken();
			parseExpressao();
			acceptToken(TokenType.FECHA_PAR);
		} else if (currentToken.getType() == TokenType.NOT) {
			acceptToken();
			parseExpBasica();
		} else if (currentToken.getType() == TokenType.SUB) {
			acceptToken();
			parseExpBasica();
		}

		else {
			throw new MitteParserException("[Exprecao Basica]" + currentToken);
		}

	}

}
