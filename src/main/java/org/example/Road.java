package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private double x;
    private double width;
    private int laneCount = 3;

    private double left;
    private double right;

    private double infinity = 1000000.0;
    private double top = - this.infinity;
    private double bottom = this.infinity;

    List<List<Point>> borders = new ArrayList<>();

    public Road(double x, double width) {
        this.x = x;
        this.width = width;
        this.left = this.x - this.width / 2;
        this.right = this.x + this.width / 2;

        Point topLeft = new Point(this.left, this.top);
        Point topRight = new Point(this.right, this.top);
        Point bottomLeft = new Point(this.left, this.bottom);
        Point bottomRight = new Point(this.right, this.bottom);


        borders.add(List.of(topLeft, bottomLeft));
        borders.add(List.of(topRight, bottomRight));
    }

    public double getLaneCenter(int laneIndex){
        double laneWidth = this.width/this.laneCount;

        return this.left + laneWidth/2 + Math.min(laneIndex, this.laneCount - 1) * laneWidth;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(5);
        gc.setStroke(Color.WHITE);

        for (int i = 1; i <= this.laneCount - 1; i++) {
            double x = Utils.lerp(
                    this.left,
                    this.right,
                    (double) i / this.laneCount
            );


            gc.setLineDashes(20, 20);
            gc.beginPath();
            gc.moveTo(x, this.top);
            gc.lineTo(x, this.bottom);
            gc.stroke();
        }

        gc.setLineDashes(0, 0);
        this.borders.forEach(border -> {
            gc.beginPath();
            gc.moveTo(border.get(0).getX(), border.get(0).getY());
            gc.lineTo(border.get(1).getX(), border.get(1).getY());
            gc.stroke();
        });


    }
}
