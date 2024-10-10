package com.example.tgt_proyecto.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:sqlserver://DESKTOP-HS7FCSK:1433;databaseName=TGT_inventario;integratedSecurity=true;encrypt=false;";

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa con la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar conectar: " + e.getMessage());
            e.printStackTrace();  // Proporciona más detalles sobre el error
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el controlador JDBC: " + e.getMessage());
            e.printStackTrace();  // Proporciona más detalles sobre el error
        }
        return conn;
    }
}