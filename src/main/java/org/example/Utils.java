package org.example;

import javafx.scene.paint.Color;

import java.util.List;

public class Utils {
    public static double lerp(double A, double B, double t) {
        return A + (B - A) * t;
    }

    public static Intersection getIntersection(Point A,Point B, Point C, Point D){

        double tTop=(D.getX()-C.getX())*(A.getY()-C.getY())-(D.getY()-C.getY())*(A.getX()-C.getX());
        double uTop=(C.getY()-A.getY())*(A.getX()-B.getX())-(C.getX()-A.getX())*(A.getY()-B.getY());
        double bottom=(D.getY()-C.getY())*(B.getX()-A.getX())-(D.getX()-C.getX())*(B.getY()-A.getY());

        if(bottom!=0){
            double t=tTop/bottom;
            double u=uTop/bottom;

            if(t>=0 && t<=1 && u>=0 && u<=1){
                return new Intersection(lerp(A.x,B.x,t), lerp(A.y,B.y,t), t);
            }
        }

        return null;
    }

    public static boolean polyIntersect(List<Point> poly1, List<Point> poly2) {
        for (int i = 0; i < poly1.size(); i++) {
            for (int j = 0; j < poly2.size(); j++) {
                Intersection touch = getIntersection(
                        poly1.get(i),
                        poly1.get((i+1)%poly1.size()),
                        poly2.get(j),
                        poly2.get((j+1)%poly2.size())

                );
                if(touch != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Color getRGBA(double value) {
        double alpha = Math.abs(value);
        int R = value < 0 ? 0 : 255;
        int G = R;
        int B = value > 0 ? 0 : 255;
        return Color.rgb(R, G, B, alpha);
    }
}


