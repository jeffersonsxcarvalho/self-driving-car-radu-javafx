package org.example;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.List;

public class Visualizer {
    public static void drawNetwork(GraphicsContext networkGc, NeuralNetwork network) {
        double margin = 50;
        double left = margin;
        double top = margin;
        double width = networkGc.getCanvas().getWidth() - margin * 4;
        double height = networkGc.getCanvas().getHeight() - margin * 2;

        double levelHeight = height/network.getLevels().size();

        for (int i = network.getLevels().size()-1; i >= 0 ; i--) {
            double levelTop = top +
                    Utils.lerp(
                            height - levelHeight,
                            0,
                            network.getLevels().size()==1
                                    ?0.5
                                    : (double) i / (network.getLevels().size() - 1)
                    );

            networkGc.setLineDashes(7, 3);
            Visualizer.drawLevel(
                    networkGc,
                    network.getLevels().get(i),
                    left,
                    levelTop,
                    width,
                    levelHeight,
                    i==network.getLevels().size()-1?new String[]{"⮝", "⮜", "⮞", "⮟"}:new String[]{}
            );
            
        }
        /**/
    }




    public static void drawLevel(GraphicsContext gc,
                                 Level level,
                                 double left,
                                 double top,
                                 double width,
                                 double height,
                                 String[] outputLabels
    ) {
        double right = left + width;
        double bottom = top + height;

        for (int i = 0; i < level.getInputs().length; i++) {
            for (int j = 0; j < level.getOutputs().length; j++) {


                gc.beginPath();
                gc.moveTo(
                        Visualizer.getNodeX(level.getInputs(), i, left, right),
                        bottom
                );
                gc.lineTo(
                        Visualizer.getNodeX(level.getOutputs(), j, left, right),
                        top
                );

                gc.setLineWidth(2);

                double value = level.getWeights().get(i)[j];
                gc.setStroke(Utils.getRGBA(value));

                gc.stroke();
            }
        }


        double nodeRadius = 18;
        for (int i = 0; i < level.getInputs().length; i++) {
            double x = 50 + i * 50;

                    /*Utils.lerp(
                    left,
                    right,
                    level.getInputs().length==1
                            ?0.5
                            : (double) i/(level.getInputs().length - 1)
            );*/

            gc.beginPath();
            gc.setFill(Color.BLACK);
            gc.fillOval(
                    x - nodeRadius,
                    bottom - nodeRadius,
                    nodeRadius * 2,
                    nodeRadius * 2
            );

            gc.beginPath();
            gc.setFill(Utils.getRGBA(level.getInputs()[i]));
            gc.fillOval(
                    x - nodeRadius*0.6,
                    bottom - nodeRadius*0.6,
                    nodeRadius * 1.2,
                    nodeRadius * 1.2
            );



        }

        for (int i = 0; i < level.getOutputs().length; i++) {
            double x = 50 + i * 50;

                    /*Utils.lerp(
                    left,
                    right,
                    level.getOutputs().length==1
                            ?0.5
                            :(double) i/(level.getOutputs().length - 1)
            );*/

            gc.beginPath();
            gc.setFill(Color.BLACK);
            gc.fillOval(
                    x - nodeRadius,
                    top - nodeRadius,
                    nodeRadius * 2,
                    nodeRadius * 2
            );

            gc.beginPath();
            gc.setFill(Utils.getRGBA(level.getOutputs()[i]));
            gc.fillOval(
                    x - nodeRadius*0.6,
                    top - nodeRadius*0.6,
                    nodeRadius * 1.2,
                    nodeRadius * 1.2
            );

            gc.beginPath();
            gc.setLineWidth(2);
            //gc.setLineDashes(3,3);
            gc.strokeOval(
                    x - nodeRadius * 0.8,
                    top - nodeRadius * 0.8,
                    nodeRadius * 1.6,
                    nodeRadius * 1.6
            );
            gc.setStroke(Utils.getRGBA(level.getBiases()[i]));
            //gc.setLineDashes(0,0);

           if(outputLabels.length > 0) {
                gc.beginPath();
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFill(Color.BLACK);
                gc.setStroke(Color.WHITE);
               gc.setFont(Font.loadFont(
                       Visualizer.class.getResourceAsStream("/fonts/nome-da-fonte.ttf"),
                       nodeRadius * 1.5
               ));
                gc.fillText(outputLabels[i], x, top);
                gc.setLineWidth(0.5);
                gc.strokeText(outputLabels[i], x, top);
            }


        }


    }

    private static double getNodeX(double[] nodes, int index, double left, double right) {
        return 50 + index * 50;

                /*Utils.lerp(
                left,
                right,
                nodes.length==1
                        ?0.5
                        :(double) index/(nodes.length - 1)
        );*/
    }
}
