package entities;

import gameCode.DataHandler;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Zombie extends Movable {

    private Direction walkDirection;
    private State mode;
    private int walkDistance;
    private Queue<AnimationLengthPair> animationQueue;
    private long waitTime;

    public Zombie(Image[][] images, AudioClip[] audioClips, int positionX, int positionY, int healthPoints) {
        super(new AnimationHandler(images), audioClips, positionX, positionY, healthPoints, 1.0);
        this.animationQueue = new LinkedList<AnimationLengthPair>();
        this.waitTime = 0;
        this.mode = State.NORMAL;
    }

    /***
     * Method which controls the bulletDirection of enemies, whereas they are drawn towards the Player.
     * @param player Requires an object of type Player in order to decide which Entity the enemies
     *               should pursue.
     */
    public void movement(Player player) {
        double diffx = (player.getPositionX()) - getPositionX();
        double diffy = (player.getPositionY()) - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);
        double distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        if(distance < 500 && distance >= 50) {
            if (angle > 340 || angle <= 25) {
                this.walkDirection = Direction.WEST;
                this.walkDistance = 10;
            } else if (angle > 25 && angle <= 70) {
                this.walkDirection = Direction.NORTHWEST;
                this.walkDistance = 10;
            } else if (angle > 70 && angle <= 115) {
                this.walkDirection = Direction.NORTH;
                this.walkDistance = 10;
            } else if (angle > 115 && angle <= 160) {
                this.walkDirection = Direction.NORTHEAST;
                this.walkDistance = 10;
            } else if (angle > 160 && angle <= 205) {
                this.walkDirection = Direction.EAST;
                this.walkDistance = 10;
            } else if (angle > 205 && angle <= 250) {
                this.walkDirection = Direction.SOUTHEAST;
                this.walkDistance = 10;
            } else if (angle > 250 && angle <= 295) {
                this.walkDirection = Direction.SOUTH;
                this.walkDistance = 10;
            } else if (angle > 295 && angle <= 340) {
                this.walkDirection = Direction.SOUTHWEST;
                this.walkDistance = 10;
            }
        }

        if(distance <= 50) {
            this.mode = State.ATTACK;
        } else {
            this.mode = State.NORMAL;
        }

        if (this.walkDistance <= 0) {
            this.walkDirection = Direction.IDLE;
            this.walkDistance = 0;
        }
        int action;
        switch (this.walkDirection) {
            case IDLE:
                stopX();
                stopY();
                this.walkDistance = 0;
                action = 0;
                //setAnimation(0);
                break;
            case NORTH:
                goUp();
                stopX();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case NORTHEAST:
                goUp();
                goRight();;
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case EAST:
                goRight();
                stopY();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case SOUTHEAST:
                goDown();
                goRight();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case SOUTH:
                goDown();
                stopX();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case SOUTHWEST:
                goDown();
                goLeft();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case WEST:
                goLeft();
                stopY();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            case NORTHWEST:
                goUp();
                goLeft();
                this.walkDistance--;
                action = 1;
                //setAnimation(1);
                break;
            default:
                stopX();
                stopY();
                this.walkDistance = 0;
                action = 0;
                //setAnimation(0);
        }

        switch (mode) {
            case NORMAL:
                break;
            case ATTACK:
                action = 2;
                break;
            case DAMAGED:
                break;
        }
        setAnimation(action);
    }

    /**
     *
     * @param animationAction
     */
    private void setAnimation(int animationAction) {
        long time = 0;
        if(animationAction == 2) {
            time = 200;
        }
        boolean not = true;
        for (AnimationLengthPair pair : animationQueue) {
            if (pair.action == animationAction)
                not = false;
        }
        if (not)
            animationQueue.add(new AnimationLengthPair(animationAction, time));
    }

    /**
     *
     */
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        AnimationLengthPair pair = animationQueue.peek();
        if (pair != null) {
            if (currentTime > this.waitTime) {
                getAnimationHandler().setImageAction(animationQueue.peek().action);
                this.waitTime = currentTime + animationQueue.peek().time;
                animationQueue.remove();
            }
        }
    }

    public DataHandler.Configuration getConfiguration() {
        DataHandler.Configuration zombieCfg = new DataHandler.Configuration();
        zombieCfg.health = this.getHealthPoints();
        zombieCfg.posX = this.getPositionX();
        zombieCfg.posY = this.getPositionY();
        zombieCfg.velX = this.getVelocityX();
        zombieCfg.velY = this.getVelocityY();
        zombieCfg.movementSpeed = this.getMovementSpeed();
        zombieCfg.direction = this.getDirection();
        return zombieCfg;
    }

    public void setConfiguration(DataHandler.Configuration zombieCfg) {
        this.setPosition(zombieCfg.posX, zombieCfg.posY);
        this.setTranslateNode(zombieCfg.posX, zombieCfg.posY);
        this.setHealthPoints(zombieCfg.health);
        this.setVelocity(zombieCfg.velX, zombieCfg.velY);
        this.setMovementSpeed(zombieCfg.movementSpeed);
        this.setDirection(zombieCfg.direction);
    }
}