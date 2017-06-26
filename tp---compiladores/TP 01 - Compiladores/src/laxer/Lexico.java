
package laxer;

import java.io.*;

public class Lexico {
	
	private static Token anterior; //um token auxiliar para ajudar a definir se o próximo token "-" será operador relacional ou unário
	private static final int END_OF_FILE = -1; // constante para fim do arquivo
	private static int lookahead = 0; // armazena o último caractere lido do arquivo
	
	public static int linha; // contador de linhas
	public static int coluna;
	
	private RandomAccessFile instance_file;

	
	public Lexico(String input_data) {
		this.linha = 1;
		this.coluna = 0;
		
		this.anterior = null; //inicialmente anterior é nulo
		
		// Abre instance_file de input_data
		try {
			instance_file = new RandomAccessFile(input_data, "r");
		} catch (IOException e) {
			System.out.println("Erro de abertura do arquivo " + input_data);
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Erro do programa ou falha da tabela de simbolos");
			System.exit(2);
		}
	}
	
	protected Token getAnterior(){
		return this.anterior;
	}
	
	public void fechaArquivo() {
		try {
			instance_file.close();
		} catch (IOException errorFile) {
			System.out.println("Erro ao fechar arquivo");
			System.exit(3);
		}
	}

	public void sinalizaErro(String mensagem) {
		System.out.println("[Erro Lexico]: " + mensagem);
	}

	public void retornaPonteiro() {

		try {
			if (lookahead != END_OF_FILE) {
				//System.out.println("c = '"+ (char) lookahead+"'");
				coluna--;
				instance_file.seek(instance_file.getFilePointer() - 1);
				//decrementa coluna, uma vez que retornou o ponteiro.
				//System.out.println("c = '"+ (char) lookahead+"'");
			}
		} catch (IOException e) {
			System.out.println("Falha ao retornar a leitura");
			System.exit(4);
		}
	}

	public boolean verificaAnterior() {
		if(this.getAnterior() == null) {
			return false;
		}
		if (this.getAnterior().getClasse() == Tag.ID ||
			this.getAnterior().getClasse() == Tag.CONSTDOUBLE ||
			this.getAnterior().getClasse() == Tag.CONSTINTEGER ||
			this.getAnterior().getClasse() == Tag.SMB_FECHACOL ||
			this.getAnterior().getClasse() == Tag.SMB_FECHAPAR ){
			return true;
		} else 
			return false;

	}
	
	private int calculaColuna(int tamanhoLexema, int coluna){		
		return (coluna - tamanhoLexema) + 1;
	}
	
