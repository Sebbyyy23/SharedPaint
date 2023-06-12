package com.example.sharedpaint;

import com.example.sharedpaint.client.ServerThread;
import com.example.sharedpaint.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloController{

    ServerThread serverThread;
    public TextField addressField;
    public TextField portField;
    public ColorPicker colorPicker;
    public Slider radiusSlider;
    public Canvas canvas;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void draw(double x, double y, double radius, Color color) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(color);
        graphicsContext.fillOval(x-radius, y-radius, radius*2, radius*2);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double radius = radiusSlider.getValue();
        Color color = colorPicker.getValue();
        draw(x, y, radius, color);
        serverThread.send(String.format("x:%f y:%f radius:%f color:%x", x, y, radius, color.hashCode()));
    }

    public void onConnectClicked(ActionEvent actionEvent) {
        serverThread = new ServerThread("localhost", 5000);
        serverThread.start();
    }


    public void onStartServerClicked(ActionEvent actionEvent) {
        Server server = new Server(Integer.parseInt(portField.getText()));
        server.start();
        serverThread = new ServerThread("localhost", 5000);
        serverThread.start();
    }
}