package entities;

import gameCode.DataHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.xml.crypto.Data;
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

    public DataHandler.BulletConfiguration getBulletConfiguration() {
        DataHandler.BulletConfiguration bulletCfg = new DataHandler.BulletConfiguration();
        bulletCfg.movementCfg = super.getMovementConfiguration();
        bulletCfg.damage = this.getDamage();
        return bulletCfg;
    }

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
