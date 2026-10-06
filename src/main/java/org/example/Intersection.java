package org.example;

public class Intersection {
    private double x;
    private double y;
    private double offset;

    public Intersection(double x, double y, double offset) {
        this.x = x;
        this.y = y;
        this.offset = offset;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getOffset() {
        return offset;
    }
}
