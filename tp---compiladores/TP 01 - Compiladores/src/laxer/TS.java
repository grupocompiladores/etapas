/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laxer;

import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

/**
 * @author Bernardo Fonseca
 * Classe que contém contém e armazena em tempo de execução, tokens reconhecidos da linguagem
 */
public class TS {

	private HashMap<Token, InfIdentificador> tabelaSimbolos; 

	public TS() {
		tabelaSimbolos = new HashMap();

		// Inserindo as palavras reservadas
		Token word;
		word = new Token(Tag.KW_IF, "if", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());

		word = new Token(Tag.KW_THEN, "then", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());

		word = new Token(Tag.KW_ELSE, "else", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_BOOL, "bool", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_INTEGER, "integer", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_STRING, "String", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_DOUBLE, "double", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());

		word = new Token(Tag.KW_VOID, "void", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_VECTOR, "vector", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_CLASS, "class", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_DEF, "def", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_DEFSTATIC, "defstatic", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_MAIN, "main", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_RETURN, "return", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_END, "end", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_WHILE, "while", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_WRITE, "write", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_WRITELN, "writeln", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_TRUE, "true", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_FALSE, "false", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_OR, "or", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());
		
		word = new Token(Tag.KW_AND, "and", 0, 0);
		this.tabelaSimbolos.put(word, new InfIdentificador());		
	}

	public void put(Token w, InfIdentificador i) {
		tabelaSimbolos.put(w, i);
	}

	// Retorna um identificador de um determinado token
	public InfIdentificador getIdentificador(Token w) {
		InfIdentificador infoIdentificador = (InfIdentificador) tabelaSimbolos.get(w);
		return infoIdentificador;
	}

	// Pesquisa na tabela de símbolos se há algum tokem com determinado lexema
	public Token retornaToken(String lexema) {
		for (Token token : tabelaSimbolos.keySet()) {
			if (token.getLexema().equals(lexema)) {
				return token;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String saida = "";
		int i = 1;
		for (Token token : tabelaSimbolos.keySet()) {
			saida += ("posicao " + i + ": \t" + token.toString()) + "\n";
			i++;
		}
		return saida;
	}

	protected static Tag isKeyWord(String lexema) {
		switch (lexema) {
			case "bool":
				return Tag.KW_BOOL;
			case "integer":
				return Tag.KW_INTEGER;
			case "String":
				return Tag.KW_STRING;
			case "double":
				return Tag.KW_DOUBLE;
			case "void":
				return Tag.KW_VOID;
			case "vector":
				return Tag.KW_VECTOR;	
			case "class":
				return Tag.KW_CLASS;
			case "def":
				return Tag.KW_DEF;
			case "defstatic":
				return Tag.KW_DEFSTATIC;
			case "main":
				return Tag.KW_MAIN;
			case "return":
				return Tag.KW_RETURN;
			case "if":
				return Tag.KW_IF;
			case "then":
				return Tag.KW_THEN;
			case "else":
				return Tag.KW_ELSE;
			case "end":
				return Tag.KW_END;
			case "while":
				return Tag.KW_WHILE;
			case "write":
				return Tag.KW_WRITE;
			case "writeln":
				return Tag.KW_WRITELN;
			case "true":
				return Tag.KW_TRUE;
			case "false":
				return Tag.KW_FALSE;
			case "or":
				return Tag.KW_OR;
			case "and":
				return Tag.KW_AND;
			
			default: { //qualquer outra coisa, retorna false.
				return Tag.ID;
			}
		}
	}
	
	protected static Tag isSymbol(String lexema){
		switch (lexema) {
			case ".":
				return Tag.SMB_PONTO;
			case ",":
				return Tag.SMB_VIRGULA;
			case ";":
				return Tag.SMB_PONTOVIRGULA;
			case ":":
				return Tag.SMB_DOISPONTOS;
			case "(":
				return Tag.SMB_ABREPAR;
			case ")":
				return Tag.SMB_FECHAPAR;
			case "[":
				return Tag.SMB_ABRECOL;
			case "]":
				return Tag.SMB_FECHAPAR;
			case "=":
				return Tag.SMB_IGUAL;
			
			default: {
				return Tag.SMB;
			}
		}
		
	}

}