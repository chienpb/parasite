package client;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;
import java.net.Socket;

public class ClientController {

    public TextField textBox1;
    public void test(ActionEvent event) throws Exception
    {
        String ip = textBox1.getText();
        Socket s = new Socket(ip, 3333);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF(ip);
        dout.flush();
        dout.close();
        s.close();
    }
}
