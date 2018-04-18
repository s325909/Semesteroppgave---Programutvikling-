package entities;

public class Drop extends Player {

    int randomNumber;
    Player player;

    public Drop() {
    }

    public Drop(int positionX, int positionY, Player player, int randomNumber) {
        super(positionX, positionY);
        this.randomNumber = randomNumber;
        this.player = player;

        if (this.randomNumber == 0) {
            player.setHealthPoints(getHealthPoints() + 25);
        } else if (this.randomNumber == 1) {
            player.getMagazinePistol().changeBulletNumber(getMagazinePistol().getMaxSize());
        } else if (this.randomNumber == 2) {
            player.getMagazineRifle().changeBulletNumber(getMagazineRifle().getMaxSize());
        } else if (this.randomNumber == 3) {
            player.getMagazineShotgun().changeBulletNumber(getMagazineShotgun().getMaxSize());
        } else if (this.randomNumber == 4) {

        }
    }

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public void pickUps(Player player, double time) {
        int random = (int) Math.floor(Math.random() * 100);
        Drop drop = new Drop();
    }
}
