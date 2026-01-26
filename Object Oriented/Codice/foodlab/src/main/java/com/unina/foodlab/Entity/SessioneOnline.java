package com.unina.foodlab.Entity;

import java.time.LocalDateTime;

import com.unina.foodlab.Enum.TipoSessione;

public class SessioneOnline extends Sessione  {

	private String teoria;

	public SessioneOnline() {
		super();
	}

	public SessioneOnline(LocalDateTime oraInizio,TipoSessione tipoSessione,String teoria) {
		super(oraInizio,tipoSessione);
		this.teoria=teoria;
	}

	public String getTeoria() {
		return teoria;
	}

	public void setTeoria(String teoria) {
		this.teoria = teoria;
	}
	
	
}
