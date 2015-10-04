package ufrpe.compiladores.mitte.test.manual;

import java.io.FileReader;

import ufrpe.compiladores.mitte.MitteParser;

public class TestParser {

	/**
	 * Este metodo executa um pequeno teste de reconhecimento.  
	 */
	public static void main(String[] args) throws Exception {
		MitteParser parser = new MitteParser(new FileReader("C:/Users/Ivan Rodrigues/Desktop/dev/workspace/Mitte-Compilador/exemplos/exemplo3.mitte"));
		

		System.out.println(" == TESTE DO PARSER ==\n");
		
		Boolean retorno = parser.parse();
		if(retorno){
			System.out.println("Sucesso! Analise Sintatica Concluida");
		}
		else{
			System.out.println("Erro na Analise Sintatica");
		}
		
		System.out.println(" == FIM ==");
	}
	
}
