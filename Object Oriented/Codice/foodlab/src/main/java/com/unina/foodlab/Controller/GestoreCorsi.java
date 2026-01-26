package com.unina.foodlab.Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.unina.foodlab.Database.Dao.CorsoDao;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

public class GestoreCorsi {

    private static GestoreCorsi instance;

    private final CorsoDao corsoDao;

    private GestoreCorsi() {
        this.corsoDao = new CorsoDao();
    }

    public java.util.List<com.unina.foodlab.Entity.Corso> getCorsiGestiti(Chef chef) {
        if (chef == null) throw new IllegalArgumentException("Chef non può essere null");
        try {
            return corsoDao.findByChefEmail(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento corsi gestiti", e);
        }
    }

    public static synchronized GestoreCorsi getInstance() {
        if (instance == null) {
            instance = new GestoreCorsi();
        }
        return instance;
    }

    public Corso creaCorso(String idCorso, LocalDate dataInizio, String nome, String frequenza,
                           int numPartecipanti, int numSessioni, List<String> categorie, Chef chef) {
        if (chef == null) {
            throw new IllegalArgumentException("Chef non può essere null");
        }

        Corso corso = new Corso();
        corso.setIdCorso(idCorso);
        corso.setDataInizio(dataInizio);
        corso.setNome(nome);
        corso.setFrequenza(frequenza);
        corso.setNumPartecipanti(numPartecipanti);
        corso.setNumSessioni(numSessioni);

        try {
            boolean ok = corsoDao.save(corso, chef, categorie);
            return ok ? corso : null;
        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio del corso: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del corso", e);
        }
    }

}
