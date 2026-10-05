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

            Car car = new Car(100, 100, 30, 50, scene);

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    car.update();

                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    car.draw(gc);
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


