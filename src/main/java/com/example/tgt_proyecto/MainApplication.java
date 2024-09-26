package com.example.tgt_proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.net.URL;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Cargar el archivo FXML del login
            URL fxmlLocation = getClass().getResource("/com/example/tgt_proyecto/login-view.fxml");
            if (fxmlLocation == null) {
                System.out.println("No se pudo encontrar el archivo login-view.fxml");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("TGT | EQUIPMENTS");

            // Obtener la resolución de pantalla actual
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Ajustar el tamaño de la ventana a la resolución de pantalla
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setResizable(true);

            // Establecer el icono del stage
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/tgt_proyecto/icons/logo.ico")));

            // Mostrar la escena
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
