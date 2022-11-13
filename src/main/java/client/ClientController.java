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

public class ClientController implements Initializable {
    @FXML
    private TextField textBox1;

    @FXML
    private Pane paneMain;
    private Pane paneProcess;
    private Pane paneSystem;

    private Pane paneScreenshot;

    public BufferedReader br;

    public BufferedWriter bw;

    public Socket s;
    private int connected = 0;

    private final FXMLLoader loaderProcess = new FXMLLoader(ClientController.class.getResource("clientProcess.fxml"));
    private final FXMLLoader loaderSystem = new FXMLLoader(ClientController.class.getResource("clientSystem.fxml"));

    private final FXMLLoader loaderScreenshot = new FXMLLoader(ClientController.class.getResource("clientScreenshot.fxml"));
    private ClientProcessController clientProcessController;
    private ClientSystemController clientSystemController;

    private ClientScreenshotController clientScreenshotController;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            paneSystem = loaderSystem.load();
            paneProcess = loaderProcess.load();
            paneScreenshot = loaderScreenshot.load();
            clientProcessController = loaderProcess.getController();
            clientSystemController = loaderSystem.getController();
            clientScreenshotController = loaderScreenshot.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSystemController.clientController = this;
        clientProcessController.clientController = this;
        clientScreenshotController.clientController = this;
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
    public void clickedSystem() {
        if (connected == 0)
            return;
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneSystem);
    }

    public void clickedProcess() throws CsvValidationException, IOException {
        if (connected == 0)
            return;
        paneMain.getChildren().clear();
        paneMain.getChildren().add(paneProcess);
        clientProcessController.getRunningProcess();
    }
    public void clickedScreenshot()
    {
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

    public void killProcess(String id) throws IOException
    {
        writeData("5");
        writeData(id);
    }
}
