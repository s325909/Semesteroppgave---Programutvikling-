package entities;

public class Drop extends Entity {

    private Sprite[] sprites;

    public Drop() {
    }

    public Drop(int positionX, int positionY) {
        super(positionX, positionY);
    }

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
        loadPowerupImages();
    }

    public void loadPowerupImages() {
        String[] imageFiles = {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_icon.png",
                "/resources/Icons/batterycharging.png",
                "/resources/Icons/lightningbolt.png"};

        this.sprites = loadSprite(imageFiles);
    }

    /**
     * Method which will randomize between which Player statistic should be altered
     * @param player Requires the specific Player this should apply to
     */
    public void randomPickup(Player player) {
        int randomNumber = (int)(Math.random()*10) % 10;
        if (randomNumber < 2) {
            setSprite(0);
            player.healthPickup(25);
        } else if (randomNumber < 4) {
            setSprite(2);
            player.getMagazinePistol().changeBulletNumber(player.getMagazinePistol().getMaxSize());
        } else if (randomNumber < 5) {
            setSprite(3);
            player.getMagazineRifle().changeBulletNumber(player.getMagazineRifle().getMaxSize());
        } else if (randomNumber < 6) {
            setSprite(3);
            player.getMagazineShotgun().changeBulletNumber(player.getMagazineShotgun().getMaxSize());
        } else if (randomNumber < 8) {
            setSprite(1);
            player.armorPickup(25);
        } else if (randomNumber <= 9) {
            setSprite(4);
            //player.setMovementSpeed(player.getMovementSpeed() + 5);
        }
    }

    public void setSprite(int i) {
        super.setSprite(this.sprites[i]);
    }
}
