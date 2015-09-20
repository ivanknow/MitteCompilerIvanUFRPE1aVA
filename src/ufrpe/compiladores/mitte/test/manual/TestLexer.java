package ufrpe.compiladores.mitte.test.manual;

import java.io.FileReader;
import java.io.Reader;

import ufrpe.compiladores.mitte.MitteLexer;
import ufrpe.compiladores.mitte.Token;
import ufrpe.compiladores.mitte.TokenType;

public class TestLexer {

	/**
	 * Este metodo permite fazer testes com o lexer usando a
	 * entrada padrão.  
	 */
	public static void main(String[] args) throws Exception {
		MitteLexer lexer ;
		lexer = new MitteLexer(new FileReader("C:/Users/Ivan Rodrigues/Desktop/dev/workspace/Mitte-Compilador/exemplos/exemplo2.mitte"));
		Token token = null;
		
		System.out.println("\n\n\n");
		System.out.println(" == TESTE DO LEXER ==\n");
		System.out.println(" Digite alguma string terminada em \";\" e tecle ENTER:\n\n");
		System.out.print(" ");
		
		// parar passar um arquivo como entrada
		//lexer.reset(new FileInputStream("caminho do arquivo"));
		
		do {
			token = lexer.nextToken();
			System.out.println("\t" + token);
		
		} while (token.getType() != TokenType.EOF);
		
		System.out.println("\n == FIM ==");
		
	}
	
}

