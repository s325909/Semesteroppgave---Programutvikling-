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

    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction, List<Rock> rocks) {
        super(new AnimationHandler(images), null, positionX, positionY, 1, movementSpeed, rocks);
        this.damage = damage;
        this.direction = direction;
    }

    public Bullet(Image[] images, int positionX, int positionY, double movementSpeed, int damage, Direction direction, List<Rock> rocks, double velX, double velY) {
        this(images, positionX, positionY, movementSpeed, damage, direction, rocks);
        adjustVelX = velX;
        adjustVelY = velY;
    }

    public void bulletDirection() {
        switch(this.direction) {
            case NORTH:
                setVelocityX(0 + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                //getNode().setRotate(270);
                break;
            case NORTHEAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                //getNode().setRotate(315);
                break;
            case EAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(0 + adjustVelY);
                //getNode().setRotate(0);
                break;
            case SOUTHEAST:
                setVelocityX(getMovementSpeed() + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                //getNode().setRotate(45);
                break;
            case SOUTH:
                setVelocityX(0 + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                //getNode().setRotate(90);
                break;
            case SOUTHWEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(getMovementSpeed() + adjustVelY);
                //getNode().setRotate(135);
                break;
            case WEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(0 + adjustVelY);
                //getNode().setRotate(180);
                break;
            case NORTHWEST:
                setVelocityX(-getMovementSpeed() + adjustVelX);
                setVelocityY(-getMovementSpeed() + adjustVelY);
                //getNode().setRotate(225);
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

        /*Bounds oldBounds = this.getNode().getBoundsInParent();
        Bounds newBounds = new Rectangle(adjustVelX,adjustVelY,oldBounds.getWidth(),oldBounds.getHeight()).getLayoutBounds();
        for (Rock rock: rocksList) {
            if(rock.isColliding(newBounds)) {
                return;
            }
        }*/
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
