package com.unina.foodlab.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.unina.foodlab.Enum.TipoSessione;

public class SessionePresenza extends Sessione{

	private int numAderenti;
	private double quantitàTotale;
	private List<Studente> studneti;
	private List<Ricetta> ricette;
	
	
	public SessionePresenza() {
		super();
		this.studneti = new ArrayList<>();
		this.ricette =  new ArrayList<>();
		
	}
	
	public SessionePresenza(LocalDate oraInizio,TipoSessione tipoSessione,int numAderenti, double quantitàTotale) {
		super(oraInizio,tipoSessione);
		this.numAderenti=numAderenti;
		this.quantitàTotale= quantitàTotale;
		this.studneti = new ArrayList<>();
		this.ricette =  new ArrayList<>();
		
	}
	
	public int getNumAderenti() {
		return numAderenti;
	}
	public void setNumAderenti(int numAderenti) {
		this.numAderenti = numAderenti;
	}
	public double getQuantitàTotale() {
		return quantitàTotale;
	}
	public void setQuantitàTotale(double quantitàTotale) {
		this.quantitàTotale = quantitàTotale;
	}

	public List<Studente> getStudneti() {
		return studneti;
	}

	public void setStudneti(List<Studente> studneti) {
		this.studneti = studneti;
	}

	public List<Ricetta> getRicette() {
		return ricette;
	}

	public void setRicette(List<Ricetta> ricette) {
		this.ricette = ricette;
	}

	
}
