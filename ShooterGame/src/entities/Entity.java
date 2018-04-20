package entities;

import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Entity{

    private int healthPoints;
    private int positionX;
    private int positionY;

    private boolean alive;
    private boolean drawn;

    private Node node;
    private ImageView iv;
    private Sprite sprite;
    private Sprite spriteDefault;

    public Entity() {}

    public Entity(int positionX, int positionY) {
        this.node = new Circle(5, 5, 5,Color.BROWN);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.alive = true;
    }

    public Entity(String filename, int positionX, int positionY) {
        this.iv = new ImageView();
        this.spriteDefault = new Sprite(this.iv, filename);
        this.sprite = this.spriteDefault;
        this.node = new Rectangle(25, 25, Color.GREEN);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.alive = true;
    }

    public Entity(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        this.iv = new ImageView();
        this.spriteDefault = new Sprite(this.iv, filename, extension, numberImages);
        this.sprite = this.spriteDefault;
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, 2*this.sprite.getHeight()/5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.healthPoints = healthPoints;
        this.alive = true;
    }

    public Entity(Sprite idleSprite, int positionX, int positionY, int healthPoints) {
        this.iv = idleSprite.getImageView();
        this.sprite = idleSprite;
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, 2*this.sprite.getHeight()/5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.healthPoints = healthPoints;
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

    public Sprite[] loadSprite(String[] filename) {
        Sprite[] sprite = new Sprite[filename.length];
        for(int i = 0; i < sprite.length; i++) {
            sprite[i] = new Sprite(this.iv, filename[i]);
        }
        System.out.println("Success");
        return sprite;
    }

    public void playAudio(AudioClip audioClip) {
        audioClip.play();
    }

    public boolean stillAlive() {
        if (this.healthPoints <= 0) {
            setAlive(false);
            return false;
        }
        return true;
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

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int health) {
        this.healthPoints = health;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Sprite getSprite() {
        return sprite;
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