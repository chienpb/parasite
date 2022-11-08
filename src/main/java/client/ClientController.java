package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.net.Socket;

public class ClientController {

    public TextField textBox1;

    private Stage stage;
    private Scene scene;

    private Parent root;
    public void connect(ActionEvent event) throws IOException
    {
        String ip = textBox1.getText();
        Socket s = new Socket(ip, 3333);
        Parent root = FXMLLoader.load(ClientController.class.getResource("clientMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        s.close();
    }
}
