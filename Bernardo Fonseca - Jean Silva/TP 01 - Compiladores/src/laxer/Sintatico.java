package laxer;

import java.security.GeneralSecurityException;

import javax.sound.midi.Soundbank;

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
	
	//feito
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
			if(isFirst(NomeProducao.prodListaArgLinha, token.getClasse())){
				prodListaArgLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaArgLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaArgLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado ', ou )', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}
	
	private void prodListaArgLinha() {
		if(!eat(Tag.SMB_VIRGULA)){
			erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
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
						erroSintatico("Esperado 'algum tipo de dado', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'algum tipo de dado ou ')'', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}

	//feito
	private void prodArg(){
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodTipoMacro, token.getClasse())){
				prodTipoMacro();
				break;
			} else {
				if(isFollow(NomeProducao.prodTipoMacro, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodTipoMacro)){
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
		if(!eat(Tag.ID)){
			erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
		}
	}

	//feito
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
	
	//feito
	private void prodTipoMacroLinha(){
		if(!eat(Tag.SMB_ABRECOL)){
			erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_FECHACOL)){
			erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
		}
	}

	
	private void prodCmdIf(){
		if(!eat(Tag.KW_IF)){
			erroSintatico("Esperado 'if', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
				prodExpressao();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
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
			if(isFirst(NomeProducao.prodCmdIfLinha, token.getClasse())) {
				prodCmdIfLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodCmdIfLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodCmdIfLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'end/else', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'end/else', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}
	
	private void prodCmdIfLinha() {
		if(isTokenEsperado(Tag.KW_END)) {
			if(!eat(Tag.KW_END)){
				erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.KW_ELSE)){
			if(!eat(Tag.KW_ELSE)){
				erroSintatico("Esperado 'else', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_DOISPONTOS)){
				erroSintatico("Esperado ':', encontrado '" + token.getLexema() + "'");
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
				erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
		} else{
			erroSintatico("Esperado 'end/else', encontrado '" + token.getLexema() + "'");
		}
		
	}

	private void prodCmdWhile(){
		if(!eat(Tag.KW_WHILE)){
			erroSintatico("Esperado 'while', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
				prodExpressao();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
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
			erroSintatico("Esperado 'end', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
		}
	}

	private void prodCmdWrite(){
		if(!eat(Tag.KW_WRITE)){
			erroSintatico("Esperado 'write', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
				prodExpressao();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.SMB_FECHAPAR)){
			erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
		}
	}
	
	private void prodCmdWriteLn(){
		if(!eat(Tag.KW_WRITELN)){
			erroSintatico("Esperado 'writeln', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_ABREPAR)){
			erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
				prodExpressao();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		if(!eat(Tag.SMB_FECHAPAR)){
			erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
		}
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
		}
	}
	
	private void prodCmdLinha() {
		if(!eat(Tag.ID)){
			erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodCmdDuasLinhas, token.getClasse())) {
				prodCmdDuasLinhas();
				break;
			} else {
				if(isFollow(NomeProducao.prodCmdDuasLinhas, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodCmdDuasLinhas)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado '= ou [ ou (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado '= ou [ ou (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}
	
	private void prodCmdDuasLinhas() {
		if(isTokenEsperado(Tag.SMB_IGUAL) || isTokenEsperado(Tag.SMB_ABRECOL)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdAtribuiLinha, token.getClasse())) {
					prodCmdAtribuiLinha();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdAtribuiLinha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdAtribuiLinha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '= ou [', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado '= ou [', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		} else if(isTokenEsperado(Tag.SMB_ABREPAR)) {
			if(!eat(Tag.SMB_ABREPAR)){
				erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
			}
			
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodFexp2, token.getClasse())) {
					prodFexp2();
					break;
				} else {
					if(isFollow(NomeProducao.prodFexp2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodFexp2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer/double/String, true/false, vector, opUnario, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer/double/String, true/false, vector, opUnario, (, )', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHAPAR)){
				erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
			
		} else{
			erroSintatico("Esperado '= ou [ ou (', encontrado '" + token.getLexema() + "'");
			//verificar se preciso dar errosSintaticos++; e advance(); neste tipo de situação
		}
		
		
	}
	
	private void prodCmdAtribuiLinha() {
		if(isTokenEsperado(Tag.SMB_IGUAL)){
			if(!eat(Tag.SMB_IGUAL)){
				erroSintatico("Esperado '=', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)){
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
		} else if(isTokenEsperado(Tag.SMB_ABRECOL)) {
			if(!eat(Tag.SMB_ABRECOL)){
				erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
			} 
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHACOL)){
				erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
			}
			if(!eat(Tag.SMB_IGUAL)){
				erroSintatico("Esperado '=', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_PONTOVIRGULA)) {
				erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
			}
		} else{
			erroSintatico("Esperado '= ou [', encontrado '" + token.getLexema() + "'");
		}
	}

	private void prodFexp() {
		if(isTokenEsperado(Tag.SMB_VIRGULA)) {
			if(!eat(Tag.SMB_VIRGULA)){
				erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodFexp, token.getClasse())) {
					prodFexp();
					break;
				} else {
					if(isFollow(NomeProducao.prodFexp, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodFexp)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado ', ou )', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		}
	}
	
	private void prodFexp2() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
				prodExpressao();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodFexp, token.getClasse())) {
				prodFexp();
				break;
			} else {
				if(isFollow(NomeProducao.prodFexp, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodFexp)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado ',', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado ', ou )', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}
	
	private void prodExpressao() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao1, token.getClasse())) {
				prodExpressao1();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao1, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao1)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressaoLinha, token.getClasse())) {
				prodExpressaoLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressaoLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressaoLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'operador lógico (or/and)', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}
	
	private void prodExpressaoLinha() {
		if(isTokenEsperado(Tag.KW_OR)){
			if(!eat(Tag.KW_OR)){
				erroSintatico("Esperado 'or', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1, token.getClasse())) {
					prodExpressao1();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressaoLinha, token.getClasse())) {
					prodExpressaoLinha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressaoLinha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressaoLinha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador lógico (or/and)', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		} else if(isTokenEsperado(Tag.KW_AND)){
			if(!eat(Tag.KW_AND)){
				erroSintatico("Esperado 'and', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1, token.getClasse())) {
					prodExpressao1();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressaoLinha, token.getClasse())) {
					prodExpressaoLinha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressaoLinha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressaoLinha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador lógico (or/and)', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		}
	}

	private void prodExpressao1() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
				prodExpressao2();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
				prodExpressao1Linha();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}

	private void prodExpressao1Linha() {
		if(isTokenEsperado(Tag.RELOP_LT)){ // <
			if(!eat(Tag.RELOP_LT)){
				erroSintatico("Esperado '<', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		} else if(isTokenEsperado(Tag.RELOP_LE)){ // <=
			if(!eat(Tag.RELOP_LE)){
				erroSintatico("Esperado '<=', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.RELOP_GT)){ // >
			if(!eat(Tag.RELOP_GT)){
				erroSintatico("Esperado '>', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.RELOP_GE)){ // >=
			if(!eat(Tag.RELOP_GE)){
				erroSintatico("Esperado '>=', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
			
		} else if(isTokenEsperado(Tag.RELOP_EQ)){ // ==
			if(!eat(Tag.RELOP_EQ)){
				erroSintatico("Esperado '==', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.RELOP_NE)){ // !=
			if(!eat(Tag.RELOP_NE)){
				erroSintatico("Esperado '!=', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2, token.getClasse())) {
					prodExpressao2();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao1Linha, token.getClasse())) {
					prodExpressao1Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao1Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao1Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'operador de comparação', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		}
	}

	private void prodExpressao2() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao3, token.getClasse())) {
				prodExpressao3();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao3, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao3)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao2Linha, token.getClasse())) {
				prodExpressao2Linha();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao2Linha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao2Linha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado '+ ou -', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}

	private void prodExpressao2Linha() {
		if(isTokenEsperado(Tag.RELOP_PL)){
			if(!eat(Tag.RELOP_PL)){
				erroSintatico("Esperado 'adição', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao3, token.getClasse())) {
					prodExpressao3();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao3, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao3)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2Linha, token.getClasse())) {
					prodExpressao2Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '+ ou -', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		} else if(isTokenEsperado(Tag.RELOP_SU)){
			if(!eat(Tag.RELOP_SU)){
				erroSintatico("Esperado 'subtração', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao3, token.getClasse())) {
					prodExpressao3();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao3, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao3)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao2Linha, token.getClasse())) {
					prodExpressao2Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao2Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao2Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '+ ou -', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		}
		
	}

	private void prodExpressao3() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao4, token.getClasse())) {
				prodExpressao4();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao4, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao4)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodExpressao3Linha, token.getClasse())) {
				prodExpressao3Linha();
				break;
			} else {
				if(isFollow(NomeProducao.prodExpressao3Linha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodExpressao3Linha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado '* ou /', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}

	private void prodExpressao3Linha() {
		if(isTokenEsperado(Tag.RELOP_MU)){
			if(!eat(Tag.RELOP_MU)){
				erroSintatico("Esperado '*', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao4, token.getClasse())) {
					prodExpressao4();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao4, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao4)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao3Linha, token.getClasse())) {
					prodExpressao3Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao3Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao3Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '* ou /', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		} else if(isTokenEsperado(Tag.RELOP_DI)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao4, token.getClasse())) {
					prodExpressao4();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao4, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao4)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao3Linha, token.getClasse())) {
					prodExpressao3Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao3Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao3Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '* ou /', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' ')' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
		}
		
	}

	private void prodExpressao4() {
		if(isTokenEsperado(Tag.ID)) {
			if(!eat(Tag.ID)) {
				erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao4Linha, token.getClasse())) {
					prodExpressao4Linha();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao4Linha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao4Linha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado '[ ou (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'operador aritmético OU operador lógico OU operador de comparação OU ';' ',' '(' ')' '[' ']'', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.CONSTINTEGER)){
			if(!eat(Tag.CONSTINTEGER)) {
				erroSintatico("Esperado 'inteiro', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.CONSTDOUBLE)){
			if(!eat(Tag.CONSTDOUBLE)) {
				erroSintatico("Esperado 'double', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.CONSTSTRING)){
			if(!eat(Tag.CONSTSTRING)) {
				erroSintatico("Esperado 'literal', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.KW_TRUE)){
			if(!eat(Tag.KW_TRUE)) {
				erroSintatico("Esperado 'true', encontrado '" + token.getLexema() + "'");
			}
		} else if(isTokenEsperado(Tag.KW_FALSE)){
			if(!eat(Tag.KW_FALSE)) {
				erroSintatico("Esperado 'false', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.KW_VECTOR)){
			if(!eat(Tag.KW_VECTOR)) {
				erroSintatico("Esperado 'vector', encontrado '" + token.getLexema() + "'");
			}
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
			if(!eat(Tag.SMB_ABRECOL)){
				erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHACOL)){
				erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
			}
			
			
			
		} else if(isTokenEsperado(Tag.OPUNARIO)){
			if(!eat(Tag.OPUNARIO)) {
				erroSintatico("Esperado 'operador unário', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.SMB_ABREPAR)){
			if(!eat(Tag.SMB_ABREPAR)) {
				erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHAPAR)){
				erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
			}
		} else {
			erroSintatico("Esperado '[ ou (', encontrado '" + token.getLexema() + "'");
		}
		
	}

	private void prodExpressao4Linha() {
		if(isTokenEsperado(Tag.SMB_ABRECOL)){
			if(!eat(Tag.SMB_ABRECOL)){
				erroSintatico("Esperado '[', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodExpressao, token.getClasse())) {
					prodExpressao();
					break;
				} else {
					if(isFollow(NomeProducao.prodExpressao, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodExpressao)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer, double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, Integer, Double, String, true, false, vector, -, !, (', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHACOL)){
				erroSintatico("Esperado ']', encontrado '" + token.getLexema() + "'");
			}
			
		} else if(isTokenEsperado(Tag.SMB_ABREPAR)){
			if(!eat(Tag.SMB_ABREPAR)){
				erroSintatico("Esperado '(', encontrado '" + token.getLexema() + "'");
			}
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodFexp2, token.getClasse())) {
					prodFexp2();
					break;
				} else {
					if(isFollow(NomeProducao.prodFexp2, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodFexp2)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID, integer/double/String, true/false, vector, opUnario, (', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID, integer/double/String, true/false, vector, opUnario, (, )', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			if(!eat(Tag.SMB_FECHAPAR)){
				erroSintatico("Esperado ')', encontrado '" + token.getLexema() + "'");
			}
		}
		
	}

	private void prodListaCmd() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaCmdLinha, token.getClasse())) {
				prodListaCmdLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaCmdLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaCmdLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'if/while/ID/write/writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'if/while/ID/write/writeln OU return/end/else/def/defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}

	private void prodListaCmdLinha() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodCmd, token.getClasse())) {
				prodCmd();
				break;
			} else {
				if(isFollow(NomeProducao.prodCmd, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodCmd)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'if/while/ID/write/writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'if/while/ID/write/writeln', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodListaCmdLinha, token.getClasse())) {
				prodListaCmdLinha();
				break;
			} else {
				if(isFollow(NomeProducao.prodListaCmdLinha, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodListaCmdLinha)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'if/while/ID/write/writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'if/while/ID/write/writeln OU return/end/else/def/defstatic', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
	}

	private void prodCmd() {
		if(isTokenEsperado(Tag.KW_IF)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdIf, token.getClasse())) {
					prodCmdIf();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdIf, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdIf)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'if', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'if', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.KW_WHILE)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdWhile, token.getClasse())) {
					prodCmdWhile();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdWhile, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdWhile)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'while', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'while', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.KW_WRITE)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdWrite, token.getClasse())) {
					prodCmdWrite();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdWrite, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdWrite)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'write', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'write', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.KW_WRITELN)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdWriteLn, token.getClasse())) {
					prodCmdWriteLn();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdWriteLn, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdWriteLn)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'writeln', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'writeln', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else if(isTokenEsperado(Tag.ID)){
			while(errosSintaticos <= 5) {
				if(isFirst(NomeProducao.prodCmdLinha, token.getClasse())) {
					prodCmdLinha();
					break;
				} else {
					if(isFollow(NomeProducao.prodCmdLinha, token.getClasse())){
						if(verificaProducaoVazia(NomeProducao.prodCmdLinha)){
							break; // vai para a próxima producao, pois permite cadeia vazia
						} else {
							erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
							errosSintaticos++;
							break;
						}
					} else {
						erroSintatico("Esperado 'ID', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						advance();
					}
				}
			}
			
		} else {
			erroSintatico("Esperado 'if/while/write/writeln ou ID', encontrado '" + token.getLexema() + "'");
		}
		
	}
	
	public void prodFDID() {
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodDeclaraID, token.getClasse())) {
				prodDeclaraID();
				break;
			} else {
				if(isFollow(NomeProducao.prodDeclaraID, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodDeclaraID)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'tipo de dado', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'tipo de dado', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		while(errosSintaticos <= 5) {
			if(isFirst(NomeProducao.prodFDID, token.getClasse())) {
				prodFDID();
				break;
			} else {
				if(isFollow(NomeProducao.prodFDID, token.getClasse())){
					if(verificaProducaoVazia(NomeProducao.prodFDID)){
						break; // vai para a próxima producao, pois permite cadeia vazia
					} else {
						erroSintatico("Esperado 'tipo de dado', encontrado '" + token.getLexema() + "'");
						errosSintaticos++;
						break;
					}
				} else {
					erroSintatico("Esperado 'tipo de dado OU if/while/write/writeln/def/defstatic/end OU ID', encontrado '" + token.getLexema() + "'");
					errosSintaticos++;
					advance();
				}
			}
		}
		
	}
	
	private void prodDeclaraID() {
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
		if(!eat(Tag.SMB_PONTOVIRGULA)){
			erroSintatico("Esperado ';', encontrado '" + token.getLexema() + "'");
		}
	}

	public void prodOpUnario(){
		if(isTokenEsperado(Tag.OPUNARIO)){
			eat(Tag.OPUNARIO);
		} else {
			erroSintatico("Esperado 'operador unário', encontrado '" + token.getLexema() + "'");
		}
	}
	
}
