package server;

import javafx.event.ActionEvent;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class ServerController {

    private BufferedReader br;

    private BufferedWriter bw;
    public void openPort(ActionEvent event) throws Exception
    {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        int mode = br.read();
        while (mode != -1)
        {
            switch (mode)
            {
                case 1:
                    getRunningProcesses();
                    break;
                case 2:
                    //shutdown();
                    System.out.println(1);
                    break;
                case 3: {
                    System.out.println(2);
                    break;
                }
            }
            mode = br.read();
        }
        br.close();
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
            if (line == null) {
                break;
            }
            bw.write(line);
            bw.newLine();
            bw.flush();
        }

    }
    public void shutdown() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.command("cmd", "/c" ,"shutdown -s");
            Process process = processBuilder.start();
            process.waitFor();
            if(process.exitValue()==0){
                System.out.println("Shut down");}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void logout() throws Exception
    {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c" ,"shutdown -l");
        Process process = processBuilder.start();
        process.waitFor();
        if(process.exitValue()==0){
            System.out.println("Shut down");}
    }
    public void getImage() throws  Exception {
        Robot robot = new Robot();
//        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        BufferedImage image = robot.createScreenCapture(rect);
//        ImageIO.write(image, "jpg", new File("C:\\Users\\Admin\\Pictures\\out.jpg"));
        System.out.println("Captured");

    }
}
