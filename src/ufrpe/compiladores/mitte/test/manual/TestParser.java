package ufrpe.compiladores.mitte.test.manual;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;

import ufrpe.compiladores.mitte.MitteParser;

public class TestParser {

	/**
	 * Este metodo executa um pequeno teste de reconhecimento.  
	 */
	public static void main(String[] args) throws Exception {
		MitteParser parser = new MitteParser(new FileReader("C:/Users/Ivan Rodrigues/Desktop/dev/workspace/Mitte-Compilador/exemplos/exemplo0.mitte"));
		

		System.out.println(" == TESTE DO PARSER ==\n");
		
		Boolean msg = parser.parse();
		System.out.println(" >>" + msg + "\n");
		
		System.out.println(" == FIM ==");
	}
	
}
