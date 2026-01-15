package com.unina.foodlab;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.unina.foodlab.Database.DatabaseConnection;

public class App {
	
    public static void main( String[] args ){
    	
    	try {
    		Connection conn = DatabaseConnection.getInstance().getConnection();

    		Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery("SELECT * FROM utente");

    		while (rs.next()) {
    			System.out.println("Email: " + rs.getString("email"));
    		}

    		rs.close();
    		st.close();
    		conn.close();

    	} catch (SQLException e) {
    		System.out.println("Errore SQL");
    		System.out.println(e);
    	}
    	 
    }
}
