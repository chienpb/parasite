module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires opencsv;
    requires com.github.kwhat.jnativehook;


    opens client to javafx.fxml;
    exports client;
    exports server;
}

