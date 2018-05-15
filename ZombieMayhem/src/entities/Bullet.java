package entities;

import gameCode.SaveHandler;
import javafx.scene.image.Image;
import java.util.List;

/**
 * Class which handles the creation of bullets when the Player fires a weapon, and is also used for handling
 * Player's knife arc and Zombie's attack. In those instances, the Bullet has no movementspeed and lives for 0 seconds.
 * The Bullet is created dependent on the Entities position and Direction.
 */
public class Bullet extends Movable {

    private int damage;
    private Direction direction;
    private double adjustVelX;
    private double adjustVelY;
    private long timeToLive;

    /**
     * Constructor which sets the Bullet's Image, position, speed, damage, direction and sets a time when to remove it.
     * This constructor is used for creation of most Bullets, including those that represent the Player's knife attack and Zombie's attack.
     * @param images Requires an Image to display. A single Image within the array simply means it won't cycle as with the animation of other Entities.
     * @param positionX Requires a X-coordinate of where to be created.
     * @param positionY Requires a Y-coordinate of where to be created.
     * @param movementSpeed Requires a number to represent the speed of the Bullet, which in turn is set as its velocity.
     * @param damage Requires a number to determine how much damage is should do upon impact.
     * @param direction Requires a Direction of where to go, based on Entity's direction.
     * @param timeToLive Requires a number to determine its lifetime.
     */
    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction, long timeToLive) {
        super(new AnimationHandler(images), null, positionX, positionY, 1, movementSpeed);
        this.damage = damage;
        this.direction = direction;
        this.timeToLive = System.currentTimeMillis() + timeToLive;
    }

    /**
     * Constructor which functions as above, but will use an adjusted velocity in order to adjust is direction.
     * This constructor is only used for shotgun pellets.
     * @param images Requires an Image to display. A single Image within the array simply means it won't cycle as with the animation of other Entities.
     * @param positionX Requires a X-coordinate of where to be created.
     * @param positionY Requires a Y-coordinate of where to be created.
     * @param movementSpeed Requires a number to represent the speed of the Bullet, which in turn is set as its velocity.
     * @param damage Requires a number to determine how much damage is should do upon impact.
     * @param direction Requires a Direction of where to go, based on Entity's direction.
     * @param timeToLive Requires a number to determine its lifetime.
     * @param velX Requires a value to adjust its trajectory.
     * @param velY Requires a value to adjust its trajectory.
     */
    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction, long timeToLive, double velX, double velY) {
        this(images, positionX, positionY, movementSpeed, damage, direction, timeToLive);
        adjustVelX = velX;
        adjustVelY = velY;
    }

    /**
     * Method which adjusts the Bullet's velocity based on the given Direction.
     * The velocity is set to Bullet's movementSpeed, and how this is set (whether X and Y direction and negative prefix)
     * is dependent on which Direction it is meant to follow.
     * Second constructor will provide two new values of which will further alter the Direction.
     * This method is continuously called in the onUpdate method in Game.
     */
    public void bulletDirection() {
        switch(this.direction) {
            case NORTH:
                setVelocityX(0 + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                break;
            case NORTHEAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                break;
            case EAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(0 + adjustVelY);
                break;
            case SOUTHEAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                break;
            case SOUTH:
                setVelocityX(0 + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                break;
            case SOUTHWEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                break;
            case WEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(0 + adjustVelY);
                break;
            case NORTHWEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                break;
            default:
                setVelocityX(0 + adjustVelX);
                setVelocityY(0 + adjustVelY);
        }
    }

    /**
     * Method which handles collision with Zombies and Rocks.
     * Bullet is set to dead in either case, but Zombie will lose health equivalent to Bullet's damage.
     * @param zombieList Requires the Game's list of Zombies.
     * @param rocksList Requires the Game's list of Rocks.
     */
    public void bulletCollision(List<Zombie> zombieList, List<Rock> rocksList) {
        for (Zombie zombie : zombieList) {
            if (isColliding(zombie)) {
                this.setAlive(false);
                zombie.setHealthPoints(zombie.getHealthPoints() - this.getDamage());
                if (zombie.getHealthPoints() <= 0) {
                    zombie.setAlive(false);
                }
            }
        }

        for (Rock rock : rocksList) {
            if (isColliding(rock)){
                this.setAlive(false);
            }
        }
    }

    /**
     * Method which functions as in superclass, but will set the bullet to dead once the timeToLive expires.
     * Position and rotation of the object, its Node and its ImageView is updated.
     * @param time Requires a double which represents the Game's timer in order to update animations in superclass.
     *             This method is continuously run, and time variable continuously changes.
     */
    @Override
    public void update(double time) {
        super.update(time);

        if(System.currentTimeMillis() > timeToLive) {
            setAlive(false);
        }
    }

    /**
     * Method which will retrieve and return requested information about a Bullet object.
     * Creates a new BulletConfiguration object from the SaveHandler class, and transfers
     * variables inherited from Movable, combined with variables specific to the Bullet class,
     * into the corresponding variables in bulletCfg.
     * @return Returns the object bulletCfg of type BulletConfiguration.
     */
    public SaveHandler.BulletConfiguration getBulletConfiguration() {
        SaveHandler.BulletConfiguration bulletCfg = new SaveHandler.BulletConfiguration();
        bulletCfg.movementCfg = super.getMovementConfiguration();
        bulletCfg.damage = getDamage();
        bulletCfg.remainingTime = timeToLive - System.currentTimeMillis();
        return bulletCfg;
    }

    /**
     * Method which will transfer provided bulletCfg's variables into corresponding variables in Bullet.
     * Variables inherited from Movable are transferred and set through a super method call.
     * Further, variables specific to the Bullet class are transferred and set.
     * @param bulletCfg Requires an object of type BulletConfiguration.
     */
    public void setBulletConfiguration(SaveHandler.BulletConfiguration bulletCfg) {
        super.setMovementConfiguration(bulletCfg.movementCfg);
        this.setDamage(bulletCfg.damage);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
