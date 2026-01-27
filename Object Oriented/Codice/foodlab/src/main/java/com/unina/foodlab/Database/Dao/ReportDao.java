package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportDao {

    private final Connection conn;

    public ReportDao() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public int contaCorsi(String emailChef) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }
        String sql = "SELECT COUNT(DISTINCT g.id_corso) AS corsi_gestiti FROM Gestisce g WHERE g.email_chef = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("corsi_gestiti");
                }
            }
        }
        return 0;
    }

    /**
     * Conteggia le sessioni per tipo (ONLINE/PRESENZA) su tutti i corsi gestiti dal chef.
     */
    public Map<String, Integer> contaSessioniPerTipo(String emailChef) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }

        String sql = "SELECT "
                + "COALESCE(SUM(CASE WHEN s.tipo_sessione = 'online' THEN 1 ELSE 0 END), 0) AS sessioni_online, "
                + "COALESCE(SUM(CASE WHEN s.tipo_sessione = 'presenza' THEN 1 ELSE 0 END), 0) AS sessioni_presenza "
                + "FROM Sessione s "
                + "JOIN Gestisce g ON g.id_corso = s.id_corso "
                + "WHERE g.email_chef = ?";

        Map<String, Integer> result = new LinkedHashMap<>();
        result.put("ONLINE", 0);
        result.put("PRESENZA", 0);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("ONLINE", rs.getInt("sessioni_online"));
                    result.put("PRESENZA", rs.getInt("sessioni_presenza"));
                }
            }
        }

        return result;
    }

    /**
     * Statistiche numero ricette preparate per sessione su tutti i corsi del chef.
     * Ritorna [min, max, media].
     */
    public double[] getStatisticheRicette(String emailChef) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }

        String sql = "SELECT "
                + "COALESCE(MIN(t.cnt), 0) AS min_cnt, "
                + "COALESCE(MAX(t.cnt), 0) AS max_cnt, "
                + "COALESCE(AVG(t.cnt), 0) AS avg_cnt "
                + "FROM ( "
                + "  SELECT s.id_sessione, COALESCE(COUNT(p.id_ricetta), 0) AS cnt "
                + "  FROM Sessione s "
                + "  JOIN Gestisce g ON g.id_corso = s.id_corso "
                + "  LEFT JOIN Prepara p ON p.id_sessione = s.id_sessione "
                + "  WHERE g.email_chef = ? "
                + "  GROUP BY s.id_sessione "
                + ") t";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double min = rs.getDouble("min_cnt");
                    double max = rs.getDouble("max_cnt");
                    double avg = rs.getDouble("avg_cnt");
                    if (min < 0) min = 0;
                    if (max < 0) max = 0;
                    if (avg < 0) avg = 0;
                    return new double[] {min, max, avg};
                }
            }
        }

        return new double[] {0, 0, 0};
    }
}
