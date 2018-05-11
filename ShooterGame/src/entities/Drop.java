package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.image.Image;

public class Drop extends Entity {
    public enum DropType {
        SCORE, HP, ARMOR, PISTOLAMMO, RIFLEAMMO, SHOTGUNAMMO
    }

    private DropType dropType;

    public Drop(Image[] images, int positionX, int positionY, DropType dropType) {
        super(new AnimationHandler(images), positionX, positionY);
        this.dropType = dropType;
        this.getAnimationHandler().getImageView().setTranslateX(this.getNode().getTranslateX());
        this.getAnimationHandler().getImageView().setTranslateY(this.getNode().getTranslateY());
        getAnimationHandler().setDuration(0.192);
    }

    public void dropCollision(Player player, Game game) {
        switch(dropType) {
            case SCORE:
                game.setScoreNumber(game.getScoreNumber() + 100);
                break;
            case HP:
                player.healthPickup(25);
                break;
            case ARMOR:
                player.armorPickup(25);
                break;
            case PISTOLAMMO:
                player.getMagazinePistol().changeBulletNumber(15);
                break;
            case RIFLEAMMO:
                player.getMagazineRifle().changeBulletNumber(30);
                break;
            case SHOTGUNAMMO:
                player.getMagazineShotgun().changeBulletNumber(8);
                break;
            default:
                game.setScoreNumber(game.getScoreNumber() + 100);
        }
    }

    public DataHandler.DropConfiguration getDropConfiguration() {
        DataHandler.DropConfiguration dropCfg = new DataHandler.DropConfiguration();
        dropCfg.entityCfg = super.getEntityConfiguration();
        dropCfg.dropType = this.getDropType();
        return dropCfg;
    }

    public void setDropConfiguration(DataHandler.DropConfiguration dropCfg) {
        super.setEntityConfiguration(dropCfg.entityCfg);
        this.setDropType(dropCfg.dropType);
    }

    public DropType getDropType() {
        return dropType;
    }

    public void setDropType(DropType dropType) {
        this.dropType = dropType;
    }
}