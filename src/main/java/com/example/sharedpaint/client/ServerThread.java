package com.example.sharedpaint.client;

import com.example.sharedpaint.HelloController;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private HelloController controller;


    public ServerThread(String address, int port,HelloController controller) {
        try {
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
                System.out.println(message);


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
}