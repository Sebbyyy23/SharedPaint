package com.example.sharedpaint.client;

import com.example.sharedpaint.HelloController;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HexFormat;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private HelloController controller;


    public ServerThread(String address, int port, HelloController controller) {
        try {
            this.controller = controller;
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while ((message = reader.readLine()) != null) {
                drawCircle(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }



    public void broadcast(String message) {

        writer.println(message);
    }

    public void drawCircle(String message) {
        double x = 0, y = 0, radius = 0;
        long color = 0;
        String[] args = message.split(";");
        x = Double.valueOf(args[0]);
        y = Double.valueOf(args[1]);
        radius = Double.valueOf(args[2]);
        color = Long.parseLong(args[3], 16);
        controller.draw(x, y, radius, Color.valueOf(Integer.toHexString((int) color)));
    }
}