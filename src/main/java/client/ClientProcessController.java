package client;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientProcessController implements Initializable {

    public ClientController clientController;
    @FXML
    private TableView<Process> tblSession;

    @FXML
    private TableView<Application> tblApplication;

    @FXML
    private TableColumn<Process, String> colName;

    @FXML
    private TableColumn<Process, String> colId;

    @FXML
    private TableColumn<Process, String> colSessionName;

    @FXML
    private TableColumn<Process, String> colSession;

    @FXML
    private TableColumn<Process, String> colMemUsage;

    @FXML
    private TableColumn<Application, String> colAppName;

    @FXML
    private TableColumn<Application, String> colAppId;

    @FXML
    private TableColumn<Application, String> colAppTitle;

    private ObservableList<Process> processList = FXCollections.observableArrayList();

    private ObservableList<Application> applicationList = FXCollections.observableArrayList();

    private int running = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        colName.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
        colId.setCellValueFactory(new PropertyValueFactory<Process, String>("id"));
        colSessionName.setCellValueFactory(new PropertyValueFactory<Process, String>("sessionName"));
        colSession.setCellValueFactory(new PropertyValueFactory<Process, String>("session"));
        colMemUsage.setCellValueFactory(new PropertyValueFactory<Process, String>("memUsage"));
        colAppName.setCellValueFactory(new PropertyValueFactory<Application, String>("name"));
        colAppId.setCellValueFactory(new PropertyValueFactory<Application, String>("id"));
        colAppTitle.setCellValueFactory(new PropertyValueFactory<Application, String>("title"));
    }

    public void kill() throws IOException
    {
        if (running == 0) {
            Process selected = tblSession.getSelectionModel().getSelectedItem();
            clientController.killProcess(selected.getId());
            processList.remove(selected);
        }
        else {
            Application selected = tblApplication.getSelectionModel().getSelectedItem();
            clientController.killProcess(selected.getId());
            applicationList.remove(selected);
        }
    }

    public void getRunningApplication() throws IOException, CsvValidationException {
        running = 1;
        tblSession.setVisible(false);
        tblApplication.setVisible(true);
        clientController.writeData("6");
        ObservableList<Application> list = FXCollections.observableArrayList();
        String[] val;
        CSVReader csvReader = new CSVReader(clientController.br);
        while (true)
        {
            val = csvReader.readNext();
            Application application = new Application(val[0], val[1], val[2]);
            if (Objects.equals(val[0], "stop")) {
                break;
            }
            list.add(application);
        }
        applicationList = list;
        tblApplication.setItems(applicationList);
    }

    public void getRunningProcess() throws IOException, CsvValidationException {
        running = 0;
        tblApplication.setVisible(false);
        tblSession.setVisible(true);
        clientController.writeData("1");
        String[] val;
        CSVReader csvReader = new CSVReader(clientController.br);
        ObservableList<Process> list = FXCollections.observableArrayList();
        while (true)
        {
            val = csvReader.readNext();
            Process process = new Process(val[0], val[1], val[2], val[3], val[4]);
            if (Objects.equals(val[0], "stop")) {
                break;
            }
            list.add(process);
        }
        processList = list;
        tblSession.setItems(processList);
    }

}
