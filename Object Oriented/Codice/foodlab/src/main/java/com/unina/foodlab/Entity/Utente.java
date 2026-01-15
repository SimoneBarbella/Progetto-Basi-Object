package com.unina.foodlab.Entity;

import com.unina.foodlab.Enum.TipoUtente;

public class Utente {
	
	private String email;
	private String nome;
	private String cognome;
	private String password;
	private TipoUtente tipoUtente;
	
	
	public Utente() {
		
	}
	
	public Utente(String email, String nome, String cognome, String password, TipoUtente tipoUtente) {
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.password = password;
		this.tipoUtente = tipoUtente;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}
	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	
	
}
