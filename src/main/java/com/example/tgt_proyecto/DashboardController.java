package com.example.tgt_proyecto;

import javafx.fxml.FXML;
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
    private Button cerrarSesionButton;  // Enlaza el botón de "Cerrar Sesión"

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
    }

    // Método para cerrar sesión y regresar a la ventana de login
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        // Cargar el archivo FXML del login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        // Añadir la hoja de estilos al login
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        // Obtener la ventana actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambiar la escena a la del login
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }
}
