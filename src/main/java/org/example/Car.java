package org.example;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Car {
    private double x;
    private double y;
    private double width;
    private double height;
    private Scene scene;

    private Controls controls;

    public Car(double x, double y, double width, double height, Scene scene) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scene = scene;

        this.controls = new Controls(scene);
    }

    public void update() {
        if(controls.isForward()){
            this.y -= 2;
        }
        if(controls.isReverse()){
            this.y += 2;
        }
    }

    public void  draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(
                x - width/2,
                y-height/2,
                width,
                height
        );
    }
}
