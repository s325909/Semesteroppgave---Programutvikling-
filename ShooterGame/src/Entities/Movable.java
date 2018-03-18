package Entities;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Movable extends Entity {

    private double velocityX;
    private double velocityY;

    public Movable() { }

    public Movable(Node node, int x, int y) {
        super(node, x, y);
    }

    public Movable(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void update(List<Enemy> entityList, double time) {
        this.getSprite().setFrame(time);
        for(Entity entity : entityList) {
            if(this.isColliding(entity)) {
                setVelocityX(-1*getVelocityX());
                setVelocityY(-1*getVelocityY());
                setHealthPoints(getHealthPoints() - 10);
            }
        }

        // Update new position by getting current position and adding current velocity
        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionX(getPositionY() + (int)getVelocityY());
        this.getNode().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getNode().setTranslateY(this.getNode().getTranslateY() + velocityY);
        this.getSprite().getImageView().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getSprite().getImageView().setTranslateY(this.getNode().getTranslateY() + velocityY);

        // Change sprite direction upon entity direction change
        if (getVelocityX() > 0) {
            if (getVelocityY() < 0) {
                this.getSprite().getImageView().setRotate(315);
                this.getNode().setRotate(315);
            }
            else if (getVelocityY() > 0) {
                this.getSprite().getImageView().setRotate(45);
                this.getNode().setRotate(45);
            }
            else
                this.getSprite().getImageView().setRotate(0);
        } else if(getVelocityX() < 0) {
            if (getVelocityY() < 0)
                this.getSprite().getImageView().setRotate(225);
            else if (getVelocityY() > 0)
                this.getSprite().getImageView().setRotate(135);
            else
                this.getSprite().getImageView().setRotate(180);
        } else {
            if (getVelocityY() < 0)
                this.getSprite().getImageView().setRotate(270);
            else if (getVelocityY() > 0)
                this.getSprite().getImageView().setRotate(90);
        }
    }

    // Functions for changing entity velocity
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
}

