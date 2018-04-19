package entities;

import javafx.scene.media.AudioClip;

import java.util.Timer;
import java.util.TimerTask;

public class Zombie extends Movable {

    Sprite[] animations;

    private Direction walkDirection;
    private int walkDistance;
    private Sprite[] animation;
    private AudioClip[] audioClips;

    public Zombie(){}

    public Zombie(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 1.0);
        loadZombieAssets();
    }

    public void loadZombieAssets() {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        this.audioClips = super.loadAudio(zombieSounds);
        this.animation = loadSprites(zombieAnimations);
    }

    public void setAnimation(int i) {
        super.setSprite(this.animation[i]);
    }

    public void setAudio(int i) {
        super.playAudio(this.audioClips[i]);
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

    public int[] getZombieInfo() {
        int[] info = {
                getPositionX(),
                getPositionY(),
                getHealthPoints()};
        return info;
    }

    public void resetZombie(int randomX, int randomY) {
        int[] values = {randomX, randomY, 100};
        setZombieInfo(values);
    }

    public void setZombieInfo(int[] zombieInfo) {
        setPosition(zombieInfo[0], zombieInfo[1]);
        setHealthPoints(zombieInfo[2]);
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
                setIdle();
                this.walkDistance = 0;
                setAnimation(0);
                //playIdle();
                break;
            case NORTH:
                goUp();
                stopX();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case NORTHEAST:
                goUp();
                goRight();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case EAST:
                goRight();
                stopY();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTHEAST:
                goDown();
                goRight();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTH:
                goDown();
                stopX();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case SOUTHWEST:
                goDown();
                goLeft();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case WEST:
                goLeft();
                stopY();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            case NORTHWEST:
                goUp();
                goLeft();
                setMoving();
                this.walkDistance--;
                setAnimation(1);
                break;
            default:
                stopX();
                stopY();
                setIdle();
                this.walkDistance = 0;
                setAnimation(0);
        }
    }
}