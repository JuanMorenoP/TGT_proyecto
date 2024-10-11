package com.example.tgt_proyecto.usuarios;

import com.example.tgt_proyecto.database.DatabaseConnection;
import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ModificarUsuarioController {

    @FXML
    private Button cerrarSesionButton;

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

    @FXML
    private ComboBox<String> rolComboBox;  // Cambiado de TextField a ComboBox para el rol

    @FXML
    private Button modificarUsuarioButton;
    @FXML
    private ImageView togglePasswordVisibility;
    @FXML
    private Button cancelarButton;


    private int usuarioId;
    private Usuario usuarioSeleccionado;
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

        // Funcionalidad para mostrar/ocultar la contraseña
        togglePasswordVisibility.setOnMouseClicked(event -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                showPasswordField.setText(passwordField.getText());
                showPasswordField.setVisible(true);
                passwordField.setVisible(false);
                togglePasswordVisibility.setImage(eyeOpenImage);
            } else {
                passwordField.setText(showPasswordField.getText());
                passwordField.setVisible(true);
                showPasswordField.setVisible(false);
                togglePasswordVisibility.setImage(eyeClosedImage);
            }
        });

        // Acción para cerrar sesión
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    // Cargar datos del usuario seleccionado
    public void cargarDatosUsuario(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
        this.usuarioId = usuario.getId();
        nombreField.setText(usuario.getNombre());
        apellidoField.setText(usuario.getApellido());
        usuarioField.setText(usuario.getUsuario());
        passwordField.setText(usuario.getContraseña());

        // Seleccionar el rol en el ComboBox
        if (usuario.getRol().equalsIgnoreCase("Administrador")) {
            rolComboBox.setValue("Administrador");
        } else {
            rolComboBox.setValue("Empleado");
        }
    }
    @FXML
    private void handleGuardarUsuario(ActionEvent event) {
        // Verificar si hay un usuario seleccionado
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el usuario. No hay usuario seleccionado.");
            return;
        }

        // Obtener los valores actualizados de los campos de texto
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String usuario = usuarioField.getText();

        // Aquí verificamos si el campo de texto visible está activo (contraseña visible)
        String contraseña = isPasswordVisible ? showPasswordField.getText() : passwordField.getText();

        String rolSeleccionado = rolComboBox.getValue();  // Obtener el rol seleccionado del ComboBox

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta("Error", "Campos Vacíos", "Todos los campos deben estar llenos.");
            return;
        }

        // Convertir el rol a un número según la lógica de tu sistema
        int rolId;
        if (rolSeleccionado.equalsIgnoreCase("Administrador")) {
            rolId = 1;  // El ID de Administrador en tu base de datos
        } else if (rolSeleccionado.equalsIgnoreCase("Empleado")) {
            rolId = 2;  // El ID de Empleado en tu base de datos
        } else {
            mostrarAlerta("Error", "Rol inválido", "Debes seleccionar un rol válido (Administrador o Empleado).");
            return;  // Terminar si el rol no es válido
        }

        // Conexión a la base de datos
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                // Verificar si el nombre de usuario ya existe para otro usuario (evitar duplicados)
                String checkQuery = "SELECT COUNT(*) FROM Usuarios WHERE us_usuario = ? AND us_id != ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, usuario);
                checkStatement.setInt(2, usuarioSeleccionado.getId());

                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    mostrarAlerta("Error", "Modificación fallida", "El nombre de usuario ya existe para otro usuario.");
                    return;  // Terminar si el nombre de usuario ya está en uso
                }

                // Si no hay duplicados, actualizar los datos del usuario
                String updateQuery = "UPDATE Usuarios SET us_nombre = ?, us_apellido = ?, us_usuario = ?, us_contraseña = ?, rol_id = ? WHERE us_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                // Establecer los valores en la consulta
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, usuario);
                preparedStatement.setString(4, contraseña);  // Usar la contraseña correcta
                preparedStatement.setInt(5, rolId);  // Aquí se utiliza el rol ID en lugar del String
                preparedStatement.setInt(6, usuarioSeleccionado.getId());  // ID del usuario a modificar

                int filasActualizadas = preparedStatement.executeUpdate();
                if (filasActualizadas > 0) {
                    mostrarAlerta("Éxito", "Usuario modificado exitosamente", "El usuario ha sido modificado correctamente.");
                    handleCancelar(event);  // Regresar a la pantalla de usuarios
                } else {
                    mostrarAlerta("Error", "Modificación fallida", "No se pudo modificar el usuario.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Modificación fallida", "Hubo un error al modificar el usuario.");
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


    // Cancelar y regresar a la pantalla de usuarios
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/usuarios/usuarios.fxml", "Usuarios TGT | EQUIPMENTS");
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

    // Cambiar escena solo si el usuario es administrador
    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!esAdministrador()) {
            mostrarAlertaPermisoDenegado();
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
    }



    // Método para manejar la acción de modificar un usuario
    @FXML
    private void handleModificarUsuario(ActionEvent event) throws IOException, SQLException {
        // Obtener los valores de los campos
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String usuario = usuarioField.getText();
        String contraseña = passwordField.getText();
        String rolSeleccionado = rolComboBox.getValue();  // Obtener el rol seleccionado del ComboBox

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta("Error", "Campos Vacíos", "Todos los campos deben estar llenos.");
            return;
        }

        // Convertir el rol a un número según la lógica de tu sistema
        int rolId;
        if (rolSeleccionado.equalsIgnoreCase("Administrador")) {
            rolId = 1;  // El ID de Administrador en tu base de datos
        } else if (rolSeleccionado.equalsIgnoreCase("Empleado")) {
            rolId = 2;  // El ID de Empleado en tu base de datos
        } else {
            mostrarAlerta("Error", "Rol inválido", "Debes seleccionar un rol válido.");
            return;
        }

        // Conectar a la base de datos y modificar el usuario
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            String updateQuery = "UPDATE Usuarios SET us_nombre = ?, us_apellido = ?, us_usuario = ?, us_contraseña = ?, rol_id = ? WHERE us_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, usuario);
                stmt.setString(4, contraseña);
                stmt.setInt(5, rolId);  // Usar el ID del rol convertido
                stmt.setInt(6, usuarioSeleccionado.getId());

                stmt.executeUpdate();
                mostrarAlerta("Éxito", "Usuario Modificado", "El usuario ha sido modificado exitosamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al modificar usuario", "Hubo un error al modificar el usuario.");
                return;
            } finally {
                connection.close();
            }
        }

        // Regresar al apartado de usuarios después de modificar el usuario
        cambiarEscena(event, "/com/example/tgt_proyecto/usuarios/usuarios.fxml", "Usuarios TGT | EQUIPMENTS");
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

    // Método auxiliar para mostrar una alerta
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
