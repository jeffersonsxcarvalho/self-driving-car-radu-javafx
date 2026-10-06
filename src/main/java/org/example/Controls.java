package org.example;

import javafx.scene.Scene;

public class Controls {
    private boolean forward = false;
    private boolean left = false;
    private boolean right = false;
    private boolean reverse = false;

    public Controls(Scene scene, String type) {


        switch (type) {
            case "KEYS" : this.addKeyboardListeners(scene);
            break;
            case "DUMMY" : this.forward = true;

        }
    }

    public void addKeyboardListeners(Scene scene){
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    forward = true;
                    break;

                case DOWN:
                    reverse = true;
                    break;

                case LEFT:
                    left = true;
                    break;

                case RIGHT:
                    right = true;
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    forward = false;
                    break;

                case DOWN:
                    reverse = false;
                    break;

                case LEFT:
                    left = false;
                    break;

                case RIGHT:
                    right = false;
                    break;
            }
        });
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
