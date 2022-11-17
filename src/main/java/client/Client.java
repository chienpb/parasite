package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ClientController clientController;

    @Override
    public void start(Stage stage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("clientMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            clientController = fxmlLoader.getController();
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event->{
                try {
                    clientController.closeServer();
                } catch (Exception e) {
                    int a = 0;
                }
                Platform.exit();
                System.exit(0);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
