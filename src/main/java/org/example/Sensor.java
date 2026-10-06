package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sensor {
    private Car car;
    private int rayCount = 5;
    private int rayLength = 150;
    private double raySpread = Math.PI/2;

    List<List<Point>> rays = new ArrayList<>();
    List<Intersection> readings = new ArrayList<>();

    public Sensor(Car car) {
        this.car = car;
    }

    public void update(List<List<Point>> roadBorders, List<Car> traffic) {
        this.castRays();
        this.readings.clear();
        for (int i = 0; i < this.rays.size(); i++) {
            this.readings.add(
                    this.getReading(
                            this.rays.get(i),
                            roadBorders,
                            traffic
                    )
            );
        }
    }

    private Intersection getReading(List<Point> ray, List<List<Point>> roadBorders, List<Car> traffic){
        List<Intersection> touches = new ArrayList<>();

        for (int i = 0; i < roadBorders.size(); i++) {
            Intersection touch = Utils.getIntersection(
                    ray.get(0),
                    ray.get(1),
                    roadBorders.get(i).get(0),
                    roadBorders.get(i).get(1)
            );
            if(touch != null) {
                touches.add(touch);
            }
        }

        for (int i = 0; i < traffic.size(); i++) {
            List<Point> poly = traffic.get(i).getPolygon();
            for (int j = 0; j < poly.size(); j++) {
                Intersection value = Utils.getIntersection(
                        ray.get(0),
                        ray.get(1),
                        poly.get(j),
                        poly.get((j+1)% poly.size())
                );
                if(value != null) {
                    touches.add(value);
                }
            }
        }

        if(touches.isEmpty()) {
            return null;
        }

        List<Double> offsets = new ArrayList<>();

        for(Intersection touch : touches) {
            offsets.add(touch.getOffset());
        }

        double minOffset = offsets.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElseThrow();

        for (Intersection touch : touches) {
            if(touch.getOffset() == minOffset) {
                return touch;
            }
        }

        return null;
    }

    private void castRays() {
        this.rays.clear();

        for (int i = 0; i < this.rayCount; i++) {
            double rayAngle = Utils.lerp(
                    this.raySpread/2,
                    -this.raySpread/2,
                    this.rayCount == 1 ? 0.5 : (double) i/(this.rayCount-1)
            ) + this.car.getAngle();

            Point start = new Point(this.car.getX(), this.car.getY());
            Point end = new Point(
                    this.car.getX()-Math.sin(rayAngle)*this.rayLength,
                    this.car.getY()-Math.cos(rayAngle)*this.rayLength
            );

            this.rays.add(List.of(start, end));
        }
    }

    public void draw(GraphicsContext gc) {
        for (int i = 0; i < this.rayCount; i++) {

            Point end = this.rays.get(i).get(1);
            if(this.readings.get(i) != null) {
                end = new Point(
                        this.readings.get(i).getX(),
                        this.readings.get(i).getY()
                );
            }

            gc.beginPath();
            gc.setLineWidth(2);
            gc.setStroke(Color.YELLOW);
            gc.moveTo(
                    this.rays.get(i).get(0).getX(),
                    this.rays.get(i).get(0).getY()
            );
            gc.lineTo(
                    end.getX(),
                    end.getY()
                    );

            gc.stroke();

            if(this.readings.get(i) != null){

                gc.beginPath();
                gc.setLineWidth(2);
                gc.setStroke(Color.BLACK);
                gc.moveTo(
                        this.rays.get(i).get(1).getX(),
                        this.rays.get(i).get(1).getY()
                );
                gc.lineTo(
                        end.getX(),
                        end.getY()
                );

                gc.stroke();
            }
        }
    }
}
