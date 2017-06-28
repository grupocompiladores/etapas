package laxer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TP {
	
	private static HashMap<String,Producao> tabelaPreditiva;
	
	public TP() {
		tabelaPreditiva = new HashMap<>();
		Producao p;
		
		p = new Producao("prodPrograma");
		p.getConjuntoFirst().add(Tag.KW_CLASS);
		p.getConjuntoFollow().add(Tag.EOF);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodClasse");
		p.getConjuntoFirst().add(Tag.KW_CLASS);
		p.getConjuntoFollow().add(Tag.EOF);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodDeclaraID");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodListaFuncao");
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodListaFuncaoLinha");
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodFuncao");
		p.getConjuntoFirst().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEF);
		p.getConjuntoFollow().add(Tag.KW_DEFSTATIC);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodFDID");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodListaArg");
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodListaArgLinha");
		p.getConjuntoFirst().add(Tag.SMB_VIRGULA);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodArg");
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.SMB_VIRGULA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodRetorno");
		p.getConjuntoFirst().add(Tag.KW_RETURN);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		//------------------------------------------
		p = new Producao("prodMain");
		p.getConjuntoFirst().add(Tag.KW_DEFSTATIC);
		p.getConjuntoFollow().add(Tag.KW_END);
		tabelaPreditiva.put(p.getNomeProducao(), p);		
		//------------------------------------------
		p = new Producao("prodTipoMacro");
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNomeProducao(), p);		
				
		p = new Producao("prodTipoMacroLinha");
		p.getConjuntoFirst().add(Tag.SMB_ABRECOL);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodTipoPrimitivo");
		p.getConjuntoFirst().add(Tag.KW_BOOL);
		p.getConjuntoFirst().add(Tag.KW_INTEGER);
		p.getConjuntoFirst().add(Tag.KW_STRING);
		p.getConjuntoFirst().add(Tag.KW_DOUBLE);
		p.getConjuntoFirst().add(Tag.KW_VOID);
		p.getConjuntoFollow().add(Tag.SMB_ABRECOL);
		p.getConjuntoFollow().add(Tag.ID);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodListaCmd");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodListaCmdLinha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmd");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdIf");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
	
		p = new Producao("prodCmdIfLinha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdWhile");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdWrite");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdWriteLn");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdLinha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdDuasLinhas");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodCmdAtribuiLinha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodFexp");
		p.getConjuntoFirst().add(Tag.SMB_VIRGULA);
		p.getConjuntoFirst().add(Tag.VAZIA);
		p.getConjuntoFollow().add(Tag.SMB_FECHAPAR);
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodFexp2");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressaoLinha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao1");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao1Linha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao2");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao2Linha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao3");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodExpressao3Linha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
			
		p = new Producao("prodExpressao4");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);	
		
		p = new Producao("prodExpressao4Linha");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
		p = new Producao("prodOpUnario");
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
		tabelaPreditiva.put(p.getNomeProducao(), p);
		
	}
	

	public static void main(String[] args) {
		List<Producao> lista = new ArrayList<>();
		Producao p;
		
		p = new Producao("prodClasse");
		p.getConjuntoFirst().add(Tag.CONSTDOUBLE);
		
		System.out.println(p.getNomeProducao());
		System.out.println(p.getConjuntoFirst().contains(Tag.CONSTDOUBLE));
		
		p = new Producao("prodOutra");
		p.getConjuntoFirst().add(Tag.CONSTSTRING);
		
		System.out.println(p.getNomeProducao());
		System.out.println(p.getConjuntoFirst().contains(Tag.CONSTDOUBLE));
		
	}
	
	public static HashMap<String, Producao> getTabelaPreditiva() {
		return tabelaPreditiva;
	}


	public static void setTabelaPreditiva(HashMap<String, Producao> tabelaPreditiva) {
		TP.tabelaPreditiva = tabelaPreditiva;
	}
	
}
