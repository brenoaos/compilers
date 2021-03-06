package lexer;

import java.util.Map;
import java.util.HashMap;

public class TS {

	private HashMap<String, Token> tabelaSimbolos; // Tabela de símbolos do ambiente

	public TS() {
		tabelaSimbolos = new HashMap<String, Token>();

		// Inserindo as palavras reservadas
		Token word;
		word = new Token(Tag.KW_ALGORITMO, "algoritmo", 0, 0);
		this.tabelaSimbolos.put("algoritmo", word);

		word = new Token(Tag.KW_DECLARE, "declare", 0, 0);
		this.tabelaSimbolos.put("declare", word);

		word = new Token(Tag.KW_SUBROTINA, "subrotina", 0, 0);
		this.tabelaSimbolos.put("subrotina", word);

		word = new Token(Tag.KW_RETORNE, "retorne", 0, 0);
		this.tabelaSimbolos.put("retorne", word);

		word = new Token(Tag.KW_LOGICO, "logico", 0, 0);
		this.tabelaSimbolos.put("logico", word);

		word = new Token(Tag.KW_NUMERICO, "numerico", 0, 0);
		this.tabelaSimbolos.put("numerico", word);

		word = new Token(Tag.KW_LITERAL, "literal", 0, 0);
		this.tabelaSimbolos.put("literal", word);

		word = new Token(Tag.KW_NULO, "nulo", 0, 0);
		this.tabelaSimbolos.put("nulo", word);

		word = new Token(Tag.KW_SE, "se", 0, 0);
		this.tabelaSimbolos.put("se", word);

		word = new Token(Tag.KW_INICIO, "inicio", 0, 0);
		this.tabelaSimbolos.put("inicio", word);

		word = new Token(Tag.KW_FIM, "fim", 0, 0);
		this.tabelaSimbolos.put("fim", word);

		word = new Token(Tag.KW_SENAO, "senao", 0, 0);
		this.tabelaSimbolos.put("senao", word);

		word = new Token(Tag.KW_ENQUANTO, "enquanto", 0, 0);
		this.tabelaSimbolos.put("enquanto", word);

		word = new Token(Tag.KW_PARA, "para", 0, 0);
		this.tabelaSimbolos.put("para", word);

		word = new Token(Tag.KW_ATE, "ate", 0, 0);
		this.tabelaSimbolos.put("ate", word);

		word = new Token(Tag.KW_FACA, "faca", 0, 0);
		this.tabelaSimbolos.put("faca", word);

		word = new Token(Tag.KW_REPITA, "repita", 0, 0);
		this.tabelaSimbolos.put("repita", word);

		word = new Token(Tag.KW_ESCREVA, "escreva", 0, 0);
		this.tabelaSimbolos.put("escreva", word);

		word = new Token(Tag.KW_LEIA, "leia", 0, 0);
		this.tabelaSimbolos.put("leia", word);

		word = new Token(Tag.KW_VERDADEIRO, "verdadeiro", 0, 0);
		this.tabelaSimbolos.put("verdadeiro", word);

		word = new Token(Tag.KW_FALSO, "falso", 0, 0);
		this.tabelaSimbolos.put("falso", word);

		word = new Token(Tag.KW_NAO, "Nao", 0, 0);
		this.tabelaSimbolos.put("Nao", word);

		word = new Token(Tag.KW_E, "E", 0, 0);
		this.tabelaSimbolos.put("E", word);

		word = new Token(Tag.KW_OU, "Ou", 0, 0);
		this.tabelaSimbolos.put("Ou", word);
	}

	public void put(String s, Token w) {

		tabelaSimbolos.put(s, w);
	}

	public Token retornaToken(String lexema) {
		Token token = tabelaSimbolos.get(lexema);
		return token;
	}

	/*
	 * @method Token
	 * 
	 * @author Breno
	 * 
	 * @description O método recebe 4 parametros oriundo do Compilador.ProxToken()
	 * Procura um token já existente na tabela de simbolos. Se existe, retorna o
	 * token. Caso contrario, cadastra um novo token e o retorna.
	 */

	public Token token(String lexema, Tag tag, int n_lin, int n_col) {
		Token token;
		token = this.retornaToken(lexema);

		if (token == null) {
			token = new Token(tag, lexema, n_lin, n_col);
			if (token.getClasse() == (Tag.ID)) {
				this.tabelaSimbolos.put(lexema, token);
			}
		} else {
			token.setColuna(n_col);
			token.setLinha(n_lin);
		}

		return token;
	}

	@Override
	public String toString() {
		String saida = "";
		int i = 1;

		for (Map.Entry<String, Token> entry : tabelaSimbolos.entrySet()) {
			saida += ("posicao " + i + ": \t" + entry.getValue().toString()) + "\n";
			i++;
		}
		return saida;
	}
}