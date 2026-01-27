package com.unina.foodlab.Controller;

import com.unina.foodlab.Database.Dao.IngredienteDao;
import com.unina.foodlab.Database.Dao.RicettaDao;
import com.unina.foodlab.Entity.Ingrediente;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class GestoreRicette {

    private static GestoreRicette instance;

    private final RicettaDao ricettaDao;
    private final IngredienteDao ingredienteDao;

    private GestoreRicette() {
        this.ricettaDao = new RicettaDao();
        this.ingredienteDao = new IngredienteDao();
    }

    public static synchronized GestoreRicette getInstance() {
        if (instance == null) {
            instance = new GestoreRicette();
        }
        return instance;
    }

    public List<Ricetta> getRicetteDisponibili() {
        try {
            return ricettaDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ricette", e);
        }
    }

    public List<Ingrediente> getIngredientiDisponibili() {
        try {
            return ingredienteDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ingredienti", e);
        }
    }

    public List<Ricetta> getRicettePerSessione(int idSessione) {
        try {
            return ricettaDao.findBySessioneId(idSessione);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ricette della sessione", e);
        }
    }

    public Ricetta creaRicetta(String nome, String descrizione, LocalTime tempo) {
        try {
            return ricettaDao.insertRicetta(nome, descrizione, tempo);
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione ricetta", e);
        }
    }

    public Ricetta creaRicettaConIngredienti(String nome, String descrizione, LocalTime tempo,
                                             List<IngredienteQuantita> ingredienti) {
        try {
            Ricetta ricetta = ricettaDao.insertRicetta(nome, descrizione, tempo);
            if (ricetta == null || ricetta.getIdRicetta() == null) {
                throw new RuntimeException("Ricetta non creata");
            }
            if (ingredienti != null) {
                for (IngredienteQuantita iq : ingredienti) {
                    if (iq == null || iq.getNome() == null || iq.getNome().isBlank()) continue;
                    ingredienteDao.ensureIngrediente(iq.getNome(), iq.getUnita());
                    ricettaDao.addRichiede(ricetta.getIdRicetta(), iq.getNome(), iq.getQuantita());
                }
            }
            return ricetta;
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione ricetta", e);
        }
    }

    public List<IngredienteQuantita> getIngredientiPerRicetta(int idRicetta) {
        try {
            return ricettaDao.findIngredientiByRicettaId(idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ingredienti ricetta", e);
        }
    }

    public void associaRicettaSessione(int idSessione, int idRicetta) {
        try {
            ricettaDao.linkRicettaToSessione(idSessione, idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore associazione ricetta-sessione", e);
        }
    }

    public void disassociaRicettaSessione(int idSessione, int idRicetta) {
        try {
            ricettaDao.unlinkRicettaFromSessione(idSessione, idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore rimozione ricetta dalla sessione", e);
        }
    }

}
