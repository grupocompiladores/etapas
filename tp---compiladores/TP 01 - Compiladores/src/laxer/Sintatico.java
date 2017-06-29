package laxer;

import java.security.GeneralSecurityException;

import javax.sound.midi.Soundbank;

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
	private Tabela tabela;
	
	
	public static final int MAX_ERROS = 5;
	
	public Sintatico(Lexico lexico) {
		this.errosSintaticos = 0;
		this.lexico = lexico;
		this.tabela = Singleton.getInstance();
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
		
		if (errosSintaticos > MAX_ERROS){
			System.err.println("A análise sintática encontrou mais que 5 erros. Processo de análise interrompido...");
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
	public boolean isTokenEsperado(Tag classe) {
		return token.getClasse() == classe;
	}

	public boolean isFirst(NomeProducao nome, Tag t) {
		Producao p = tabela.getTabela().get(nome);
		
		return ( p.getConjuntoFirst().contains(t) );
	}

	public boolean isFollow(NomeProducao nome, Tag t) {
		Producao p = tabela.getTabela().get(nome);
		
		return ( p.getConjuntoFollow().contains(t) );
	}

	public boolean verificaProducaoVazia(NomeProducao nome){
		Producao p = tabela.getTabela().get(nome);
		
		return ( p.getConjuntoFirst().contains(Tag.VAZIA));
	}
	
	//-------------------------------------PRODUCOES COMEÇAM AQUI-------------------------------------------------
	public void prodPrograma(){
		 advance();
		 if(isFirst(NomeProducao.prodClasse, token.getClasse())) { //veio o token correto para a producao
			 prodClasse();
			 if(!eat(Tag.EOF)){ //consome EOF.
				 erroSintatico("Esperado 'fim de arquivo', encontrado '" + token.getLexema() + "'");
			 }
		 } else {
			 erroSintatico("Esperado 'class', encontrado '" + token.getLexema() + "'");
			 errosSintaticos++;
			 if(errosSintaticos <= 5)
				 prodPrograma();
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
		} 
		
		if(isFirst(NomeProducao.prodListaFuncao, token.getClasse())){
			prodListaFuncao();
		} else{
			if(!verificaProducaoVazia(NomeProducao.prodListaFuncao)){
				//avançar até encontrar o first ou follow.
			}
		}
		
		
		
		
		
	}
	
	//ListaFuncao -> ListaFuncaoLinha
	public void prodListaFuncao() {
		if(isTokenEsperado(Tag.KW_DEF)){
			prodListaFuncaoLinha();
		} 
	}
	
	public void prodListaFuncaoLinha(){
		if(isTokenEsperado(Tag.KW_DEF)){
			prodFuncao();
			prodListaFuncaoLinha();
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
		prodListaCmd();
		if(!eat(Tag.KW_END)){
			erroSintatico("Esperado 'end', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', porem encontrado '" + token.getLexema() + "'");
		}	
	}
	
	public void prodFuncao() {
		
		if(isTokenEsperado(Tag.KW_DEF)){
			eat(Tag.KW_DEF);
			prodTipoMacro();
			if(!eat(Tag.ID)){
				erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_ABREPAR)){
				erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
			}
			prodListaArg();
			if(!eat(Tag.SMB_FECHAPAR)){
				erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_DOISPONTOS)){
				erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
			}
			prodFDID();
			prodListaCmd();
			prodRetorno();
			if(!eat(Tag.KW_END)){
				erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
			
		} else{
			erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
		}
		
		
		
	}
	
	public void prodTipoPrimitivo() {
		if(!eat(Tag.KW_BOOL) || !eat(Tag.KW_INTEGER) || !eat(Tag.KW_STRING) || !eat(Tag.KW_DOUBLE) || 
		   !eat(Tag.KW_VOID) ){
			erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
		}
	}
	
	private void prodRetorno() {
		
	}

	private void prodListaArg() {
		if(isTokenEsperado(Tag.KW_BOOL) || isTokenEsperado(Tag.KW_INTEGER) || isTokenEsperado(Tag.KW_STRING) || 
		   isTokenEsperado(Tag.KW_DOUBLE) || isTokenEsperado(Tag.KW_VOID)){
			
			prodArg();
			prodListaArgLinha();
		}
		
	}
	
	private void prodListaArgLinha() {
		if(isTokenEsperado(Tag.SMB_VIRGULA)){
			if(!eat(Tag.SMB_VIRGULA)){
				erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
			} else
				prodListaArg();
		}
	}

	private void prodArg(){
		if(isTokenEsperado(Tag.KW_BOOL) || isTokenEsperado(Tag.KW_INTEGER) || isTokenEsperado(Tag.KW_STRING) || 
		   isTokenEsperado(Tag.KW_DOUBLE) || isTokenEsperado(Tag.KW_VOID)) {
			
			prodTipoMacro();
			if(!eat(Tag.ID)){
				erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
			}
		} else
			erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
	}

	private void prodTipoMacro() {
		if(isTokenEsperado(Tag.KW_BOOL) || isTokenEsperado(Tag.KW_INTEGER) || isTokenEsperado(Tag.KW_STRING) || 
		   isTokenEsperado(Tag.KW_DOUBLE) || isTokenEsperado(Tag.KW_VOID)) {
			prodTipoPrimitivo();
		} else
			erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
		
		if(isTokenEsperado(Tag.SMB_ABRECOL)){
			prodTipoMacroLinha();
		}
	}
	
	private void prodTipoMacroLinha(){
		if(isTokenEsperado(Tag.SMB_ABRECOL)){
			eat(Tag.SMB_ABRECOL);
			if(isTokenEsperado(Tag.SMB_FECHACOL)){
				eat(Tag.SMB_FECHACOL);
			} else
				erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
		}
	}

	
	
	private void prodCmdIf(){
		if(isTokenEsperado(Tag.KW_IF)){
			eat(Tag.KW_IF);
			
			if(!eat(Tag.SMB_ABREPAR)){
				erroSintatico("Esperado '(', porem encontrado '" + token.getLexema() + "'");
			} 
			
		} else
			erroSintatico("Esperado 'if', porem encontrado '" + token.getLexema() + "'");
	}
	
	private void prodListaCmd() {
		
		
	}

	public void prodFDID() { //pronto
		if(isTokenEsperado(Tag.KW_BOOL) || isTokenEsperado(Tag.KW_INTEGER) || isTokenEsperado(Tag.KW_STRING) || 
		   isTokenEsperado(Tag.KW_DOUBLE) || isTokenEsperado(Tag.KW_VOID)){
			
			prodDeclaraID();
			prodFDID();
		}
		
	}
	
	private void prodDeclaraID() { //pronto
		if(isTokenEsperado(Tag.KW_BOOL) || isTokenEsperado(Tag.KW_INTEGER) || isTokenEsperado(Tag.KW_STRING) || 
		   isTokenEsperado(Tag.KW_DOUBLE) || isTokenEsperado(Tag.KW_VOID)){
			
			prodTipoMacro();
			
			if(!eat(Tag.ID)){
				erroSintatico("Esperado 'ID', porem encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', porem encontrado '" + token.getLexema() + "'");
			}
		} else
			erroSintatico("Esperado 'bool/integer/String/double/void', porem encontrado '" + token.getLexema() + "'");
		
	}

	public void prodOpUnario(){
		if(!eat(Tag.OPUNARIO)){
			erroSintatico("Esperado '! ou -', porem encontrado '" + token.getLexema() + "'");
		}
	}
	
	public static void main(String[] args) {
		
		
	}
	
	
}