	public Token proxToken() {	
		StringBuilder lexema = new StringBuilder();
		int estado = 1;
		char c;
		
		while (true) {
			c = '\u0000'; // null char
			try {
				lookahead = instance_file.read();
				if (lookahead != END_OF_FILE){
					c = (char) lookahead;
					coluna++;
				}	
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo");
				System.exit(3);
			}
 			switch (estado) {
			case 1:
				if (lookahead == END_OF_FILE)
					return new Token(Tag.EOF, "EOF", linha, calculaColuna(lexema.length(), coluna));
				else if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
					// Permance no estado = 1
					if (c == '\n'){
						linha++;
						coluna = 0;
					}
				} else if (Character.isLetter(c)) {
					lexema.append(c);
					estado = 14;
				} else if (Character.isDigit(c)) {
					lexema.append(c);
					estado = 12;
				} else if (c == '<') {
					lexema.append(c);
					estado = 6;
				} else if (c == '>') {
					lexema.append(c);
					estado = 9;
				} else if (c == '=') {
					lexema.append(c);
					estado = 2;
				} else if (c == '!') {
					lexema.append(c);
					estado = 4;
				} else if (c == '/') {
					lexema.append(c);
					estado = 16;	
				} else if (c == '+') {
					lexema.append(c);
					return new Token(Tag.RELOP_PL, "+", linha, calculaColuna(lexema.length(), coluna));
				} else if (c == '*') {
					lexema.append(c);
					return new Token(Tag.RELOP_MU, "*", linha, calculaColuna(lexema.length(), coluna));
				} else if (c == '-') {
					if(this.verificaAnterior() == true) { // anterior é id, constdouble, constinteger, ) ou ]
						estado = 29;
						lexema.append(c);
						return new Token(Tag.RELOP_SU, "-", linha, calculaColuna(lexema.length(), coluna));
					}
					estado = 29;
					lexema.append(c);
					return new Token(Tag.OPUNARIO, "-", linha, calculaColuna(lexema.length(), coluna));
				} else if (this.isSymbol(c)) {
					lexema.append(c);
					Tag classe = TS.isSymbol(lexema.toString());
					return new Token(classe, String.valueOf(c), linha, calculaColuna(lexema.length(), coluna));
				} else if(c == '"') {
					lexema.append(c);
					estado = 26;
				} else { // outro (caractere nao reconhecido pela linguagem)
					estado = 28;
					sinalizaErro("Caractere desconhecido: " + c + " na linha " + linha + " coluna " + coluna);
					//volta para estado 1, para ler proximos caracteres
					lexema.append(c);
					return new Token(Tag.ERROR, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 2:
				if (c == '=') { // Estado 3
					estado = 3;
					lexema.append(c);
					return new Token(Tag.RELOP_EQ, "==", linha, calculaColuna(lexema.length(), coluna));
				} else {
					estado = 25;
					retornaPonteiro();
					return new Token(Tag.OPATRIBUICAO, "=", linha, calculaColuna(lexema.length(), coluna));
				}
			case 4:
				if (c == '=') { // Estado 5
					estado = 5;
					lexema.append(c);
					return new Token(Tag.RELOP_NE, "!=", linha, calculaColuna(lexema.length(), coluna));
				} else {
					estado = 19; //estado operador unario
					retornaPonteiro();
					return new Token(Tag.OPUNARIO, "!", linha, calculaColuna(lexema.length(), coluna));
				}
			case 6:
				if (c == '=') { // Estado 7
					estado = 7;
					lexema.append(c);
					return new Token(Tag.RELOP_LE, "<=", linha, calculaColuna(lexema.length(), coluna));
				} else { // Estado 8
					estado = 8;
					retornaPonteiro();
					return new Token(Tag.RELOP_LT, "<", linha, calculaColuna(lexema.length(), coluna));
				}
			case 9:
				if (c == '=') { // Estado 10
					estado = 10;
					lexema.append(c);
					return new Token(Tag.RELOP_GE, ">=", linha, calculaColuna(lexema.length(), coluna));
				} else { // Estado 11
					estado = 11;
					retornaPonteiro();
					return new Token(Tag.RELOP_GT, ">", linha, calculaColuna(lexema.length(), coluna));
				}
			case 12:
				if (Character.isDigit(c)) {
					// Permanece no estado 12
					lexema.append(c);
				} else if (c == '.'){
					lexema.append(c);
					estado = 22;
				}
				else { // Estado 13
					estado = 13;
					retornaPonteiro();
					return new Token(Tag.CONSTINTEGER, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 14: // foi letra
				if (Character.isLetterOrDigit(c) || c == '_') {
					lexema.append(c);
					// Permanece no estado 14
				} else { // Estado 15
					estado = 15;
					retornaPonteiro();
					
					Tag classe = TS.isKeyWord(lexema.toString());
					return new Token(classe, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 16:
				if (c == '*'){
					estado = 17;
					lexema.append(c);
				}
				else if (c == '/'){
					estado = 21;
					lexema.append(c);
				}
				else {
					retornaPonteiro();
					return new Token(Tag.RELOP_DI, "/", linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 17: 
				if (c == '*') {
					estado = 18;
					lexema.append(c);
				} else if (c == '\n'){
					linha++;
					coluna=0;
				}
				else if (lookahead == END_OF_FILE) {
					sinalizaErro("O comentario deve ser fechado com */ antes do fim do arquivo");
					estado = 1;
				}
				// Se vier outro, permanece no estado 17
				break;
			case 18:
				if (lookahead == END_OF_FILE) {
					sinalizaErro("O comentario deve ser fechado com */ antes do fim do arquivo");
					//return null;
					estado = 1;
				} else if (c == '/') {
					lexema.append(c);
					estado = 1;
				} else if (c == '\n'){
					estado = 17;
					linha++;
					coluna=0;
				}
				else {
					estado = 17;
					lexema.append(c);
				}
				break;
			case 21:
				if (c == '\n' || c == '\r'){
					retornaPonteiro();
					estado = 1;
				} else{
					lexema.append(c);
				}
				//permanece no 21 ate quebrar a linha
				break;
			case 22:
				if (Character.isDigit(c)) {
					lexema.append(c);
					estado = 23;
				} else { 
					estado = 24;
					lexema.deleteCharAt(lexema.length() - 1); //como so retorna um token a cada execucao, retirar o '.' e retornar um inteiro, pois nao veio nada apï¿½s '.'
					retornaPonteiro();
					retornaPonteiro();
					return new Token(Tag.CONSTINTEGER, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 23:
				if(Character.isDigit(c)) {
					//permanece no mesmo estado
					lexema.append(c);
				} else {
					retornaPonteiro();
					return new Token(Tag.CONSTDOUBLE, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				}
				break;
			case 26:
				if (c != '"' && c != '\n' && c != '\r' && c != '\u0000') {
					//permanece no mesmo estado
					lexema.append(c);
				} else if (c == '"') {
					estado = 27;
					lexema.append(c);
					//retornaPonteiro();
					return new Token(Tag.CONSTSTRING, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
				} else {
					sinalizaErro("String não fechada na linha " + linha + " e coluna " + calculaColuna(lexema.length(), coluna));
					lexema.delete(0, lexema.length()); //identificou string nao fechada, limpa o lexema e le o proximo token
					estado = 1;
					if (c == '\n'){
						linha++;
						coluna=1;
					}
				}
				break;
			case 27:
				return new Token(Tag.CONSTSTRING, lexema.toString(), linha, calculaColuna(lexema.length(), coluna));
			}
		}
	}
	
	private boolean isSymbol(char c) {
		if (c == '.' || c == ',' || c == ';' || c == ':' || c == '(' || c == ')' || c == '[' || c == ']' || c == '=') {
			return true;
		}
		return false;
	}

	private void removerZerosEsquerda(Token token) {
		
		if (token.getClasse().equals(Tag.CONSTINTEGER)) {
			Integer novoLexema = Integer.parseInt(token.getLexema().toString());
			token.setLexema(novoLexema.toString());
		} else if(token.getClasse().equals(Tag.CONSTDOUBLE)) {
			Double novoLexema = Double.parseDouble(token.getLexema().toString());
			token.setLexema(novoLexema.toString());
		}
	}
	
	public static void main(String[] args) {
		TS tabelaSimbolos = new TS();
		Lexico lexer = new Lexico("./arquivos/programa1.txt");
		
		Token token = null;
		
		// Enquanto não houver erro ou não for fim de arquivo:
		do {
			lexer.anterior = token;
			token = lexer.proxToken();
			
			//caso o token seja um CONSTDOUBLE ou CONSTINTEGER, retira 0's à esquerda.
			if (token.getClasse().equals(Tag.CONSTDOUBLE) || token.getClasse().equals(Tag.CONSTINTEGER)) {
				lexer.removerZerosEsquerda(token);
			}
			
			// Imprime token
			if (token != null) {
				System.out.println("Token: " + token.toString() + "\t\t Linha: " + token.getLinha() + "\t\tColuna: " + token.getColuna());

				// Verifica se existe o lexema na tabela de símbolos
				if (tabelaSimbolos.retornaToken(token.getLexema()) == null)
					tabelaSimbolos.put(token, new InfIdentificador());
			}

		} while (token != null && token.getClasse() != Tag.EOF);
		lexer.fechaArquivo();

//		// Imprime a tabela de simbolos
//		System.out.println("");
//		System.out.println("Tabela de símbolos:");
//		System.out.println(tabelaSimbolos.toString());
	}

}