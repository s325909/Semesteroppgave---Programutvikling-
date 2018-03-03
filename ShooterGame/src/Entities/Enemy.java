package Entities;

import javafx.scene.Node;

public class Enemy extends Movable {
    public Enemy() {
    }

    public Enemy(int x, int y, int health, double velocityX, double velocityY) {
        super(x, y, health, velocityX, velocityY);
    }

    public Enemy(Node node, int x, int y) {
        super(node, x, y);
    }
}
