package entities;

import gameCode.DataHandler;
import javafx.scene.media.AudioClip;

public class Movable extends Entity {
    public enum Direction {
        IDLE, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    public enum State {
        NORMAL, DAMAGED, ATTACK
    }

    private int healthPoints;
    private double velocityX;
    private double velocityY;
    private int newPositionX;
    private int newPositionY;
    private double newRotation;
    private double movementSpeed;

    private AudioClip[] audioClips;
    private Direction direction;


    public Movable(AnimationHandler allAnimation, AudioClip[] audioClips, int positionX, int positionY, int healthPoints, double movementSpeed) {
        super(allAnimation, positionX, positionY);
        this.audioClips = audioClips;
        this.healthPoints = healthPoints;
        this.movementSpeed = movementSpeed;
        this.movementSpeed = movementSpeed;
        this.velocityX = 0;
        this.velocityY = 0;
        this.direction = Direction.IDLE;
    }

    public void movement() { //List<Entity> objects) {
        //Also, need to replace position if window is resized.
        //Calculates the new position on the screen
        newPositionX = (int)(getNode().getTranslateX() + velocityX);
        newPositionY = (int)(getNode().getTranslateY() + velocityY);

        //Move node to check for collision
        getNode().setTranslateX(newPositionX);
        getNode().setTranslateY(newPositionY);

        // Change sprite direction upon entity direction change based on user input
        if (getVelocityX() > 0) {
            if (getVelocityY() < 0) {
                newRotation = 315;
                direction = Direction.NORTHEAST;
            } else if (getVelocityY() > 0) {
                newRotation = 45;
                direction = Direction.SOUTHEAST;
            } else {
                newRotation = 0;
                direction = Direction.EAST;
            }
        } else if (getVelocityX() < 0) {
            if (getVelocityY() < 0) {
                newRotation = 225;
                direction = Direction.NORTHWEST;
            } else if (getVelocityY() > 0) {
                newRotation = 135;
                direction = Direction.SOUTHWEST;
            } else {
                newRotation = 180;
                direction = Direction.WEST;
            }
        } else if (getVelocityX() == 0) {
            if (getVelocityY() < 0) {
                newRotation = 270;
                direction = Direction.NORTH;
            } else if (getVelocityY() > 0) {
                newRotation = 90;
                direction = Direction.SOUTH;
            }
        } else if (getVelocityX() == 0 && getVelocityY() == 0) {
            direction = Direction.IDLE;
        }
    }

    public void moveBack() {
        newPositionX = getPositionX();
        newPositionY = getPositionY();
        direction = Direction.IDLE;
    }

    public void moveAway(Movable object) {
        double diffx = (object.getPositionX()) - getPositionX();
        double diffy = (object.getPositionY()) - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);

        double changeInX = 0, changeInY = 0;
        if (angle > 340 || angle <= 25) {
            changeInX = -0.5;
        } else if (angle > 25 && angle <= 70) {
            changeInX = -0.5;
            changeInY = -0.5;
        } else if (angle > 70 && angle <= 115) {
            changeInY = -0.5;
        } else if (angle > 115 && angle <= 160) {
            changeInX = 0.5;
            changeInY = -0.5;
        } else if (angle > 160 && angle <= 205) {
            changeInX = 0.5;
        } else if (angle > 205 && angle <= 250) {
            changeInX = 0.5;
            changeInY = 0.5;
        } else if (angle > 250 && angle <= 295) {
            changeInY = 0.5;
        } else if (angle > 295 && angle <= 340) {
            changeInX = -0.5;
            changeInY = 0.5;
        }

        newPositionX = (int)(getNode().getTranslateX() - changeInX);
        newPositionY = (int)(getNode().getTranslateY() - changeInY);
    }

    @Override
    public void update(double time) {
        super.update(time);
        // Update actual position of object
        setPositionX(newPositionX);
        setPositionY(newPositionY);
        // Update position of the visible representation of the object (Node and Sprite)
        getNode().setTranslateX(newPositionX);
        getNode().setTranslateY(newPositionY);
        getAnimationHandler().getImageView().setTranslateX(newPositionX);
        getAnimationHandler().getImageView().setTranslateY(newPositionY);
        getAnimationHandler().getImageView().setRotate(newRotation);
        getNode().setRotate(newRotation);
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

    public void playAudioClip(int i) {
        this.audioClips[i].play();
    }

    /**
     * Method which will retrieve and return requested information about a Movable object.
     * Creates a new MovementConfiguration object from the DataHandler class, and transfers
     * variables inherited from Entity, combined with variables specific to the Movable class,
     * into the corresponding variables in movementCfg.
     * @return Returns the object movementCfg of type MovementConfiguration.
     */
    public DataHandler.MovementConfiguration getMovementConfiguration() {
        DataHandler.MovementConfiguration movementCfg = new DataHandler.MovementConfiguration();
        movementCfg.entityCfg = super.getEntityConfiguration();
        movementCfg.health = getHealthPoints();
        movementCfg.velX = getVelocityX();
        movementCfg.velY = getVelocityY();
        movementCfg.movementSpeed = getMovementSpeed();
        movementCfg.direction = getDirection();
        movementCfg.rotation = newRotation;
        return movementCfg;
    }

    /**
     * Method which will transfer provided movementCfg's variables into corresponding variables in Movable.
     * Variables inherited from Entity are transferred and set through a super method call.
     * Further, variables specific to the Movable class are transferred and set.
     * @param movementCfg Requires an object of type MovementConfiguration.
     */
    public void setMovementConfiguration(DataHandler.MovementConfiguration movementCfg) {
        super.setEntityConfiguration(movementCfg.entityCfg);
        setHealthPoints(movementCfg.health);
        setVelocity(movementCfg.velX, movementCfg.velY);
        setMovementSpeed(movementCfg.movementSpeed);
        setDirection(movementCfg.direction);
        setNewRotation(movementCfg.rotation);
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int health) {
        this.healthPoints = health;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void addVelocityX(double velocityX) {
        this.velocityX += velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void addVelocityY(double velocityY) {
        this.velocityY += velocityY;
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getNewRotation() {
        return newRotation;
    }

    public void setNewRotation(double newRotation) {
        this.newRotation = newRotation;
    }

    /**
     * Inner class for handling length of animation
     */
    public class AnimationLengthPair {
        int animationType;
        int animationAction;
        long time;
        double duration;

        public AnimationLengthPair(int type, int action, long time, double duration) {
            this.animationType = type;
            this.animationAction = action;
            this.time = time;
            this.duration = duration;
        }
    }
}
