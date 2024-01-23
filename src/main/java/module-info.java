module com.wieik.amberbronze {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires ormlite.jdbc;
    requires java.sql;
    requires org.json.chargebee;

    opens com.wieik.amberbronze to javafx.fxml, ormlite.jdbc;
    exports com.wieik.amberbronze;
    exports com.wieik.amberbronze.controller;
    opens com.wieik.amberbronze.controller to javafx.fxml, ormlite.jdbc;
    exports com.wieik.amberbronze.entities;
    opens com.wieik.amberbronze.entities to javafx.fxml, ormlite.jdbc;
    exports com.wieik.amberbronze.logic;
    opens com.wieik.amberbronze.logic to javafx.fxml, ormlite.jdbc;
    exports com.wieik.amberbronze.logic.transfers;
    opens com.wieik.amberbronze.logic.transfers to javafx.fxml, ormlite.jdbc;
    exports com.wieik.amberbronze.controller.dialog;
    opens com.wieik.amberbronze.controller.dialog to javafx.fxml, ormlite.jdbc;
}