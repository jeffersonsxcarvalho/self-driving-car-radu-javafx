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

    private double speed = 0;
    private double acceleration = 0.2;
    private double maxSpeed = 3;
    private double friction = 0.05;
    private double angle = 0;

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
        this.move();
    }

    private void move() {
        if(controls.isForward()){
            this.speed += this.acceleration;
        }
        if(controls.isReverse()){
            this.speed -= this.acceleration;
        }
        if(this.speed > this.maxSpeed) {
            this.speed = this.maxSpeed;
        }
        if(this.speed < -this.maxSpeed/2) {
            this.speed = -this.maxSpeed/2;
        }
        if(this.speed > 0) {
            this.speed -= this.friction;
        }
        if(this.speed < 0) {
            this.speed += this.friction;
        }
        if(Math.abs(this.speed)<this.friction){
            this.speed = 0;
        }

        if(this.speed != 0){
            int flip = this.speed > 0 ? 1 : -1;
            if(this.controls.isLeft()) {
                this.angle += 0.03*flip;
            }

            if(this.controls.isRight()) {
                this.angle -= 0.03*flip;
            }
        }

        this.x -= Math.sin(this.angle)*this.speed;
        this.y -= Math.cos(this.angle)*this.speed;
    }

    public void  draw(GraphicsContext gc) {
        gc.save();

        gc.translate(this.x, this.y);
        gc.rotate(Math.toDegrees(-this.angle));

        gc.setFill(Color.BLACK);
        gc.fillRect(
                -this.width/2,
                -this.height/2,
                width,
                height
        );

        gc.restore();
    }
}
