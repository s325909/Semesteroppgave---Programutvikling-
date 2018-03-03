package Entities;

import javafx.scene.Node;

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
}
