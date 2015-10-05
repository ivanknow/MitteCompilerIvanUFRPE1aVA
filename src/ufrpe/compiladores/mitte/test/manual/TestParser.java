package ufrpe.compiladores.mitte.test.manual;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ufrpe.compiladores.mitte.MitteParser;

public class TestParser {

	public static void main(String args[]) throws IOException {

		MitteParser parser;
		
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String arquivo;
		
		if (args.length == 0) {
			System.out.print("Digite o nome do arquivo: ");
			arquivo = in.readLine();
		} else {
			arquivo = args[0];
		}		
		
		try {
		
			FileReader file = new FileReader(arquivo);
			parser = new MitteParser(file);

			parser.parse();
			System.out.println("OK");
		
		} catch (Exception e) {
			System.out.println("Erro");
			e.printStackTrace();
		}

	}

}
