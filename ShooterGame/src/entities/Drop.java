package entities;

public class Drop extends Entity {

    private boolean drawn;

    public Drop() {
    }

    public Drop(int positionX, int positionY) {
        super(positionX, positionY);
    }

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public void loadPowerupImages() {
        String[] images = {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_icon.png"};
    }

//    public void setAnimation(int i) {
//        super.setSprite(this.animation[i]);
//    }

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
            player.setMovementSpeed(player.getMovementSpeed() + 5);
        }
    }

    public boolean isDrawn() { return drawn; };

    public void setDrawn() { drawn = true; };
}
