package entities;

import javafx.scene.Node;
import javafx.scene.media.AudioClip;

import javafx.scene.Node;

public class Enemy extends Movable {
    public Enemy(){}

    public Enemy(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void movement(double time) {
        int i = (int)(time % 10);

        setMoving();

        if (i < 2) {
            goUp();
            goRight();
        } else if (i < 4) {
            goRight();
            goDown();
        } else if (i < 6) {
            goDown();
            goLeft();
        } else if (i < 8){
            goLeft();
            goUp();
        } else {
            stopX();
            stopY();
            setIdle();
        }
    }
}

