package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientSystemController {
    public ClientController clientController;

    public void clickedShutdown() throws IOException
    {
        clientController.clickedShutdown();
    }

    public void clickedLogout() throws IOException
    {
        clientController.clickedLogout();
    }
}
