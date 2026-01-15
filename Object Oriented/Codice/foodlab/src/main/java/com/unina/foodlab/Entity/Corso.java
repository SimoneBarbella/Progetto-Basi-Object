package com.unina.foodlab.Entity;

import java.util.ArrayList;
import java.util.List;

public class Corso {

	
	private String idCorso;
	private String nome;
	private String frequenza; 
	private int numPartecipanti;
	private int numSessioni;
	private List<Studente> studenti;
	private List<Chef> chefs;
	private List<Sessione> sessioni;
	
	public Corso() {
		this.chefs=new ArrayList<>();
		this.studenti=new ArrayList<>();
		this.sessioni=new ArrayList<>();
	}
	
	public Corso(String idCorso, String nome, String frequenza, int numPartecipanti, int numSessioni) {
		this.idCorso = idCorso;
		this.nome = nome;
		this.frequenza = frequenza;
		this.numPartecipanti = numPartecipanti;
		this.numSessioni = numSessioni;
		this.chefs=new ArrayList<>();
		this.studenti=new ArrayList<>();
		this.sessioni=new ArrayList<>();
	}


	public String getIdCorso() {
		return idCorso;
	}


	public void setIdCorso(String idCorso) {
		this.idCorso = idCorso;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getFrequenza() {
		return frequenza;
	}


	public void setFrequenza(String frequenza) {
		this.frequenza = frequenza;
	}


	public int getNumPartecipanti() {
		return numPartecipanti;
	}


	public void setNumPartecipanti(int numPartecipanti) {
		this.numPartecipanti = numPartecipanti;
	}


	public int getNumSessioni() {
		return numSessioni;
	}


	public void setNumSessioni(int numSessioni) {
		this.numSessioni = numSessioni;
	}

	public List<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Studente> studenti) {
		this.studenti = studenti;
	}

	public List<Chef> getChefs() {
		return chefs;
	}

	public void setChefs(List<Chef> chefs) {
		this.chefs = chefs;
	}

	public List<Sessione> getSessioni() {
		return sessioni;
	}

	public void setSessioni(List<Sessione> sessioni) {
		this.sessioni = sessioni;
	}
	
	
	
	
}
