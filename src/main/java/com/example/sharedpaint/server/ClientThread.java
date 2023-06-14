package com.example.sharedpaint.server;

import com.example.sharedpaint.server.Server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientThread extends Thread {
    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    private PrintWriter writer;
    private Server server;


    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run(){
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while ((message = reader.readLine()) != null){
                server.broadcast(message);
                server.saveDot(message);
            }

            server.removeClient(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        writer.println(message);
    }







}