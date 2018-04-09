package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends Movable {

    public Player(){}

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    /***
     * Method for turning player keyboard input into movement on the screen
     * @param keyEvent
     */
    public void movePlayer(KeyEvent keyEvent){
<<<<<<< HEAD
        if (keyEvent.getCode() == KeyCode.LEFT) {
            setMoving();
            goLeft();
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            setMoving();
            goRight();
        } else if (keyEvent.getCode() == KeyCode.UP && keyEvent.getCode() != KeyCode.DOWN) {
            setMoving();
            goUp();
        } else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.getCode() != KeyCode.UP) {
            setMoving();
            goDown();
        }

        if (keyEvent.getCode() == KeyCode.E) {
            setMelee();
            System.out.println("Melee!");
        }

        if (keyEvent.getCode() == KeyCode.SPACE) {
            setShooting();
            System.out.println("Fire!");
        }

        if (keyEvent.getCode() == KeyCode.R) {
            setReloading();
            System.out.println("Reload!");
=======
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
>>>>>>> master
        }
    }

    /***
     * Method for stopping movement upon key released
     * @param keyEvent
     */
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