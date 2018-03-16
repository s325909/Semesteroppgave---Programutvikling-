package Entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Player extends Movable {

    public Player() {
    }

    public Player(int x, int y, int health, double velocityX, double velocityY) {
        super(x, y, health, velocityX, velocityY);
    }

    public Player(Node node, int x, int y) {
        super(node, x, y);
    }

    public Player(String filename, int x, int y) {
        super(filename, x, y);
    }

    public Player(String filename, String extension, int n, int x, int y) {
        super(filename, extension, n, x, y);
    }
}
