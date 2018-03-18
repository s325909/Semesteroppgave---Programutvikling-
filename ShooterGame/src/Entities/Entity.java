package Entities;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Entity {

    private int healthpoints;
    private int positionX;
    private int positionY;

    private Node node;
    private Sprite sprite;

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

    public Entity(String filename, int x, int y) {
        this.sprite = new Sprite(filename, x, y);
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, this.sprite.getHeight()/2, Color.RED);
        this.positionX = x;
        this.positionY = y;
        this.node.setTranslateX(x);
        this.node.setTranslateY(y);
    }

    public Entity(String filename, String extension, int numberImages, double durationBetween, int positionX, int positionY, int healthpoints) {
        this.sprite = new Sprite(filename, extension, numberImages, durationBetween);
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, 2*this.sprite.getHeight()/5, Color.RED);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.healthpoints = healthpoints;
    }

    public Entity(Node node, int x, int y) {
        this.node = node;
        this.positionX = x;
        this.positionY = y;
        this.node.setTranslateX(x);
        this.node.setTranslateY(y);
    }

    public Entity(int health, int positionX, int positionY, Node node, Sprite sprite) {
        this.healthpoints = health;
        this.positionX = positionX;
        this.positionY = positionY;
        this.node = node;
        this.sprite = sprite;
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

    public int getHealthpoints() {
        return healthpoints;
    }

    public void setHealthpoints(int healthpoints) {
        this.healthpoints = healthpoints;
    }

    public Entity(){
        this.positionX = 0;
        this.positionY = 0;
        this.healthpoints = 100;
    }

    public Entity(int x, int y, int healthpoints, double velocityX, double velocityY){
        //this.positon = new Position(x,y);
        this.positionX = x;
        this.positionY = y;
        this.healthpoints = healthpoints;
    }

    public boolean isColliding(Entity otherEntity) {
        return this.node.getBoundsInParent().intersects(otherEntity.getNode().getBoundsInParent());
    }
}
