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
	
	//100%
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
			 if(errosSintaticos <= 5){
				 prodPrograma();
			 }
			 else{
				 System.err.println("A análise sintática encontrou mais que 5 erros. Processo de análise interrompido...");
				 System.exit(1);
			 }
				 
		 }
	}
	
	//falta verificar Strings em erroSintatico(String)
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
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaFuncao, token.getClasse())){
				prodListaFuncao(); //veio um first, entro na producao
				break;
			} else {
				if(isFollow(NomeProducao.prodListaFuncao, token.getClasse())){ //veio follow de listaFuncao
					if(verificaProducaoVazia(NomeProducao.prodListaFuncao)) {
						break; 
					} else {
						erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'def ou defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodMain, token.getClasse())){
				prodMain();
				break;
			} else {
				if(isFollow(NomeProducao.prodMain, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodMain)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else{
						erroSintatico("Esperado 'defstatic', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}					
				} else {
					erroSintatico("Esperado 'defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.KW_END)){
			erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTO)){
			erroSintatico("Esperado '.', encontrado '" + token.getLexema() + "'");
		}
	}
	
	//falta verificar Strings em erroSintatico(String)
	public void prodListaFuncao() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaFuncaoLinha, token.getClasse())){
				prodListaFuncaoLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaFuncaoLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaFuncaoLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'def ou defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}
	
	//falta verificar Strings em erroSintatico(String)
	public void prodListaFuncaoLinha(){ 
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodFuncao, token.getClasse())){
				prodFuncao();
				break;
			} else {
				if(isFollow(NomeProducao.prodFuncao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodFuncao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'def ou defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaFuncaoLinha, token.getClasse())){
				prodListaFuncaoLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaFuncaoLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaFuncaoLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'def ou defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}
	
	//falta verificar Strings em erroSintatico(String)
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
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodFDID, token.getClasse())){
				prodFDID();
				break;
			} else {
				if(isFollow(NomeProducao.prodFDID, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodFDID)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'algum tipo de dado ou IF, while, ID, write, writeln, Def, Defstatic, end', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaCmd, token.getClasse())) {
				prodListaCmd();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaCmd, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaCmd)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'IF, while, ID, write, writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'IF, while, ID, write, writeln ou Return, end, else, Def, Defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.KW_END)){
			erroSintatico("Esperado 'end', porem encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', porem encontrado '" + token.getLexema() + "'");
		}	
	}
	
	//falta verificar Strings em erroSintatico(String)
	public void prodFuncao() {
		if(!eat(Tag.KW_DEF)){
			erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodTipoMacro, token.getClasse())){
				prodTipoMacro();
				break;
			} else {
				if(isFollow(NomeProducao.prodTipoMacro, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodTipoMacro)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.ID)){
			erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaArg, token.getClasse())){
				prodListaArg();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaArg, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaArg)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'bool/integer/String/double/void ou )', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.SMB_FECHAPAR)){
			erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_DOISPONTOS)){
			erroSintatico("Esperado ':', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodFDID, token.getClasse())){
				prodFDID();
				break;
			} else {
				if(isFollow(NomeProducao.prodFDID, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodFDID)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'bool/integer/String/double/void', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'algum tipo de dado ou IF, while, ID, write, writeln, Def, Defstatic, end', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaCmd, token.getClasse())) {
				prodListaCmd();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaCmd, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaCmd)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'IF, while, ID, write, writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'IF, while, ID, write, writeln ou Return, end, else, Def, Defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodRetorno, token.getClasse())) {
				prodRetorno();
				break;
			} else {
				if(isFollow(NomeProducao.prodRetorno, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodRetorno)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'return', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'return ou end', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.KW_END)){
			erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
		}
	}
	
	//fazer alguns testes nessa producao
	public void prodTipoPrimitivo() {
		if(isFirst(NomeProducao.prodTipoPrimitivo, token.getClasse())){
			eat(token.getClasse());
		} else {
			erroSintatico("Esperado 'tipo de dados', encontrado '" + token.getLexema() + "'");
		}
	}
	
	//implementar
	private void prodRetorno() {
		if(!eat(Tag.KW_RETURN)){
			erroSintatico("Esperado 'algum tipo de dado', encontrado '" + token.getLexema() + "'");
		}
		
	}
	
	//parei neste, arrumar while
	private void prodListaArg() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodArg, token.getClasse())){
				prodArg();
				break;
			} else {
				if(isFollow(NomeProducao.prodArg, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodArg)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'def', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'def ou defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
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
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodTipoPrimitivo, token.getClasse())) {
				prodTipoPrimitivo();
				break;
			} else {
				if(isFollow(NomeProducao.prodTipoPrimitivo, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodTipoPrimitivo)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'algum tipo de dado', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'algum tipo de dado', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodTipoMacroLinha, token.getClasse())) {
				prodTipoMacroLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodTipoMacroLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodTipoMacroLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado '[ ou ID', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
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
			erroSintatico("Esperado 'operador unario (! ou -)', porem encontrado '" + token.getLexema() + "'");
		}
	}
	
	
	
	
}
