package org.example;

public class Utils {
    public static double lerp(double A, double B, double t) {
        return A + (B - A) * t;
    }
}
