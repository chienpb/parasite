package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;

public class ClientController implements Initializable {
    @FXML
    private TextField textBox1;

    @FXML
    private Pane paneMain;
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

    private ClientKeyloggerController clientKeyloggerController;

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
    public void clickedSystem() {
        if (connected == 0) {
            infoBox("Not connected to server");
            return;
        }
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneSystem);
    }

    public void clickedProcess() throws CsvValidationException, IOException {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneProcess);
        clientProcessController.getRunningProcess();
    }
    public void clickedScreenshot()
    {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneScreenshot);
    }

    public void clickedShutdown() throws IOException
    {
        writeData("2");
    }

    public void clickedLogout() throws IOException
    {
        writeData("3");
    }

    public void clickedKeylogger()
    {
        if (connected == 0)
        {
            infoBox("Not connected to server");
            return;
        }
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
}
