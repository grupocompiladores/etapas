package laxer;

public class Singleton {
	private static Tabela unica;
	
	private Singleton(){
		
	}
	
	public static synchronized Tabela getInstance(){
        if(unica == null){
            unica = new Tabela();
            unica.geraFirstFollow(); // se tabela ainda nao foi instanciado, instancio e populo tabela
        }
        return unica;
    }
}
