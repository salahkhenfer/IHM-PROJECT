module com.example.demoihm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    opens com.example.demoihm to javafx.fxml;
    exports com.example.demoihm;
}