module com.example.parasite {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.parasite to javafx.fxml;
    exports com.example.parasite;
}