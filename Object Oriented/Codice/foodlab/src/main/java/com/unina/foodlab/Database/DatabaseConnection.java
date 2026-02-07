package com.unina.foodlab.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection istanza = null;
    private Connection conn = null;
    private SQLException lastSqlException = null;

    // Costruttore privato 
    private DatabaseConnection() {
        try {
            // Registrazione del driver 
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = ""; //Il tuo user di postgres
            String password = ""; //La password è la tua di postgres

            // Apertura connessione 
            conn = DriverManager.getConnection(url, user, password);
            
            // Imposto lo schema
            Statement st = conn.createStatement();
            st.execute("SET search_path TO uninafoodlab");
            st.close();
            
            // Per avere un feedback 
            System.out.println("Connessione OK");

        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL non trovato");
            System.out.println(e);
        } catch (SQLException e) {
            lastSqlException = e;
            System.out.println("Connessione Fallita");
            System.out.println(e);
        }
    }

    // Metodo statico per ottenere l'unica istanza
    public static DatabaseConnection getInstanza() {
        if (istanza == null) {
            istanza = new DatabaseConnection();
        }
        return istanza;
    }

    // Restituisce la Connection
    public Connection getConnection() {
        if (conn == null) {
            throw new IllegalStateException(
                    "Connessione DB non disponibile. Controlla url/user/password in DatabaseConnection.",
                    lastSqlException);
        }
        return conn;
    }
}
