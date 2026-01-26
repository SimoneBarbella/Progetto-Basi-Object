package com.unina.foodlab.Entity;

import java.time.LocalDateTime;

import com.unina.foodlab.Enum.TipoSessione;

public class Sessione {

	private Integer idSessione;
	private LocalDateTime oraInizio;
	private TipoSessione tipoSessione;
	private Corso corso;
	
	
	
	public Sessione() {
		
	}
	
	public Sessione(LocalDateTime oraInizio, TipoSessione tipoSessione) {
		this.oraInizio = oraInizio;
		this.tipoSessione=tipoSessione;
	}

	public Integer getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(Integer idSessione) {
		this.idSessione = idSessione;
	}

	public LocalDateTime getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(LocalDateTime oraInizio) {
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
