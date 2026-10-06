package org.example;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static org.example.Utils.polyIntersect;

public class Car {
    private double x;
    private double y;
    private double width;
    private double height;
    private Scene scene;
    private List<Point> polygon;

    private double speed = 0;
    private double acceleration = 0.2;
    private double maxSpeed = 3;
    private double friction = 0.05;
    private double angle = 0;
    private boolean damaged = false;

    private Sensor sensor;
    private Controls controls;

    public Car(double x, double y, double width, double height, Scene scene) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scene = scene;

        this.sensor = new Sensor(this);
        this.controls = new Controls(scene);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void update(List<List<Point>> roadBorders) {
        if(!this.damaged){
            this.move();
            this.polygon = createPolygon();
            this.damaged = assessDamage(roadBorders);
        }
        this.sensor.update(roadBorders);
    }

    private boolean assessDamage(List<List<Point>> roadBorders) {
        for (int i = 0; i < roadBorders.size(); i++) {
            if(polyIntersect(this.polygon, roadBorders.get(i))){
                return true;
            }
        }
        return false;
    }

    private List<Point> createPolygon() {
        List<Point> points = new ArrayList<>();

        double rad = Math.hypot(this.width, this.height)/2;
        double alpha = Math.atan2(this.width, this.height);

        points.add(new Point(
                this.x - Math.sin(this.angle - alpha)*rad,
                this.y - Math.cos(this.angle - alpha)*rad
                ));
        points.add(new Point(
                this.x - Math.sin(this.angle + alpha)*rad,
                this.y - Math.cos(this.angle + alpha)*rad
        ));
        points.add(new Point(
                this.x - Math.sin(Math.PI + this.angle - alpha)*rad,
                this.y - Math.cos(Math.PI + this.angle - alpha)*rad
        ));
        points.add(new Point(
                this.x - Math.sin(Math.PI + this.angle + alpha)*rad,
                this.y - Math.cos(Math.PI + this.angle + alpha)*rad
        ));

        return points;

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

        if(this.damaged){
            gc.setFill(Color.GRAY);
        }else{
            gc.setFill(Color.BLACK);
        }

        gc.beginPath();
        gc.moveTo(
                this.polygon.get(0).getX(),
                this.polygon.get(0).getY()
        );
        for (int i = 1; i < this.polygon.size(); i++) {
            gc.lineTo(
                    this.polygon.get(i).getX(),
                    this.polygon.get(i).getY()
            );
        }
        gc.fill();

        this.sensor.draw(gc);
    }


}
