package entities;

import javafx.scene.Node;

public class Enemy extends Movable {
    public Enemy() { }

//    public Enemy(Node node, int x, int y) {
//        super(node, x, y);
//    }

    public Enemy(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }
}
