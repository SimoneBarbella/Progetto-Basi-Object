package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RicettaDao {

    private final Connection conn;

    public RicettaDao() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Ricetta> findAll() throws SQLException {
        String sql = "SELECT id_ricetta, nome, descrizione, tempo FROM Ricetta ORDER BY nome";
        List<Ricetta> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ricetta r = new Ricetta();
                r.setIdRicetta(rs.getInt("id_ricetta"));
                r.setNome(rs.getString("nome"));
                r.setDescrizione(rs.getString("descrizione"));
                Time t = rs.getTime("tempo");
                r.setTempo(t != null ? t.toLocalTime() : null);
                result.add(r);
            }
        }
        return result;
    }

    public List<Ricetta> findBySessioneId(int idSessione) throws SQLException {
        String sql = "SELECT r.id_ricetta, r.nome, r.descrizione, r.tempo "
                + "FROM Ricetta r JOIN Prepara p ON p.id_ricetta = r.id_ricetta "
                + "WHERE p.id_sessione = ? ORDER BY r.nome";
        List<Ricetta> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSessione);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ricetta r = new Ricetta();
                    r.setIdRicetta(rs.getInt("id_ricetta"));
                    r.setNome(rs.getString("nome"));
                    r.setDescrizione(rs.getString("descrizione"));
                    Time t = rs.getTime("tempo");
                    r.setTempo(t != null ? t.toLocalTime() : null);
                    result.add(r);
                }
            }
        }
        return result;
    }

    public Ricetta insertRicetta(String nome, String descrizione, LocalTime tempo) throws SQLException {
        String sql = "INSERT INTO Ricetta(nome, descrizione, tempo) VALUES (?, ?, ?) RETURNING id_ricetta";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setTime(3, tempo != null ? Time.valueOf(tempo) : null);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ricetta r = new Ricetta();
                    r.setIdRicetta(rs.getInt("id_ricetta"));
                    r.setNome(nome);
                    r.setDescrizione(descrizione);
                    r.setTempo(tempo);
                    return r;
                }
            }
        }
        return null;
    }

    public void linkRicettaToSessione(int idSessione, int idRicetta) throws SQLException {
        String sql = "INSERT INTO Prepara(id_sessione, id_ricetta) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSessione);
            ps.setInt(2, idRicetta);
            ps.executeUpdate();
        }
    }

    public void unlinkRicettaFromSessione(int idSessione, int idRicetta) throws SQLException {
        String sql = "DELETE FROM Prepara WHERE id_sessione = ? AND id_ricetta = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSessione);
            ps.setInt(2, idRicetta);
            ps.executeUpdate();
        }
    }

    public void addRichiede(int idRicetta, String nomeIngrediente, BigDecimal quantita) throws SQLException {
        String sql = "INSERT INTO Richiede(id_ricetta, nome_ingrediente, quantita_necessaria) VALUES (?, ?, ?) "
                + "ON CONFLICT (id_ricetta, nome_ingrediente) DO UPDATE SET quantita_necessaria = EXCLUDED.quantita_necessaria";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRicetta);
            ps.setString(2, nomeIngrediente);
            ps.setBigDecimal(3, quantita);
            ps.executeUpdate();
        }
    }

    public List<IngredienteQuantita> findIngredientiByRicettaId(int idRicetta) throws SQLException {
        String sql = "SELECT i.nome, i.unita_di_misura, r.quantita_necessaria "
                + "FROM Richiede r JOIN Ingrediente i ON i.nome = r.nome_ingrediente "
                + "WHERE r.id_ricetta = ? ORDER BY i.nome";
        List<IngredienteQuantita> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRicetta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    IngredienteQuantita iq = new IngredienteQuantita();
                    iq.setNome(rs.getString("nome"));
                    iq.setUnita(rs.getString("unita_di_misura"));
                    iq.setQuantita(rs.getBigDecimal("quantita_necessaria"));
                    result.add(iq);
                }
            }
        }
        return result;
    }
}
