package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Class which represents Entities on the ground which will provide the Player with attribute increases,
 * or increase the Game's score value. They are either created randomly, or upon Zombie death.
 */
public class Drop extends Entity {
    public enum DropType {
        SCORE, HP, ARMOR, PISTOLAMMO, RIFLEAMMO, SHOTGUNAMMO
    }

    private DropType dropType;

    /**
     * Constructor which sets the Image or animation to display, the position, and type of Drop.
     * The Image is adjusted to display on the location of the Node, and a duration is set for the animation
     * belonging to a Drop of type SCORE.
     * @param images Requires the Image or Images to display.
     * @param positionX Requires a X-coordinate to determine place to creation.
     * @param positionY Requires a Y-coordinate to determine place to creation.
     * @param dropType Requires DropType to be set in order to determine what
     *                 happens upon collision with Player.
     */
    public Drop(Image[] images, int positionX, int positionY, DropType dropType) {
        super(new AnimationHandler(images), positionX, positionY);
        this.dropType = dropType;
        this.getAnimationHandler().getImageView().setTranslateX(this.getNode().getTranslateX());
        this.getAnimationHandler().getImageView().setTranslateY(this.getNode().getTranslateY());
        getAnimationHandler().setDuration(0.192);
    }

    /**
     * Method which functions mostly as its superclass equivalent, but draws the Image furthest to the back.
     * @param game Requires the Game object of which to draw the Image and Node to.
     */
    @Override
    public void drawImage(Game game) {
        if (!isDrawn()) {
            super.drawImage(game);

            getAnimationHandler().getImageView().toBack();
            getNode().toBack();
        }
    }

    /**
     * Method which handles collision between an object of type Drop and a Player.
     * Based on the DropType, the Game's score is increased, or the Player's attributes are increased
     * in the form of health, armor, or ammunition.
     * @param player Requires an object of type Player to adjust its attributes.
     * @param game Requires an object of type Game to adjust its score.
     */
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