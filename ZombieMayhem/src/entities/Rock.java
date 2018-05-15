package entities;

import gameCode.DataHandler;
import javafx.scene.image.Image;

/**
 * Class which represents a Rock, with the same qualities as an Entity.
 */
public class Rock extends Entity {
    public Rock(Image[] images, int positionX, int positionY) {
        super(new AnimationHandler(images), positionX, positionY);
        this.getAnimationHandler().getImageView().setTranslateX(this.getNode().getTranslateX());
        this.getAnimationHandler().getImageView().setTranslateY(this.getNode().getTranslateY());
    }

    /**
     * Method which will retrieve and return requested information about a Rock object.
     * @return Returns the object entityCfg of type EntityConfiguration.
     */
    public DataHandler.EntityConfiguration getRockConfiguration() {
        return super.getEntityConfiguration();
    }

    /**
     * Method which will transfer provided entityCfg's variables into corresponding variables in Entity.
     * @param rockCfg Requires an object of type EntityConfiguration.
     */
    public void setEntityConfiguration(DataHandler.EntityConfiguration rockCfg) {
        super.setEntityConfiguration(rockCfg);
    }
}
