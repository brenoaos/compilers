package lexer;

public enum Tag {
    
    // fim de arquivo
    EOF,
    
    //Operadores
    RELOP_LT,       // 		<
    RELOP_LE,       // 		<=
    RELOP_GT,       // 		>
    RELOP_GE,       // 		>=
    RELOP_EQ,       // 		==
    RELOP_NE,       // 		!=
    RELOP_ASSIGN,   // 		<--
    RELOP_SUM,      // 		+
    RELOP_MINUS,    // 		-
    RELOP_MULT,     // 		*
    RELOP_DIV,      // 		/
    
    
    
    //COMENTARIOS
    CMNT_OPEN,		//		/*
    CMNT_CLOSE,		//		*/
    CMNT_LINE,		//		//
    
    //Simbolos
    SMB_OP,         // 		(
    SMB_CP,         // 		)
    SMB_SEMICOLON,  // 		;
    SMB_COMMA,		// 		,
    
    //identificador
    ID,
    
    //tipos
    TP_NUMERICO,
    TP_LOGICO,
    TP_LITERAL,
    TP_NULO,
 
    // palavra reservada
    KW;
}
