package entities;

import javafx.scene.image.Image;

public class Rock extends Entity {
    public Rock(Image[] images, int positionX, int positionY) {
        super(new AnimationHandler(images), positionX, positionY);
    }
}
