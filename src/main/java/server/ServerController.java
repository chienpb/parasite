package server;

import javafx.event.ActionEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerController {

    private BufferedReader br;

    private BufferedWriter bw;

    private OutputStream outputStream;

    private Socket s;

    public void openPort() throws IOException, AWTException {
        ServerSocket ss = new ServerSocket(3333);
        s = ss.accept();
        outputStream = s.getOutputStream();
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        int mode = Integer.parseInt(br.readLine());
        while (mode != -1) {
            switch (mode) {
                case 1:
                    getRunningProcesses();
                    break;
                case 2:
                    //shutdown();
                    System.out.println(1);
                    break;
                case 3:
                    System.out.println(2);
                    break;
                case 4:
                    getImage();
                    break;
                case 5:
                    String zs = br.readLine();
                    kill(zs);
                    break;
            }
            mode = Integer.parseInt(br.readLine());
        }
        br.close();
        s.close();
        ss.close();
    }

    public void getRunningProcesses() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        int cnt = 0;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            ++cnt;
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        String zz = "\"stop\", \"pls\", \"pls\", \"pls\", \"pls\"";
        bw.write(zz);
        bw.newLine();
        bw.flush();

    }

    public void shutdown() {
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        try {
//            processBuilder.command("cmd", "/c" ,"shutdown -s");
//            Process process = processBuilder.start();
//            process.waitFor();
//            if(process.exitValue()==0){
//                System.out.println("Shut down");}
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        System.out.println("yespapi");
    }

    public void logout() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c", "shutdown -l");
        Process process = processBuilder.start();
        process.waitFor();
        if (process.exitValue() == 0) {
            System.out.println("Shut down");
        }
    }

    public void getImage() throws IOException, AWTException {
        Robot robot = new Robot();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bimg;
        bimg = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));
        ImageIO.write(bimg, "jpg", byteArrayOutputStream);
        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        System.out.println("Captured");
    }

    public void kill(String id) throws IOException {
        System.out.println(id);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("taskkill", "/F", "/PID", id);
        processBuilder.start();
    }

    public void startProcess(String id)
    {

    }
}
