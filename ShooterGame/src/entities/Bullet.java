package entities;

public class Bullet extends Movable {

    private int damage;

    public Bullet(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints, int damage) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
        this.damage = damage;
    }
}
