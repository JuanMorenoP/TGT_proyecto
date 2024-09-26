package com.example.tgt_proyecto;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.layout.Pane;

public class DashboardController {

    @FXML
    private StackPane contentArea; // Esto se debe referenciar en el archivo FXML con fx:id="contentArea"

    private void loadView(String fxmlFile) {
        try {
            Pane newView = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleInicio() {
        loadView("/com/example/tgt_proyecto/inicio-view.fxml");
    }

    @FXML
    private void handleInventario() {
        loadView("/com/example/tgt_proyecto/inventario-view.fxml");
    }

    @FXML
    private void handleVentas() {
        loadView("/com/example/tgt_proyecto/ventas-view.fxml");
    }

    @FXML
    private void handleUsuarios() {
        loadView("/com/example/tgt_proyecto/usuarios-view.fxml");
    }

    @FXML
    private void handleMantenimientos() {
        loadView("/com/example/tgt_proyecto/mantenimientos-view.fxml");
    }

}
