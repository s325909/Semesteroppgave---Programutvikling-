package entities;

public class Zombie extends Movable {

    Sprite[] animations;

    private Direction walkDirection;
    private int walkDistance;

    public Zombie(){}

    public Zombie(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 1.0);
        loadZombieAssets();
    }

    public void loadZombieAssets() {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        loadBasicSounds(zombieSounds);
//        Sprite[] sprites = new Sprite[zombieAnimations.length];
//        for(int i = 0; i < zombieAnimations.length; i++) {
//            sprites[i] = loadSprites(zombieAnimations[i]
//        }
    }

    /***
     * Method which controls the bulletDirection of enemies, whereas they are drawn towards the Player.
     * @param player Requires an object of type Player in order to decide which Entity the enemies
     *               should pursue.
     */
    public void movement(Player player) {
        double diffx = player.getPositionX() - getPositionX();
        double diffy = player.getPositionY() - getPositionY();
        double angle = 180 + Math.atan2(diffy, diffx) * (180 / Math.PI);
        double distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        if(distance > 0 && distance < 1000 && this.walkDistance <= 0) {
            if (angle > 340 && angle <= 25) {
                this.walkDirection = Direction.WEST;
                this.walkDistance = 10;
            } else if (angle > 25 && angle <= 70) {
                this.walkDirection = Direction.NORTHWEST;
                this.walkDistance = 10;
            } else if (angle > 70 && angle <= 115) {
                this.walkDirection = Direction.NORTH;
                this.walkDistance = 10;
            } else if (angle > 115 && angle <= 160) {
                this.walkDirection = Direction.NORTHEAST;
                this.walkDistance = 10;
            } else if (angle > 160 && angle <= 205) {
                this.walkDirection = Direction.EAST;
                this.walkDistance = 10;
            } else if (angle > 205 && angle <= 250) {
                this.walkDirection = Direction.SOUTHEAST;
                this.walkDistance = 10;
            } else if (angle > 250 && angle <= 295) {
                this.walkDirection = Direction.SOUTH;
                this.walkDistance = 10;
            } else if (angle > 295 && angle <= 340) {
                this.walkDirection = Direction.SOUTHWEST;
                this.walkDistance = 10;
            }
        }

        if (this.walkDistance <= 0) {
            this.walkDirection = Direction.IDLE;
            this.walkDistance = 0;
        }
        switch (this.walkDirection) {
            case IDLE:
                stopX();
                stopY();
                setIdle();
                this.walkDistance = 0;
                break;
            case NORTH:
                goUp();
                stopX();
                setMoving();
                this.walkDistance--;
                break;
            case NORTHEAST:
                goUp();
                goRight();
                setMoving();
                this.walkDistance--;
                break;
            case EAST:
                goRight();
                stopY();
                setMoving();
                this.walkDistance--;
            case SOUTHEAST:
                goDown();
                goRight();
                setMoving();
                this.walkDistance--;
                break;
            case SOUTH:
                goDown();
                stopX();
                setMoving();
                this.walkDistance--;
                break;
            case SOUTHWEST:
                goDown();
                goLeft();
                setMoving();
                this.walkDistance--;
                break;
            case WEST:
                goLeft();
                stopY();
                setMoving();
                this.walkDistance--;
                break;
            case NORTHWEST:
                goUp();
                goLeft();
                setMoving();
                this.walkDistance--;
                break;
            default:
                stopX();
                stopY();
                setIdle();
                this.walkDistance = 0;
        }
    }

//    public void setAnimation(int i) {
//        super.setSprite(this.animations[i]);
//    }

}