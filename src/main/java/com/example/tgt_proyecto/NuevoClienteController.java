package com.example.tgt_proyecto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NuevoClienteController {
    @FXML
    private void handleInicio(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/dashboard-view.fxml", "Dashboard TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMaquinaria(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/maquinaria.fxml", "Maquinaria TGT | EQUIPMENTS");
    }

    @FXML
    private void handleEmpleados(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/empleados.fxml", "Empleados TGT | EQUIPMENTS");
    }

    @FXML
    private void handleClientes(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    @FXML
    private void handleProveedores(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/proveedores.fxml", "Proveedores TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMantenimiento(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/mantenimiento.fxml", "Mantenimiento TGT | EQUIPMENTS");
    }

    @FXML
    private void handleInventario(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/inventario.fxml", "Inventario TGT | EQUIPMENTS");
    }

    @FXML
    private void handleCompras(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/compras.fxml", "Compras TGT | EQUIPMENTS");
    }

    @FXML
    private void handleVentas(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/ventas.fxml", "Ventas TGT | EQUIPMENTS");
    }

    @FXML
    private void handleConfiguracion(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/configuracion.fxml", "Configuración TGT | EQUIPMENTS");
    }
    @FXML
    private TextField nombreField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField documentoField;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button agregarClienteButton;

    // Método para manejar la acción del botón "Cancelar"
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    // Método para manejar la acción del botón "Agregar cliente"
    @FXML
    private void handleAgregarCliente(ActionEvent event) throws IOException, SQLException {
        // Obtener los valores de los campos
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();
        String documento = documentoField.getText();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || direccion.isEmpty() || email.isEmpty() || telefono.isEmpty() || documento.isEmpty()) {
            // Mostrar mensaje de error o retornar
            return;
        }

        // Conectar a la base de datos y agregar el cliente
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            String insertQuery = "INSERT INTO Clientes (cli_nombre, cli_telefono, cli_email, cli_direccion, cli_documento) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setString(1, nombre);
                stmt.setString(2, telefono);
                stmt.setString(3, email);
                stmt.setString(4, direccion);
                stmt.setString(5, documento);

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return;  // Mostrar mensaje de error
            } finally {
                connection.close();
            }
        }

        // Regresar al apartado de clientes después de agregar el cliente
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }


    // Método auxiliar para cambiar escenas
    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(fxmlLoader.load());
        newScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.setTitle(titulo);
        stage.show();
    }
}
