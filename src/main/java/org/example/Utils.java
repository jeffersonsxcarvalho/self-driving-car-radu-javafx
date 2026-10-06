package org.example;

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
}


