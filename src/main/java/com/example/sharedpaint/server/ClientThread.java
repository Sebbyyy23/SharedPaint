package com.example.sharedpaint.server;

import com.example.sharedpaint.server.Server;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    private PrintWriter writer;
    private Server server;
    private String clientName = null;

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
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
            System.out.println("closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }
}