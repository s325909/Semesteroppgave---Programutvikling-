package entities;

public class Bullet extends Movable {

    private Direction direction;
    private Player player;
    private int damage;

    public Bullet(int positionX, int positionY, double movementSpeed, int damage) {
        super(positionX, positionY, movementSpeed);
        this.damage = damage;
    }

    public Bullet(String filename, int positionX, int positionY, double movementSpeed, int damage) {
        super(filename, positionX, positionY, movementSpeed);
        this.damage = damage;
    }

//    public void adjustDirection() {
//        switch(player.getDirection()) {
//            case NORTH:
//            case NORTHEAST:
//            case EAST:
//            case SOUTHEAST:
//            case SOUTH:
//            case SOUTHWEST:
//            case WEST:
//            case NORTHWEST:
//        }
//    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
