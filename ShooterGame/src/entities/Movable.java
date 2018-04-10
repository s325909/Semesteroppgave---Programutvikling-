package entities;

import javafx.scene.image.ImageView;
import java.util.List;

public class Movable extends Entity {

    private double velocityX;
    private double velocityY;

    private boolean idleSet = false;
    private Sprite spriteIdle;
    private boolean moveSet = false;
    private Sprite spriteMoving;
    private boolean meleeSet = false;
    private Sprite spriteMelee;
    private boolean shootingSet = false;
    private Sprite spriteShooting;
    private boolean reloadSet = false;
    private Sprite spriteReloading;

    public Movable() { }

//    public Movable(Node node, int x, int y) {
//        super(node, x, y);
//    }

    public Movable(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    /***
     * Method for updating entity's position based on user input and interaction with other objects
     * @param entityList
     * @param time
     */
    public void update(List<Entity> entityList, double time) {
        this.getSprite().setFrame(time);

        // Update actual position of object
        setPositionX(getPositionX() + (int)getVelocityX());
        setPositionY(getPositionY() + (int)getVelocityY());

        // Update position of the visible representation of the object (Node and Sprite)
        this.getNode().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getNode().setTranslateY(this.getNode().getTranslateY() + velocityY);
        this.getSprite().getImageView().setTranslateX(this.getNode().getTranslateX() + velocityX);
        this.getSprite().getImageView().setTranslateY(this.getNode().getTranslateY() + velocityY);

        // Change sprite direction upon entity direction change based on user input
        if (getVelocityX() > 0) {
            if (getVelocityY() < 0) {
                this.getSprite().getImageView().setRotate(315);
                this.getNode().setRotate(315);
            }
            else if (getVelocityY() > 0) {
                this.getSprite().getImageView().setRotate(4+5);
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

        // Check for collision between entities and update position and/or velocity
        for(Entity entity : entityList) {
            if(this.isColliding(entity)) {
                setVelocityX(-2 * getVelocityX());
                setVelocityY(-2 * getVelocityY());
                setHealthPoints(getHealthPoints() - 10);
            }
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

    public void setSpriteIdle(String spriteFileName, String extension, int numberImages) {
        this.idleSet = true;
        this.spriteIdle = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteMoving(String spriteFileName, String extension, int numberImages) {
        this.moveSet = true;
        this.spriteMoving = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteMelee(String spriteFileName, String extension, int numberImages) {
        this.meleeSet = true;
        this.spriteMelee = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteShooting(String spriteFileName, String extension, int numberImages) {
        this.shootingSet = true;
        this.spriteShooting = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setSpriteReloading(String spriteFileName, String extension, int numberImages) {
        this.reloadSet = true;
        this.spriteReloading = new Sprite(super.getIv(), spriteFileName, extension, numberImages);
    }

    public void setIdle() {
        if (this.idleSet)
            super.setSprite(this.spriteIdle);
    }

    public void setMoving() {
        if (this.moveSet)
            super.setSprite(this.spriteMoving);
    }

    public void setMelee() {
        if (this.meleeSet)
            super.setSprite(this.spriteMelee);
    }

    public void setShooting() {
        if (this.shootingSet)
            super.setSprite(this.spriteShooting);
    }

    public void setReloading() {
        if (this.reloadSet)
            super.setSprite(this.spriteReloading);
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


