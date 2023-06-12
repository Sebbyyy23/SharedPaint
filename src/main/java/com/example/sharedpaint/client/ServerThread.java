package com.example.sharedpaint.client;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket socket;
    private PrintWriter writer;
    public ServerThread(String host, int port){
        try {
            this.socket = new Socket(host,port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            InputStream input  = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(String message) {
        writer.println(message);
    }
}