package com.unina.foodlab.Entity;

import java.math.BigDecimal;

public class IngredienteQuantita {

    private String nome;
    private String unita;
    private BigDecimal quantita;

    public IngredienteQuantita() {
    }

    public IngredienteQuantita(String nome, String unita, BigDecimal quantita) {
        this.nome = nome;
        this.unita = unita;
        this.quantita = quantita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnita() {
        return unita;
    }

    public void setUnita(String unita) {
        this.unita = unita;
    }

    public BigDecimal getQuantita() {
        return quantita;
    }

    public void setQuantita(BigDecimal quantita) {
        this.quantita = quantita;
    }
}
