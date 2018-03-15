package Entities;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Movable extends Entity {

    private double velocityX;
    private double velocityY;

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

    public Movable() {
    }

    public Movable(Node node, int x, int y) {
        super(node, x, y);
    }

    public Movable(String filename, int x, int y) {
        super(filename, x, y);
    }

    public Movable(int x, int y, int health, double velocityX, double velocityY) {
        super(x, y, health, velocityX, velocityY);
    }

    public void update(List<Enemy> entityList) {
        for(Entity entity : entityList) {
            if(this.isColliding(entity)) {

                setPositionX(getPositionX()+50);

                setPositionY(getPositionY()+50);

            }
        }

        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionX(getPositionY() + (int)getVelocityY());
        this.getNode().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getNode().setTranslateY(this.getNode().getTranslateY() + velocityY);

    }

    public void goLeft() {
        setVelocityX(-5.0);
    }

    public void goRight() {
        setVelocityX(5.0);
    }

    public void stopX() {
        setVelocityX(0.0);
    }

    public void goUp() {
        setVelocityY(-5.0);
    }

    public void goDown() {
        setVelocityY(5.0);
    }

    public void stopY() {
        setVelocityY(0.0);
    }
}
