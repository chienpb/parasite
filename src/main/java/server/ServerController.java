package server;

import javafx.event.ActionEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    public void openPort(ActionEvent event) throws Exception
    {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        int mode = din.read();
        while (mode != -1)
        {
            switch (mode)
            {
                case 1:
                {
                    getRunningProcesses();
                }
            }
            mode = din.read();
        }
        din.close();
        s.close();
        ss.close();
    }
    public void getRunningProcesses() throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
}
