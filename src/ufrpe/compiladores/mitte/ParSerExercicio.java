package ufrpe.compiladores.mitte;

import java.io.FileReader;
import java.io.IOException;

import ufrpe.compiladores.mitte.exception.MitteParserException;

public class ParSerExercicio {
	private MitteLexer lexer;
	private Token currentToken;
	private FileReader source;

	public ParSerExercicio(FileReader source) {
		this.source = source;
		lexer = new MitteLexer(source);
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

	public boolean parse() throws Exception {

		try {
			lexer.yyreset(source);
			setCurrentToken(lexer.nextToken());
			parseProgram();

			return true;
		} catch (IOException e) {

			System.out.println("Exception: erro na leitura do arquivo");
			return false;
		}

	}
	private void parseProgram() throws Exception {
		if(getCurrentToken().getType() != TokenType.EOF) {

			if (getCurrentToken().getType() == TokenType.KEY_INT
					|| getCurrentToken().getType() == TokenType.IDENTIFICADOR) {
				parseComando();
			}else{
				parseProgram();
			}
		}else{
			acceptToken();
		}

	}

	private void parseComando() throws Exception {
		if(getCurrentToken().getType() == TokenType.KEY_INT){
			acceptToken();
			acceptToken(TokenType.IDENTIFICADOR);
			
		}
		else if(getCurrentToken().getType() == TokenType.IDENTIFICADOR){
			acceptToken();
			acceptToken(TokenType.ATRIBUICAO);
			parseExpressao();
		}
		else{
			throw new Exception("");
		}
		
	}

	private void parseExpressao() throws MitteParserException, IOException {
		parseTermo();
		parserRestoExpressao();
	}

	private void parserRestoExpressao() throws MitteParserException, IOException {
		if(currentToken.getType()==TokenType.SOMA){
			acceptToken();
			parseExpressao();
		}else{
			//vazio
		}
		
	}

	private void parseTermo() throws MitteParserException, IOException {
		
		if(currentToken.getType()==TokenType.IDENTIFICADOR){
			
		}else if(currentToken.getType()==TokenType.KEY_INT){
				
		}else{
			throw new MitteParserException("erro");
		}
		
	}

}
