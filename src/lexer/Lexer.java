package lexer;

import java.io.*;

public class Lexer {

	private final int END_OF_FILE = -1; // contante para fim do arquivo
	private int lookahead = 0; // armazena o último caractere lido do arquivo
	public int n_line = 1; // contador de linhas
	public int n_column = 1; // contador de linhas
	private RandomAccessFile instance_file; // referencia para o arquivo
	private TS tabelaSimbolos; // tabela de simbolos
	private int cod_erro = -1;
	private String msg_erro = "";
	private static int num_erro = 0;
	private boolean panico = false;

	public String tabSimbolos() {
		return tabelaSimbolos.toString();
	}

	public Lexer(String input_data) {
		tabelaSimbolos = new TS();
		// Abre instance_file de input_data
		try {
			instance_file = new RandomAccessFile(input_data, "r");
		} catch (IOException e) {
			System.out.println("Erro de abertura do arquivo " + input_data + "\n" + e);
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Erro do programa ou falha da Tabela de Simbolos\n" + e);
			System.exit(2);
		}
	}

	// Fecha instance_file de input_data
	public void fechaArquivo() {

		try {
			instance_file.close();
		} catch (IOException errorFile) {
			System.out.println("Erro ao fechar arquivo\n" + errorFile);
			System.exit(3);
		}
	}

	// Controle de ponteiro
	// Soma linha e retorna coluna
	public void proxLinha() {
		n_line++;
		n_column = 1;
	}

	//
	public void resumoCompilador() {
		if (num_erro > 0) {
			System.out.println("Encontrado(os): " + num_erro + " erro(os).");
		} else {
			System.out.println("Compilado com sucesso!");
		}
	}

	// Reporta erro para o usuário
	public void sinalizaErro(String mensagem) {
		if (!panico) {
			System.out.println("[Erro Lexico]: " + mensagem + "\n");
		}
		panico = true;
	}

	// Volta uma posição do buffer de leitura
	public void retornaPonteiro() {

		try {
			// Não é necessário retornar o ponteiro em caso de Fim de Arquivo
			if (lookahead != END_OF_FILE) {
				instance_file.seek(instance_file.getFilePointer() - 1);
				n_column--;
			}
		} catch (IOException e) {
			System.out.println("Falha ao retornar a leitura\n" + e);
			System.exit(4);
		}
	}

