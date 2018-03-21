package entities;

import javafx.scene.Node;

public class NonMovable extends Entity {

    public NonMovable() {
    }

    public NonMovable(Node node, int x, int y) {
        super(node, x, y);
    }

    public NonMovable(String filename, int x, int y) {
        super(filename, x, y);
    }

    public NonMovable(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }
}
