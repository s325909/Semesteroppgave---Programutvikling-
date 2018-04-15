package entities;

import javafx.scene.Node;
import javafx.scene.media.AudioClip;

import javafx.scene.Node;

public class Enemy extends Movable {
    private enum DIRECTION { IDLE, UP, UPRIGHT, UPLEFT, DOWN, DOWNRIGHT, DOWNLEFT, LEFT, RIGHT };

    private DIRECTION walkDirection;
    private int walkDistance;

    public Enemy(){}

    public Enemy(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 1.0);
    }

    public void movement(Player player) {
        double diffx = player.getPositionX() - getPositionX();
        double diffy = player.getPositionY() - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);
        double distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        if(distance > 0 && distance < 1000 && this.walkDistance <= 0) {
            if (angle > 340 && angle <= 25) {
                this.walkDirection = DIRECTION.LEFT;
                this.walkDistance = 10;
            } else if (angle > 25 && angle <= 70) {
                this.walkDirection = DIRECTION.UPLEFT;
                this.walkDistance = 10;
            } else if (angle > 70 && angle <= 115) {
                this.walkDirection = DIRECTION.UP;
                this.walkDistance = 10;
            } else if (angle > 115 && angle <= 160) {
                this.walkDirection = DIRECTION.UPRIGHT;
                this.walkDistance = 10;
            } else if (angle > 160 && angle <= 205) {
                this.walkDirection = DIRECTION.RIGHT;
                this.walkDistance = 10;
            } else if (angle > 205 && angle <= 250) {
                this.walkDirection = DIRECTION.DOWNRIGHT;
                this.walkDistance = 10;
            } else if (angle > 250 && angle <= 295) {
                this.walkDirection = DIRECTION.DOWN;
                this.walkDistance = 10;
            } else if (angle > 295 && angle <= 340) {
                this.walkDirection = DIRECTION.DOWNLEFT;
                this.walkDistance = 10;
            }
        }

        if (this.walkDistance <= 0) {
            this.walkDirection = DIRECTION.IDLE;
            this.walkDistance = 0;
        }
        switch (this.walkDirection) {
            case IDLE:
                stopX();
                stopY();
                setIdle();
                this.walkDistance = 0;
                break;
            case UP:
                goUp();
                stopX();
                setMoving();
                this.walkDistance--;
                break;
            case UPRIGHT:
                goUp();
                goRight();
                setMoving();
                this.walkDistance--;
                break;
            case UPLEFT:
                goUp();
                goLeft();
                setMoving();
                this.walkDistance--;
                break;
            case DOWN:
                goDown();
                stopX();
                setMoving();
                this.walkDistance--;
                break;
            case DOWNRIGHT:
                goDown();
                goRight();
                setMoving();
                this.walkDistance--;
                break;
            case DOWNLEFT:
                goDown();
                goLeft();
                setMoving();
                this.walkDistance--;
                break;
            case LEFT:
                goLeft();
                stopY();
                setMoving();
                this.walkDistance--;
                break;
            case RIGHT:
                goRight();
                stopY();
                setMoving();
                this.walkDistance--;
            default:
                stopX();
                stopY();
                setIdle();
                this.walkDistance = 0;
        }
    }
}

