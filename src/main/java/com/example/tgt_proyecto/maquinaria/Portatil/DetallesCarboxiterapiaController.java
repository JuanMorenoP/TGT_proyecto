package com.example.tgt_proyecto.maquinaria.Portatil;

import com.example.tgt_proyecto.login.LoginController;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DetallesCarboxiterapiaController {

    @FXML
    private Button cerrarSesionButton;

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

    // Método para cerrar sesión
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }

    // Método para volver a la vista inicial de Profesional
    @FXML
    private void volverAProfesional(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/maquinaria/Profesional/Profesional.fxml", "Profesional TGT | EQUIPMENTS");
    }

    // Métodos de navegación
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

    // Método para mostrar alertas
    private void mostrarAlerta(Alert.AlertType tipoAlerta, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
