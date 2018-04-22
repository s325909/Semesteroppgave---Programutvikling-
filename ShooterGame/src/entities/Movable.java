package entities;

import javafx.scene.media.AudioClip;

public class Movable extends Entity {

    public enum Direction {
        IDLE, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    private double velocityX;
    private double velocityY;
    private double movementSpeed;

    private AudioClip[] basicSounds;
    private AudioClip[] audioClips;
    private Direction direction;

    public Movable(String filename, int positionX, int positionY, double movementSpeed) {
        super(filename, positionX, positionY);
        this.movementSpeed = movementSpeed;
        this.velocityX = 0;
        this.velocityY = 0;
        this.direction = Direction.IDLE;
    }

    public Movable(Sprite idleSprite, int positionX, int positionY, int healthPoints, double movementSpeed) {
        super(idleSprite, positionX, positionY, healthPoints);
        this.movementSpeed = movementSpeed;
        this.velocityX = 0;
        this.velocityY = 0;
        this.direction = Direction.IDLE;
    }

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
        } else if (getVelocityX() == 0 && getVelocityY() == 0) {
            this.direction = Direction.IDLE;
        }

        // Check for collision between entities and update position and/or velocity
//        for(Entity entity : entityList) {
//            if(this.isColliding(entity)) {
//                setVelocityX(-0.5 * getVelocityX());
//                setVelocityY(-0.5 * getVelocityY());
//            }
//        }
    }

    public void goLeft() {
        setVelocityX(-movementSpeed);
    }

    public void goRight() {
        setVelocityX(movementSpeed);
    }

    public void stopX() {
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

    public void playIdleSound(double time, int everyNthSecond) {
        int dur = (int)(time % everyNthSecond);
        System.out.println(dur);
        if (dur == 0) {
        }
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

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
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

    public Direction getDirecitonFromString(String direction) {
        switch(direction) {
            case "north":
                return Direction.NORTH;
            case "northeast":
                return Direction.NORTHEAST;
            case "east":
                return Direction.EAST;
            case "southeast":
                return Direction.SOUTHEAST;
            case "south":
                return Direction.SOUTH;
            case "southwest":
                return Direction.SOUTHWEST;
            case "west":
                return Direction.WEST;
            case "northwest":
                return Direction.NORTHWEST;
            default:
                return Direction.NORTH;
        }
    }
}
