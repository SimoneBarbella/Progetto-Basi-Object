package com.unina.foodlab.Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.unina.foodlab.Database.Dao.CorsoDao;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

public class GestoreCorsi {

    private static GestoreCorsi instanza;

    private final CorsoDao corsoDao;

    private GestoreCorsi() {
        this.corsoDao = new CorsoDao();
    }

    public List<Corso> getCorsiGestiti(Chef chef) {
        if (chef == null) throw new IllegalArgumentException("Chef non può essere null");
        try {
            return corsoDao.cercaPerEmailChef(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento corsi gestiti", e);
        }
    }

    public static synchronized GestoreCorsi getInstanza() {
        if (instanza == null) {
            instanza = new GestoreCorsi();
        }
        return instanza;
    }

    public Corso creaCorso(LocalDate dataInizio, String nome, String frequenza,
                           int numPartecipanti, int numSessioni, List<String> categorie, Chef chef) {
        if (chef == null) {
            throw new IllegalArgumentException("Chef non può essere null");
        }

        Corso corso = new Corso();
        corso.setDataInizio(dataInizio);
        corso.setNome(nome);
        corso.setFrequenza(frequenza);
        corso.setNumPartecipanti(numPartecipanti);
        corso.setNumSessioni(numSessioni);

        try {
            boolean ok = corsoDao.salva(corso, chef, categorie);
            return ok ? corso : null;
        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio del corso: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del corso", e);
        }
    }

    public List<String> getCategorieEsistenti() {
        try {
            return corsoDao.cercaTutteCategorieDistinte();
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento categorie esistenti", e);
        }
    }
}
