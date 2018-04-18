package entities;

public class Drop extends Player {

    public Drop() {
    }

    public Drop(int positionX, int positionY) {
        super(positionX, positionY);
    }

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public void randomPickup(Player player) {
        int randomNumber = (int)(Math.random()*10) % 10;
        if (randomNumber < 2) {
            player.setHealthPoints(player.getHealthPoints() + 25);
        } else if (randomNumber < 4) {
            player.getMagazinePistol().changeBulletNumber(player.getMagazinePistol().getMaxSize());
        } else if (randomNumber < 5) {
            player.getMagazineRifle().changeBulletNumber(player.getMagazineRifle().getMaxSize());
        } else if (randomNumber < 6) {
            player.getMagazineShotgun().changeBulletNumber(player.getMagazineShotgun().getMaxSize());
        } else if (randomNumber < 8) {
            player.setArmor(player.getArmor() + 25);
        } else if (randomNumber <= 9) {
            player.setMovementSpeed(player.getMovementSpeed() + 10);
        }
    }
}
