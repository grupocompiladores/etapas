package laxer;

import java.util.ArrayList;
import java.util.List;

public class Producao {
	
	private NomeProducao nome;
	private List<Tag> conjuntoFirst;
	private List<Tag> conjuntoFollow;
	
	
	public Producao(NomeProducao nome){
		this.nome = nome;
		conjuntoFirst = new ArrayList<>();
		conjuntoFollow = new ArrayList<>();
	}

	public NomeProducao getNome() {
		return nome;
	}

	public void setNomeProducao(NomeProducao nome) {
		this.nome = nome;
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
