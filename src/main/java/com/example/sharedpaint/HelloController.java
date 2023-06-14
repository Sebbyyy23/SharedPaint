package com.example.sharedpaint;
import com.example.sharedpaint.client.ServerThread;
import com.example.sharedpaint.server.Server;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HelloController {

    public TextField addressField;
    public TextField portField;
    public ColorPicker colorPicker;
    public Slider radiusSlider;
    public Canvas canvas;
    private ServerThread serverThread;
    private Server server;
    public void draw(double x, double y, double radius, Color color){
        GraphicsContext graphicsContext =  canvas.getGraphicsContext2D();
        graphicsContext.setFill(color);
        graphicsContext.fillOval(x-radius,y-radius,radius*2,radius*2);
    }

    public void onStartServerClicked(ActionEvent actionEvent) {
        int port = Integer.valueOf(portField.getText());
        String address = "localhost";
        server = new Server(port);
        server.start();
        serverThread= new ServerThread(address, port,this);
        serverThread.start();
    }

    public void onConnectClicked(ActionEvent actionEvent) {
        int port = Integer.valueOf(portField.getText());
        String address = "localhost";
        serverThread= new ServerThread(address, port,this);
        serverThread.start();
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double radius = radiusSlider.getValue();
        Color color = colorPicker.getValue();
        draw(x,y,radius,color);

        serverThread.send(String.format("%f;%f;%f;%x",x,y,radius,color.hashCode()));//x;y;radius;color

    }


    public void onMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            double radius = radiusSlider.getValue();
            Color color = colorPicker.getValue();
            draw(x, y, radius, color);

            serverThread.send(String.format("%f;%f;%f;%x", x, y, radius, color.hashCode()));
        }
    }

}