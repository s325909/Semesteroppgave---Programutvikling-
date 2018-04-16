package entities;

import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.util.List;

public class Movable extends Entity {

    public enum Direction {
        IDLE, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    private double velocityX;
    private double velocityY;

    private boolean idleSet = false;
    private Sprite spriteIdle;
    private boolean moveSet = false;
    private Sprite spriteMoving;
    private boolean meleeSet = false;
    private Sprite spriteMelee;

    private Direction direction;

    private double movementSpeed;

    private AudioClip[] basicSounds;

    public Movable() { }

    public Movable(int positionX, int positionY, double movementSpeed) {
        super(positionX, positionY);
        this.movementSpeed = movementSpeed;
    }

    public Movable(String filename, int positionX, int positionY, double movementSpeed) {
        super(filename, positionX, positionY);
        this.movementSpeed = movementSpeed;
    }

    public Movable(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints, double movementSpeed) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
        this.movementSpeed = movementSpeed;
    }

    //public void update(List<Entity> entityList, double time) {
    public void update(double time) {
        this.getSprite().setFrame(time);

        // Update actual position of object
        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionY(getPositionY() + (int)getVelocityY());

        // Update position of the visible representation of the object (Node and Sprite)
        this.getNode().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getNode().setTranslateY(this.getNode().getTranslateY() + velocityY);
        this.getSprite().getImageView().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getSprite().getImageView().setTranslateY(this.getNode().getTranslateY() + velocityY);

        // Change sprite direction upon entity direction change based on user input
        if (getVelocityX() > 0) {
            if (getVelocityY() < 0) {
                this.getSprite().getImageView().setRotate(315);
                this.direction = Direction.NORTHEAST;
            } else if (getVelocityY() > 0) {
                this.getSprite().getImageView().setRotate(45);
                this.direction = Direction.SOUTHEAST;
            } else {
                this.getSprite().getImageView().setRotate(0);
                this.direction = Direction.EAST;
            }
        } else if (getVelocityX() < 0) {
            if (getVelocityY() < 0) {
                this.getSprite().getImageView().setRotate(225);
                this.direction = Direction.NORTHWEST;
            } else if (getVelocityY() > 0) {
                this.getSprite().getImageView().setRotate(135);
                this.direction = Direction.SOUTHWEST;
            } else {
                this.getSprite().getImageView().setRotate(180);
                this.direction = Direction.WEST;
            }
        } else if (getVelocityX() == 0) {
            if (getVelocityY() < 0) {
                this.getSprite().getImageView().setRotate(270);
                this.direction = Direction.NORTH;
            } else if (getVelocityY() > 0) {
                this.getSprite().getImageView().setRotate(90);
                this.direction = Direction.SOUTH;
            }
        }

        // Check for collision between entities and update position and/or velocity
//        for(Entity entity : entityList) {
//            if(this.isColliding(entity)) {
//                setVelocityX(-0.5 * getVelocityX());
//                setVelocityY(-0.5 * getVelocityY());
//            }
//        }
    }

    // Functions for changing entity velocity
    public void goLeft() {
        setVelocityX(-movementSpeed);
    }

    public void goRight() {
        setVelocityX(movementSpeed);
    }

    public void stopX() {
        if (getVelocityX() > 0)
            setVelocityX(0.0);
        else if (getVelocityX() < 0)
            setVelocityX(0.0);
    }

    public void goUp() {
        setVelocityY(-movementSpeed);
    }

    public void goDown() {
        setVelocityY(movementSpeed);
    }

    public void stopY() {
        setVelocityY(0.0);
    }

    public void setSpriteIdle(String spriteFileName, String extension, int numberImages) {
        this.idleSet = true;
        this.spriteIdle = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteMoving(String spriteFileName, String extension, int numberImages) {
        this.moveSet = true;
        this.spriteMoving = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteMelee(String spriteFileName, String extension, int numberImages) {
        this.meleeSet = true;
        this.spriteMelee = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setIdle() {
        if (this.idleSet)
            super.setSprite(this.spriteIdle);
    }

    public void setMoving() {
        if (this.moveSet)
            super.setSprite(this.spriteMoving);
    }

    public void loadBasicSounds(String[] audioFiles) {
        this.basicSounds = loadAudio(audioFiles);
    }

    public void playBasicSounds(int index) {
        this.basicSounds[index].play();
    }

    public void playIdleSound(double time, int everyNthSecond) {
        int dur = (int)(time % everyNthSecond);
        System.out.println(dur);
        if (dur == 0) {
            playBasicSounds(0);
        }
    }

    public AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    public void setBasicSounds(AudioClip[] basicSounds) {
        this.basicSounds = basicSounds;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
