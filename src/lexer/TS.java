package lexer;

import java.util.Map;
import java.util.HashMap;

public class TS {
    
    private HashMap<String, Token> tabelaSimbolos; // Tabela de símbolos do ambiente

    public TS() {
        tabelaSimbolos = new HashMap();

        // Inserindo as palavras reservadas
        Token word;
        word = new Token(Tag.KW, "algoritmo", 0, 0);
        this.tabelaSimbolos.put("algoritmo", word);
        
        word = new Token(Tag.KW, "declare", 0, 0);
        this.tabelaSimbolos.put("declare", word);
        
        word = new Token(Tag.KW, "fim", 0, 0);
        this.tabelaSimbolos.put("fim", word);
        
        word = new Token(Tag.KW, "subrotina", 0, 0);
        this.tabelaSimbolos.put("subrotina", word);
        
        word = new Token(Tag.KW, "declare", 0, 0);
        this.tabelaSimbolos.put("declare", word);

        word = new Token(Tag.KW, "retorne", 0, 0);
        this.tabelaSimbolos.put("retorne", word);
        
        word = new Token(Tag.TP_LOGICO, "logico", 0, 0);
        this.tabelaSimbolos.put("logico", word);
        
        word = new Token(Tag.TP_NUMERICO, "numerico", 0, 0);
        this.tabelaSimbolos.put("numerico", word);
        
        word = new Token(Tag.TP_LITERAL, "literal", 0, 0);
        this.tabelaSimbolos.put("literal", word);
        
        word = new Token(Tag.TP_NULO, "nulo", 0, 0);
        this.tabelaSimbolos.put("nulo", word);
        
        word = new Token(Tag.KW, "se", 0, 0);
        this.tabelaSimbolos.put("se", word);
        
        word = new Token(Tag.KW, "inicio", 0, 0);
        this.tabelaSimbolos.put("inicio", word);
        
        word = new Token(Tag.KW, "fim", 0, 0);
        this.tabelaSimbolos.put("fim", word);
        
        word = new Token(Tag.KW, "senao", 0, 0);
        this.tabelaSimbolos.put("senao", word);
        
        word = new Token(Tag.KW, "enquanto", 0, 0);
        this.tabelaSimbolos.put("enquanto", word);
        
        word = new Token(Tag.KW, "faca", 0, 0);
        this.tabelaSimbolos.put("faca", word);
        
        word = new Token(Tag.KW, "para", 0, 0);
        this.tabelaSimbolos.put("para", word);
        
        word = new Token(Tag.KW, "ate", 0, 0);
        this.tabelaSimbolos.put("ate", word);
        
        word = new Token(Tag.KW, "faca", 0, 0);
        this.tabelaSimbolos.put("faca", word);
        
        word = new Token(Tag.KW, "repita", 0, 0);
        this.tabelaSimbolos.put("repita", word);
        
        word = new Token(Tag.KW, "escreva", 0, 0);
        this.tabelaSimbolos.put("escreva", word);
        
        word = new Token(Tag.KW, "leia", 0, 0);
        this.tabelaSimbolos.put("leia", word);
        
        word = new Token(Tag.KW, "verdadeiro", 0, 0);
        this.tabelaSimbolos.put("verdadeiro", word);
        
        word = new Token(Tag.KW, "falso", 0, 0);
        this.tabelaSimbolos.put("falso", word);
        
        word = new Token(Tag.KW, "nao", 0, 0);
        this.tabelaSimbolos.put("nao", word);
        
        word = new Token(Tag.KW, "e", 0, 0);
        this.tabelaSimbolos.put("e", word);
        
        word = new Token(Tag.KW, "ou", 0, 0);
        this.tabelaSimbolos.put("ou", word);   
    }
    
    public void put(String s, Token w) {
        tabelaSimbolos.put(s, w);
    }


    public Token retornaToken(String lexema) {
		  Token token = tabelaSimbolos.get(lexema); 
		  return token;   	
    }
    
    @Override
    public String toString() {
        String saida = "";
        int i = 1;
        
        for(Map.Entry<String, Token> entry : tabelaSimbolos.entrySet()) {
            saida += ("posicao " + i + ": \t" + entry.getValue().toString()) + "\n";
            i++;
        }
        return saida;
    }
}