package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientProcessController implements Initializable {

    public ClientController clientController;
    @FXML
    private TableView<Process> tblSession;

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

    private ObservableList<Process> processList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        colName.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
        colId.setCellValueFactory(new PropertyValueFactory<Process, String>("id"));
        colSessionName.setCellValueFactory(new PropertyValueFactory<Process, String>("sessionName"));
        colSession.setCellValueFactory(new PropertyValueFactory<Process, String>("session"));
        colMemUsage.setCellValueFactory(new PropertyValueFactory<Process, String>("memUsage"));
    }

    public void setData(ObservableList<Process> processList0 )
    {
        processList = processList0;
        tblSession.setItems(processList);
    }

    public void kill() throws IOException
    {
        Process selected = tblSession.getSelectionModel().getSelectedItem();
        clientController.killProcess(selected.getId());
        processList.remove(selected);
    }

}
