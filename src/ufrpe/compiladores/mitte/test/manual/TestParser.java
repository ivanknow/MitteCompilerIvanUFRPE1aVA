package ufrpe.compiladores.mitte.test.manual;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import ufrpe.compiladores.mitte.MitteParser;
import ufrpe.compiladores.mitte.exception.MitteParserException;

public class TestParser {

	/**
	 * Este metodo executa um pequeno teste de reconhecimento.  
	 */
	public static void main(String[] args) throws Exception {
		MitteParser parser = new MitteParser(new FileReader("exemplos/exercico2.mitte"));

		
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String arquivo;
		
		if (args.length == 0) {
			System.out.print("Digite o nome do arquivo: ");
			arquivo = in.readLine();
		} else {
			arquivo = args[0];
		}		

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
			System.out.println("[ERRO]"+e.getMessage()+e.getClass());
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("[ERRO]"+e.getMessage()+e.getClass());
		}
			parser.parse();
			System.out.println("OK");
	

	}

}
