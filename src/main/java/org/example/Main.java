package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

        @Override
        public void start(Stage stage) {

            Canvas canvas = new Canvas(200, 600);

            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(
                    0,
                    0,
                    canvas.getWidth(),
                    canvas.getHeight()
            );

            StackPane root = new StackPane(canvas);
            StackPane.setAlignment(canvas, Pos.TOP_CENTER);

            Scene scene = new Scene(root, 200, 600);
            scene.setFill(Color.DARKGRAY);

            // A altura do Canvas acompanha a altura da janela.
            canvas.heightProperty().bind(scene.heightProperty());

            Road road = new Road(canvas.getWidth()/2, canvas.getWidth() * 0.9);

            Car car = new Car(road.getLaneCenter(1), 100, 30, 50, scene, "AI", 3);

            List<Car> traffic = new ArrayList<>();
            traffic.add(new Car(road.getLaneCenter(1), -100, 30, 50, scene, "DUMMY", 2));

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).update(road.borders, new ArrayList<>());
                    }
                    car.update(road.borders, traffic);

                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    gc.save();
                    gc.translate(0, -car.getY() + canvas.getHeight()*0.7);

                    road.draw(gc);

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).draw(gc, Color.RED);
                    }
                    car.draw(gc, Color.BLUE);

                    gc.restore();
                }
            };

            timer.start();

            stage.setTitle("Teste JavaFX - Carro Autônomo");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }


