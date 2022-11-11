module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires opencsv;


    opens client to javafx.fxml;
    exports client;
    exports server;
}

