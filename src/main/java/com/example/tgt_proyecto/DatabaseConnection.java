package com.example.tgt_proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseConnection {
    // Cadena de conexión para SQL Server con autenticación de Windows
    private final String url = "jdbc:sqlserver://DESKTOP-HS7FCSK;databaseName=TGT_inventario;integratedSecurity=true;encrypt=false;";



    public Connection connect() {
        Connection conn = null;
        try {
            // Establecer conexión
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa con la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return conn;
    }

    // Método para insertar un cliente nuevo
    public void insertarCliente(String nombre, String telefono, String email, String direccion) {
        String query = "INSERT INTO Clientes (cli_nombre, cli_telefono, cli_email, cli_direccion) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, email);
            stmt.setString(4, direccion);
            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Cliente agregado con éxito. Filas afectadas: " + filasAfectadas);
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    // Método para consultar todos los clientes
    public void consultarClientes() {
        String query = "SELECT * FROM Clientes";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("cli_id"));
                System.out.println("Nombre: " + rs.getString("cli_nombre"));
                System.out.println("Teléfono: " + rs.getString("cli_telefono"));
                System.out.println("Email: " + rs.getString("cli_email"));
                System.out.println("Dirección: " + rs.getString("cli_direccion"));
                System.out.println("-------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar clientes: " + e.getMessage());
        }
    }

}
