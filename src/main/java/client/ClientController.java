package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.opencsv.exceptions.CsvValidationException;
import javafx.stage.Stage;

import javax.swing.*;

public class ClientController implements Initializable {
    @FXML
    private TextField textBox1;
    @FXML
    private Pane paneMain;
    @FXML
    private Button btnSystem;
    @FXML
    private Button btnScreenshot;
    @FXML
    private Button btnProcess;
    @FXML
    private Button btnKeylogger;
    private Pane paneProcess;
    private Pane paneSystem;
    private Pane paneScreenshot;

    private Pane paneKeylogger;

    public BufferedReader br;

    public BufferedWriter bw;

    public Socket s;
    private int connected = 0;

    private final FXMLLoader loaderProcess = new FXMLLoader(ClientController.class.getResource("clientProcess.fxml"));
    private final FXMLLoader loaderSystem = new FXMLLoader(ClientController.class.getResource("clientSystem.fxml"));
    private final FXMLLoader loaderScreenshot = new FXMLLoader(ClientController.class.getResource("clientScreenshot.fxml"));

    private final FXMLLoader loaderKeylogger = new FXMLLoader(ClientController.class.getResource("clientKeylogger.fxml"));
    private ClientProcessController clientProcessController;
    private ClientSystemController clientSystemController;
    private ClientScreenshotController clientScreenshotController;

    public ClientKeyloggerController clientKeyloggerController;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            paneSystem = loaderSystem.load();
            paneProcess = loaderProcess.load();
            paneScreenshot = loaderScreenshot.load();
            paneKeylogger = loaderKeylogger.load();
            clientProcessController = loaderProcess.getController();
            clientSystemController = loaderSystem.getController();
            clientScreenshotController = loaderScreenshot.getController();
            clientKeyloggerController = loaderKeylogger.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSystemController.clientController = this;
        clientProcessController.clientController = this;
        clientScreenshotController.clientController = this;
        clientKeyloggerController.clientController = this;
    }
    public void connect() throws IOException
    {
        String ip = textBox1.getText();
        s = new Socket(ip, 3333);
        connected = 1;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneSystem);
        try {
            getSpecs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeData(String s) throws IOException
    {
        bw.write(s);
        bw.newLine();
        bw.flush();
    }

    public void infoBox(String message)
    {
        JOptionPane.showMessageDialog(null, message);
    }
    public void clickedSystem() throws IOException, InterruptedException {
        if (connected == 0) {
            infoBox("Not connected to server");
            return;
        }
        btnSystem.setStyle("-fx-background-color: #fd9644");
        btnKeylogger.setStyle("-fx-background-color: #fefad4");
        btnScreenshot.setStyle("-fx-background-color: #fefad4");
        btnProcess.setStyle("-fx-background-color: #fefad4");
        clientKeyloggerController.stop();
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneSystem);
    }

    public void clickedProcess() throws CsvValidationException, IOException, InterruptedException {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
        btnProcess.setStyle("-fx-background-color: #fd9644");
        btnKeylogger.setStyle("-fx-background-color: #fefad4");
        btnScreenshot.setStyle("-fx-background-color: #fefad4");
        btnSystem.setStyle("-fx-background-color: #fefad4");
        clientKeyloggerController.stop();
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneProcess);
        clientProcessController.getRunningProcess();
    }
    public void clickedScreenshot() throws IOException, InterruptedException {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
        btnScreenshot.setStyle("-fx-background-color: #fd9644");
        btnKeylogger.setStyle("-fx-background-color: #fefad4");
        btnSystem.setStyle("-fx-background-color: #fefad4");
        btnProcess.setStyle("-fx-background-color: #fefad4");
        clientKeyloggerController.stop();
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneScreenshot);
    }

    public void shutdown(String timer) throws IOException
    {
        writeData("2");
        writeData(timer);
    }

    public void logout(String timer) throws IOException
    {
        writeData("3");
        writeData(timer);
    }

    public void restart(String timer) throws IOException
    {
        writeData("11");
        writeData(timer);
    }

    public void getSpecs() throws IOException
    {
        writeData("12");
        clientSystemController.setSpecs();
    }
    public void clickedKeylogger()
    {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
        btnKeylogger.setStyle("-fx-background-color: #fd9644");
        btnSystem.setStyle("-fx-background-color: #fefad4");
        btnScreenshot.setStyle("-fx-background-color: #fefad4");
        btnProcess.setStyle("-fx-background-color: #fefad4");
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneKeylogger);
    }

    public void killProcess(String id) throws IOException
    {
        writeData("5");
        writeData(id);
    }
    public void startProcess(String name) throws IOException
    {
        writeData("7");
        writeData(name);
    }
    public void closeServer() throws IOException, InterruptedException {
        clientKeyloggerController.stop();
        try {
            writeData("10");
        }
        catch (RuntimeException e)
        {
            System.out.println("runtime exception");
        }
        s.close();
    }

}
