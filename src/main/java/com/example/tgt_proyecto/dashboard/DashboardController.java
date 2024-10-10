package com.example.tgt_proyecto.dashboard;

import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;  // Importar SessionManager
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class DashboardController {

    @FXML
    private TableView<?> tableView;

    @FXML
    private ImageView userIcon;

    @FXML
    private Button cerrarSesionButton;

    @FXML
    private Button inicioButton;

    @FXML
    public void initialize() {
        // Inicialización de los elementos del dashboard, como cargar íconos
        userIcon.setImage(new Image(getClass().getResourceAsStream("/com/example/tgt_proyecto/icons/user.png")));

        // Configurar el botón de cerrar sesión para volver al login
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);  // Acción para volver al login
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Aplicar el estilo de la clase "nav-button-active" al botón de Inicio
        inicioButton.getStyleClass().add("nav-button-active");
    }

    // Método para cerrar sesión y regresar a la ventana de login
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        // Cargar el archivo FXML del login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        // Añadir la hoja de estilos al login
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        // Obtener el controlador del login y limpiar los campos
        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();  // Limpiar los campos del login

        // Obtener la ventana actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambiar la escena a la del login
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }

    // Verificar si el usuario es administrador
    private boolean esAdministrador() {
        return SessionManager.esAdministrador();  // Utilizar SessionManager para verificar si es administrador
    }

    // Método para cambiar de escena solo si es administrador
    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!esAdministrador()) {
            mostrarAlertaPermisoDenegado();
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
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

    // Mostrar alerta de permiso denegado
    private void mostrarAlertaPermisoDenegado() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Permiso denegado");
        alerta.setHeaderText("No tienes permisos suficientes para acceder a esta sección.");
        alerta.setContentText("Por favor, contacta al administrador.");
        alerta.showAndWait();
    }
}
