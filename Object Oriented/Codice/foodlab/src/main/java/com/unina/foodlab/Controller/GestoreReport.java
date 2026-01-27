package com.unina.foodlab.Controller;

import com.unina.foodlab.Database.Dao.ReportDao;
import com.unina.foodlab.Entity.Chef;

import java.sql.SQLException;
import java.util.Map;

public class GestoreReport {

    private static GestoreReport instance;

    private final ReportDao reportDao;

    private GestoreReport() {
        this.reportDao = new ReportDao();
    }

    public static synchronized GestoreReport getInstance() {
        if (instance == null) {
            instance = new GestoreReport();
        }
        return instance;
    }

    public int getNumeroCorsi(Chef chef) {
        if (chef == null || chef.getEmail() == null || chef.getEmail().isBlank()) {
            return 0;
        }
        try {
            return reportDao.contaCorsi(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore conteggio corsi", e);
        }
    }

    public Map<String, Integer> getSessioniStats(Chef chef) {
        if (chef == null || chef.getEmail() == null || chef.getEmail().isBlank()) {
            return Map.of("ONLINE", 0, "PRESENZA", 0);
        }
        try {
            return reportDao.contaSessioniPerTipo(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore statistiche sessioni", e);
        }
    }

    /**
     * Statistiche ricette per sessione: [min, max, media]
     */
    public double[] getRicetteStats(Chef chef) {
        if (chef == null || chef.getEmail() == null || chef.getEmail().isBlank()) {
            return new double[] {0, 0, 0};
        }
        try {
            return reportDao.getStatisticheRicette(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore statistiche ricette", e);
        }
    }
}
