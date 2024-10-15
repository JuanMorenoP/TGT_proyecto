package com.example.tgt_proyecto.proveedores;

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
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class ModificarProveedorController {

    private Proveedor proveedorSeleccionado;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField contactoField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField metodoPagoField;

    @FXML
    private TextField nitRutField;

    @FXML
    private Button cerrarSesionButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button modificarButton;

    @FXML
    public void initialize() {
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Método para cerrar sesión y regresar a la ventana de login
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        // Limpiar los campos de sesión
        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }

    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/proveedores/proveedores.fxml", "Proveedores TGT | EQUIPMENTS");
    }

    @FXML
    private void handleModificarProveedor(ActionEvent event) {
        if (proveedorSeleccionado == null) {
            mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el proveedor. No hay proveedor seleccionado.");
            return;
        }

        // Obtener los valores actualizados
        String nombre = nombreField.getText();
        String contacto = contactoField.getText();
        String direccion = direccionField.getText();
        String metodoPago = metodoPagoField.getText();
        String nitRut = nitRutField.getText();

        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                // Verificar si el NIT/RUT ya existe
                String checkQuery = "SELECT COUNT(*) FROM Proveedores WHERE proveedor_nit_rut = ? AND proveedor_nit_rut != ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, nitRut);
                checkStatement.setString(2, proveedorSeleccionado.getNitRut());

                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    mostrarAlerta("Error", "Modificación fallida", "El NIT/RUT ya existe para otro proveedor.");
                    return;
                }

                // Si no hay duplicados, proceder a la modificación
                String updateQuery = "UPDATE Proveedores SET proveedor_nombre = ?, proveedor_contacto = ?, proveedor_direccion = ?, proveedor_metodo_pago = ?, proveedor_nit_rut = ? WHERE proveedor_nit_rut = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, contacto);
                preparedStatement.setString(3, direccion);
                preparedStatement.setString(4, metodoPago);
                preparedStatement.setString(5, nitRut);
                preparedStatement.setString(6, proveedorSeleccionado.getNitRut());

                int filasActualizadas = preparedStatement.executeUpdate();
                if (filasActualizadas > 0) {
                    mostrarAlerta("Éxito", "Proveedor modificado exitosamente", "El proveedor ha sido modificado correctamente.");
                    handleCancelar(event);
                } else {
                    mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el proveedor.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Modificación fallida", "Hubo un error al modificar el proveedor.");
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

    public void cargarDatosProveedor(Proveedor proveedor) {
        this.proveedorSeleccionado = proveedor;
        nombreField.setText(proveedor.getNombre());
        contactoField.setText(proveedor.getContacto());
        direccionField.setText(proveedor.getDireccion());
        metodoPagoField.setText(proveedor.getMetodoPago());
        nitRutField.setText(proveedor.getNitRut());
    }

    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(fxmlLoader.load());
        newScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.setTitle(titulo);
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

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

    // Verificar si el usuario es administrador
    private boolean esAdministrador() {
        return SessionManager.esAdministrador();
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
}
