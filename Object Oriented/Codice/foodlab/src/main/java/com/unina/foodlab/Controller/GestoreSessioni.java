package com.unina.foodlab.Controller;

import com.unina.foodlab.Database.Dao.SessioneDao;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Enum.TipoSessione;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GestoreSessioni {

    private static GestoreSessioni instance;

    private final SessioneDao sessioneDao;

    private GestoreSessioni() {
        this.sessioneDao = new SessioneDao();
    }

    public static synchronized GestoreSessioni getInstance() {
        if (instance == null) {
            instance = new GestoreSessioni();
        }
        return instance;
    }

    public List<Sessione> getSessioniByCorso(Corso corso) {
        if (corso == null) {
            throw new IllegalArgumentException("Corso non può essere null");
        }
        try {
            return sessioneDao.findByCorso(corso);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento sessioni del corso", e);
        }
    }

    public List<Sessione> getSessioniByCorsoId(int idCorso) {
        try {
            return sessioneDao.findByCorsoId(idCorso);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento sessioni del corso", e);
        }
    }

    public void creaSessione(Corso corso, LocalDateTime data, TipoSessione tipo, String teoria) {
        try {
            sessioneDao.insertSessione(corso, data, tipo, teoria);
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione sessione", e);
        }
    }

    public BigDecimal getQuantitaTotaleBySessioneId(int idSessione) {
        try {
            return sessioneDao.getQuantitaTotaleBySessioneId(idSessione);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento quantità totale", e);
        }
    }

    public List<IngredienteQuantita> getListaSpesaBySessioneId(int idSessione) {
        try {
            return sessioneDao.getListaSpesaBySessioneId(idSessione);
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento lista della spesa", e);
        }
    }
}
