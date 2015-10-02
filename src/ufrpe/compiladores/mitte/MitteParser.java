package ufrpe.compiladores.mitte;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ufrpe.compiladores.mitte.exception.MitteParserException;

public class MitteParser {
	private MitteLexer lexer;
	private Token currentToken;
	private FileReader source;
	private ArrayList<TokenType> firstComando = new ArrayList<TokenType>(){{
		add(TokenType.IDENTIFICADOR);
		add(TokenType.KEY_WHILE);
		add(TokenType.KEY_IF);
		add(TokenType.KEY_PRINT);
		add(TokenType.KEY_CALL);
		add(TokenType.KEY_RETURN);
		add(TokenType.ABRE_CHAVE);
	}};

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

	private void parseListaComandos() throws MitteParserException, IOException {
		while(firstComando.contains(currentToken.getType())){
			parseComando();
		}
		
	}

	private void parseComando() throws MitteParserException, IOException {
		TokenType tp = currentToken.getType();
		switch (tp) {
	
		case IDENTIFICADOR://atribuicao ou declaracao de var
			acceptToken();//TODO lembrar de fazer otro metodo
			if(currentToken.getType() == TokenType.DOIS_PONTOS){//declaracao
				parseDeclVariavel();
			}else if(currentToken.getType() == TokenType.OP_IGUAL){//atribuicao
				parseAtribuicao();
			}else{
				throw new MitteParserException("[PARSE COMANDO]");
			}
			break;
		case KEY_WHILE:// iteracao
			parseIteracao();
			break;
		case KEY_IF://decisao
			parseDecisao();
			break;
		case KEY_PRINT://escrita
			parseEscrita();
			break;
		case KEY_CALL://chamada de funcao cmd
			parseChamadaFuncaoCMD();
			break;
		case KEY_RETURN://retorno
			parseRetorno();
			break;
		case ABRE_CHAVE://bloco
			parseBloco();
			break;

		}
		
	}

	private void parseRetorno() throws MitteParserException, IOException {
		acceptToken();//retorno
		parseExprecao();
		acceptToken(TokenType.PONTO_VIRGULA);
	}


	private void parseChamadaFuncaoCMD() throws MitteParserException, IOException {//chamada de funcao cmd
		acceptToken();
		parseChamadaFuncao();
		acceptToken(TokenType.PONTO_VIRGULA);
		
	}

	private void parseChamadaFuncao() throws MitteParserException, IOException {
		acceptToken(TokenType.IDENTIFICADOR);
		acceptToken(TokenType.ABRE_PAR);
		
		parseListaExp();
		acceptToken(TokenType.FECHA_PAR);
		
	}

	private void parseListaExp() {
		// TODO Auto-generated method stub
		
	}

	private void parseEscrita() throws MitteParserException, IOException {
		acceptToken();
		acceptToken(TokenType.ABRE_PAR);
		parseExprecao();
		acceptToken(TokenType.FECHA_PAR);
	}

	private void parseDecisao() throws MitteParserException, IOException {
		acceptToken();
		acceptToken(TokenType.ABRE_PAR);
		parseExprecao();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();
		parseRestoDecisao();
		
	}

	private void parseRestoDecisao() throws MitteParserException, IOException {
		if(currentToken.getType()==TokenType.KEY_ELSE){
			acceptToken();
			parseComando();
		}else{
			//vazio
		}
		
	}

	private void parseIteracao() throws MitteParserException, IOException {
		acceptToken();
		acceptToken(TokenType.ABRE_PAR);
		parseExprecao();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();
		
	}

	private void parseAtribuicao() throws MitteParserException, IOException {
		acceptToken();//igual
		parseExprecao();
		
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
	private void parseExprecao() {
		
		
	}

}
