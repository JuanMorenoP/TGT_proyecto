package com.example.tgt_proyecto;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class ClientesController {

    @FXML
    private Button cerrarSesionButton;  // Enlaza el botón de "Cerrar Sesión"

    @FXML
    private TableView<Cliente> tableView;

    @FXML
    private TableColumn<Cliente, Integer> numeroColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;
    @FXML
    private ObservableList<Cliente> clientesData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Configurar el botón de cerrar sesión para volver al login
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);  // Acción para volver al login
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Configurar las columnas de la tabla
        numeroColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente, Integer>, ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Cliente, Integer> param) {
                return new javafx.beans.property.SimpleIntegerProperty(tableView.getItems().indexOf(param.getValue()) + 1).asObject();
            }
        });

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Cargar datos en la tabla
        cargarClientes();
    }

    private void cargarClientes() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                String query = "SELECT cli_nombre FROM Clientes";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String nombre = rs.getString("cli_nombre");
                    clientesData.add(new Cliente(nombre));  // Añadir los datos a la lista
                }

                tableView.setItems(clientesData);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Cliente {
        private String nombre;

        public Cliente(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    // Método para cerrar sesión y regresar a la ventana de login, limpiando los campos
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        // Cargar el archivo FXML del login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        // Añadir la hoja de estilos al login
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        // Obtener el controlador del login para limpiar los campos
        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();  // Limpiar los campos de texto y mensajes

        // Obtener la ventana actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambiar la escena a la del login
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }

    // Métodos de navegación a las diferentes secciones
    @FXML
    private void handleInicio(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/dashboard-view.fxml", "Dashboard TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMaquinaria(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/maquinaria.fxml", "Maquinaria TGT | EQUIPMENTS");
    }

    @FXML
    private void handleEmpleados(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/empleados.fxml", "Empleados TGT | EQUIPMENTS");
    }

    @FXML
    private void handleClientes(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    @FXML
    private void handleProveedores(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/proveedores.fxml", "Proveedores TGT | EQUIPMENTS");
    }

    @FXML
    private void handleMantenimiento(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/mantenimiento.fxml", "Mantenimiento TGT | EQUIPMENTS");
    }

    @FXML
    private void handleInventario(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/inventario.fxml", "Inventario TGT | EQUIPMENTS");
    }

    @FXML
    private void handleCompras(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/compras.fxml", "Compras TGT | EQUIPMENTS");
    }

    @FXML
    private void handleVentas(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/ventas.fxml", "Ventas TGT | EQUIPMENTS");
    }

    @FXML
    private void handleConfiguracion(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/configuracion.fxml", "Configuración TGT | EQUIPMENTS");
    }

    // Método para navegar a la vista de "Añadir Nuevo Cliente"
    @FXML
    private void handleAgregar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/nuevo-cliente.fxml", "Añadir Nuevo Cliente");
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
}
