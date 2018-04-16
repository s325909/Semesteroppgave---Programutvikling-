package entities;

public class Bullet extends Movable {

    private int damage;

    public Bullet(int positionX, int positionY, double velocityX, double velocityY, double movementSpeed, int damage) {
        super(positionX, positionY, velocityX, velocityY, movementSpeed);
        this.damage = damage;
    }

    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, double movementSpeed, int damage) {
        super(filename, positionX, positionY, velocityX, velocityY, movementSpeed);
        this.damage = damage;
    }

    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, int damage) {
        super(filename, positionX, positionY, velocityX, velocityY, 10);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
