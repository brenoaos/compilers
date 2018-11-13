import lexer.*;
import sintaxe.*;

public class Compilador {
	public static void main(String[] args) {
		Lexer 	lexer = new Lexer("teste2_portugolo.ptgl");
		
		new Sintaxe(lexer);
		
		lexer.fechaArquivo();

	}
}
