package com.example.tgt_proyecto;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPasswordField;

    @FXML
    private ImageView togglePasswordVisibility;

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
    }
}
