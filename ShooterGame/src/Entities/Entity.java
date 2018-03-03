package Entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

public class Entity {

    /*private class Position {

        private int positionX;
        private int positionY;

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

        public Position(int x, int y) {
            this.positionX = x;
            this.positionY = y;
        }
    }

    private Position position;
W
    public  Position getPosition() { //int[]
        //int[] ret = {this.position.getPositionX(), this.position.getPositionY()};
        //return ret;
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.setPositionX(x);
        this.position.setPositionY(y);
    }*/

    private int health;
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
        this.sprite = new Sprite();
        this.sprite.setSprite(filename);
        this.positionX = x;
        this.positionY = y;
    }

    public Entity(Node node, int x, int y) {
        this.node = node;
        this.positionX = x;
        this.positionY = y;
        this.node.setTranslateX(x);
        this.node.setTranslateY(y);
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Entity(){
        this.positionX = 0;
        this.positionY = 0;
        this.health = 100;
    }

    public Entity(int x, int y, int health, double velocityX, double velocityY){
        //this.positon = new Position(x,y);
        this.positionX = x;
        this.positionY = y;
        this.health = health;
    }



    public boolean isColliding(Entity otherEntity) {
        return this.node.getBoundsInParent().intersects(otherEntity.getNode().getBoundsInParent());
    }
}
