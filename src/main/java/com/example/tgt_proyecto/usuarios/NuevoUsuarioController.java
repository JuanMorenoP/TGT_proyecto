package com.example.tgt_proyecto.usuarios;

import com.example.tgt_proyecto.database.DatabaseConnection;
import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NuevoUsuarioController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPasswordField;

     // Recomendación: Usar PasswordField para contraseñas

    @FXML
    private ComboBox<String> rolComboBox;  // ComboBox para seleccionar el rol


    @FXML
    private Button cerrarSesionButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button agregarUsuarioButton;

    @FXML
    private ImageView togglePasswordVisibility;

    private boolean isPasswordVisible = false;
    @FXML
    public void initialize() {
        // Cargar opciones de roles en el ComboBox
        ObservableList<String> roles = FXCollections.observableArrayList("Administrador", "Empleado");
        rolComboBox.setItems(roles);
// Ocultar el campo de mostrar contraseña al inicio
        showPasswordField.setVisible(false);

        // Rutas para los íconos del ojo
        Image eyeClosedImage = new Image(getClass().getResource("/com/example/tgt_proyecto/icons/eye-closed.png").toExternalForm());
        Image eyeOpenImage = new Image(getClass().getResource("/com/example/tgt_proyecto/icons/eye-open.png").toExternalForm());

        // Establecer el ícono de ojo cerrado al inicio
        togglePasswordVisibility.setImage(eyeClosedImage);

        // Configurar la funcionalidad para mostrar/ocultar la contraseña
        togglePasswordVisibility.setOnMouseClicked(event -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                showPasswordField.setText(passwordField.getText());
                showPasswordField.setVisible(true);
                passwordField.setVisible(false);
                togglePasswordVisibility.setImage(eyeOpenImage);  // Cambiar al icono de ojo abierto
            } else {
                passwordField.setText(showPasswordField.getText());
                passwordField.setVisible(true);
                showPasswordField.setVisible(false);
                togglePasswordVisibility.setImage(eyeClosedImage);  // Cambiar al icono de ojo cerrado
            }
        });
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
        // Configurar el botón de cerrar sesión para volver al login



    // Método para manejar la acción del botón "Cancelar"
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/usuarios/usuarios.fxml", "Usuarios TGT | EQUIPMENTS");
    }

    // Verificar si el usuario es administrador
    private boolean esAdministrador() {
        return SessionManager.esAdministrador();  // Utilizar SessionManager para verificar si es administrador
    }


    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!esAdministrador()) {
            mostrarAlertaPermisoDenegado();
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
    }

    // Método para agregar el usuario a la base de datos
    @FXML
    private void handleAgregarUsuario(ActionEvent event) throws IOException {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String usuario = usuarioField.getText();
        String contraseña = passwordField.getText();
        String rolSeleccionado = rolComboBox.getValue();  // Obtener el valor del ComboBox para el rol

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta("Error", "Campos Vacíos", "Todos los campos deben estar llenos.");
            return;
        }

        // Transformar el rol seleccionado a un ID
        int rolId;
        if (rolSeleccionado.equalsIgnoreCase("Administrador")) {
            rolId = 1;  // ID de Administrador
        } else if (rolSeleccionado.equalsIgnoreCase("Empleado")) {
            rolId = 2;  // ID de Empleado
        } else {
            mostrarAlerta("Error", "Rol inválido", "Debes seleccionar un rol válido.");
            return;
        }

        // Conectar a la base de datos y agregar el nuevo usuario
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            String insertQuery = "INSERT INTO Usuarios (us_nombre, us_apellido, us_usuario, us_contraseña, rol_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, usuario);
                stmt.setString(4, contraseña);
                stmt.setInt(5, rolId);  // Usar el rol ID

                stmt.executeUpdate();
                mostrarAlerta("Éxito", "Usuario Agregado", "El usuario ha sido agregado exitosamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al agregar usuario", "Hubo un error al agregar el usuario.");
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Volver al panel de usuarios
        cambiarEscena(event, "/com/example/tgt_proyecto/usuarios/usuarios.fxml", "Usuarios TGT | EQUIPMENTS");
    }




    // Método para cerrar sesión y regresar a la ventana de login, limpiando los campos
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
    }

    // Método auxiliar para mostrar una alerta
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    // Mostrar alerta de permiso denegado
    private void mostrarAlertaPermisoDenegado() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Permiso denegado");
        alerta.setHeaderText("No tienes permisos suficientes para acceder a esta sección.");
        alerta.setContentText("Por favor, contacta al administrador.");
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

    // Métodos de navegación a las diferentes secciones
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
}
