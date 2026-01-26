package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CorsoDao {

    private final Connection conn;

    public CorsoDao() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Salva un nuovo corso e lo collega allo Chef che lo crea.
     */
    public boolean save(Corso corso, Chef chef, List<String> categorie) throws SQLException {
        if (corso == null || chef == null) {
            throw new IllegalArgumentException("Corso e Chef non possono essere null");
        }

        if (corso.getDataInizio() == null) {
            throw new IllegalArgumentException("dataInizio del Corso non può essere null");
        }

        String insertCorso = "INSERT INTO uninafoodlab.Corso(data_inizio, nome, frequenza, num_partecipanti, num_sessioni) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id_corso";
        String insertGestisce = "INSERT INTO uninafoodlab.Gestisce(email_chef, id_corso) VALUES(?, ?)";
        String insertCategoria = "INSERT INTO uninafoodlab.Categoria_Corso(id_corso, categoria) VALUES(?, ?) ON CONFLICT DO NOTHING";

        boolean oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            int idCorso;
            try (PreparedStatement ps = conn.prepareStatement(insertCorso)) {
                ps.setDate(1, Date.valueOf(corso.getDataInizio()));
                ps.setString(2, corso.getNome());
                ps.setString(3, corso.getFrequenza());
                ps.setInt(4, corso.getNumPartecipanti());
                ps.setInt(5, corso.getNumSessioni());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    idCorso = rs.getInt("id_corso");
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(insertGestisce)) {
                ps2.setString(1, chef.getEmail());
                ps2.setInt(2, idCorso);
                ps2.executeUpdate();
            }

            if (categorie != null && !categorie.isEmpty()) {
                try (PreparedStatement ps3 = conn.prepareStatement(insertCategoria)) {
                    for (String categoria : categorie) {
                        if (categoria == null || categoria.isBlank()) continue;
                        ps3.setInt(1, idCorso);
                        ps3.setString(2, categoria.trim());
                        ps3.executeUpdate();
                    }
                }
            }

            corso.setIdCorso(String.valueOf(idCorso));
            conn.commit();
            return true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }

    public List<Corso> findByChefEmail(String emailChef) throws SQLException {
        String sql = "SELECT c.id_corso, c.data_inizio, c.nome, c.frequenza, c.num_partecipanti, c.num_sessioni, "
                + "string_agg(cc.categoria, ',') AS categorie "
                + "FROM uninafoodlab.Corso c "
                + "JOIN uninafoodlab.Gestisce g ON g.id_corso = c.id_corso "
                + "LEFT JOIN uninafoodlab.Categoria_Corso cc ON cc.id_corso = c.id_corso "
                + "WHERE g.email_chef = ? "
                + "GROUP BY c.id_corso, c.data_inizio, c.nome, c.frequenza, c.num_partecipanti, c.num_sessioni "
                + "ORDER BY c.id_corso";

        List<Corso> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailChef);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Corso c = new Corso();
                    c.setIdCorso(String.valueOf(rs.getInt("id_corso")));
                    Date d = rs.getDate("data_inizio");
                    if (d != null) c.setDataInizio(d.toLocalDate());
                    c.setNome(rs.getString("nome"));
                    c.setFrequenza(rs.getString("frequenza"));
                    c.setNumPartecipanti(rs.getInt("num_partecipanti"));
                    c.setNumSessioni(rs.getInt("num_sessioni"));
                    String cats = rs.getString("categorie");
                    if (cats != null && !cats.isBlank()) {
                        List<String> list = new ArrayList<>();
                        for (String s : cats.split(",")) {
                            String t = s.trim();
                            if (!t.isEmpty()) list.add(t);
                        }
                        c.setCategorie(list);
                    } else {
                        c.setCategorie(new ArrayList<>());
                    }
                    result.add(c);
                }
            }
        }
        return result;
    }

    public List<String> findAllCategorieDistinct() throws SQLException {
        String sql = "SELECT DISTINCT categoria FROM uninafoodlab.Categoria_Corso WHERE categoria IS NOT NULL AND TRIM(categoria) <> '' ORDER BY categoria";
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String cat = rs.getString("categoria");
                if (cat != null) {
                    String trimmed = cat.trim();
                    if (!trimmed.isEmpty()) {
                        result.add(trimmed);
                    }
                }
            }
        }
        return result;
    }
}
