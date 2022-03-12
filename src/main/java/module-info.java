module com.example.crazyputtin {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.crazyputtin to javafx.fxml;
    exports com.example.crazyputtin;
}