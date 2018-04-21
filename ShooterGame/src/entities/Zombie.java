package entities;

import javafx.scene.media.AudioClip;

import java.util.Timer;
import java.util.TimerTask;

public class Zombie extends Movable {

    private Direction walkDirection;
    private int walkDistance;
    private Sprite[] allAnimation;
    private AudioClip[] audioClips;

    public Zombie(){}

    public Zombie(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 1.0);
    }

    public Zombie(Sprite[] allAnimation, AudioClip[] audioClips, int positionX, int positionY, int healthPoints) {
        super(allAnimation[0], positionX, positionY, healthPoints, 1.0);
        this.allAnimation = allAnimation;
        this.audioClips = audioClips;
    }

    /***
     * Method which controls the bulletDirection of enemies, whereas they are drawn towards the Player.
     * @param player Requires an object of type Player in order to decide which Entity the enemies
     *               should pursue.
     */
    public void movement(Player player) {
        double diffx = player.getPositionX() - getPositionX();
        double diffy = player.getPositionY() - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);
        double distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        if(distance > 0 && distance < 1000 && this.walkDistance <= 0) {
            if (angle > 340 && angle <= 25) {
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

        if (this.walkDistance <= 0) {
            this.walkDirection = Direction.IDLE;
            this.walkDistance = 0;
        }
        switch (this.walkDirection) {
            case IDLE:
                stopX();
                stopY();
                this.walkDistance = 0;
                setAnimation(0);
                break;
            case NORTH:
                goUp();
                stopX();
                this.walkDistance--;
                setAnimation(1);
                break;
            case NORTHEAST:
                goUp();
                goRight();;
                this.walkDistance--;
                setAnimation(1);
                break;
            case EAST:
                goRight();
                stopY();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTHEAST:
                goDown();
                goRight();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTH:
                goDown();
                stopX();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTHWEST:
                goDown();
                goLeft();
                this.walkDistance--;
                setAnimation(1);
                break;
            case WEST:
                goLeft();
                stopY();
                this.walkDistance--;
                setAnimation(1);
                break;
            case NORTHWEST:
                goUp();
                goLeft();
                this.walkDistance--;
                setAnimation(1);
                break;
            default:
                stopX();
                stopY();
                this.walkDistance = 0;
                setAnimation(0);
        }
    }

    public void playIdle() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setAudio(0);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000); //start immediately, 1000ms period
    }

    public void setAnimation(int i) {
        super.setSprite(this.allAnimation[i]);
    }

    public void setAudio(int i) {
        super.playAudio(this.audioClips[i]);
    }
}