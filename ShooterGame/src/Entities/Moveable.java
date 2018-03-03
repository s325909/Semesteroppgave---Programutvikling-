package Entities;

public class Moveable extends Entity {

    public Moveable() {
    }

    public Moveable(int x, int y, int health, double velocityX, double velocityY) {
        super(x, y, health, velocityX, velocityY);
    }

    public void update() {
        setPositionX(getPositionX() - (int)getVelocityX());
        setPositionX(getPositionY() - (int)getVelocityY());
    }

    public void goLeft() {
        setVelocityX(-1.0);
    }

    public void goRight() {
        setVelocityX(1.0);
    }

    public void goUp() {
        setVelocityY(-1.0);
    }

    public void goDown() {
        setVelocityY(1.0);
    }
}
