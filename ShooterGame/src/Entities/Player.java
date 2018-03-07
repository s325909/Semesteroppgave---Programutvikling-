package Entities;

import javafx.scene.Node;
import javafx.scene.image.Image;

public class Player extends Movable {

    Image image = new Image("/resources/test_sprites.png");
    Sprite playerSprite = new Sprite(50,50,image);

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
