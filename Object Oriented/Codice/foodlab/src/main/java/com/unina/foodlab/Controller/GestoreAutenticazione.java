package com.unina.foodlab.Controller;

import java.sql.SQLException;
import java.util.Optional;

import com.unina.foodlab.Database.Dao.ChefDao;
import com.unina.foodlab.Entity.Chef;

public class GestoreAutenticazione {

    private static GestoreAutenticazione instanza;

    private final ChefDao chefDao;

    private GestoreAutenticazione() {
        this.chefDao = new ChefDao();
    }

    public static synchronized GestoreAutenticazione getInstanza() {
        if (instanza == null) {
            instanza = new GestoreAutenticazione();
        }
        return instanza;
    }

    public Chef loginChef(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        try {
            Optional<Chef> chefOpt = chefDao.cercaPerEmail(email);
            if (chefOpt.isEmpty()) {
                return null;
            }

            Chef chef = chefOpt.get();
            if (password.equals(chef.getPassword())) {
                return chef;
            }

            return null;
        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
