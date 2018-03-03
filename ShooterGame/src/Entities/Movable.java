package Entities;

import javafx.scene.Node;

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

    public Movable(int x, int y, int health, double velocityX, double velocityY) {
        super(x, y, health, velocityX, velocityY);
    }

    public void update() {
        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionX(getPositionY() + (int)getVelocityY());
        this.getNode().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getNode().setTranslateY(this.getNode().getTranslateY() + velocityY);
    }

    public void goLeft() {
        setVelocityX(-1.0);
    }

    public void goRight() {
        setVelocityX(1.0);
    }

    public void stopX() {
        setVelocityX(0.0);
    }

    public void goUp() {
        setVelocityY(-1.0);
    }

    public void goDown() {
        setVelocityY(1.0);
    }

    public void stopY() {
        setVelocityY(0.0);
    }
}
