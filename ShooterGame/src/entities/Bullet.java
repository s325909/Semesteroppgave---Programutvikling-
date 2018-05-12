package entities;

import gameCode.DataHandler;
import javafx.scene.image.Image;
import java.util.List;


public class Bullet extends Movable {

    private int damage;
    private Direction direction;
    private double adjustVelX;
    private double adjustVelY;

    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction) {
        super(new AnimationHandler(images), null, positionX, positionY, 1, movementSpeed);
        this.damage = damage;
        this.direction = direction;
    }

    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction, double velX, double velY) {
        this(images, positionX, positionY, movementSpeed, damage, direction);
        adjustVelX = velX;
        adjustVelY = velY;
    }

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
     * Method which will retrieve and return requested information about a Bullet object.
     * Creates a new BulletConfiguration object from the DataHandler class, and transfers
     * variables inherited from Movable, combined with variables specific ot the Bullet class,
     * into the corresponding variables in bulletCfg.
     * @return Returns the object bulletCfg of type BulletConfiguration.
     */
    public DataHandler.BulletConfiguration getBulletConfiguration() {
        DataHandler.BulletConfiguration bulletCfg = new DataHandler.BulletConfiguration();
        bulletCfg.movementCfg = super.getMovementConfiguration();
        bulletCfg.damage = this.getDamage();
        return bulletCfg;
    }

    /**
     * Method which will transfer provided bulletCfg's variables into corresponding variables in Bullet.
     * Variables inherited from Movable are transferred and set through a super method call.
     * Further, variables specific to the Bullet class are transferred and set.
     * @param bulletCfg Requires an object of type BulletConfiguration.
     */
    public void setBulletConfiguration(DataHandler.BulletConfiguration bulletCfg) {
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
