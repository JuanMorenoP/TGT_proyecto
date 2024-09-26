module com.example.tgt_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.tgt_proyecto to javafx.fxml;
    exports com.example.tgt_proyecto;
}
