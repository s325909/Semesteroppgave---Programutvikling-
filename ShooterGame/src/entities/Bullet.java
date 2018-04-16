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

    public void movement(Player player) {
        switch(player.getDirection()) {
            case NORTH:
                setVelocityX(0);
                setVelocityY(-getMovementSpeed());
                break;
            case NORTHEAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(-getMovementSpeed());
                break;
            case EAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(0);
                break;
            case SOUTHEAST:
                setVelocityX(getMovementSpeed());
                setVelocityY(getMovementSpeed());
                break;
            case SOUTH:
                setVelocityX(0);
                setVelocityY(getMovementSpeed());
                break;
            case SOUTHWEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(getMovementSpeed());
                break;
            case WEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(0);
                break;
            case NORTHWEST:
                setVelocityX(-getMovementSpeed());
                setVelocityY(-getMovementSpeed());
                break;
            default:
                setVelocityX(0);
                setVelocityY(0);
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
