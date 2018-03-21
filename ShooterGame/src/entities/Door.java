package entities;

import javafx.scene.Node;

public class Door extends NonMovable {

    boolean isClosed;
    String direction;

    public Door() {
    }

    public Door(Node node, int x, int y) {
        super(node, x, y);
    }

    public Door(String filename, int x, int y) {
        super(filename, x, y);
    }

    public Door(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }
}
