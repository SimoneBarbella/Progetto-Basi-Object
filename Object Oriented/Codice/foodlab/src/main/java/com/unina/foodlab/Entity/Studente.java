package com.unina.foodlab.Entity;

import java.util.ArrayList;
import java.util.List;

import com.unina.foodlab.Enum.TipoUtente;
public class Studente extends Utente {

    private String matricola;
    private List<SessionePresenza> sessioni;
    private List<Corso> corsi;

    public Studente() {
        super();
        this.sessioni = new ArrayList<>();
        this.corsi = new ArrayList<>();
    }

    public Studente(String email, String nome, String cognome, String password, TipoUtente tipoUtente, String matricola) {
        super(email, nome, cognome, password, tipoUtente);
        this.matricola = matricola;
        this.sessioni = new ArrayList<>();
        this.corsi = new ArrayList<>();
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public List<SessionePresenza> getSessioni() {
        return sessioni;
    }

    public void setSessioni(List<SessionePresenza> sessioni) {
        this.sessioni = sessioni;
    }

    public List<Corso> getCorsi() {
        return corsi;
    }

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }
}

