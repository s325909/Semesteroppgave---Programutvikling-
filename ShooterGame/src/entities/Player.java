package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends Movable {

    public Player() { }

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void movePlayer(KeyEvent keyEvent){
        //setMoving();
        if (keyEvent.getCode() == KeyCode.LEFT) {
            goLeft();
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            goRight();
        } else if (keyEvent.getCode() == KeyCode.UP && keyEvent.getCode() != KeyCode.DOWN) {
            goUp();
        } else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.getCode() != KeyCode.UP) {
            goDown();
        }
    }

    public void releasedPlayer(KeyEvent keyEvent){
        //setIdle();
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
            stopY();
        }
    }
}