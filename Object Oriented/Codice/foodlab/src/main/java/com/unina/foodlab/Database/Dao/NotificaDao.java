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
        this.conn = DatabaseConnection.getInstanza().getConnection();
    }

    public List<Notifica> cercaPerEmailChef(String emailChef) throws SQLException {
        if (emailChef == null || emailChef.isBlank()) {
            throw new IllegalArgumentException("emailChef non valida");
        }

        String sql = "SELECT DISTINCT n.id_notifica, n.messaggio, n.data_invio, n.email_chef, n.id_corso "
            + "FROM uninafoodlab.Notifica n "
            + "LEFT JOIN uninafoodlab.Gestisce g ON g.id_corso = n.id_corso AND g.email_chef = ? "
            + "WHERE n.email_chef = ? OR g.email_chef = ? "
            + "ORDER BY n.data_invio DESC, n.id_notifica DESC";

        List<Notifica> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            ps.setString(2, emailChef);
            ps.setString(3, emailChef);
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

    public Notifica inserisciNotifica(String emailChef, String messaggio, Integer idCorso) throws SQLException {
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

    public boolean eliminaPerId(int idNotifica) throws SQLException {
        String sql = "DELETE FROM uninafoodlab.Notifica WHERE id_notifica = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNotifica);
            return ps.executeUpdate() > 0;
        }
    }
}
