package entities;

import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Entity{

    private int positionX;
    private int positionY;

    private boolean alive;
    private boolean drawn;

    private Node node;
    private ImageView iv;
    private AnimationHandler animationhandler;

    public Entity(AnimationHandler allAnimation, int positionX, int positionY) {
        this.iv = allAnimation.getImageView();
        this.animationhandler = allAnimation;
        this.node = new Circle(this.animationhandler.getWidth()/2, this.animationhandler.getHeight()/2, 2*this.animationhandler.getHeight()/5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.alive = true;
    }

    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setTranslateNode(int positionX, int positionY) {
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
    }

    /***
     * Method for checking for collision between two objects of type Node
     * @param otherEntity
     * @return
     */
    public boolean isColliding(Entity otherEntity) {
        return this.node.getBoundsInParent().intersects(otherEntity.getNode().getBoundsInParent());
    }

    public boolean isDead() {
        return !alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) { this.positionX = positionX; }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) { this.positionY = positionY; }

    public AnimationHandler getAnimationHandler() {
        return animationhandler;
    }

    public Node getNode() {
        return node;
    }

    public ImageView getIv() {
        return this.iv;
    }

    public boolean isDrawn() { return drawn; }

    public void setDrawn() { drawn = true; }
}