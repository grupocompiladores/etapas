package laxer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TP {
	
	private static HashMap<NomeProducao,Producao> tabelaPreditiva;
	
	public TP() {
		//tabelaPreditiva = new HashMap<>();
		Producao p;
		
		p = new Producao(NomeProducao.prodPrograma);
		p.getConjuntoFirst().add(Tag.KW_CLASS);
		p.getConjuntoFollow().add(Tag.EOF);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodClasse);
		p.getConjuntoFirst().add(Tag.KW_CLASS);
		p.getConjuntoFollow().add(Tag.EOF);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodDeclaraID);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.KW_BOOL);
		p.getConjuntoFollow().add(Tag.KW_INTEGER);
		p.getConjuntoFollow().add(Tag.KW_STRING);
		p.getConjuntoFollow().add(Tag.KW_DOUBLE);
		p.getConjuntoFollow().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodListaFuncao);
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodListaFuncaoLinha);
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodFuncao);
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodFDID);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodListaArg);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodListaArgLinha);
		p.getConjuntoFirst().add(Tag.SMB_VIRGULA);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodArg);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodRetorno);
		p.getConjuntoFirst().add(Tag.KW_RETURN);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNome(), p);
		//------------------------------------------
		p = new Producao(NomeProducao.prodMain);
		p.getConjuntoFirst().add(Tag.KW_DEFSTATIC);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNome(), p);		
		//------------------------------------------
		p = new Producao(NomeProducao.prodTipoMacro);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNome(), p);		
				
		p = new Producao(NomeProducao.prodTipoMacroLinha);
		p.getConjuntoFirst().add(Tag.SMB_ABRECOL);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodTipoPrimitivo);
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.SMB_ABRECOL);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodListaCmd);
		p.getConjuntoFirst().add(Tag.KW_IF);
		p.getConjuntoFirst().add(Tag.KW_WHILE);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.KW_WRITE);
		p.getConjuntoFirst().add(Tag.KW_WRITELN);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodListaCmdLinha);
		p.getConjuntoFirst().add(Tag.KW_IF);
		p.getConjuntoFirst().add(Tag.KW_WHILE);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.KW_WRITE);
		p.getConjuntoFirst().add(Tag.KW_WRITELN);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmd);
		p.getConjuntoFirst().add(Tag.KW_IF);
		p.getConjuntoFirst().add(Tag.KW_WHILE);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.KW_WRITE);
		p.getConjuntoFirst().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdIf);
		p.getConjuntoFirst().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
	
		p = new Producao(NomeProducao.prodCmdIfLinha);
		p.getConjuntoFirst().add(Tag.KW_END);	
		p.getConjuntoFirst().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdWhile);
		p.getConjuntoFirst().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdWrite);
		p.getConjuntoFirst().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdWriteLn);
		p.getConjuntoFirst().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdLinha);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdDuasLinhas);
		p.getConjuntoFirst().add(Tag.SMB_IGUAL);
		p.getConjuntoFirst().add(Tag.SMB_ABRECOL);
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodCmdAtribuiLinha);
		p.getConjuntoFirst().add(Tag.SMB_IGUAL);
		p.getConjuntoFirst().add(Tag.SMB_ABRECOL);
		p.getConjuntoFollow().add(Tag.KW_IF);
		p.getConjuntoFollow().add(Tag.KW_WHILE);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.KW_WRITE);
		p.getConjuntoFollow().add(Tag.KW_WRITELN);
		p.getConjuntoFollow().add(Tag.KW_RETURN);
		p.getConjuntoFollow().add(Tag.KW_END);
		p.getConjuntoFollow().add(Tag.KW_ELSE);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodFexp);
		p.getConjuntoFirst().add(Tag.SMB_VIRGULA);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodFexp2);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressaoLinha);
		p.getConjuntoFirst().add(Tag.KW_OR);
		p.getConjuntoFirst().add(Tag.KW_AND);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao1);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO);
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao1Linha);
		p.getConjuntoFirst().add(Tag.RELOP_LT);
		p.getConjuntoFirst().add(Tag.RELOP_LE);
		p.getConjuntoFirst().add(Tag.RELOP_GT);
		p.getConjuntoFirst().add(Tag.RELOP_GE);
		p.getConjuntoFirst().add(Tag.RELOP_EQ);
		p.getConjuntoFirst().add(Tag.RELOP_NE);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao2);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao2Linha);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao3);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodExpressao3Linha);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFirst().add(Tag.RELOP_DI);
		p.getConjuntoFirst().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
			
		p = new Producao(NomeProducao.prodExpressao4);
		p.getConjuntoFirst().add(Tag.ID);
		p.getConjuntoFirst().add(Tag.CONSTINTEGER);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		p.getConjuntoFirst().add(Tag.KW_TRUE);
		p.getConjuntoFirst().add(Tag.KW_FALSE);
		p.getConjuntoFirst().add(Tag.KW_VECTOR);
		p.getConjuntoFirst().add(Tag.OPUNARIO); // - OU !
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);	
		
		p = new Producao(NomeProducao.prodExpressao4Linha);
		p.getConjuntoFirst().add(Tag.SMB_ABRECOL);
		p.getConjuntoFirst().add(Tag.SMB_ABREPAR);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.RELOP_MU);
		p.getConjuntoFollow().add(Tag.RELOP_DI);
		p.getConjuntoFollow().add(Tag.RELOP_PL);
		p.getConjuntoFollow().add(Tag.RELOP_SU);
		p.getConjuntoFollow().add(Tag.RELOP_LT);
		p.getConjuntoFollow().add(Tag.RELOP_LE);
		p.getConjuntoFollow().add(Tag.RELOP_GT);
		p.getConjuntoFollow().add(Tag.RELOP_GE);
		p.getConjuntoFollow().add(Tag.RELOP_EQ);
		p.getConjuntoFollow().add(Tag.RELOP_NE);
		p.getConjuntoFollow().add(Tag.KW_OR);
		p.getConjuntoFollow().add(Tag.KW_AND);
		p.getConjuntoFollow().add(Tag.SMB_PONTOVIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		p.getConjuntoFollow().add(Tag.SMB_FECHACOL);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		tabelaPreditiva.put(p.getNome(), p);
		
		p = new Producao(NomeProducao.prodOpUnario);
		p.getConjuntoFirst().add(Tag.OPUNARIO);
		p.getConjuntoFollow().add(Tag.ID);
		p.getConjuntoFollow().add(Tag.CONSTINTEGER);
		p.getConjuntoFollow().add(Tag.CONSTDOUBLE);
		p.getConjuntoFollow().add(Tag.CONSTSTRING);
		p.getConjuntoFollow().add(Tag.KW_TRUE);
		p.getConjuntoFollow().add(Tag.KW_FALSE);
		p.getConjuntoFollow().add(Tag.KW_VECTOR);
		p.getConjuntoFollow().add(Tag.OPUNARIO);
		p.getConjuntoFollow().add(Tag.SMB_ABREPAR);
		tabelaPreditiva.put(p.getNome(), p);
		
	}
	
	public static boolean ifFirst(NomeProducao nome, Tag t){
		return true;
	}
	
	public static boolean isFollow(NomeProducao nome, Tag t){
		return false;
	}
	public static void main(String[] args) {
		List<Producao> lista = new ArrayList<>();
		Producao p;
		
		p = new Producao(NomeProducao.prodClasse);
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		
		System.out.println(p.getNome());
		System.out.println(p.getConjuntoFirst().contains(Tag.CONSTINTEGER));
		
	}
	
	public static HashMap<NomeProducao, Producao> getTabelaPreditiva() {
		return tabelaPreditiva;
	}


	public static void setTabelaPreditiva(HashMap<NomeProducao, Producao> tabelaPreditiva) {
		TP.tabelaPreditiva = tabelaPreditiva;
	}
	
}
