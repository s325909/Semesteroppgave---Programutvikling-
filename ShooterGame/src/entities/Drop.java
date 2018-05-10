package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.image.Image;

public class Drop extends Entity {
    public Drop(Image[] images, int positionX, int positionY) {
        super(new AnimationHandler(images, 5), positionX, positionY);
        this.getAnimationHandler().getImageView().setTranslateX(this.getNode().getTranslateX());
        this.getAnimationHandler().getImageView().setTranslateY(this.getNode().getTranslateY());
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

    public DataHandler.Configuration getConfiguration() {
        DataHandler.Configuration dropCfg = new DataHandler.Configuration();
        dropCfg.posX = this.getPositionX();
        dropCfg.posY = this.getPositionY();
        return dropCfg;
    }

    public void setConfiguration(DataHandler.Configuration dropCfg) {
        this.setPosition(dropCfg.posX, dropCfg.posY);
        this.setTranslateNode(dropCfg.posX, dropCfg.posY);
    }
}