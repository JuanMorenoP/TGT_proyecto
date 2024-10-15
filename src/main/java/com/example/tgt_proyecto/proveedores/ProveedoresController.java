package com.example.tgt_proyecto.proveedores;

import com.example.tgt_proyecto.database.DatabaseConnection;
import com.example.tgt_proyecto.session.SessionManager;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ProveedoresController {


    @FXML
    private Button cerrarSesionButton;

    @FXML
    private TableView<Proveedor> proveedoresTable;

    @FXML
    private TableColumn<Proveedor, Integer> numeroColumn;

    @FXML
    private TableColumn<Proveedor, String> nombreColumn;

    private ObservableList<Proveedor> proveedoresData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cerrarSesionButton.setOnAction(event -> {
            try {
                cerrarSesionAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Configurar las columnas de la tabla
        numeroColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proveedor, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Proveedor, Integer> param) {
                return new javafx.beans.property.SimpleIntegerProperty(proveedoresTable.getItems().indexOf(param.getValue()) + 1).asObject();
            }
        });

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Cargar datos en la tabla
        cargarProveedores();
    }

    private void cargarProveedores() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                // Consulta SQL para obtener los datos de los proveedores
                String query = "SELECT proveedor_nombre, proveedor_contacto, proveedor_direccion, proveedor_metodo_pago, proveedor_nit_rut FROM Proveedores";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // Limpiar la lista de proveedores antes de agregar nuevos elementos
                proveedoresData.clear();

                // Iterar sobre los resultados de la consulta
                while (rs.next()) {
                    String nombre = rs.getString("proveedor_nombre");
                    String contacto = rs.getString("proveedor_contacto");
                    String direccion = rs.getString("proveedor_direccion");
                    String metodoPago = rs.getString("proveedor_metodo_pago");
                    String nitRut = rs.getString("proveedor_nit_rut");

                    // Agregar el proveedor a la lista, sin el ID ya que no se incluye en la consulta
                    proveedoresData.add(new Proveedor(0, nombre, contacto, direccion, metodoPago, nitRut));
                }

                // Asignar los datos a la tabla
                proveedoresTable.setItems(proveedoresData);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close(); // Cerrar la conexión a la base de datos
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleAgregar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/example/tgt_proyecto/proveedores/nuevo-proveedor.fxml", "Añadir Nuevo Proveedor");
    }

    @FXML
    private void handleEditar(ActionEvent event) throws IOException {
        Proveedor proveedorSeleccionado = proveedoresTable.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/proveedores/modificar-proveedor.fxml"));
            Parent root = loader.load();

            ModificarProveedorController modificarController = loader.getController();
            modificarController.cargarDatosProveedor(proveedorSeleccionado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene modificarProveedorScene = new Scene(root);
            modificarProveedorScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

            stage.setScene(modificarProveedorScene);
            stage.setTitle("Modificar Proveedor TGT | EQUIPMENTS");
            stage.show();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede editar", "No se ha seleccionado ningún proveedor.");
        }
    }

    @FXML
    private void handleDetalle(ActionEvent event) throws IOException {
        Proveedor proveedorSeleccionado = proveedoresTable.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            // Cargar la vista de detalles del proveedor
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/proveedores/detalles-proveedor.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de detalles
            DetallesProveedorController detallesProveedorController = loader.getController();

            // Pasar los datos del proveedor seleccionado al controlador de detalles
            detallesProveedorController.cargarDatosProveedor(proveedorSeleccionado);

            // Cambiar la escena para mostrar los detalles del proveedor
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Detalles del Proveedor");
            stage.show();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "No se seleccionó ningún proveedor", "Debes seleccionar un proveedor para ver los detalles.");
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Proveedor proveedorSeleccionado = proveedoresTable.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar al proveedor?");
            alert.setContentText("Proveedor: " + proveedorSeleccionado.getNombre());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                eliminarProveedorDeBaseDeDatos(proveedorSeleccionado);
                proveedoresData.remove(proveedorSeleccionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Proveedor eliminado", "El proveedor ha sido eliminado exitosamente.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar", "No se ha seleccionado ningún proveedor.");
        }
    }

    private void eliminarProveedorDeBaseDeDatos(Proveedor proveedor) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                String deleteQuery = "DELETE FROM Proveedores WHERE proveedor_nit_rut = ?";
                PreparedStatement stmt = connection.prepareStatement(deleteQuery);
                stmt.setString(1, proveedor.getNitRut());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al eliminar proveedor", "Hubo un error al eliminar el proveedor.");
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Métodos de navegación a las diferentes secciones
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

    private void cerrarSesionAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgt_proyecto/login/login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        loginScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TGT | EQUIPMENTS - Login");
    }

    private void cambiarEscenaSiAdministrador(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        if (!SessionManager.esAdministrador()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Permiso denegado", "No tienes permisos suficientes para acceder a esta sección.", "Contacta al administrador.");
            return;
        }
        cambiarEscena(event, fxmlPath, titulo);
    }

    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(fxmlLoader.load());
        newScene.getStylesheets().add(getClass().getResource("/com/example/tgt_proyecto/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.setTitle(titulo);
    }

    private void mostrarAlerta(Alert.AlertType tipoAlerta, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
