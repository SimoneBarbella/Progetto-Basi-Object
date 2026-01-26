package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.Ingrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDao {

    private final Connection conn;

    public IngredienteDao() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Ingrediente> findAll() throws SQLException {
        String sql = "SELECT nome, unita_di_misura FROM Ingrediente ORDER BY nome";
        List<Ingrediente> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ingrediente i = new Ingrediente();
                i.setNome(rs.getString("nome"));
                i.setUnitàDiMisura(rs.getString("unita_di_misura"));
                result.add(i);
            }
        }
        return result;
    }

    public void ensureIngrediente(String nome, String unita) throws SQLException {
        String sql = "INSERT INTO Ingrediente(nome, unita_di_misura) VALUES (?, ?) ON CONFLICT (nome) DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, unita);
            ps.executeUpdate();
        }
    }
}
