module app.harikarthik.nutrifusion {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires java.sql;
    requires org.json;
    requires javax.mail.api;
    requires mysql.connector.j;
    requires jdk.httpserver;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires jdk.compiler;

    opens app.harikarthik.nutrifusion to javafx.fxml;
    exports app.harikarthik.nutrifusion.login;
    opens app.harikarthik.nutrifusion.login to javafx.fxml;
    exports app.harikarthik.nutrifusion.dashboard;
    opens app.harikarthik.nutrifusion.dashboard to javafx.fxml;
    exports app.harikarthik.nutrifusion.utils;
    opens app.harikarthik.nutrifusion.utils to javafx.fxml;
    exports app.harikarthik.nutrifusion;
}