module com.example.tgt_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    // Exportar los paquetes principales y abrirlos para FXML
    opens com.example.tgt_proyecto to javafx.fxml;
    exports com.example.tgt_proyecto;

    // Exportar y abrir paquetes relacionados con la funcionalidad del login
    exports com.example.tgt_proyecto.login;
    opens com.example.tgt_proyecto.login to javafx.fxml;

    // Exportar y abrir el paquete de clientes
    exports com.example.tgt_proyecto.clientes;
    opens com.example.tgt_proyecto.clientes to javafx.fxml;

    // Exportar y abrir el paquete de dashboard
    exports com.example.tgt_proyecto.dashboard;
    opens com.example.tgt_proyecto.dashboard to javafx.fxml;

    // Exportar y abrir el paquete de la base de datos
    exports com.example.tgt_proyecto.database;
    opens com.example.tgt_proyecto.database to javafx.fxml;

    // Abrir el paquete de proveedores para FXML y base
    opens com.example.tgt_proyecto.proveedores to javafx.fxml, javafx.base;

    // Abrir el paquete de la sesión para FXML (no exportar si no es necesario)
    opens com.example.tgt_proyecto.session to javafx.fxml;

    // Exportar y abrir el paquete de usuarios
    exports com.example.tgt_proyecto.usuarios;
    opens com.example.tgt_proyecto.usuarios to javafx.fxml;

    // Abrir y exportar el paquete de maquinaria para FXML
    exports com.example.tgt_proyecto.maquinaria;
    opens com.example.tgt_proyecto.maquinaria to javafx.fxml;

    // Abrir los paquetes restantes para FXML
    opens com.example.tgt_proyecto.mantenimiento to javafx.fxml;
    opens com.example.tgt_proyecto.inventario to javafx.fxml;
    opens com.example.tgt_proyecto.compras to javafx.fxml;
    opens com.example.tgt_proyecto.ventas to javafx.fxml;
}
