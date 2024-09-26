package com.example.tgt_proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
//HOLAAAAAAAAAAAAA ACTU
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load(), 1007, 641);

            stage.setTitle("TGT | EQUIPMENTS");
            stage.setMaximized(true);
            stage.setResizable(true);

            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/tgt_proyecto/icons/logo.ico")));

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
