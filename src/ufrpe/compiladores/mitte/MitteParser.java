package ufrpe.compiladores.mitte;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import ufrpe.compiladores.mitte.exception.MitteParserException;

public class MitteParser {
	private MitteLexer lexer;
	private Token currentToken;
	private FileReader source;
	public MitteParser(FileReader source){
		this.source = source;
		lexer =  new MitteLexer(source);
	}
	
	public boolean parse(InputStream input) throws MitteParserException {

		try {
			currentToken = lexer.nextToken();
			parseProgram();
			acceptToken();//tirei o eof pois o mesmo ja sera lido no program

			return true;
		} catch (IOException e) {
			
			System.out.println("IOException: erro na leitura do arquivo");
			return false;
		}
		
	}
	
	private void parseProgram() throws MitteParserException, IOException {
		while(currentToken.getType()!=TokenType.EOF){
		parseDeclaracaoGlobal();
		}
		
	}

	private void parseDeclaracaoGlobal() throws MitteParserException, IOException {
		if(currentToken.getType()==TokenType.KEY_DEF){
			parseDeclFucao();
		}
		else if(currentToken.getType()==TokenType.IDENTIFICADOR){
			parseDeclVariavel();
		}
		else{
			throw new MitteParserException("");
		}
		
	}

	private void parseDeclVariavel() throws MitteParserException, IOException {
		
		parseListaIdent();
		acceptToken();//dois pontos
		parseTipo();
		acceptToken(TokenType.PONTO_VIRGULA);
		
	}

	private void parseListaIdent() throws MitteParserException, IOException {
		acceptToken();//identificador
		while(currentToken.getType()!=TokenType.DOIS_PONTOS){
			acceptToken(TokenType.VIRGULA);
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
		acceptToken();//def
		
	}

	private void acceptToken() throws MitteParserException, IOException {
		currentToken = lexer.nextToken();		
	}

	private void acceptToken(TokenType tp) throws MitteParserException, IOException {
		if (currentToken.getType() == tp) {
			currentToken = lexer.nextToken();

		} else {
			throw new MitteParserException("Token inesperado: "
					        + "foi lido um \"" + currentToken.getType() 
					        + "\", quando o esperado era \"" + tp + "\".");
		}

	}
}
