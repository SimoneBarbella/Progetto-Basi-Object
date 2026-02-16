package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Entity.Chef;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ChefDaoPort {
    List<Chef> cercaTutti() throws SQLException;

    Optional<Chef> cercaPerEmail(String email) throws SQLException;

    boolean salva(Chef chef) throws SQLException;

    boolean aggiorna(Chef chef) throws SQLException;

    boolean eliminaPerEmail(String email) throws SQLException;
}
