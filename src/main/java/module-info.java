module com.example.cookmasterdesktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.pdfbox;
    requires com.google.gson;

    opens com.example.cookmasterdesktop to javafx.fxml;
    exports com.example.cookmasterdesktop;
}