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
        if (player.isColliding(this)) {
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

            setAlive(false);
        }
    }

    /**
     * Method which will retrieve and return requested information about a Drop object.
     * Creates a new DropConfiguration object from the DataHandler class, and transfers
     * variables inherited from Entity, combined with variables specific to the Drop class,
     * into the corresponding variables in dropCfg.
     * @return Returns an object of type DropConfiguration.
     */
    public DataHandler.DropConfiguration getDropConfiguration() {
        DataHandler.DropConfiguration dropCfg = new DataHandler.DropConfiguration();
        dropCfg.entityCfg = super.getEntityConfiguration();
        dropCfg.dropType = this.getDropType();
        return dropCfg;
    }

    /**
     * Method which will transfer provided dropCfg's variables into corresponding variables in Drop.
     * Variables inherited from Entity are transferred and set through a super method call.
     * Further, variables specific to the Drop class are transferred and set.
     * @param dropCfg Requires an object of type DropConfiguration.
     */
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