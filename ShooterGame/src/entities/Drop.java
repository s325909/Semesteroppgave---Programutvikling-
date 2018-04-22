package entities;

public class Drop extends Entity {

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public Drop(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    /**
     * Method which will randomize between which Player statistic should be altered
     * @param player Requires the specific Player this should apply to
     */
    public void randomPickup(Player player) {
        int randomNumber = (int)(Math.random()*10) % 10;
        if (randomNumber < 2) {
            player.healthPickup(25);
        } else if (randomNumber < 4) {
            player.getMagazinePistol().changeBulletNumber(player.getMagazinePistol().getMaxSize());
        } else if (randomNumber < 5) {
            player.getMagazineRifle().changeBulletNumber(player.getMagazineRifle().getMaxSize());
        } else if (randomNumber < 6) {
            player.getMagazineShotgun().changeBulletNumber(player.getMagazineShotgun().getMaxSize());
        } else if (randomNumber < 8) {
            player.armorPickup(25);
        } else if (randomNumber <= 9) {
            //player.setMovementSpeed(player.getMovementSpeed() + 5);
        }
    }
}
