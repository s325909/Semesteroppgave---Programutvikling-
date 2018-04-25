package entities;

import gameCode.Game;

public class Drop extends Entity {

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public Drop(Sprite idleSprite, int positionX, int positionY) {
        super(idleSprite, positionX, positionY);
    }

    public void dropCollision(Player player, Game game) {
        game.setScoreNumber(game.getScoreNumber() + 50);
        int randomNumber = (int)(Math.random()*10) % 10;

        if (randomNumber < 2) {
            player.healthPickup(25);
        } else if (randomNumber < 4) {
            player.armorPickup(25);
        } else if (randomNumber < 5) {
            player.getMagazinePistol().changeBulletNumber(15);
        } else if (randomNumber < 6) {
            player.getMagazineRifle().changeBulletNumber(30);
        } else if (randomNumber < 8) {
            player.getMagazineShotgun().changeBulletNumber(8);
        } else if (randomNumber <= 9) {
            //player.setMovementSpeed(player.getMovementSpeed() + 5);
        }
    }
}