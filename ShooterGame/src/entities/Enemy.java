package entities;

import javafx.scene.Node;

public class Enemy extends Movable {
    public Enemy(){}

    public Enemy(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void movement(double time) {
        int i = (int)(time % 30);

        if (i < 5) {
            goUp();
            stopX();
            setMoving();
        }
        else if (i < 10) {
            goDown();
            stopX();
            setMoving();
        }
        else if (i < 15) {
            goLeft();
            stopY();
            setMoving();
        }
        else if (i < 20){
            goRight();
            stopY();
            setMoving();
        }
        else {
            stopX();
            stopY();
            setIdle();
        }
    }
}
