package com.unina.foodlab.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.unina.foodlab.Enum.TipoSessione;

public class SessionePresenza extends Sessione{

	private int numAderenti;
	private BigDecimal quantitàTotale;
	private List<Studente> studenti;
	private List<Ricetta> ricette;
	
	
	public SessionePresenza() {
		super();
		this.quantitàTotale = BigDecimal.ZERO;
		this.studenti = new ArrayList<>();
		this.ricette =  new ArrayList<>();
		
	}
	
	public SessionePresenza(LocalDateTime oraInizio,TipoSessione tipoSessione,int numAderenti, BigDecimal quantitàTotale) {
		super(oraInizio,tipoSessione);
		this.numAderenti=numAderenti;
		this.quantitàTotale= quantitàTotale != null ? quantitàTotale : BigDecimal.ZERO;
		this.studenti = new ArrayList<>();
		this.ricette =  new ArrayList<>();
		
	}
	
	public int getNumAderenti() {
		return numAderenti;
	}
	public void setNumAderenti(int numAderenti) {
		this.numAderenti = numAderenti;
	}
	public BigDecimal getQuantitàTotale() {
		return quantitàTotale;
	}
	public void setQuantitàTotale(BigDecimal quantitàTotale) {
		this.quantitàTotale = quantitàTotale != null ? quantitàTotale : BigDecimal.ZERO;
	}

	public List<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Studente> studenti) {
		this.studenti = studenti;
	}

	public List<Ricetta> getRicette() {
		return ricette;
	}

	public void setRicette(List<Ricetta> ricette) {
		this.ricette = ricette;
	}

	
}
