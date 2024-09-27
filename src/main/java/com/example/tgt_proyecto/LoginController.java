package com.example.tgt_proyecto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPasswordField;

    @FXML
    private ImageView togglePasswordVisibility;

    @FXML
    private Button loginButton; // Vinculación correcta del botón

    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        // Al inicio, el campo de mostrar la contraseña está oculto
        showPasswordField.setVisible(false);

        // Ruta de las imágenes del ojo (asegúrate de que las rutas sean correctas)
        Image eyeClosedImage = new Image(getClass().getResource("/com/example/tgt_proyecto/icons/eye-closed.png").toExternalForm());
        Image eyeOpenImage = new Image(getClass().getResource("/com/example/tgt_proyecto/icons/eye-open.png").toExternalForm());

        // Establece la imagen del ojo cerrado al inicio
        togglePasswordVisibility.setImage(eyeClosedImage);

        // Alternar entre mostrar y ocultar la contraseña
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

        // Configurar el botón de inicio de sesión para abrir el dashboard
        loginButton.setOnAction(event -> {
            try {
                loginAction(event);  // Acción para abrir el dashboard
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Método para abrir el dashboard
    private void loginAction(ActionEvent event) throws IOException {
        // Cargar el archivo FXML del dashboard
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/dashboard-view.fxml"));
        Scene dashboardScene = new Scene(fxmlLoader.load());

        // Añadir la hoja de estilos al dashboard
        dashboardScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        // Obtener la ventana actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambiar la escena a la del dashboard
        stage.setScene(dashboardScene);
        stage.setTitle("Dashboard TGT | EQUIPMENTS");
        stage.show();
    }
}
