
package laxer;

/**
 * @author Bernardo Fonseca
 */
public enum Tag {
    
    /*
     * FIM DE ARQUIVO
     */
    EOF,
    
    ERROR,
    
    /*
     * OPERADOR RELACIONAL <
     */
    RELOP_LT, // <
    
    /*
     * OPERADOR RELACIONAL <=
     */
    RELOP_LE, // <=
    
    /*
     * OPERADOR RELACIONAL >
     */
    RELOP_GT, // >
    
    /*
     * OPERADOR RELACIONAL >=
     */
    RELOP_GE, // >=
    
    /*
     * OPERADOR RELACIONAL ==
     */
    RELOP_EQ, // ==
    
    /*
     * OPERADOR RELACIONAL !=
     */
    RELOP_NE, // !=
    
    /*
     * OPERADOR RELACIONAL /
     */
    RELOP_DI, // /
    
    /*
     * OPERADOR RELACIONAL +
     */
    RELOP_PL, // +
    
    /*
     * OPERADOR RELACIONAL *
     */
    RELOP_MU, // *
    
    /*
     * OPERADOR RELACIONAL -
     */
    RELOP_SU, // -
    
    
    
    /*
     * OPERADOR UNARIO - OU !
     */
    OPUNARIO,
    
    /*
     * OPERADOR ATRIBUIÇÃO =
     */
    OPATRIBUICAO,

    /*
     * SÍMBOLOS . , ; : () [] =
     */
    SMB, SMB_PONTO, SMB_VIRGULA, SMB_PONTOVIRGULA, SMB_DOISPONTOS, SMB_ABREPAR, SMB_FECHAPAR, SMB_ABRECOL, SMB_FECHACOL, SMB_IGUAL, 
    
    /*
     * IDENTIFICADOR
     */
    ID,
    
    /*
     * NÚMEROS INTEIROS
     */
    CONSTINTEGER,
    
    /*
     * NÚMEROS DECIMAIS
     */
    CONSTDOUBLE,
    
    /*
     * STRING LITERAL
     */
    CONSTSTRING,
    
    /*
     * PALAVRAS RESERVADAS DA LINGUAGEM
     */
    KW, KW_IF, KW_THEN, KW_ELSE, KW_BOOL, KW_INTEGER, KW_STRING, KW_DOUBLE, KW_VOID, KW_VECTOR, KW_CLASS, 
    KW_DEF, KW_DEFSTATIC, KW_MAIN, KW_RETURN, KW_END, KW_WHILE, KW_WRITE, KW_WRITELN, KW_TRUE, KW_FALSE, KW_OR, KW_AND;
	
	
	
	
}
