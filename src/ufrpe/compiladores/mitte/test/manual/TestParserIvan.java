package ufrpe.compiladores.mitte.test.manual;

import java.io.FileReader;

import ufrpe.compiladores.mitte.MitteParser;
import ufrpe.compiladores.mitte.exception.MitteParserException;

public class TestParserIvan {

	/**
	 * Este metodo executa um pequeno teste de reconhecimento.  
	 */
	public static void main(String[] args) throws Exception {
		MitteParser parser = new MitteParser(new FileReader("exemplos/exemplo3.mitte"));
		

		System.out.println(" == TESTE DO PARSER ==\n");
		try{
			Boolean retorno = parser.parse();
			if(retorno){
				System.out.println("Sucesso! Analise Sintatica Concluida");
			}
			else{
				System.out.println("Erro na Analise Sintatica");
			}
			
		}catch(MitteParserException e){
			System.out.println("[ERRO]"+e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("[ERRO]"+e.getMessage());
		}
		
		System.out.println(" == FIM ==");
	}
	
}
