package com.example.tgt_proyecto.clientes;

import com.example.tgt_proyecto.database.DatabaseConnection;
import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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

import javafx.scene.control.Alert;

import javafx.scene.control.*;

public class NuevoClienteController {

    @FXML
    private void handleInicio(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/dashboard/dashboard-view.fxml", "Dashboard TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMaquinaria(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/maquinaria/maquinaria.fxml", "Maquinaria TGT | EQUIPMENTS");
    }


    @FXML
    private void handleUsuarios(ActionEvent event) throws IOException {
        cambiarEscenaSiAdministrador(event, "/com/example/tgt_proyecto/usuarios/usuarios.fxml", "Usuarios TGT | EQUIPMENTS");
    }

    @FXML
    private void handleClientes(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    @FXML
    private void handleProveedores(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/proveedores/proveedores.fxml", "Proveedores TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMantenimiento(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/mantenimiento/mantenimiento.fxml", "Mantenimiento TGT | EQUIPMENTS");
    }

    @FXML
    private void handleInventario(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/inventario/inventario.fxml", "Inventario TGT | EQUIPMENTS");
    }

    @FXML
    private void handleCompras(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/compras/compras.fxml", "Compras TGT | EQUIPMENTS");
    }

    @FXML
    private void handleVentas(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/ventas/ventas.fxml", "Ventas TGT | EQUIPMENTS");
    }

    @FXML
    private void handleConfiguracion(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/configuracion/configuracion.fxml", "Configuración TGT | EQUIPMENTS");
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
    private Button cerrarSesionButton;
    @FXML
    private Button cancelarButton;

    @FXML
    private Button agregarClienteButton;

    @FXML
    public void initialize() {
        // Configurar el botón de cerrar sesión para volver al login
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private TableView<Cliente> tableView;

    // Método para manejar la acción del botón "Cancelar"
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    // Verificar si el usuario es administrador
    private boolean esAdministrador() {
        return SessionManager.esAdministrador();  // Utilizar SessionManager para verificar si es administrador
    }

    // Mostrar alerta de permiso denegado
    private void mostrarAlertaPermisoDenegado() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Permiso denegado");
        alerta.setHeaderText("No tienes permisos suficientes para acceder a esta sección.");
        alerta.setContentText("Por favor, contacta al administrador.");
        alerta.showAndWait();
    }

    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!esAdministrador()) {
            mostrarAlertaPermisoDenegado();
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
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
            mostrarAlerta("Error", "Campos Vacíos", "Todos los campos deben estar llenos.");
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
                mostrarAlerta("Éxito", "Cliente Agregado", "El cliente ha sido agregado exitosamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al agregar cliente", "Hubo un error al agregar el cliente.");
                return;
            } finally {
                connection.close();
            }
        }

        // Regresar al apartado de clientes después de agregar el cliente
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    // Método para cerrar sesión y regresar a la ventana de login, limpiando los campos
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm()); // Asegurando que los estilos se carguen

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }

    // Método auxiliar para mostrar una alerta
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
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
