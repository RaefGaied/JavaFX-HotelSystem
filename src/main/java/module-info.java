module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;
    requires annotations;
    requires static lombok;


    opens org.example.demo.controller to javafx.fxml;
    // Export the controller package
    exports org.example.demo.model;      // Export other necessary packages if needed
    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}