package laxer;

/**
 *
 * @author gustavo
 */

/*
 * [TODO]
 *
 * 1: Ajustar os erros de acordo com a TP 2: Verificar Recuperacao de Erro: 2.1:
 * Criar metodos synch() e skip() 3: Ajustar contagem de colunas no lexer 4:
 * Verificar o cadastro dos tokens na Tabela de Simbolos
 *
 */

public class Sintatico {
	
	private final Lexico lexico;
	private Token token;
	private int errosSintaticos;
	
	public Sintatico(Lexico lexico) {
		this.errosSintaticos = 0;
		this.lexico = lexico;
	}

	// Fecha os arquivos de entrada e de tokens
	public void fechaArquivos() {

		lexico.fechaArquivo();
	}

	public void erroSintatico(String mensagem) {

		System.out.println("Erro sintatico na linha " + token.getLinha() + " e coluna " + token.getColuna() + ":");
		System.out.println(mensagem + "\n");
	}

	// lexer retorna o proximo token
	public void advance() {
		token = lexico.proxToken();
	}

	public boolean eat(Tag classe) {
		
		if (errosSintaticos >= 5){
			System.err.println("A an�lise sint�tica encontrou mais que 5 erros. Processo de an�lise interrompido...");
			System.exit(1);
			return false;
		} else {
			// enquanto token nao reconhecido (modo panico Lexer)
			while (token.getClasse() == Tag.ERROR)
				advance();

			// se token eh o terminal esperado
			if (token.getClasse() == classe) {
				advance();
				return true;
			} else{
				errosSintaticos++;
				return false;
			}
		}
	}
	
	//antigo isToken();
	public boolean isClasseEsperada(Tag classe) {
		return token.getClasse() == classe;
	}

	//-------------------------------------PRODUCOES COME�AM AQUI-------------------------------------------------
	public void prodPrograma(){
		advance(); //pega o primeiro token para inicio do processamento
		
		if (isClasseEsperada(Tag.KW_CLASS)){
			prodClasse();
			
			if(!eat(Tag.EOF)) {
				erroSintatico("Esperado fim de arquivo, encontrado '" + token.getLexema() + "'");
			}
			System.out.println();
			
		} else{
			erroSintatico("Esperado 'class', encontrado '" + token.getLexema() + "'");
		}
	}
	
	public void prodClasse() {
		if (!eat(Tag.KW_CLASS)){
			erroSintatico("Esperado 'class', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.ID)) {
			erroSintatico("Esperado um ID, encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_DOISPONTOS)){
			erroSintatico("Esperado ':', encontrado '" + token.getLexema() + "'");
		} else {
			prodListaFuncao();
			prodMain();
			
			if(!eat(Tag.KW_END)){
				erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTO)){
				erroSintatico("Esperado '.', encontrado '" + token.getLexema() + "'");
			}
		}
	}
	
	//ListaFuncao -> ListaFuncaoLinha
	public void prodListaFuncao() {
		if(isClasseEsperada(Tag.KW_DEF)){
			prodListaFuncaoLinha();
		} 
	}
	
	public void prodListaFuncaoLinha(){
		if(isClasseEsperada(Tag.KW_DEF)){
			prodFuncao();
		} 
		
	}
	
	public void prodFuncao() {
		if(!eat(Tag.KW_DEF)){
			erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
		}
		prodTipoMacro();
		
		
	}
	
	private void prodTipoMacro() {
		if(isClasseEsperada(Tag.KW_BOOL) || isClasseEsperada(Tag.KW_INTEGER) || isClasseEsperada(Tag.KW_STRING) || 
		   isClasseEsperada(Tag.KW_DOUBLE) || isClasseEsperada(Tag.KW_VOID)) {
			prodTipoPrimitivo();
		} else
			erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
		
		if(isClasseEsperada(Tag.SMB_ABRECOL)){
			prodTipoMacroLinha();
		}
	}
	
	private void prodTipoMacroLinha(){
		if(!eat(Tag.SMB_ABRECOL)){
			erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_FECHACOL)){
			erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
		}
	}

	public void prodMain(){
		if(!eat(Tag.KW_DEFSTATIC)){
			erroSintatico("Esperado 'defstatic', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.KW_VOID)){
			erroSintatico("Esperado 'void', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.KW_MAIN)){
			erroSintatico("Esperado 'main', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.KW_STRING)){
			erroSintatico("Esperado 'String', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABRECOL)){
			erroSintatico("Esperado '[', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_FECHACOL)){
			erroSintatico("Esperado ']', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.ID)){
			erroSintatico("Esperado 'ID', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_FECHAPAR)){
			erroSintatico("Esperado ')', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_DOISPONTOS)){
			erroSintatico("Esperado ':', porem encontrado '" + token.getLexema() + "'");
		}
		prodFDID();
		prodListCmd();
		if(!eat(Tag.KW_END)){
			erroSintatico("Esperado 'end', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', porem encontrado '" + token.getLexema() + "'");
		}
		
		
	}
	
	private void prodListCmd() {
		
		
	}

	public void prodFDID() {
		
		
	}

	public void prodTipoPrimitivo() {
		if(!eat(Tag.KW_BOOL) || !eat(Tag.KW_INTEGER) || !eat(Tag.KW_STRING) || !eat(Tag.KW_DOUBLE) || !eat(Tag.KW_VOID) ){
			erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
		}
	}
	
	public void prodOpUnario(){
		if(!eat(Tag.OPUNARIO)){
			erroSintatico("Esperado '! ou -', porem encontrado '" + token.getLexema() + "'");
		}
	}
	
	
}
