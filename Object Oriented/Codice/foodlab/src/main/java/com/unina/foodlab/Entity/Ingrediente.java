package com.unina.foodlab.Entity;

public class Ingrediente {

	private String nome;
	private String unitàDiMisura;
	
	
	public Ingrediente() {
	}

	public Ingrediente(String nome, String unitàDiMisura) {
		this.nome = nome;
		this.unitàDiMisura = unitàDiMisura;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUnitàDiMisura() {
		return unitàDiMisura;
	}
	public void setUnitàDiMisura(String unitàDiMisura) {
		this.unitàDiMisura = unitàDiMisura;
	}
	
	
}
