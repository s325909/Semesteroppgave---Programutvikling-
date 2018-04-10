package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends Movable {

    public Player() { }

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void movePlayer(KeyEvent keyEvent){
        setMoving();
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            goLeft();
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            goRight();
        } else if (keyEvent.getCode() == KeyCode.UP && keyEvent.getCode() != KeyCode.DOWN
                || keyEvent.getCode() == KeyCode.W && keyEvent.getCode() != KeyCode.S) {
            goUp();
        } else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.getCode() != KeyCode.UP
                || keyEvent.getCode() == KeyCode.S && keyEvent.getCode() != KeyCode.W) {
            goDown();
        }

        if (keyEvent.getCode() == KeyCode.Q) {
            setAttack();
        }
    }

    public void releasedPlayer(KeyEvent keyEvent){
        setIdle();
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT
                || keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.D) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN
                || keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.S) {
            stopY();
        }
    }
}