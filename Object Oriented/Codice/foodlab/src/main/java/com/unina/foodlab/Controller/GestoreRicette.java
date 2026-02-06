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

    private static GestoreRicette instanza;

    private final RicettaDao ricettaDao;
    private final IngredienteDao ingredienteDao;

    private GestoreRicette() {
        this.ricettaDao = new RicettaDao();
        this.ingredienteDao = new IngredienteDao();
    }

    public static synchronized GestoreRicette getInstanza() {
        if (instanza == null) {
            instanza = new GestoreRicette();
        }
        return instanza;
    }

    public List<Ricetta> getRicetteDisponibili() {
        try {
            return ricettaDao.cercaTutti();
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ricette", e);
        }
    }

    public List<Ingrediente> getIngredientiDisponibili() {
        try {
            return ingredienteDao.cercaTutti();
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ingredienti", e);
        }
    }

    public List<Ricetta> getRicettePerSessione(int idSessione) {
        try {
            return ricettaDao.cercaPerSessioneId(idSessione);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ricette della sessione", e);
        }
    }

    public Ricetta creaRicetta(String nome, String descrizione, LocalTime tempo) {
        try {
            return ricettaDao.inserisciRicetta(nome, descrizione, tempo);
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione ricetta", e);
        }
    }

    public Ricetta creaRicettaConIngredienti(String nome, String descrizione, LocalTime tempo,
                                             List<IngredienteQuantita> ingredienti) {
        try {
            Ricetta ricetta = ricettaDao.inserisciRicetta(nome, descrizione, tempo);
            if (ricetta == null || ricetta.getIdRicetta() == null) {
                throw new RuntimeException("Ricetta non creata");
            }
            if (ingredienti != null) {
                for (IngredienteQuantita iq : ingredienti) {
                    if (iq == null || iq.getNome() == null || iq.getNome().isBlank()) continue;
                    ingredienteDao.controllaIngrediente(iq.getNome(), iq.getUnita());
                    ricettaDao.aggiungiRichiede(ricetta.getIdRicetta(), iq.getNome(), iq.getQuantita());
                }
            }
            return ricetta;
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione ricetta", e);
        }
    }

    public List<IngredienteQuantita> getIngredientiPerRicetta(int idRicetta) {
        try {
            return ricettaDao.cercaIngredientiPerRicettaId(idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento ingredienti ricetta", e);
        }
    }

    public void associaRicettaSessione(int idSessione, int idRicetta) {
        try {
            ricettaDao.collegaRicettaASessione(idSessione, idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore associazione ricetta-sessione", e);
        }
    }

    public void disassociaRicettaSessione(int idSessione, int idRicetta) {
        try {
            ricettaDao.scollegaRicettaDaSessione(idSessione, idRicetta);
        } catch (SQLException e) {
            throw new RuntimeException("Errore rimozione ricetta dalla sessione", e);
        }
    }

}
