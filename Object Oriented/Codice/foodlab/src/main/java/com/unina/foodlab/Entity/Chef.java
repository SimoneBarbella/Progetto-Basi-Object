package com.unina.foodlab.Entity;

import java.util.ArrayList;
import java.util.List;

import com.unina.foodlab.Enum.TipoUtente;

public class Chef extends Utente{

	private String specializzazione;
	private List<Corso> corsi;
	
	
	public Chef() {
		super();
		this.corsi = new ArrayList<>();
	}

	public Chef(String email, String nome, String cognome, String password, TipoUtente tipoUtente, String specializzazione) {
		super(email, nome, cognome, password, tipoUtente);
		this.specializzazione = specializzazione;
		this.corsi = new ArrayList<>();
	}

	public String getSpecializzazione() {
		return specializzazione;
	}

	public void setSpecializzazione(String specializzazione) {
		this.specializzazione = specializzazione;
	}

	public List<Corso> getCorsi() {
		return corsi;
	}

	public void setCorsi(List<Corso> corsi) {
		this.corsi = corsi;
	}
	
	
	
}
