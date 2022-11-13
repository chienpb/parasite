package server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerController {

    private BufferedWriter bw;

    private OutputStream outputStream;

    public void openPort() throws IOException, AWTException, InterruptedException {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        outputStream = s.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        int mode = Integer.parseInt(br.readLine());
        while (mode != -1) {
            switch (mode) {
                case 1 -> getRunningProcesses();
                case 2 ->
                    //shutdown();
                        System.out.println(1);
                case 3 -> System.out.println(2);
                case 4 -> getImage();
                case 5 -> {
                    String zs = br.readLine();
                    kill(zs);
                }
                case 6 -> getRunningApplication();
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
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        String zz = "\"stop\", \"pls\", \"pls\", \"pls\", \"pls\"";
        bw.write(zz);
        bw.newLine();
        bw.flush();
    }

    public void getRunningApplication() throws IOException {
        String cmd = "powershell Get-Process | Where-Object { $_.MainWindowTitle } | Select-Object -Property Name, Id, Mainwindowtitle | Export-Csv -path .\\app.csv -NoTypeInformation ; Get-Content -path .\\app.csv";
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        r.readLine();
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        String zz = "\"stop\", \"pls\", \"pls\"";
        bw.write(zz);
        bw.newLine();
        bw.flush();
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
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        bimg = robot.createScreenCapture(rect);
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