	/*
	 * //[1] Voce devera se preocupar quando incremetar as linhas e colunas, //
	 * assim como quando decrementar ou reseta-las. //[2] Toda vez que voce
	 * encontrar um lexema completo, voce deve retornar // um objeto
	 * tabelaSimbolos.token(Tag, "lexema", linha, coluna). Cuidado com as //
	 * palavras reservadas que ja sao cadastradas na TS. Essa consulta // voce
	 * devera fazer somente quando encontrar um Identificador. //[3] Se o caractere
	 * lido nao casar com nenhum caractere esperado, // apresentar a mensagem de
	 * erro na linha e coluna correspondente.
	 * 
	 */
	// Obtém próximo token: esse metodo simula um AFD
	public Token proxToken() {

		StringBuilder lexema = new StringBuilder();
		int estado = 0;
		char c;
		this.panico = false;
		// recupera de um erro

		while (true) {
			c = '\u0000'; // null char
			// avanca caractere ou retorna token
			try {
				// read() retorna um inteiro. -1 em caso de EOF
				lookahead = instance_file.read();

				if (lookahead != END_OF_FILE) {
					c = (char) lookahead; // conversao int para char
					n_column++;
				}
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo");
				System.exit(3);
			}

			// movimentacao do automato
			switch (estado) {
			// estado 1
			case 0:
				if (lookahead == END_OF_FILE)
					return tabelaSimbolos.token("EOF", Tag.EOF, n_line, n_column);

				else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
					// Permance no estado = 0
					estado = 0;
					if (c == '\n') {
						this.proxLinha();
					} else if (c == '\r') {

					} else if (c == '\t') {
						n_column += 3;
					}
				} else if (Character.isLetter(c)) {
					lexema.append(c);
					estado = 27;
				} else if (Character.isDigit(c)) {
					lexema.append(c);
					estado = 22;
				} else if (c == '<') {
					estado = 5;
				} else if (c == '>') {
					estado = 11;
				} else if (c == '(') {
					// estado Q14;
					return tabelaSimbolos.token("(", Tag.SMB_OP, n_line, n_column);
				} else if (c == ')') {
					// estado Q15;
					return tabelaSimbolos.token(")", Tag.SMB_CP, n_line, n_column);
				} else if (c == ';') {
					// estado Q16;
					return tabelaSimbolos.token(";", Tag.SMB_SEMICOLON, n_line, n_column);
				} else if (c == ',') {
					// estado Q17;
					return tabelaSimbolos.token(",", Tag.SMB_COMMA, n_line, n_column);
				} else if (c == '"') {
					estado = 29;
				} else if (c == '/') {
					estado = 4;
				} else if (c == '*') {
					estado = 3;
				} else if (c == '-') {
					return tabelaSimbolos.token("-", Tag.RELOP_MINUS, n_line, n_column);
				} else if (c == '+') {
					// Q1 será inibido por não ter um derivação a partir do simbolo encontrado
					// estado = 1;
					return tabelaSimbolos.token("+", Tag.RELOP_SUM, n_line, n_column);
				} else if (c == '=') {
					return tabelaSimbolos.token("=", Tag.RELOP_EQ, n_line, n_column);
				} else {
					sinalizaErro("Caractere invalido '" + c + "'.\n\tLinha " + n_line + "\t coluna " + n_column);
				}
				break;
			case 3:
				if (c == '/') {
					// Comentario
					estado = 18;
				} else {
					retornaPonteiro();
					return tabelaSimbolos.token("*", Tag.RELOP_MULT, n_line, n_column);
				}
				break;
			case 4:
				if (c == '/') {
					estado = 19;
				} else if (c == '*') {
					estado = 18;
				} else {
					return tabelaSimbolos.token("/", Tag.RELOP_DIV, n_line, n_column);
				}
				break;
			case 5:
				if (c == '=') {
					// Estado Q6
					return tabelaSimbolos.token("<=", Tag.RELOP_LE, n_line, n_column);
				} else if (c == '>') {
					// Estado Q7
					return tabelaSimbolos.token("<>", Tag.RELOP_NE, n_line, n_column);
				} else if (c == '-') {
					estado = 8;
				} else {
					this.retornaPonteiro();
					return tabelaSimbolos.token("<", Tag.RELOP_LT, n_line, n_column);
				}
				break;

			case 6:
				if (c == '=') {
					// estado = 7;
					return tabelaSimbolos.token("<=", Tag.RELOP_LE, n_line, n_column);
				} else {
					// estado = 8;
					retornaPonteiro();
					return tabelaSimbolos.token("<", Tag.RELOP_LT, n_line, n_column);
				}
				// break;
			case 8: // Tratamento modo Panico
				if (c == '-') {
					// Estado Q9;
					return tabelaSimbolos.token("<--", Tag.RELOP_ASSIGN, n_line, n_column);
				} else if (c == '\n') {
					proxLinha();
				} else if (lookahead == END_OF_FILE) {
					sinalizaErro(
							"Esperado '-' mas recebeu FIM DE ARQUIVO.\n\tLinha: " + n_line + "\tColuna: " + n_column);
					return null;
				}
				sinalizaErro("Esperado '-' mas recebeu '" + c + "'\n\tLinha: " + n_line + "\tColuna: " + n_column);
				break;

			case 11:
				if (c == '=') {
					// estado = 12;
					return tabelaSimbolos.token(">=", Tag.RELOP_GE, n_line, n_column);

				} else {
					// estado = 13;
					retornaPonteiro();
					return tabelaSimbolos.token(">", Tag.RELOP_GT, n_line, n_column);
				}
				// break;
			case 17:
				if (Character.isLetterOrDigit(c)) {
					lexema.append(c);
					// Permanece no estado 17
				} else {
					// estado = 18;
					retornaPonteiro();
					return tabelaSimbolos.token(lexema.toString(), Tag.ID, n_line, n_column);
				}
				break;
			case 18:
				if (c == '\n') {
					proxLinha();
				} else if (c == '\r') {
					//
				} else if (c == '\t') {
					n_column += 3;
				} else if (c == '*') {
					estado = 180;
				}
				break;
			case 180:
				if (c == '/') {
					estado = 0;
				} else {
					retornaPonteiro();
					estado = 18;
				}
				break;
			case 19:
				if (c == '\n') {
					// Reinicia o automato
					proxLinha();
					estado = 0;
				}
				break;
			case 20:
				if (c == '/') {
					// Reinicia o automato
					estado = 0;
				}
				break;
			case 21:
				if (Character.isDigit(c)) {
					lexema.append(c);
					estado = 22;
				} else {
					retornaPonteiro();
					sinalizaErro("Padrao para [ConstNumDouble] invalido na linha " + n_line + " coluna " + n_column);
					return null;
				}
				break;
			case 22:
				if (Character.isDigit(c)) {
					lexema.append(c);
					// permanece no estado 22
				} else if (c == '.') {
					lexema.append(c);
					estado = 24;
					// vai para o estado 24
				} else {
					// estado Q26
					retornaPonteiro();
					return tabelaSimbolos.token(lexema.toString(), Tag.TP_NUMERICO, n_line, n_column);
				}
				break;
			case 24:
				if (Character.isDigit(c)) {
					lexema.append(c);
					estado = 25;
				} else if (lookahead == END_OF_FILE) {
					return tabelaSimbolos.token("EOF", Tag.EOF, n_line, n_column);
				} else if (c == '\n') {
					proxLinha();
				} else {
					sinalizaErro("Esperado um digito, mas recebeu '" + c + "'\n\tLinha: " + n_line + "\tColuna: "
							+ n_column);
				}
				break;
			case 25:
				if (Character.isDigit(c)) {
					lexema.append(c);
				} else {
					// Estado Q26
					retornaPonteiro();
					return tabelaSimbolos.token(lexema.toString(), Tag.TP_NUMERICO, n_line, n_column);
				}
				break;

			case 27:
				if (Character.isLetterOrDigit(c) || Character.isDigit(c)) {
					lexema.append(c);
				} else {
					// Estado = 28
					this.retornaPonteiro();
					return tabelaSimbolos.token(lexema.toString(), Tag.ID, n_line, n_column);
				}
				break;
			case 29:
				if (c == '"') {
					// Estado Q30
					return tabelaSimbolos.token(lexema.toString(), Tag.TP_LITERAL, n_line, n_column);
				} else if (c == '\n') {
					proxLinha();
				} else if (lookahead == END_OF_FILE) {
					sinalizaErro("Esperado um literal mas encontrou EOF" + "'.\n\tLinha " + n_line + "\t coluna "
							+ n_column);
					return tabelaSimbolos.token("EOF", Tag.EOF, n_line, n_column);
//					return null;
				}
				lexema.append(c);
				break;

			} // fim switch
		} // fim while
	} // fim proxToken()

	/**
	 * @param args the command line arguments
	 */

}
