package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Entity.Ingrediente;

import java.sql.SQLException;
import java.util.List;

public interface IngredienteDaoPort {
    List<Ingrediente> cercaTutti() throws SQLException;

    void controllaIngrediente(String nome, String unita) throws SQLException;
}
