package sintaxe;

import lexer.Lexer;
import lexer.Token;
import lexer.Tag;

public class Sintaxe {
	private final Lexer lexer;
	private Token token;
	private String[] esperando = new String[0];
	private boolean semErro = true;
	private int num_erro = 0;

	public Sintaxe(Lexer l) {
		this.lexer = l;
		advance();
		programa();
	}

	public void erroSintatico(String mensagem) {
		num_erro++;
		System.out.print("[Erro Sintatico] na linha " + token.getLinha() + " e coluna " + token.getColuna() + ": ");
		System.out.println(mensagem + "\n");
	}

	public void resumoSintaxe() {
		if (num_erro > 0) {
			System.out.println("Encontrado(os) " + num_erro + " sintático(os).");
		}
	}

	public void advance() {
		token = lexer.proxToken();
		if (token == null) {
			lexer.fechaArquivo();
		}
	}

	public boolean eat(Tag t) {
		if (token.getClasse() == t) {
			advance();
			this.semErro = true;
			return true;
		} else {
			return false;
		}
	}

	public void skip(String mensagem) {
		if (this.semErro) {
			erroSintatico(mensagem);
			this.semErro = false;
		}
	}

	public void skip(String elementos[], String recebeu) {
		if (esperando.length > 0) {
			for (int i = 0; i < esperando.length; i++) {
				if (esperando[i] == elementos[i]) {
					if (esperando[i] == recebeu) {
						esperando = new String[0];
						this.semErro = true;
					}
				}
			}

		} else {
			esperando = elementos;
			erroSintatico("Esperando" + pseudoToString(elementos) + ", recebeu " + recebeu);
			this.semErro = false;
		}
	}

	public String pseudoToString(String[] a) {
		String A = "";
		if (a.length > 0) {
			for (int i = 0; i < a.length; i++) {
				if (i > 0)
					A += " ou";
				A += " '" + a[i] + "'";
			}
		}
		return A;
	}

	public void programa() {
		if (!eat(Tag.KW_ALGORITMO)) {
			String e[] = { "algoritmo" };
			skip(e, token.getLexema());
		}

		RegexDeclVar();

		ListaCmd();

		if (!eat(Tag.KW_FIM)) {
			String e[] = { "fim" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_ALGORITMO)) {
			String e[] = { "algoritmo" };
			skip(e, token.getLexema());
		}

		listaRotina();

		if (!eat(Tag.EOF)) {
			String e[] = { "fim de arquivo (EOF)" };
			skip(e, token.getLexema());
		}

		System.out.println("Compilação chegou ao fim.");
	}

	public void RegexDeclVar() {
		if (eat(Tag.KW_DECLARE)) {

			tipo();

			if (eat(Tag.SMB_SEMICOLON)) {
				ListaCmd();
			}

			declaraVar();

			if (!eat(Tag.SMB_SEMICOLON)) {
				String e[] = { ";" };
				skip(e, token.getLexema());
			}

		}

	}

	public void ListaCmd() {
		ListaCmd_();
	}

	public void listaRotina() {
		listaRotina_();
	}

	public void listaRotina_() {
		if (eat(Tag.KW_SUBROTINA)) {
			rotina();
			listaRotina_();
		}
	}

