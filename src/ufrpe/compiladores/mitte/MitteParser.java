package ufrpe.compiladores.mitte;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import ufrpe.compiladores.mitte.exception.MitteParserException;

public class MitteParser {
	private MitteLexer lexer;
	private Token currentToken;
	private FileReader source;

	public MitteParser(FileReader source) {
		this.source = source;
		lexer = new MitteLexer(source);
	}

	public boolean parse() throws MitteParserException {

		try {
			lexer.yyreset(source);
			setCurrentToken(lexer.nextToken());
			parseProgram();
			acceptToken();// tirei o eof pois o mesmo ja sera lido no program

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

	private void parseListaComandos() {
		// TODO Auto-generated method stub
		
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
			acceptToken();
			acceptToken(TokenType.DOIS_PONTOS);
			parseTipo();
			parseRestoParamFormais();
		} else {
			// vazio
		}

	}

	private void parseRestoParamFormais() throws MitteParserException, IOException {
		while (currentToken.getType() == TokenType.VIRGULA) {
			acceptToken();
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.DOIS_PONTOS);
			parseTipo();
		}

	}

	private void parseRestoAssinatura() throws MitteParserException, IOException {
		if (currentToken.getType() == TokenType.DOIS_PONTOS) {
			acceptToken();
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

}
