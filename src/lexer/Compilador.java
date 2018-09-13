package lexer;

public class Compilador {
		
	public static void main(String[] args) {
		Lexer lexer = new Lexer("/home/breno/projetos_outros/compilers/src/fntPortugolo/exemplo_1.ptgl");
		Token token;
		TS tabelaSimbolos = new TS();

		// Enquanto nao houver erros ou nao for fim de arquivo:
		do {
			token = lexer.proxToken();

			// Imprime token
			if (token != null) {
				System.out.println(
						"Token: " + token.toString() + "\n\t Linha: " + lexer.n_line + "\t Coluna: " + lexer.n_column + "");
			}

		} while (token != null && token.getClasse() != Tag.EOF);

		lexer.fechaArquivo();

		// Imprime a tabela de simbolos
		System.out.println("");
		System.out.println("Tabela de simbolos:");
		System.out.println(tabelaSimbolos.toString());
		lexer.resumoCompilador();
	}
}