	public void rotina() {
		if (!eat(Tag.ID)) {
			String e[] = { "ID" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.SMB_OP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		listaParam();

		if (!eat(Tag.SMB_CP)) {
			String e[] = { ")" };
			skip(e, token.getLexema());
		}

		RegexDeclVar();

		ListaCmd();

		Retorno();

		if (!eat(Tag.KW_FIM)) {
			String e[] = { "fim" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_SUBROTINA)) {
			String e[] = { "subrotina" };
			skip(e, token.getLexema());
		}

	}

	public void listaParam() {

		param();
		listaParam_();
	}

	public void listaParam_() {
		if (eat(Tag.SMB_COMMA)) {
			listaParam_();
		}
	}

	public void param() {
		tipo();
	}

	public void Retorno() {

	}

	public void tipo() {
		if (!eat(Tag.KW_LOGICO) && !eat(Tag.KW_NULO) && !eat(Tag.KW_NUMERICO) && !eat(Tag.KW_LITERAL)) {
			String e[] = { "logico", "nulo", "numerico", "literal" };
			skip(e, token.getLexema());
		}
		listaID();
	}

	public void declaraVar() {
		tipo();
	}

	public void listaID() {
		if (!eat(Tag.ID)) {
			String e[] = { "ID" };
			skip(e, token.getLexema());
		}

		listaID_();
	}

	public void listaID_() {
		if (eat(Tag.SMB_COMMA)) {
			listaID();
		}
	}

	public void ListaCmd_() {
		if (eat(Tag.ID)) {
			cmd_();
			ListaCmd_();
		} else if (eat(Tag.KW_SE)) {
			cmdSe();
		} else if (eat(Tag.KW_ENQUANTO)) {
			cmdEnquanto();
		} else if (eat(Tag.KW_PARA)) {
			cmdPara();
		} else if (eat(Tag.KW_REPITA)) {
			cmdRepita();
		} else if (eat(Tag.KW_ESCREVA)) {
			cmdEscreva();
		} else if (eat(Tag.KW_LEIA)) {
			cmdLeia();
		}
	}

	public void cmd_() {
		if (eat(Tag.RELOP_ASSIGN)) {

			cmdAtrib();

		} else if (eat(Tag.SMB_OP)) {

			cmdChamaRotina();

		} else {
			String e[] = { "<--", "(" };
			skip(e, token.getLexema());

		}
	}

	public void cmdChamaRotina() {
		regexExp();

		if (!eat(Tag.SMB_CP)) {
			String e[] = { ")" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.SMB_SEMICOLON)) {
			String e[] = { "," };
			skip(e, token.getLexema());

		}

	}

	public void cmdAtrib() {
		expressao();
		if (!eat(Tag.SMB_SEMICOLON)) {
			String e[] = { ";" };
			skip(e, token.getLexema());

		}

	}

	public void cmdLeia() {

		if (!eat(Tag.SMB_OP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.ID)) {
			String e[] = { "ID" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.SMB_CP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.SMB_SEMICOLON)) {
			String e[] = { ";" };
			skip(e, token.getLexema());
		}

		ListaCmd_();
	}

	public void cmdEscreva() {

		if (!eat(Tag.SMB_OP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		expressao();

		if (!eat(Tag.SMB_CP)) {
			String e[] = { ")" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.SMB_SEMICOLON)) {
			String e[] = { ";" };
			skip(e, token.getLexema());
		}

		ListaCmd_();
	}

	public void expressao() {
		exp1();
		exp_();
	}

	public void exp_() {
		if (eat(Tag.RELOP_LT) || eat(Tag.RELOP_LE) || eat(Tag.RELOP_GT) || eat(Tag.RELOP_GE) || eat(Tag.RELOP_EQ)
				|| eat(Tag.RELOP_NE)) {
			exp1();
			exp_();
		}
	}

	public void exp1() {
		exp2();
		exp1_();
	}

	public void exp1_() {
		if (eat(Tag.KW_E) || eat(Tag.KW_OU)) {
			exp2();
			exp1_();
		}
	}

	public void exp2() {
		exp3();
		exp2_();
	}

	public void exp2_() {
		if (eat(Tag.RELOP_SUM) || eat(Tag.RELOP_MINUS)) {
			exp3();
			exp2_();
		}
	}

	public void exp3() {
		exp4();
		exp3_();
	}

	public void exp3_() {
		if (eat(Tag.RELOP_MULT) || eat(Tag.RELOP_DIV)) {
			exp4();
			exp3_();
		}
	}

	public void exp4() {
		if (eat(Tag.ID)) {

			exp4_();
		} else if (eat(Tag.SMB_OP)) {

			expressao();
			if (!eat(Tag.SMB_CP)) {
				String e[] = { ")" };
				skip(e, token.getLexema());
			}

		} else if (eat(Tag.TP_NUMERICO) || eat(Tag.TP_LITERAL) || eat(Tag.KW_VERDADEIRO) || eat(Tag.KW_FALSO)) {
			return;
		} else {
			opUnario();
		}
	}

	public void exp4_() {
		if (eat(Tag.SMB_OP)) {
			regexExp();
			if (!eat(Tag.SMB_CP)) {
				String e[] = { ")" };
				skip(e, token.getLexema());
			}
		}

	}

	public void opUnario() {
		if (eat(Tag.KW_NAO)) {

			expressao();
		}

	}

	public void cmdSe() {

		if (!eat(Tag.SMB_OP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		expressao();

		if (!eat(Tag.SMB_CP)) {
			String e[] = { ")" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_INICIO)) {
			String e[] = { "Inicio" };
			skip(e, token.getLexema());
		}

		ListaCmd();

		if (!eat(Tag.KW_FIM)) {
			String e[] = { "Fim" };
			skip(e, token.getLexema());
		}

		cmdSe_();

	}

	public void cmdSe_() {
		if (eat(Tag.KW_SENAO)) {

			if (!eat(Tag.KW_INICIO)) {
				String e[] = { "Inicio" };
				skip(e, token.getLexema());
			}

			ListaCmd();

			if (!eat(Tag.KW_FIM)) {
				String e[] = { "Fim" };
				skip(e, token.getLexema());
			}

		}
	}

	public void regexExp() {
		expressao();
		regexExp_();
	}

	public void regexExp_() {
		if (eat(Tag.SMB_COMMA)) {

			expressao();
			regexExp_();
		}
	}

	public void cmdSenao() {

	}

	public void cmdEnquanto() {

		if (!eat(Tag.SMB_OP)) {
			String e[] = { "(" };
			skip(e, token.getLexema());
		}

		expressao();

		if (!eat(Tag.SMB_CP)) {
			String e[] = { ")" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_FACA)) {
			String e[] = { "faca" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_INICIO)) {
			String e[] = { "Inicio" };
			skip(e, token.getLexema());
		}

		ListaCmd();

		if (!eat(Tag.KW_FIM)) {
			String e[] = { "fim" };
			skip(e, token.getLexema());
		}

		cmdSe_();

	}

	public void cmdPara() {

		if (!eat(Tag.ID)) {
			String e[] = { "ID" };
			skip(e, token.getLexema());
		}

		cmdAtrib();

		if (!eat(Tag.KW_ATE)) {
			String e[] = { "ate" };
			skip(e, token.getLexema());
		}

		expressao();

		if (!eat(Tag.KW_FACA)) {
			String e[] = { "faca" };
			skip(e, token.getLexema());
		}

		if (!eat(Tag.KW_INICIO)) {
			String e[] = { "inicio" };
			skip(e, token.getLexema());
		}

		ListaCmd();

		if (!eat(Tag.KW_FIM)) {
			String e[] = { "fim" };
			skip(e, token.getLexema());
		}

		cmdSe_();

	}

	public void cmdRepita() {

		ListaCmd();
		if (!eat(Tag.KW_ATE)) {
			String e[] = { "ate" };
			skip(e, token.getLexema());
		}

		expressao();
	}
}