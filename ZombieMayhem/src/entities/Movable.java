package entities;

import gameCode.DataHandler;
import javafx.scene.media.AudioClip;

/**
 * Class which handles all Entities in the gameWindow that can move around. As such these
 * have statistics regarding health, velocity. As they can move around, their Image direction
 * is updated based on the direction of the inputted velocity. In turn, the Image may be directed
 * correctly in the direction of where the Movable Entity is headed.
 */
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

    private long idleSoundWait;
    private long walkSoundWait;
    private AudioClip[] audioClips;
    private Direction direction;

    /**
     * Constructor which adds the ability to control and use AudioClips to the super class constructor.
     * @param allAnimation Requires an AnimationHandler object, see AnimationHandler.
     * @param audioClips Requires an array of type AudioClip, which must contain the desired sounds for the Entity.
     * @param positionX Requires a desired X-coordinate of where to place the object.
     * @param positionY Requires a desired Y-coordinate of where to place the object.
     */
    Movable(AnimationHandler allAnimation, AudioClip[] audioClips, int positionX, int positionY) {
        super(allAnimation, positionX, positionY);
        this.audioClips = audioClips;
    }

    /**
     * Constructor which creates an AnimationHandler to handle the Entity's Images, and creates a Node
     * on the same position as the object, which is then used for collision check. The other fields sets a number of
     * necessary statistics for all Movable Entities, so that movement and interaction with other Entities can be altered.
     * @param allAnimation Requires an AnimationHandler object, see AnimationHandler.
     * @param audioClips Requires an array of type AudioClip, which must contain the desired sounds for the Entity.
     * @param positionX Requires a desired X-coordinate of where to place the object.
     * @param positionY Requires a desired Y-coordinate of where to place the object.
     * @param healthPoints Requires a set number of healthpoints to alter and decide when the object is dead.
     * @param movementSpeed Requires a number to decide the object's movementspeed, which in turn is set as the object's velocity.
     */
    Movable(AnimationHandler allAnimation, AudioClip[] audioClips, int positionX, int positionY, int healthPoints, double movementSpeed) {
        this(allAnimation, audioClips, positionX, positionY);
        this.healthPoints = healthPoints;
        this.movementSpeed = movementSpeed;
        this.velocityX = 0;
        this.velocityY = 0;
        this.direction = Direction.IDLE;
    }

    /**
     * Method which handles animation, and updates the position and rotation of the Entity, and its Node and Image.
     * @param time Requires the Game's timer.
     */
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

    /**
     * Method which updates the position of the Entity, and adjusts the Image direction based on velocity direction.
     */
    public void movement() {
        // The new position of the Entity
        newPositionX = (int)(getNode().getTranslateX() + velocityX);
        newPositionY = (int)(getNode().getTranslateY() + velocityY);

        // Set the Node to this position
        getNode().setTranslateX(newPositionX);
        getNode().setTranslateY(newPositionY);

        // Change image direction based on velocity direction
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

    /**
     * Method which sets the object's desired location back to the original position.
     * This is called when an Entity collides with either another Entity or the edge of the screen.
     * This will make the Entity unable to move into another Entity or past the screen.
     */
    public void moveBack() {
        newPositionX = getPositionX();
        newPositionY = getPositionY();
        direction = Direction.IDLE;
    }

    /**
     * Method which moves a object back a bit, such as if collision is true.
     * Currently not used.
     * @param object Requires a Movable object to adjust.
     */
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

    /**
     * Method which is run instead of the super class equivalent as not all Movable objects require to update animation.
     */
    public void updateAnimation() {
    }

    /**
     * Method which plays a sound every now and then, in order to simulate idle sounds Entities may create.
     * @param minThreshold Double between 0 and 1 to determine often method will run.
     *                     Number is compared to a Math.random number.
     * @param maxSeconds Requires a number to represent the maximum time between
     *                   each time the sound can be played. minThreshold value times
     *                   this value equates to minimum time.
     */
    public void playIdleSound(double minThreshold, int maxSeconds ) {
        long currentTime = System.currentTimeMillis();
        int clipIndex = 0;

        if (!audioClips[clipIndex].isPlaying()) {
            if (currentTime > idleSoundWait) {
                double random = Math.random();
                int duration = 0;
                if (random > minThreshold) {
                    duration = (int) (random * (maxSeconds * 1000));
                }
                playSound(0, 1);
                idleSoundWait = currentTime + duration;
            }
        }
    }

    /**
     * Method for playing a sound every now and then whilst Entity is walking.
     * Should ideally take in distance moved in order to determine when to play,
     * especially for Zombie entities.
     * @param clipLength Requires the length between each time the clip should play.
     */
    void walkSound(int clipLength) {
        long currentTime = System.currentTimeMillis();
        int i = 1;

        if(!audioClips[i].isPlaying()) {
            if (currentTime > walkSoundWait) {
                playSound(i, 1);
                walkSoundWait = currentTime + clipLength;
            }
        }
    }

    /**
     * Method for playing a sound when Entity is hit.
     * Player and Zombie has two hit sounds, and the method randomizes between playing the two.
     * Plays a death sound if the Entity will die in the given hit.
     * Checks if the clip is already playing.
     * @param damage Requires a number to represent the damage received, which then
     *               is used to determine whether the Entity will die in the next hit or not.
     */
    public void hitSound(int damage) {
        double random = Math.random();
        int i;

        if (healthPoints > damage) {
            if (random > 0.5)
                i = 2;
            else
                i = 3;
        } else
            i = 4;

        if (!audioClips[i].isPlaying())
            playSound(i, 1);
    }

    /**
     * Method for selecting a AudioClip, settings its playback rate/speed, and playing it.
     * @param i Requires the index of the clip to play.
     * @param rate Requires a number to set the rate/speed of which to play the clip.
     *             A higher number equates to a quicker execution.
     */
    void playSound(int i, double rate) {
        audioClips[i].setRate(rate);
        audioClips[i].play();
    }

    /**
     * Method which will retrieve and return requested information about a Movable object.
     * Creates a new MovementConfiguration object from the DataHandler class, and transfers
     * variables inherited from Entity, combined with variables specific to the Movable class,
     * into the corresponding variables in movementCfg.
     * @return Returns the object movementCfg of type MovementConfiguration.
     */
    DataHandler.MovementConfiguration getMovementConfiguration() {
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
    void setMovementConfiguration(DataHandler.MovementConfiguration movementCfg) {
        super.setEntityConfiguration(movementCfg.entityCfg);
        setHealthPoints(movementCfg.health);
        setVelocity(movementCfg.velX, movementCfg.velY);
        setMovementSpeed(movementCfg.movementSpeed);
        setDirection(movementCfg.direction);
        setNewRotation(movementCfg.rotation);
    }

    /**
     * Method which sets velocity in both directions simultaneously.
     * @param velocityX Requires object's velocity in x-coordinate direction.
     * @param velocityY Requires object's velocity in y-coordinate direction.
     */
    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    AudioClip[] getAudioClips() {
        return audioClips;
    }

    public void setAudioClips(AudioClip[] audioClips) {
        this.audioClips = audioClips;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int health) {
        this.healthPoints = health;
    }

    private double getVelocityX() {
        return velocityX;
    }

    void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    void addVelocityX(double velocityX) {
        this.velocityX += velocityX;
    }

    private double getVelocityY() {
        return velocityY;
    }

    void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    void addVelocityY(double velocityY) {
        this.velocityY += velocityY;
    }

    double getMovementSpeed() {
        return movementSpeed;
    }

    private void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    double getNewRotation() {
        return newRotation;
    }

    void setNewRotation(double newRotation) {
        this.newRotation = newRotation;
    }

    /**
     * Inner class used for handling animations that need to display a certain amount of time,
     * before a new animation can be set and displayed.
     */
    class AnimationLengthPair {
        int animationType;
        int animationAction;
        long time;
        double duration;

        AnimationLengthPair(int type, int action, long time, double duration) {
            this.animationType = type;
            this.animationAction = action;
            this.time = time;
            this.duration = duration;
        }
    }
}
