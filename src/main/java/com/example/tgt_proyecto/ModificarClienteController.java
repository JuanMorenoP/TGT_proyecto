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
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

import javafx.scene.control.*;

public class ModificarClienteController {
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

    private Cliente clienteSeleccionado;
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
    private Button modificarButton;
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
    // Método para cerrar sesión y regresar a la ventana de login, limpiando los campos
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm()); // Asegurando que los estilos se carguen

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }
    @FXML
    private TableView<Cliente> tableView;
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    @FXML
    private void handleModificarCliente(ActionEvent event) {
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el cliente. No hay cliente seleccionado.");
            return;
        }

        // Obtener los valores actualizados de los campos de texto
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();
        String documento = documentoField.getText();

        // Actualizar los datos del cliente en la base de datos
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                // Verificar si el documento ya existe
                String checkQuery = "SELECT COUNT(*) FROM Clientes WHERE cli_documento = ? AND cli_documento != ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, documento);
                checkStatement.setString(2, clienteSeleccionado.getDocumento());

                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    mostrarAlerta("Error", "Modificación fallida", "El número de documento ya existe para otro cliente.");
                    return;  // Terminar si el documento ya existe
                }

                // Si no hay duplicados, actualizar el cliente
                String updateQuery = "UPDATE Clientes SET cli_nombre = ?, cli_direccion = ?, cli_email = ?, cli_telefono = ?, cli_documento = ? WHERE cli_documento = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                // Establecer los valores en la consulta
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, direccion);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, telefono);
                preparedStatement.setString(5, documento);  // Nuevo documento
                preparedStatement.setString(6, clienteSeleccionado.getDocumento());  // Documento original

                int filasActualizadas = preparedStatement.executeUpdate();
                if (filasActualizadas > 0) {
                    mostrarAlerta("Éxito", "Cliente modificado exitosamente", "El cliente ha sido modificado correctamente.");
                    handleCancelar(event); // Regresar a la pantalla de clientes
                } else {
                    mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el cliente.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Modificación fallida", "Hubo un error al modificar el cliente.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void cargarDatosCliente(Cliente cliente) {
        this.clienteSeleccionado = cliente;  // Guardar cliente seleccionado
        nombreField.setText(cliente.getNombre());
        direccionField.setText(cliente.getDireccion());
        emailField.setText(cliente.getEmail());
        telefonoField.setText(cliente.getTelefono());
        documentoField.setText(cliente.getDocumento());
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

    // Método para mostrar alertas en pantalla
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
