package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.Notifica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificaDao {

    private final Connection conn;

    public NotificaDao() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Notifica> findByChefEmail(String emailChef) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }

        String sql = "SELECT id_notifica, messaggio, data_invio, email_chef, id_corso "
                + "FROM uninafoodlab.Notifica "
                + "WHERE email_chef = ? "
                + "ORDER BY data_invio DESC, id_notifica DESC";

        List<Notifica> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notifica n = new Notifica();
                    n.setIdNotifica(rs.getInt("id_notifica"));
                    n.setMessaggio(rs.getString("messaggio"));

                    Timestamp ts = rs.getTimestamp("data_invio");
                    LocalDateTime dt = ts != null ? ts.toLocalDateTime() : null;
                    n.setDataInvio(dt);

                    n.setEmailChef(rs.getString("email_chef"));

                    int idCorso = rs.getInt("id_corso");
                    n.setIdCorso(rs.wasNull() ? null : idCorso);

                    result.add(n);
                }
            }
        }

        return result;
    }

    public Notifica insertNotifica(String emailChef, String messaggio, Integer idCorso) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }
        if (messaggio == null || messaggio.isBlank()) {
            throw new IllegalArgumentException("messaggio non valido");
        }

        String sql = "INSERT INTO uninafoodlab.Notifica(messaggio, email_chef, id_corso) "
                + "VALUES(?, ?, ?) RETURNING id_notifica, data_invio";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messaggio.trim());
            ps.setString(2, emailChef);
            if (idCorso == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, idCorso);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Notifica n = new Notifica();
                n.setIdNotifica(rs.getInt("id_notifica"));
                Timestamp ts = rs.getTimestamp("data_invio");
                n.setDataInvio(ts != null ? ts.toLocalDateTime() : null);
                n.setMessaggio(messaggio.trim());
                n.setEmailChef(emailChef);
                n.setIdCorso(idCorso);
                return n;
            }
        }
    }

    public boolean deleteById(int idNotifica) throws SQLException {
        String sql = "DELETE FROM uninafoodlab.Notifica WHERE id_notifica = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNotifica);
            return ps.executeUpdate() > 0;
        }
    }
}
