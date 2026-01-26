package com.unina.foodlab.Entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Ricetta {

	private Integer idRicetta;
	private String nome;
	private String descrizione;
	private LocalTime tempo;
	private List<Ingrediente> ingredienti;
	private List<SessionePresenza> sessioniPresenza;
	
	
	public Ricetta() {
		this.ingredienti = new ArrayList<>();
		this.sessioniPresenza = new ArrayList<>();
	}
	
	public Ricetta(String nome, String descrizione, LocalTime tempo) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.tempo = tempo;
		this.ingredienti = new ArrayList<>();
		this.sessioniPresenza = new ArrayList<>();
	
	}

	public Integer getIdRicetta() {
		return idRicetta;
	}

	public void setIdRicetta(Integer idRicetta) {
		this.idRicetta = idRicetta;
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
	public LocalTime getTempo() {
		return tempo;
	}
	public void setTempo(LocalTime tempo) {
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
