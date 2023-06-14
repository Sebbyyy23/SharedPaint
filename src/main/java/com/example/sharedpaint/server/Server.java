package com.example.sharedpaint.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private List<ClientThread> clients = new ArrayList<>();
    private Connection connection;
    private PreparedStatement statement;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        while(true){
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                ClientThread thread = new ClientThread(clientSocket, this);
                clients.add(thread);
                thread.start();
                loadDatabaseOnCanvas(getSavedDot(), thread);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);

    }

    public void broadcast( String message){
        for(var currentClient : clients)
            currentClient.send(message);

        saveDot(message);
    }

    private void saveDot(String message) {
        String[] parameters = message.split(";");
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parameters[i].replaceAll("\\..*", "");
        }

        try{
            statement = connection.prepareStatement("INSERT INTO dot (x, y, color, radius) VALUES (?, ?, ?, ?)");
            statement.setInt(1, Integer.parseInt(parameters[0]));
            statement.setInt(2, Integer.parseInt(parameters[1]));
            statement.setString(3, parameters[3]);
            statement.setInt(4, Integer.parseInt(parameters[2]));
            statement.executeUpdate();
            System.out.println("Record inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting record: " + e.getMessage());
        }
    }


    private List<String> getSavedDot() {
        List<String> dotRecords = new ArrayList<>();

        try {
             statement = connection.prepareStatement("SELECT * FROM dot");
             ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String color = resultSet.getString("color");
                int radius = resultSet.getInt("radius");

                String record = x + ";" + y + ";" + radius + ";" + color;
                dotRecords.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving dot records: " + e.getMessage());
        }

        return dotRecords;
    }

    public void loadDatabaseOnCanvas(List<String> dots, ClientThread newClient){
            for(String dot : dots)
                newClient.send(dot);
    }
}