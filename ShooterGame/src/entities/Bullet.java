package entities;

import gameCode.DataHandler;
import javafx.scene.image.Image;

import javax.xml.crypto.Data;
import java.util.List;

public class Bullet extends Movable {

    private int damage;
    private Direction direction;

    /*public Bullet(String filename, int positionX, int positionY, double movementSpeed, int damage, Direction direction) {
        super(filename, positionX, positionY, movementSpeed);
        this.damage = damage;
        this.direction = direction;
    }*/

    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction) {
        super(new AnimationHandler(images, 0), null, positionX, positionY, 1, movementSpeed);
        this.damage = damage;
        this.direction = direction;
    }

    public void bulletDirection() {
        switch(this.direction) {
            case NORTH:
                setVelocityX(0);
                setVelocityY(-getMovementSpeed());
                break;
            case NORTHEAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(-getMovementSpeed());
                break;
            case EAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(0);
                break;
            case SOUTHEAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(getMovementSpeed());
                break;
            case SOUTH:
                setVelocityX(0);
                setVelocityY(getMovementSpeed());
                break;
            case SOUTHWEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(getMovementSpeed());
                break;
            case WEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(0);
                break;
            case NORTHWEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(-getMovementSpeed());
                break;
            default:
                setVelocityX(0);
                setVelocityY(0);
        }
    }

    public void bulletCollision(List<Zombie> zombieList) {
        for(Zombie zombie : zombieList) {
            if (isColliding(zombie)) {
                this.setAlive(false);
                zombie.setHealthPoints(zombie.getHealthPoints() - this.getDamage());
                if (zombie.getHealthPoints() <= 0) {
                    zombie.setAlive(false);
                }
            }
        }
    }

    public DataHandler.Configuration getConfiguration() {
        DataHandler.Configuration bulletCfg = new DataHandler.Configuration();
        bulletCfg.posX = this.getPositionX();
        bulletCfg.posY = this.getPositionY();
        bulletCfg.velX = this.getVelocityX();
        bulletCfg.velY = this.getVelocityY();
        bulletCfg.movementSpeed = this.getMovementSpeed();
        bulletCfg.direction = this.getDirection();
        bulletCfg.damage = this.getDamage();
        return bulletCfg;
    }

    public void setConfiguration(DataHandler.Configuration bulletCfg) {
        this.setPosition(bulletCfg.posX, bulletCfg.posY);
        this.setTranslateNode(bulletCfg.posX, bulletCfg.posY);
        this.setVelocity(bulletCfg.velX, bulletCfg.velY);
        this.setMovementSpeed(bulletCfg.movementSpeed);
        this.setDirection(bulletCfg.direction);
        this.setDamage(bulletCfg.damage);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
