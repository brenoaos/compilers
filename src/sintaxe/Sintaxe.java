package sintaxe;


import com.sun.javafx.tools.packager.Param;

import lexer.Lexer;
import lexer.Token;
import lexer.Tag;

public class Sintaxe {
	private final Lexer lexer;
	private Token token;
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
			return true;
		} else {
			return false;
		}
	}

	public void skip(String mensagem) {
		erroSintatico(mensagem);
		advance();
	}

	public void programa() {
		if ((token.getClasse() != Tag.KW) && !token.getLexema().equals("algoritmo")) {
			skip("Esperado \"algoritmo\", encontrado " + "\"" + token.getLexema() + "\"");
		}
		advance();
		RegexDeclVar();
		
		ListaCmd();
			
		if ( (token.getClasse() != Tag.KW) && !token.getLexema().equals("Fim")) {
			skip("Esperado 'fim' mas recebeu '" + token.getLexema() + "'");
		}
				
		advance();
		if ((token.getClasse() != Tag.KW) && !token.getLexema().equals("algoritmo")) {
			skip("Esperado \"algoritmo\", encontrado " + "\"" + token.getLexema() + "\"");
		}		
		
		advance();
		listaRotina();
		
		if ((token.getClasse() != Tag.EOF) && !token.getLexema().equals("EOF")) {
			skip("Esperado \"Fim de arquivo\", encontrado " + "\"" + token.getLexema() + "\"");
		}
		
		System.out.println("Compilação chegou ao fim.");
	}

	public void RegexDeclVar() {
		if (token.getClasse() == Tag.KW && (token.getLexema().equals("declare"))) {
			advance();
			tipo();
			
			ListaID();
			
			if (token.getClasse() != Tag.SMB_SEMICOLON && (!token.getLexema().equals(";"))) {
				ListaCmd();
			}
			
			advance();
			declaraVar();
			
			if (token.getClasse() != Tag.SMB_SEMICOLON && (!token.getLexema().equals(";"))) {
				skip("Esperado ';' mas recebeu '" + token.getLexema()  + "'");
			}
			advance();
		}
		
	}

	public void ListaCmd() {
		ListaCmd_();
	}
	
	public void listaRotina() {
		listaRotina_();
	}
	
	public void listaRotina_() {
		if (token.getClasse() == Tag.KW && (token.getLexema().equals("subrotina"))){
			advance();
			rotina();
			listaRotina_();
		}		
	}
	
	public void rotina() {
		if(token.getClasse() != Tag.ID) {
			skip("Esperad um 'ID', mas recebeu " + token.getLexema());
		}
		
		advance();
		if (token.getClasse() != Tag.SMB_OP && token.getLexema().equals("(")) {
			skip("Esperad um '(', mas recebeu " + token.getLexema());
		}
		
		advance();
		listaParam();
		
		if (token.getClasse() != Tag.SMB_CP && token.getLexema().equals(")")) {
			skip("Esperad um ')', mas recebeu " + token.getLexema());
		}
		
		advance();
		RegexDeclVar();
		
		ListaCmd();
		
		Retorno();
		
		if (token.getClasse() != Tag.KW && token.getLexema().equals("fim")) {
			skip("Esperad um 'fim', mas recebeu " + token.getLexema());
		}
		
		advance();
		
		if (token.getClasse() != Tag.KW && token.getLexema().equals("rotina")) {
			skip("Esperado um 'rotina', mas recebeu " + token.getLexema());
		}
		
		advance();
	
	}
	
	public void listaParam() {
		
		param();
		listaParam_();
	}
	
	public void listaParam_() {
		if (token.getClasse() != Tag.SMB_COMMA && token.getLexema().equals(",")) {
			advance();
			listaParam_();
		}
	}
	
	public void param() {
		tipo();
		ListaID();
	}
	
	public void Retorno() {
		
	}
	
	public void tipo() {
		if (token.getClasse() != Tag.KW && !(token.getLexema().equals("logico") || token.getLexema().equals("numerico")
				|| token.getLexema().equals("literal") || token.getLexema().equals("nulo"))) {
			skip("Esperado um TIPO para declaração");
		}
		advance();
	}

	public void declaraVar() {
		if (token.getClasse() != Tag.KW && !(token.getLexema().equals("logico") || token.getLexema().equals("numerico")
				|| token.getLexema().equals("literal") || token.getLexema().equals("nulo"))) {
			skip("Esperado um TIPO para declaração");
		}
		advance();
		ListaID();
	}

	public void ListaID() {
		if (token.getClasse() != Tag.ID) {
			skip("Esperando um ID mas recebeu " + token.getClasse() + "->" + token.getLexema());
		}
		advance();
		ListaID_();
	}

	public void ListaID_() {
		if (token.getClasse() == Tag.SMB_COMMA) {
			advance();
			ListaID();
		}
	}

	public void ListaCmd_() {
		if (token.getClasse() == Tag.KW) {
			switch (token.getLexema()) {
			case "se":
				cmdSe();
				break;
			case "enquanto":
				cmdEnquanto();
				break;
			case "para":
				cmdPara();
				break;
			case "repita":
				cmdRepita();
				break;
			case "escreva":
				cmdEscreva();
				break;
			case "leia":
				cmdLeia();
				break;
			}
		}
		else if (token.getClasse() == Tag.ID) {
			advance();
			cmd_();
			ListaCmd_();
		}
	}

	public void cmd_() {
		if ((token.getClasse() == Tag.RELOP_ASSIGN) && (token.getLexema().equals("<--"))) {
			advance();
			cmdAtrib();
		}
		else if((token.getClasse() == Tag.SMB_OP) && (token.getLexema().equals("("))) {
			advance();
			cmdChamaRotina();
		}
		else {
			skip("Esperando '(' ");
		}
	}

	
	public void cmdChamaRotina() {
		regexExp();
		
		if (token.getClasse() != Tag.SMB_CP && (!token.getLexema().equals(")"))){
			skip("Esperando ')' ");
		}
		
		advance();
		if (token.getClasse() != Tag.SMB_SEMICOLON && (token.getLexema().equals(";"))){
			skip("Esperando ';' ");
		}	
		advance();
	}
	
	public void cmdAtrib() {
		expressao();
		if ((token.getClasse() != Tag.SMB_SEMICOLON) && (!token.getLexema().equals(";"))) {
			skip("Esperado ';' mas recebeu " + token.getLexema());
		}
		advance();
	}
	
	public void cmdLeia() {
		advance();
		if ((token.getClasse() != Tag.SMB_OP) && (token.getLexema() != "(")) {
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}

		advance();
		if (token.getClasse() != Tag.ID) {
			skip("Esperado ID mas recebeu " + token.getLexema());
		}

		advance();
		if ((token.getClasse() != Tag.SMB_CP) && (token.getLexema() != ")")) {
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}

		advance();
		if ((token.getClasse() != Tag.SMB_SEMICOLON) && (token.getLexema() != ";")) {
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}

		advance();
		ListaCmd_();
	}

	public void cmdEscreva() {
		advance();
		if ((token.getClasse() != Tag.SMB_OP) && (!token.getLexema().equals("("))) {
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}
		advance();
		
		expressao();

		if ((token.getClasse() != Tag.SMB_CP) && (!token.getLexema().equals(")"))) {
			skip("Esperado ')' mas recebeu " + token.getLexema());
		}

		advance();
		if ((token.getClasse() != Tag.SMB_SEMICOLON) && (!token.getLexema().equals(";"))) {
			skip("Esperado ';' mas recebeu " + token.getLexema());
		}
		
		advance();
		ListaCmd_();
	}

	public void expressao() {
		exp1();
		exp_();
	}
	
	public void exp_() {
		if ((token.getClasse() == Tag.RELOP_LT) && token.getLexema().equals(">") || (token.getClasse() == Tag.RELOP_LE) && token.getLexema().equals("<=") || 
			(token.getClasse() == Tag.RELOP_GT) && token.getLexema().equals("<") || (token.getClasse() == Tag.RELOP_GE) && token.getLexema().equals(">=") || 
			(token.getClasse() == Tag.RELOP_EQ) && token.getLexema().equals("=") || (token.getClasse() == Tag.RELOP_NE) && token.getLexema().equals("<>")) {
				advance();
				exp1();
				exp_();
		}
	}
	
	public void exp1() {
		exp2();
		exp1_();
	}
	
	public void exp1_() {
		if ((token.getClasse() == Tag.KW) && token.getLexema().equals("E") || (token.getClasse() == Tag.KW) && token.getLexema().equals("Ou")) {
			advance();
			exp2();
			exp1_();
		}
	}
	
	public void exp2() {
		exp3();
		exp2_();
	}
	
	public void exp2_() {
		if ((token.getClasse() == Tag.RELOP_SUM) && token.getLexema().equals("+") || (token.getClasse() == Tag.RELOP_MINUS) && token.getLexema().equals("-")) {
			advance();
			exp3();
			exp2_();
		}
	}
	
	public void exp3() {
		exp4();
		exp3_();
	}
	
	public void exp3_() {
		if ((token.getClasse() == Tag.RELOP_MULT) && token.getLexema().equals("*") || (token.getClasse() == Tag.RELOP_DIV) && token.getLexema().equals("/")) {
			advance();
			exp4();
			exp3_();
		}
	}
	
	public void exp4() {
		if (token.getClasse() == Tag.ID) {
			advance();
			exp4_();
		}
		else if(token.getClasse() == Tag.SMB_OP && token.getLexema().equals("(")) {
			advance();
			expressao();
			if(token.getClasse() != Tag.SMB_CP && !token.getLexema().equals(")")){
				skip("Esperado ')', mas recebeu "+ token.getLexema());
			}
			advance();
		}
		else if ((token.getClasse() == Tag.TP_NUMERICO || token.getClasse() == Tag.TP_LITERAL) || 
				(token.getClasse() == Tag.KW && token.getLexema().equals("verdadeiro")) ||
				(token.getClasse() == Tag.KW && token.getLexema().equals("falso"))){
					advance();
				}
		else {
			opUnario();
		}
	}
	
	public void exp4_() {
		if (token.getClasse() == Tag.SMB_OP && token.getLexema().equals("(")){
			regexExp();
			if (token.getClasse() != Tag.SMB_CP && !token.getLexema().equals(")")){
				skip("expressão invalida 3");
			}
		}
		
	}
	
	public void opUnario() {
		if((token.getClasse() == Tag.KW) && token.getLexema().equals("Nao")){
			advance();
			expressao();	
		}
	}
	
	public void cmdSe() {
		advance();
		if((token.getClasse() != Tag.SMB_OP) && !token.getLexema().equals("(")){
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}
		
		advance();
		expressao();
		
		if((token.getClasse() != Tag.SMB_CP) && !token.getLexema().equals(")")){
			skip("Esperado ')' mas recebeu " + token.getLexema());
		}
		
		advance();
		
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("inicio")){
			skip("Esperado 'Inicio' mas recebeu " + token.getLexema());
		}
		
		advance();
		ListaCmd();
				
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("fim")){
			skip("Esperado 'Fim' mas recebeu " + token.getLexema());
		}
		
		advance();
		cmdSe_();
				
	}

	public void cmdSe_() {
		if((token.getClasse() == Tag.KW) && token.getLexema().equals("senao")){
			advance();
			if((token.getClasse() != Tag.KW) && !token.getLexema().equals("inicio")){
				skip("Esperado 'inicio' mas recebeu " + token.getLexema());
			}
			
			advance();
			ListaCmd();
			
			if((token.getClasse() != Tag.KW) && !token.getLexema().equals("fim")){
				skip("Esperado 'Fim' mas recebeu " + token.getLexema());
			}
			
		}
	}

	public void regexExp() {
		expressao();
		regexExp_();
	}
	
	public void regexExp_(){
		if((token.getClasse() == Tag.SMB_COMMA) && token.getLexema().equals(",")) {
			advance();
			expressao();
			regexExp_();
		}
	}
	
	public void cmdSenao() {

	}

	public void cmdEnquanto() {

		advance();
		if((token.getClasse() != Tag.SMB_OP) && !token.getLexema().equals("(")){
			skip("Esperado '(' mas recebeu " + token.getLexema());
		}
		
		advance();
		expressao();
		
		if((token.getClasse() != Tag.SMB_CP) && !token.getLexema().equals(")")){
			skip("Esperado ')' mas recebeu " + token.getLexema());
		}
		
		advance();
		
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("faca")){
			skip("Esperado 'faca' mas recebeu " + token.getLexema());
		}
		
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("inicio")){
			skip("Esperado 'Inicio' mas recebeu " + token.getLexema());
		}
		
		advance();
		ListaCmd();
				
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("fim")){
			skip("Esperado 'Fim' mas recebeu " + token.getLexema());
		}
		
		advance();
		cmdSe_();
				
	

	}

	public void cmdPara() {
		advance();
		if((token.getClasse() != Tag.ID)){
			skip("Esperado 'ID' mas recebeu " + token.getLexema());
		}

		advance();
		cmdAtrib();
				
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("ate")){
			skip("Esperado 'ate' mas recebeu " + token.getLexema());
		}
		
		advance();
		expressao();
		
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("faca")){
			skip("Esperado 'faca' mas recebeu " + token.getLexema());
		}
		
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("inicio")){
			skip("Esperado 'Inicio' mas recebeu " + token.getLexema());
		}
		
		advance();
		ListaCmd();
				
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("fim")){
			skip("Esperado 'Fim' mas recebeu " + token.getLexema());
		}
		
		advance();
		cmdSe_();
				
	}

	public void cmdRepita() {
		advance();
		ListaCmd();
		if((token.getClasse() != Tag.KW) && !token.getLexema().equals("ate")){
			skip("Esperado 'ate' mas recebeu " + token.getLexema());
		}
		
		advance();
		expressao();
	}
}