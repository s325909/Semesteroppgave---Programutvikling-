package entities;

import gameCode.DataHandler;
import javafx.scene.media.AudioClip;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Zombie extends Movable {

    private Direction walkDirection;
    private int walkDistance;
    private Sprite[] allAnimation;
    private AnimationHandler allAnimationer;
    private Queue<SpritePair2> animationQueue;
    private long waitTime;

    public Zombie(Sprite[] allAnimation, AudioClip[] audioClips, int positionX, int positionY, int healthPoints) {
        super(allAnimation[0], audioClips, positionX, positionY, healthPoints, 1.0);
        this.allAnimation = allAnimation;
        this.animationQueue = new LinkedList<SpritePair2>();
        this.waitTime = 0;
    }

    public Zombie(AnimationHandler allAnimation, AudioClip[] audioClips, int positionX, int positionY, int healthPoints) {
        super(allAnimation, audioClips, positionX, positionY, healthPoints, 1.0);
        this.allAnimationer = allAnimation;
        this.animationQueue = new LinkedList<SpritePair2>();
        this.waitTime = 0;
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

        if(distance > 50 && distance < 1000 && this.walkDistance <= 0) {
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

        if(distance <= 50 && this.walkDistance <= 0) {
            attack();
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

    @Override
    public void update(double time) {
        this.getAnimationHandler().setFrame(time);

        // Update actual position of object
        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionY(getPositionY() + (int)getVelocityY());

        // Update position of the visible representation of the object (Node and Sprite)
        this.getNode().setTranslateX(this.getNode().getTranslateX() + getVelocityX());
        this.getNode().setTranslateY(this.getNode().getTranslateY() + getVelocityY());
        this.getAnimationHandler().getImageView().setTranslateX(this.getNode().getTranslateX() + getVelocityX());
        this.getAnimationHandler().getImageView().setTranslateY(this.getNode().getTranslateY() + getVelocityY());

        // Change sprite direction upon entity direction change based on user input
        if (getVelocityX() > 0) {
            if (getVelocityY() < 0) {
                this.getAnimationHandler().getImageView().setRotate(315);
                setDirection(Direction.NORTHEAST);
            } else if (getVelocityY() > 0) {
                this.getAnimationHandler().getImageView().setRotate(45);
                setDirection(Direction.SOUTHEAST);
            } else {
                this.getAnimationHandler().getImageView().setRotate(0);
                setDirection(Direction.EAST);
            }
        } else if (getVelocityX() < 0) {
            if (getVelocityY() < 0) {
                this.getAnimationHandler().getImageView().setRotate(225);
                setDirection(Direction.NORTHWEST);
            } else if (getVelocityY() > 0) {
                this.getAnimationHandler().getImageView().setRotate(135);
                setDirection(Direction.SOUTHWEST);
            } else {
                this.getAnimationHandler().getImageView().setRotate(180);
                setDirection(Direction.WEST);
            }
        } else if (getVelocityX() == 0) {
            if (getVelocityY() < 0) {
                this.getAnimationHandler().getImageView().setRotate(270);
                setDirection(Direction.NORTH);
            } else if (getVelocityY() > 0) {
                this.getAnimationHandler().getImageView().setRotate(90);
                setDirection(Direction.SOUTH);
            }
        } else if (getVelocityX() == 0 && getVelocityY() == 0) {
            setDirection(Direction.IDLE);
        }

        // Check for collision between entities and update position and/or velocity
//        for(Entity entity : entityList) {
//            if(this.isColliding(entity)) {
//                setVelocityX(-0.5 * getVelocityX());
//                setVelocityY(-0.5 * getVelocityY());
//            }
//        }
    }

    public void attack() {
        setAnimation(2);
    }

    /**
     *
     * @param i
     */
    private void setAnimation(int i) {
        long time = 0;
        if(i == 2) {
            time = 500;
        }
        animationQueue.add(new SpritePair2(i, time));
        //animationQueue.add(new SpritePair(this.allAnimation[i], time));
    }

    /**
     *
     */
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        SpritePair2 pair = animationQueue.peek();
        if (pair != null) {
            if (currentTime > this.waitTime) {
                //System.out.println("Change animation!");
                this.allAnimationer.setI(animationQueue.peek().i);
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

//    public void setAnimation(int i) {
//        super.setSprite(this.allAnimation[i]);
//    }
}