package ufrpe.compiladores.mitte.exception;

public class MitteParserException extends Exception{
	public MitteParserException(String msg) {
		super("[PARSER]:"+msg);
	}
}
