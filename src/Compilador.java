import lexer.*;
import sintaxe.*;

public class Compilador {
	// E:\Projetos\compilador\src\fntPortugolo
	// /home/breno/projetos_outros/compilers/src/fntPortugolo

	public static void main(String[] args) {
		Lexer 	lexer = new Lexer("/home/breno/projetos_outros/compilers/src/fntPortugolo/teste2_portugolo.ptgl");
		
		new Sintaxe(lexer);
		
		lexer.fechaArquivo();

	}
}
