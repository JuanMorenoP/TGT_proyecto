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
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPasswordField;

    @FXML
    private ImageView togglePasswordVisibility;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel; // Para mostrar el mensaje de error

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

        // Configurar el botón de inicio de sesión para verificar credenciales
        loginButton.setOnAction(event -> {
            try {
                verificarCredenciales(event);  // Acción para abrir el dashboard o mostrar el error
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Método para verificar las credenciales
    private void verificarCredenciales(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verificar si los campos están vacíos
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Los campos no pueden estar vacíos.");
            return;
        }

        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            String query = "SELECT * FROM Usuarios WHERE us_usuario = ? AND us_contraseña = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Usuario encontrado, cambiar a la escena del dashboard
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/dashboard-view.fxml"));
                    Scene dashboardScene = new Scene(fxmlLoader.load());
                    dashboardScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());
                    // Cambiar la escena actual al dashboard
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(dashboardScene);
                    stage.setTitle("Dashboard TGT | EQUIPMENTS");
                    stage.show();
                } else {
                    // Usuario no encontrado
                    errorLabel.setText("Usuario o contraseña incorrectos. Inténtalo de nuevo.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorLabel.setText("Error al verificar credenciales.");
            }
        } else {
            errorLabel.setText("Error en la conexión a la base de datos.");
        }
    }

    // Método para limpiar los campos de texto
    public void limpiarCampos() {
        usernameField.clear();
        passwordField.clear();
        showPasswordField.clear();
        errorLabel.setText(""); // Limpiar cualquier mensaje de error
    }
}
