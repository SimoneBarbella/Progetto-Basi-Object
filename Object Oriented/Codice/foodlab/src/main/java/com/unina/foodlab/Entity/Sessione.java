package com.unina.foodlab.Entity;

import java.time.LocalDate;

import com.unina.foodlab.Enum.TipoSessione;

public class Sessione {
	
	private LocalDate oraInizio;
	private TipoSessione tipoSessione;
	private Corso corso;
	
	
	
	public Sessione() {
		
	}
	
	public Sessione(LocalDate oraInizio, TipoSessione tipoSessione) {
		this.oraInizio = oraInizio;
		this.tipoSessione=tipoSessione;
	}

	public LocalDate getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(LocalDate oraInizio) {
		this.oraInizio = oraInizio;
	}

	public TipoSessione getTipoSessione() {
		return tipoSessione;
	}

	public void setTipoSessione(TipoSessione tipoSessione) {
		this.tipoSessione = tipoSessione;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}
	
	
}
