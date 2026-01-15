package com.unina.foodlab.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ricetta {
	
	private String nome;
	private String descrizione;
	private LocalDate tempo;
	private List<Ingrediente> ingredienti;
	private List<SessionePresenza> sessioniPresenza;
	
	
	public Ricetta() {
		this.ingredienti = new ArrayList<>();
		this.sessioniPresenza = new ArrayList<>();
	}
	
	public Ricetta(String nome, String descrizione, LocalDate tempo) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.tempo = tempo;
		this.ingredienti = new ArrayList<>();
		this.sessioniPresenza = new ArrayList<>();
	
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public LocalDate getTempo() {
		return tempo;
	}
	public void setTempo(LocalDate tempo) {
		this.tempo = tempo;
	}

	public List<Ingrediente> getIngredienti() {
		return ingredienti;
	}

	public void setIngredienti(List<Ingrediente> ingredienti) {
		this.ingredienti = ingredienti;
	}

	public List<SessionePresenza> getSessioniPresenza() {
		return sessioniPresenza;
	}

	public void setSessioniPresenza(List<SessionePresenza> sessioniPresenza) {
		this.sessioniPresenza = sessioniPresenza;
	}

	
	
}
