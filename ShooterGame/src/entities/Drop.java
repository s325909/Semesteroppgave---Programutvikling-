package entities;

public class Drop extends Entity {

    private int scorePickup;
    private int healthPickup;
    private int armorPickup;
    private int pistolAmmoPickup;
    private int rifleAmmoPickup;
    private int shotgunAmmoPickup;

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public Drop(Sprite idleSprite, int positionX, int positionY) {
        super(idleSprite, positionX, positionY);
    }

    // WIP
    // Vil at den skal ha random egenskap om hva den gir spiller
    // og da må også ikonet endre seg til tilsvarende egenskap
    // Evt. ha score += 25 som standard for de som spawner random
    public Drop(String filename, String extension, int numberImages, int positionX, int positionY) {
        super(filename, extension, numberImages, positionX, positionY);

        this.scorePickup = 25;

        int randomNumber = (int)(Math.random()*10) & 10;
        if (randomNumber < 2) {
            this.healthPickup = 25;
        } else if (randomNumber < 4) {
            this.armorPickup = 25;
        } else if (randomNumber < 5) {
            this.pistolAmmoPickup = 15;
        } else if (randomNumber < 6) {
            this.rifleAmmoPickup = 30;
        } else if (randomNumber < 8) {
            this.shotgunAmmoPickup = 8;
        } else if (randomNumber <= 9) {
            //player.setMovementSpeed(player.getMovementSpeed() + 5);
        }
    }
}
