package laxer;

import java.util.ArrayList;
import java.util.List;

public class Producao {
	
	private String nomeProducao;
	private List<Tag> conjuntoFirst;
	private List<Tag> conjuntoFollow;
	
	
	public Producao(String nomeProducao){
		this.nomeProducao = nomeProducao;
		conjuntoFirst = new ArrayList<>();
		conjuntoFollow = new ArrayList<>();
	}

	public String getNomeProducao() {
		return nomeProducao;
	}

	public void setNomeProducao(String nomeProducao) {
		this.nomeProducao = nomeProducao;
	}

	public List<Tag> getConjuntoFirst() {
		return conjuntoFirst;
	}

	public void setConjuntoFirst(List<Tag> conjuntoFirst) {
		this.conjuntoFirst = conjuntoFirst;
	}

	public List<Tag> getConjuntoFollow() {
		return conjuntoFollow;
	}
	
	public void setConjuntoFollow(List<Tag> conjuntoFollow) {
		this.conjuntoFollow = conjuntoFollow;
	}
}
