package com.unina.foodlab.Database.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Enum.TipoUtente;

public class ChefDao {

    private Connection conn;

    public ChefDao() {
        this.conn = DatabaseConnection.getInstanza().getConnection();
    }

    private TipoUtente mapTipoUtente(String dbVal) {
        if (dbVal == null) return null;
        if (dbVal.equalsIgnoreCase("chef")) return TipoUtente.Chef;
        if (dbVal.equalsIgnoreCase("studente")) return TipoUtente.Studente;
        if (dbVal.equalsIgnoreCase("chefStudente")) return TipoUtente.ChefStudente;
        return null;
    }

    public List<Chef> cercaTutti() throws SQLException {
        String sql = "SELECT u.email, u.nome, u.cognome, u.password, u.tipo_utente, "
                + "STRING_AGG(sc.specializzazione, ',') AS specializzazioni "
                + "FROM Utente u LEFT JOIN Specializzazione_Chef sc ON u.email = sc.email_chef "
                + "WHERE u.tipo_utente IN ('chef','chefStudente') "
                + "GROUP BY u.email, u.nome, u.cognome, u.password, u.tipo_utente";

        List<Chef> res = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Chef c = new Chef();
                c.setEmail(rs.getString("email"));
                c.setNome(rs.getString("nome"));
                c.setCognome(rs.getString("cognome"));
                c.setPassword(rs.getString("password"));
                c.setTipoUtente(mapTipoUtente(rs.getString("tipo_utente")));
                String spec = rs.getString("specializzazioni");
                c.setSpecializzazione(spec);
                res.add(c);
            }
        }
        return res;
    }

    public Optional<Chef> cercaPerEmail(String email) throws SQLException {
        String sql = "SELECT u.email, u.nome, u.cognome, u.password, u.tipo_utente, "
                + "STRING_AGG(sc.specializzazione, ',') AS specializzazioni "
                + "FROM Utente u LEFT JOIN Specializzazione_Chef sc ON u.email = sc.email_chef "
                + "WHERE u.email = ? AND u.tipo_utente IN ('chef','chefStudente') "
                + "GROUP BY u.email, u.nome, u.cognome, u.password, u.tipo_utente";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Chef c = new Chef();
                    c.setEmail(rs.getString("email"));
                    c.setNome(rs.getString("nome"));
                    c.setCognome(rs.getString("cognome"));
                    c.setPassword(rs.getString("password"));
                    c.setTipoUtente(mapTipoUtente(rs.getString("tipo_utente")));
                    String spec = rs.getString("specializzazioni");
                    c.setSpecializzazione(spec);
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    public boolean salva(Chef chef) throws SQLException {
        String insertUtente = "INSERT INTO Utente(email,nome,cognome,password,tipo_utente) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(insertUtente)) {
            ps.setString(1, chef.getEmail());
            ps.setString(2, chef.getNome());
            ps.setString(3, chef.getCognome());
            ps.setString(4, chef.getPassword());

            TipoUtente t = chef.getTipoUtente();
            ps.setString(5, t == null ? "chef" : (t.name().toLowerCase()));
            int updated = ps.executeUpdate();
            if (updated == 0) return false;
        }


        if (chef.getSpecializzazione() != null && !chef.getSpecializzazione().isEmpty()) {
            String[] specs = chef.getSpecializzazione().split(",");
            String insertSpec = "INSERT INTO Specializzazione_Chef(email_chef, specializzazione) VALUES(?,?)";
            try (PreparedStatement ps2 = conn.prepareStatement(insertSpec)) {
                for (String s : specs) {
                    String trimmed = s.trim();
                    if (trimmed.isEmpty()) continue;
                    ps2.setString(1, chef.getEmail());
                    ps2.setString(2, trimmed);
                    try { ps2.executeUpdate(); } catch (SQLException ex) { /* ignore duplicates */ }
                }
            }
        }
        return true;
    }

    public boolean aggiorna(Chef chef) throws SQLException {
        String updateUtente = "UPDATE Utente SET nome = ?, cognome = ?, password = ?, tipo_utente = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateUtente)) {
            ps.setString(1, chef.getNome());
            ps.setString(2, chef.getCognome());
            ps.setString(3, chef.getPassword());
            TipoUtente t = chef.getTipoUtente();
            ps.setString(4, t == null ? "chef" : (t.name().toLowerCase()));
            ps.setString(5, chef.getEmail());
            int updated = ps.executeUpdate();
            if (updated == 0) return false;
        }

        String deleteSpecs = "DELETE FROM Specializzazione_Chef WHERE email_chef = ?";
        try (PreparedStatement psd = conn.prepareStatement(deleteSpecs)) {
            psd.setString(1, chef.getEmail());
            psd.executeUpdate();
        }
        if (chef.getSpecializzazione() != null && !chef.getSpecializzazione().isEmpty()) {
            String[] specs = chef.getSpecializzazione().split(",");
            String insertSpec = "INSERT INTO Specializzazione_Chef(email_chef, specializzazione) VALUES(?,?)";
            try (PreparedStatement ps2 = conn.prepareStatement(insertSpec)) {
                for (String s : specs) {
                    String trimmed = s.trim();
                    if (trimmed.isEmpty()) continue;
                    ps2.setString(1, chef.getEmail());
                    ps2.setString(2, trimmed);
                    ps2.executeUpdate();
                }
            }
        }

        return true;
    }

    public boolean eliminaPerEmail(String email) throws SQLException {
        String deleteUtente = "DELETE FROM Utente WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteUtente)) {
            ps.setString(1, email);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }
}
