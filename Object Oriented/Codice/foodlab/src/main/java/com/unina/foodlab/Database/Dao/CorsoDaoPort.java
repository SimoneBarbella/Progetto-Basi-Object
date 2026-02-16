package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import java.sql.SQLException;
import java.util.List;

public interface CorsoDaoPort {
    boolean salva(Corso corso, Chef chef, List<String> categorie) throws SQLException;

    List<Corso> cercaPerEmailChef(String emailChef) throws SQLException;

    List<String> cercaTutteCategorieDistinte() throws SQLException;
}
