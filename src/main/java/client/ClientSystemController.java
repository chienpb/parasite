package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;


public class ClientSystemController {
    public ClientController clientController;

    @FXML
    private TextField txtTimer;

    @FXML
    private TextArea txtSpecs;

    public void clickedShutdown() throws IOException
    {
        clientController.shutdown(txtTimer.getText());
    }

    public void clickedLogout() throws IOException
    {
        clientController.logout(txtTimer.getText());
    }

    public void clickedRestart() throws IOException
    {
        clientController.restart(txtTimer.getText());
    }

    public void setSpecs() throws IOException {
        StringBuilder specs = new StringBuilder();
        String line = clientController.br.readLine();
        while (!Objects.equals(line, "stop"))
        {
            specs.append(line).append(System.lineSeparator());
            line = clientController.br.readLine();
        }
        txtSpecs.setText(specs.toString());
    }

}
