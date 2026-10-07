package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

        @Override
        public void start(Stage stage) {

            Canvas carCanvas = new Canvas(200, 600);
            Canvas networkCanvas = new Canvas(300, 600);

            GraphicsContext carGc = carCanvas.getGraphicsContext2D();
            GraphicsContext networkGc = networkCanvas.getGraphicsContext2D();

            carGc.setFill(Color.LIGHTGRAY);
            carGc.fillRect(
                    0,
                    0,
                    carCanvas.getWidth(),
                    carCanvas.getHeight()
            );

            networkGc.setFill(Color.BLACK);
            networkGc.fillRect(
                    0,
                    0,
                    networkCanvas.getWidth(),
                    networkCanvas.getHeight()
            );

            HBox root = new HBox(carCanvas, networkCanvas);
            root.setAlignment(Pos.TOP_LEFT);

            Scene scene = new Scene(root, 1080, 720);
            scene.setFill(Color.DARKGRAY);

            // A altura do Canvas acompanha a altura da janela.
            carCanvas.heightProperty().bind(scene.heightProperty());
            networkCanvas.heightProperty().bind(scene.heightProperty());
            networkCanvas.widthProperty().bind(scene.widthProperty());

            Road road = new Road(carCanvas.getWidth()/2, carCanvas.getWidth() * 0.9);

            Car car = new Car(road.getLaneCenter(1), 100, 30, 50, scene, "AI", 3);

            List<Car> traffic = new ArrayList<>();
            traffic.add(new Car(road.getLaneCenter(1), -100, 30, 50, scene, "DUMMY", 2));

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {

                    double time = (l / 1_000_000_000.0)%10;

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).update(road.borders, new ArrayList<>());
                    }
                    car.update(road.borders, traffic);

                    carGc.setFill(Color.LIGHTGRAY);
                    carGc.fillRect(0, 0, carCanvas.getWidth(), carCanvas.getHeight());

                    networkGc.setFill(Color.BLACK);
                    networkGc.fillRect(0, 0, networkCanvas.getWidth(), networkCanvas.getHeight());

                    carGc.save();
                    carGc.translate(0, -car.getY() + carCanvas.getHeight()*0.7);

                    road.draw(carGc);

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).draw(carGc, Color.RED);
                    }
                    car.draw(carGc, Color.BLUE);

                    carGc.restore();

                    networkGc.setLineDashOffset(time);

                    Visualizer.drawNetwork(networkGc, car.getBrain());
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


