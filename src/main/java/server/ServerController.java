package server;

import javafx.event.ActionEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    public void openPort(ActionEvent event) throws Exception
    {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        System.out.println("connected");
        DataInputStream din = new DataInputStream(s.getInputStream());
        din.close();
        s.close();
        ss.close();
    }
}
