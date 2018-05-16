package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Arc;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class which handles all aspects around the Zombie Entity.
 * This includes
 */
public class Zombie extends Movable {

    private final int HUNTDISTANCE = 500;
    private final int ATTACKDISTANCE = 80;

    private int walkDistance;
    private long waitTime;
    private long soundWaitTime;

    private Direction walkDirection;
    private State state;
    private Queue<AnimationLengthPair> animationQueue;
    private List<Bullet> attackList;
    private Image[] placeHolder;

    public Zombie(Image[][] images, AudioClip[] audioClips, int positionX, int positionY, int healthPoints) {
        super(new AnimationHandler(images), audioClips, positionX, positionY, healthPoints, 1.0);
        this.animationQueue = new LinkedList<>();
        this.waitTime = 0;
        this.state = State.NORMAL;
        this.walkDirection = Direction.IDLE;
        this.attackList = new ArrayList<>();
        this.placeHolder = images[0];
    }

    /***
     * Method which controls the walk direction of enemies, whereas they are drawn towards the Player.
     * @param player Requires an object of type Player in order to decide which Entity the enemies
     *               should pursue.
     */
    public void chasePlayer(Player player) {
        double diffx = (player.getPositionX()) - getPositionX();
        double diffy = (player.getPositionY()) - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);
        double distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        if (distance < HUNTDISTANCE && distance >= ATTACKDISTANCE && this.walkDistance <= 0) {
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

        if (distance <= ATTACKDISTANCE && this.walkDistance <= 0) {
            this.state = State.ATTACK;
            this.walkDistance = 10;
        } else {
            this.state = State.NORMAL;
        }

        if (this.walkDistance <= 0) {
            this.walkDirection = Direction.IDLE;
            this.walkDistance = 0;
        }
    }

    /**
     * Method which handles movement and actions of the Zombie. Velocity and animation is set based on
     * the direction the Zombie is moving, and if state is changed to ATTACK, the attack() method is run.
     * @param damageMod Requires a number to represent the damage modifier based on set difficulty.
     */
    public void action(int damageMod) {
        int action;
        int animationLength = 0;
        switch (this.walkDirection) {
            case IDLE:
                setVelocity(0,0);
                this.walkDistance = 0;
                action = 0;
                break;
            case NORTH:
                setVelocity(0, -getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            case NORTHEAST:
                setVelocity(getMovementSpeed(), -getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            case EAST:
                setVelocity(getMovementSpeed(), 0);
                this.walkDistance--;
                action = 1;
                break;
            case SOUTHEAST:
                setVelocity(getMovementSpeed(), getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            case SOUTH:
                setVelocity(0, getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            case SOUTHWEST:
                setVelocity(-getMovementSpeed(), getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            case WEST:
                setVelocity(-getMovementSpeed(), 0);
                this.walkDistance--;
                action = 1;
                break;
            case NORTHWEST:
                setVelocity(-getMovementSpeed(), -getMovementSpeed());
                this.walkDistance--;
                action = 1;
                break;
            default:
                setVelocity(0, 0);
                this.walkDistance = 0;
                action = 0;
        }

        switch (state) {
            case NORMAL:
                break;
            case ATTACK:
                setVelocity(0, 0);
                action = 2;
                animationLength = 500;
                attack(damageMod);
                this.walkDistance = 0;
                break;
            case DAMAGED:
                break;
        }
        setAnimation(action, animationLength);
    }

    /**
     * Method which handles what happens a Zombie attacks.
     * There is a check to set the time between each time the Zombie can attack.
     * When the Zombie attacks, an invisible bullet which lives 0 seconds is created based on the position of the Zombie and placed in the
     * vicinity of the object and angled according to the direction of the Zombie.
     * This attack is then added to a List, which is retrieved in the onUpdate() method in Game for collision check.
     * @param damageMod Requires a number to represent the damage modifier based on set difficulty.
     */
    private void attack(int damageMod) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.waitTime) {
            int posX = getPositionX();
            int posY = getPositionY();
            switch (this.getDirection()) {
                case EAST:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case NORTHEAST:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case WEST:
                    posX += this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    break;
                case NORTHWEST:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += 2 * this.getAnimationHandler().getImageView().getImage().getHeight() / 3;
                    break;
                case NORTH:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case SOUTH:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case SOUTHEAST:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case SOUTHWEST:
                    posX += 2 * this.getAnimationHandler().getImageView().getImage().getWidth() / 3;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight() / 2;
                    break;
                case IDLE:
                    break;
            }
            int fireRate = 500;
            int damage = 10 * damageMod;
            Bullet zombieSlash = new Bullet(placeHolder, posX, posY, 0, damage, this.getDirection(), 1000);
            zombieSlash.setDrawn();
            zombieSlash.setNewRotation(getNewRotation());
            Arc knifeArc = new Arc(0, 0, 25, 30.0, 90, 180);
            knifeArc.setTranslateX(posX);
            knifeArc.setTranslateY(posY);
            knifeArc.getTransforms().add(new Rotate(180, 0, 0));
            zombieSlash.setNode(knifeArc);
            attackList.add(zombieSlash);
            this.waitTime = currentTime + fireRate;
        }
    }

    /**
     * Method for handling animations and queuing them.
     * Sets the duration of the animation, which affects how quickly each Image should cycle.
     * @param animationAction Requires a number in order to retrieve action type.
     * @param animationLength Requires a number to determine how long the queue should last.
     */
    private void setAnimation(int animationAction, int animationLength) {
        double duration = 0.064;
        if(animationAction == 2) {
            duration = 0.128;
        }
        boolean not = true;
        for (AnimationLengthPair pair : animationQueue) {
            if (pair.animationAction == animationAction)
                not = false;
        }
        if (not)
            animationQueue.add(new AnimationLengthPair(0, animationAction, animationLength, duration));
    }

    /**
     * Method for updating the animation and handling the queue order set, so that an animation
     * can be set to play through before a new one can be set.
     */
    @Override
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        AnimationLengthPair pair = animationQueue.peek();
        if (pair != null) {
            if (currentTime > this.waitTime) {
                getAnimationHandler().setImageAction(animationQueue.peek().animationAction);
                this.waitTime = currentTime + animationQueue.peek().time;
                getAnimationHandler().setDuration(animationQueue.peek().duration);
                animationQueue.remove();
            }
        }
    }

    /**
     * Method which calls super method in order to remove Node and ImageView of the Zombie.
     * It also updates the score when the Zombie dies, and creates a Drop object on its location.
     * @param game Requires the Game object of which to remove the Image and Node from.
     */
    @Override
    public void removeImage(Game game) {
        if (!isAlive()) {
            super.removeImage(game);
            game.scorePerKill();
            game.getDrops().add(game.getRandomDropType(this));
        }
    }

    /**
     * Method which will retrieve and return requested information about a Zombie object.
     * In this instance, Zombie shares the same relevant variables as the super class, Movable.
     * @return Returns an object of type MovementConfiguration set by a super method call.
     */
    public DataHandler.MovementConfiguration getZombieConfiguration() {
        return super.getMovementConfiguration();
    }

    /**
     * Method which will transfer provided zombieCfg's variables into corresponding variables in Zombie.
     * Variables are inherited from Movable, and are thus transferred and set through a super method call.
     * @param zombieCfg Requires an object of type MovementConfiguration.
     */
    public void setZombieConfiguration(DataHandler.MovementConfiguration zombieCfg) {
        super.setMovementConfiguration(zombieCfg);
    }

    public List<Bullet> getAttackList() {
        return attackList;
    }
}