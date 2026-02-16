package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Entity.Notifica;

import java.sql.SQLException;
import java.util.List;

public interface NotificaDaoPort {
    List<Notifica> cercaPerEmailChef(String emailChef) throws SQLException;

    Notifica inserisciNotifica(String emailChef, String messaggio, Integer idCorso) throws SQLException;

    boolean eliminaPerId(int idNotifica) throws SQLException;
}
