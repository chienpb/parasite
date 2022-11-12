package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ClientController implements Initializable {
    @FXML
    private TextField textBox1;

    @FXML
    private Pane paneMain;

    private Stage stage;
    private Scene scene;

    private Pane paneProcess;
    private Pane paneSystem;

    private Pane paneScreenshot;

    public BufferedReader br;

    public BufferedWriter bw;

    public Socket s;
    private int connected = 0;

    private ObservableList<Process> processList = FXCollections.observableArrayList();

    private FXMLLoader loaderProcess = new FXMLLoader(ClientController.class.getResource("clientProcess.fxml"));
    private FXMLLoader loaderSystem = new FXMLLoader(ClientController.class.getResource("clientSystem.fxml"));

    private FXMLLoader loaderScreenshot = new FXMLLoader(ClientController.class.getResource("clientScreenshot.fxml"));
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
    public void clickedSystem() throws IOException
    {
        if (connected == 1) {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(paneSystem);
        }
    }
    public void clickedProcess(ActionEvent event) throws IOException, CsvValidationException {
        if (connected == 1) {
            Pane pane = new FXMLLoader(ClientController.class.getResource("clientProcess.fxml")).load();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(paneProcess);
        }
        writeData("1");
        String[] val;
        CSVReader csvReader = new CSVReader(br);
        while (true)
        {
            val = csvReader.readNext();
            Process process = new Process(val[0], val[1], val[2], val[3], val[4]);
            if (Objects.equals(val[0], "stop")) {
                break;
            }
            processList.add(process);
        }
        clientProcessController.setData(processList);
    }
    public void clickedScreenshot(ActionEvent event) throws IOException
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
