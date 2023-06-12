package com.example.sharedpaint.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String message;
                while((message = reader.readLine()) != null) {
                    System.out.printf(message);
                }
                System.out.println("closed");


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}