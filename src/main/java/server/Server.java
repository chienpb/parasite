package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {

    private ServerController serverController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("serverConnect.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        serverController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event->{
            try {
                serverController.closeServer();
            } catch (Exception e) {
                int a = 0;
            }
            Platform.exit();
            System.exit(0);
        });
    }

}
