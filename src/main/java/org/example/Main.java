package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main extends Application {

        private Car bestCar;

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

            Brainstorage brainStorage = new Brainstorage();

            int n = 2000;
            List<Car> cars = generateCars(n, road, scene, "AI");

            try {
                NeuralNetwork bestBrain = brainStorage.load();

                if(bestBrain != null){
                    for (int i = 0; i < cars.size(); i++) {

                        NeuralNetwork brain = brainStorage.copy(bestBrain);

                        cars.get(i).setBrain(brain);

                        if(i!=0){
                            NeuralNetwork.mutate(
                                    cars.get(i).getBrain(),
                                    0.1);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Message: " + e.getMessage());
            }





            List<Car> traffic = new ArrayList<>();

            traffic.add(new Car(road.getLaneCenter(1), -300, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -550, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -750, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(1), -1100, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -1000, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(0), -1300, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(1), -1500, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(0), -1650, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -1700, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(1), -1850, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(0), -1950, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(0), -2080, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -2080, 30, 50, scene, "DUMMY", 2));
            traffic.add(new Car(road.getLaneCenter(2), -2250, 30, 50, scene, "DUMMY", 2));

            try {
                Database.createTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.S) {
                    try {
                        if (bestCar != null) {
                            brainStorage.save(bestCar.getBrain());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (event.getCode() == KeyCode.D) {
                    try {
                            brainStorage.discard();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });




            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {

                    double time = (l / 1_000_000_000.0)%10;

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).update(road.borders, new ArrayList<>());
                    }

                    for (int i = 0; i < cars.size(); i++) {
                        cars.get(i).update(road.borders, traffic);
                    }

                    bestCar = cars
                            .stream()
                            .min(Comparator.comparingDouble(Car::getY))
                            .orElse(null);


                    carGc.setFill(Color.LIGHTGRAY);
                    carGc.fillRect(0, 0, carCanvas.getWidth(), carCanvas.getHeight());

                    networkGc.setFill(Color.BLACK);
                    networkGc.fillRect(0, 0, networkCanvas.getWidth(), networkCanvas.getHeight());

                    carGc.save();
                    carGc.translate(0, -bestCar.getY() + carCanvas.getHeight()*0.7);

                    road.draw(carGc);

                    for (int i = 0; i < traffic.size(); i++) {
                        traffic.get(i).draw(carGc, Color.RED, false);
                    }

                    carGc.setGlobalAlpha(0.2);
                    for (int i = 0; i < cars.size(); i++) {
                        cars.get(i).draw(carGc, Color.BLUE, false);
                    }
                    carGc.setGlobalAlpha(1);
                    bestCar.draw(carGc, Color.BLUE, true);

                    carGc.restore();

                    networkGc.setLineDashOffset(time);

                    Visualizer.drawNetwork(networkGc, bestCar.getBrain());
                }


            };

            timer.start();

            stage.setTitle("Teste JavaFX - Carro Autônomo");
            stage.setScene(scene);
            stage.show();

        }



        public static List<Car> generateCars(int n, Road road, Scene scene, String controlType) {
            List<Car> cars = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                cars.add(new Car(road.getLaneCenter(1), 100, 30, 50, scene, controlType, 3));
            }

            return cars;
        }

        public static void main(String[] args) {
            launch();
        }
    }


