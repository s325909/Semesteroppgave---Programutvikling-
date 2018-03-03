package Entities;

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

    public double getVelocity() {
        return velocityX;
    }

    public void setVelocity(double velocity) {
        this.velocityX = velocity;
    }

    public Entity(){
        this.positionX = 0;
        this.positionY = 0;
        this.health = 100;
        this.velocityX = 0.1;
        this.velocityY = 0.1;
    }

    public Entity(int x, int y, int health, double velocityX, double velocityY){
        //this.positon = new Position(x,y);
        this.positionX = x;
        this.positionY = y;
        this.health = health;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
