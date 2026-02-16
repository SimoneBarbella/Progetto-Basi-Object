package com.unina.foodlab.Database.Dao;

import com.unina.foodlab.Database.DatabaseConnection;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Entity.SessioneOnline;
import com.unina.foodlab.Entity.SessionePresenza;
import com.unina.foodlab.Enum.TipoSessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessioneDao {

    private final Connection conn;

    public SessioneDao() {
        this.conn = DatabaseConnection.getInstanza().getConnection();
    }

    private TipoSessione mapTipoSessione(String dbVal) {
        if (dbVal == null) return null;
        if (dbVal.equalsIgnoreCase("online")) return TipoSessione.online;
        if (dbVal.equalsIgnoreCase("presenza")) return TipoSessione.presenza;
        return null;
    }

    private LocalDateTime mapOraInizio(Timestamp ts) {
        if (ts == null) return null;
        return ts.toLocalDateTime();
    }

    private Sessione mapRow(ResultSet rs, Corso corso) throws SQLException {
        String tipoDb = rs.getString("tipo_sessione");
        TipoSessione tipo = mapTipoSessione(tipoDb);
        LocalDateTime oraInizio = mapOraInizio(rs.getTimestamp("ora_inizio"));

        Sessione sessione;
        if (tipo == TipoSessione.online) {
            SessioneOnline online = new SessioneOnline();
            online.setTeoria(rs.getString("teoria"));
            sessione = online;
        } else if (tipo == TipoSessione.presenza) {
            SessionePresenza presenza = new SessionePresenza();
            presenza.setNumAderenti(rs.getInt("num_aderenti"));
            sessione = presenza;
        } else {
            // fallback generico se tipo_sessione è nullo o non riconosciuto
            sessione = new Sessione();
        }

        sessione.setIdSessione(rs.getInt("id_sessione"));
        sessione.setOraInizio(oraInizio);
        sessione.setTipoSessione(tipo);
        if (corso != null) {
            sessione.setCorso(corso);
        }
        return sessione;
    }

    public List<Sessione> findByCorsoId(int idCorso) throws SQLException {
        String sql = "SELECT id_sessione, ora_inizio, num_aderenti, teoria, tipo_sessione, id_corso "
                + "FROM Sessione WHERE id_corso = ? ORDER BY ora_inizio";

        List<Sessione> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs, null));
                }
            }
        }
        return result;
    }

    public List<Sessione> findByCorso(Corso corso) throws SQLException {
        if (corso == null || corso.getIdCorso() == null) {
            throw new IllegalArgumentException("Corso o idCorso non valido");
        }
        int idCorso = corso.getIdCorso();
        if (idCorso <= 0) {
            throw new IllegalArgumentException("Corso o idCorso non valido");
        }
        String sql = "SELECT id_sessione, ora_inizio, num_aderenti, teoria, tipo_sessione, id_corso "
                + "FROM Sessione WHERE id_corso = ? ORDER BY ora_inizio";

        List<Sessione> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs, corso));
                }
            }
        }
        return result;
    }

    public void insertSessione(Corso corso, LocalDateTime oraInizio, TipoSessione tipo, String teoria) throws SQLException {
		if (corso == null || corso.getIdCorso() == null) {
            throw new IllegalArgumentException("Corso o idCorso non valido");
        }
		int idCorso = corso.getIdCorso();
		if (idCorso <= 0) {
			throw new IllegalArgumentException("Corso o idCorso non valido");
		}
        if (oraInizio == null) {
            throw new IllegalArgumentException("Data sessione non valida");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo sessione non valido");
        }

        String checkSql = "SELECT 1 FROM Sessione WHERE id_corso = ? AND ora_inizio = ? LIMIT 1";
        try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
			psCheck.setInt(1, idCorso);
            psCheck.setTimestamp(2, Timestamp.valueOf(oraInizio));
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalStateException("Esiste già una sessione con lo stesso orario per questo corso");
                }
            }
        }

        String sql = "INSERT INTO Sessione(ora_inizio, tipo_sessione, teoria, id_corso) VALUES (?, ?::tipo_sessione_enum, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(oraInizio));
            ps.setString(2, tipo.name().toLowerCase());
            if (tipo == TipoSessione.online) {
                ps.setString(3, teoria);
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }
			ps.setInt(4, idCorso);
            ps.executeUpdate();
        }
    }

    public BigDecimal getQuantitaTotaleBySessioneId(int idSessione) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantita_totale), 0::numeric) AS tot FROM Vista_Fabbisogni_Sessione WHERE id_sessione = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSessione);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal tot = rs.getBigDecimal("tot");
                    return tot != null ? tot : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public List<IngredienteQuantita> getListaSpesaBySessioneId(int idSessione) throws SQLException {
        String sql = "SELECT v.nome_ingrediente, i.unita_di_misura, v.quantita_totale "
                + "FROM Vista_Fabbisogni_Sessione v "
                + "JOIN Ingrediente i ON i.nome = v.nome_ingrediente "
                + "WHERE v.id_sessione = ? "
                + "ORDER BY v.nome_ingrediente";

        List<IngredienteQuantita> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSessione);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    IngredienteQuantita iq = new IngredienteQuantita();
                    iq.setNome(rs.getString("nome_ingrediente"));
                    iq.setUnita(rs.getString("unita_di_misura"));
                    iq.setQuantita(rs.getBigDecimal("quantita_totale"));
                    result.add(iq);
                }
            }
        }
        return result;
    }
}
