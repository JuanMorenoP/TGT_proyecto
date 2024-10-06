package com.example.tgt_proyecto;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ClientesController {

    @FXML
    private Button cerrarSesionButton;

    @FXML
    private TableView<Cliente> tableView;

    @FXML
    private TableColumn<Cliente, Integer> numeroColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;

    @FXML
    private ObservableList<Cliente> clientesData = FXCollections.observableArrayList();

    // Método para navegar a la vista de "Añadir Nuevo Cliente"
    @FXML
    private void handleAgregar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/nuevo-cliente.fxml", "Añadir Nuevo Cliente");
    }

    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/clientes.fxml", "Clientes TGT | EQUIPMENTS");
    }

    @FXML
    public void initialize() {
        // Configurar el botón de cerrar sesión para volver al login
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
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
                String query = "SELECT cli_nombre, cli_direccion, cli_email, cli_telefono, cli_documento FROM Clientes";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String nombre = rs.getString("cli_nombre");
                    String direccion = rs.getString("cli_direccion");
                    String email = rs.getString("cli_email");
                    String telefono = rs.getString("cli_telefono");
                    String documento = rs.getString("cli_documento");

                    clientesData.add(new Cliente(nombre, direccion, email, telefono, documento));
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
    // Método para eliminar el cliente seleccionado
    @FXML
    private void handleEliminar(ActionEvent event) {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Mostrar un cuadro de diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar al cliente?");
            alert.setContentText("Cliente: " + clienteSeleccionado.getNombre());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar cliente y mostrar alerta de éxito
                eliminarClienteDeBaseDeDatos(clienteSeleccionado);
                clientesData.remove(clienteSeleccionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado", "El cliente ha sido eliminado exitosamente.");
            }

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar", "No se ha seleccionado ningún cliente.");

        }
    }

    // Método para eliminar el cliente de la base de datos
    private void eliminarClienteDeBaseDeDatos(Cliente cliente) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                String deleteQuery = "DELETE FROM Clientes WHERE cli_documento = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, cliente.getDocumento());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar cliente", "Hubo un error al eliminar el cliente.", "");
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Método para editar el cliente seleccionado
    @FXML
    private void handleEditar(ActionEvent event) throws IOException {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Cargar el archivo FXML de modificar cliente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/modificar-cliente.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de modificación
            ModificarClienteController modificarClienteController = loader.getController();

            // Cargar los datos del cliente seleccionado en el controlador de modificación
            modificarClienteController.cargarDatosCliente(clienteSeleccionado);

            // Cambiar a la escena de modificación
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene modificarClienteScene = new Scene(root);

            // Asegurarse de que los estilos se carguen
            modificarClienteScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

            // Mostrar la nueva escena
            stage.setScene(modificarClienteScene);
            stage.setTitle("Modificar Cliente TGT | EQUIPMENTS");
            stage.show();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede editar", "No se ha seleccionado ningún cliente.");

        }
    }



    // Método para mostrar una alerta con diferentes tipos
    private void mostrarAlerta(Alert.AlertType tipoAlerta, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipoAlerta);  // Ahora acepta el tipo de alerta
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


    // Método para cerrar sesión y regresar a la ventana de login, limpiando los campos
    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm()); // Asegurando que los estilos se carguen

        LoginController loginController = fxmlLoader.getController();
        loginController.limpiarCampos();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
        stage.show();
    }
    @FXML
    private void handleDetalle(ActionEvent event) throws IOException {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Cargar el archivo FXML de detalles del cliente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/detalles-cliente.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de detalles
            DetallesClienteController detallesClienteController = loader.getController();

            // Cargar los datos del cliente seleccionado
            detallesClienteController.cargarDatosCliente(clienteSeleccionado);

            // Cambiar a la escena de detalles
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene detallesClienteScene = new Scene(root);

            // Asegurarse de que los estilos se carguen
            detallesClienteScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

            // Mostrar la nueva escena
            stage.setScene(detallesClienteScene);
            stage.setTitle("Perfil Cliente TGT | EQUIPMENTS");
            stage.show();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pueden ver los detalles", "No se ha seleccionado ningún cliente.");
        }
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

    // Método auxiliar para cambiar escenas
    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(fxmlLoader.load());
        newScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm()); // Asegurando que los estilos se carguen

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.setTitle(titulo);
        stage.show();
    }
}
