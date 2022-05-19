module com.example.crazyputtin {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
	requires exp4j;

	opens graphics to javafx.fxml;
    exports graphics;
    exports Main;
    opens Main to javafx.fxml;
    exports bot;
    opens bot to javafx.fxml;
    exports tests;
    opens tests to javafx.fxml;
}