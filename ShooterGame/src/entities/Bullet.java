package entities;

public class Bullet extends Movable {

    private int damage;
    private boolean drawn;
    private Sprite[] sprites;

    public Bullet(int positionX, int positionY, double movementSpeed, int damage) {
        super(positionX, positionY, movementSpeed);
        this.damage = damage;
    }

    public Bullet(String filename, int positionX, int positionY, double movementSpeed, int damage) {
        super(filename, positionX, positionY, movementSpeed);
        this.damage = damage;
        loadBulletImages();
    }

    public void loadBulletImages() {
        String[] images = {
                "/resources/Art/pistol_bullet.png"};

        this.sprites = loadSprite(images);
    }

    public void setSprite(int i) {
        super.setSprite(this.sprites[i]);
    }

    public void bulletDirection(Player player) {
        setSprite(0);
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

    public boolean isDrawn() { return drawn; };

    public void setDrawn() { drawn = true; };
}
