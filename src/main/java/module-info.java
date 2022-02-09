module com.example.OnTrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.ontrack to javafx.fxml;
    exports com.example.ontrack;
    exports com.example.ontrack.authentication;
    opens com.example.ontrack.main to javafx.fxml;
    opens com.example.ontrack.authentication to javafx.fxml;
    opens com.example.ontrack.overview.calendar to javafx.fxml;
    opens com.example.ontrack.task.info to javafx.fxml;
    opens com.example.ontrack.repetition to javafx.fxml, javafx.base;
    opens com.example.ontrack.task to javafx.base, javafx.fxml;
    opens com.example.ontrack.task.form.add to javafx.fxml;
}