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
    private Sprite sprite;
    private AnimationHandler idleAnimation;

    public Entity(String filename, int positionX, int positionY) {
        this.iv = new ImageView();
        this.sprite = new Sprite(this.iv, filename);
        this.node = new Rectangle(25, 25, Color.GREEN);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.sprite.getImageView().setTranslateX(positionX);
        this.sprite.getImageView().setTranslateY(positionY);
        this.alive = true;
    }

    public Entity(Sprite idleSprite, int positionX, int positionY) {
        this.iv = idleSprite.getImageView();
        this.sprite = idleSprite;
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, 2*this.sprite.getHeight()/5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.alive = true;
    }

    public Entity(AnimationHandler idleAnimation, int positionX, int positionY) {
        this.iv = idleAnimation.getImageView();
        this.idleAnimation = idleAnimation;
        this.node = new Circle(this.idleAnimation.getWidth()/2, this.idleAnimation.getHeight()/2, 2*this.idleAnimation.getHeight()/5, Color.BLUE);
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

    public void setSpriteSize(int width, int height) {
        this.sprite.setWidth(width);
        this.sprite.setHeight(height);
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

    public Sprite getSprite() {
        return sprite;
    }

    public AnimationHandler getAnimationHandler() {
        return idleAnimation;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ImageView getIv() {
        return this.iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public boolean isDrawn() { return drawn; };

    public void setDrawn() { drawn = true; };
}