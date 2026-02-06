package com.unina.foodlab.Controller;

import com.unina.foodlab.Database.Dao.NotificaDao;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Notifica;

import java.sql.SQLException;
import java.util.List;

public class GestoreNotifiche {

    private static GestoreNotifiche instanza;

    private final NotificaDao notificaDao;

    private GestoreNotifiche() {
        this.notificaDao = new NotificaDao();
    }

    public static synchronized GestoreNotifiche getInstanza() {
        if (instanza == null) {
            instanza = new GestoreNotifiche();
        }
        return instanza;
    }

    public List<Notifica> getNotifiche(Chef chef) {
        if (chef == null || chef.getEmail() == null || chef.getEmail().isBlank()) {
            throw new IllegalArgumentException("Chef non valido");
        }
        try {
            return notificaDao.cercaPerEmailChef(chef.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Errore caricamento notifiche", e);
        }
    }

    public Notifica inviaNotifica(Chef chef, String messaggio, Integer idCorso) {
        if (chef == null || chef.getEmail() == null || chef.getEmail().isBlank()) {
            throw new IllegalArgumentException("Chef non valido");
        }
        try {
            return notificaDao.inserisciNotifica(chef.getEmail(), messaggio, idCorso);
        } catch (SQLException e) {
            throw new RuntimeException("Errore invio notifica", e);
        }
    }

    public void eliminaNotifica(int idNotifica) {
        try {
            notificaDao.eliminaPerId(idNotifica);
        } catch (SQLException e) {
            throw new RuntimeException("Errore eliminazione notifica", e);
        }
    }
}
