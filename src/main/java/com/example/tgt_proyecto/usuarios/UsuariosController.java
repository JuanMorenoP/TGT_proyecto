package com.example.tgt_proyecto.usuarios;

import com.example.tgt_proyecto.database.DatabaseConnection;
import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuariosController {

    @FXML
    private Button cerrarSesionButton;
    @FXML
    private TableView<Usuario> usuariosTable;

    @FXML
    private TableColumn<Usuario, Integer> idColumn;

    @FXML
    private TableColumn<Usuario, String> nombreColumn;

    @FXML
    private TableColumn<Usuario, String> apellidoColumn;

    @FXML
    private TableColumn<Usuario, String> usuarioColumn;

    @FXML
    private TableColumn<Usuario, String> rolColumn;

    @FXML
    private ObservableList<Usuario> usuariosData = FXCollections.observableArrayList();

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

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            // Incluye el campo us_contraseña en la consulta SQL
            String query = "SELECT us_id, us_nombre, us_apellido, us_usuario, us_contraseña, rol_id FROM Usuarios";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                // Limpiar los datos antes de cargarlos
                usuariosData.clear();

                while (rs.next()) {
                    int id = rs.getInt("us_id");
                    String nombre = rs.getString("us_nombre");
                    String apellido = rs.getString("us_apellido");
                    String usuario = rs.getString("us_usuario");
                    String contraseña = rs.getString("us_contraseña"); // Cargar la contraseña
                    String rol = obtenerNombreRol(rs.getInt("rol_id"));

                    // Crea un objeto Usuario con la contraseña incluida
                    Usuario usuarioObj = new Usuario(id, nombre, apellido, usuario, contraseña, rol);
                    usuariosData.add(usuarioObj);
                }
                usuariosTable.setItems(usuariosData);
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al cargar usuarios", "Hubo un error al cargar los usuarios.");
            }
        }
    }


    private String obtenerNombreRol(int rolId) {
        switch (rolId) {
            case 1:
                return "Administrador";
            case 2:
                return "Empleado";
            default:
                return "Desconocido";
        }
    }

    @FXML
    private void handleEliminarUsuario() {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar al usuario?");
            confirmacion.setContentText("Usuario: " + usuarioSeleccionado.getNombre());



            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                DatabaseConnection dbConnection = new DatabaseConnection();
                Connection connection = dbConnection.connect();

                if (connection != null) {
                    try {
                        // Eliminar solo el usuario de la tabla "Usuarios"
                        String deleteQuery = "DELETE FROM Usuarios WHERE us_id = ?";
                        PreparedStatement stmt = connection.prepareStatement(deleteQuery);
                        stmt.setInt(1, usuarioSeleccionado.getId());
                        stmt.executeUpdate();

                        // Eliminar el usuario de la lista de usuarios en la tabla visual
                        usuariosData.remove(usuarioSeleccionado);

                        // Mostrar confirmación de éxito
                        mostrarAlerta("Éxito", "Usuario eliminado", "El usuario ha sido eliminado exitosamente.");

                    } catch (SQLException e) {
                        e.printStackTrace();
                        mostrarAlerta("Error", "Error al eliminar usuario", "Hubo un error al eliminar el usuario.");
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            mostrarAlerta("Advertencia", "No se seleccionó ningún usuario", "Debes seleccionar un usuario para eliminar.");
        }
    }


    private void eliminarEmpleado(int usuarioId) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();
        if (connection != null) {
            String deleteEmpleadoQuery = "DELETE FROM Empleados WHERE us_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteEmpleadoQuery)) {
                stmt.setInt(1, usuarioId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al eliminar empleado asociado", "Hubo un error al eliminar el empleado.");
            }
        }
    }

    @FXML
    private void handleAgregarUsuario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/usuarios/nuevo-usuario.fxml"));
        Scene nuevoUsuarioScene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        nuevoUsuarioScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());
        stage.setScene(nuevoUsuarioScene);
        stage.setTitle("Nuevo Usuario TGT | EQUIPMENTS");
    }

    @FXML
    private void handleEditarUsuario(ActionEvent event) throws IOException {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/usuarios/modificar-usuario.fxml"));
            Parent root = loader.load();

            ModificarUsuarioController modificarController = loader.getController();
            modificarController.cargarDatosUsuario(usuarioSeleccionado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene modificarUsuarioScene = new Scene(root);

            modificarUsuarioScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());
            stage.setScene(modificarUsuarioScene);
            stage.setTitle("Modificar Usuario TGT | EQUIPMENTS");
            stage.show();
        } else {
            mostrarAlerta("Advertencia", "No se seleccionó ningún usuario", "Debes seleccionar un usuario para editar.");
        }
    }


    @FXML
    private void handleDetallesUsuario(ActionEvent event) throws IOException {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/usuarios/detalles-usuario.fxml"));
            Scene detallesUsuarioScene = new Scene(loader.load());
            DetallesUsuarioController detallesController = loader.getController();
            detallesController.cargarDatosUsuario(usuarioSeleccionado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            detallesUsuarioScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());
            stage.setScene(detallesUsuarioScene);
            stage.setTitle("Detalles del Usuario TGT | EQUIPMENTS");
        } else {
            mostrarAlerta("Advertencia", "No se seleccionó ningún usuario", "Debes seleccionar un usuario para ver los detalles.");
        }
    }


    // Métodos de navegación entre las diferentes secciones
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

    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().
                getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
    }

    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!SessionManager.esAdministrador()) {
            mostrarAlerta("Permiso denegado", "No tienes permisos suficientes para acceder a esta sección.", "Contacta al administrador.");
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
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
}
