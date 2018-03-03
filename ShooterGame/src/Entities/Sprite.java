package Entities;

import javafx.scene.image.Image;

public class Sprite {

    private double width;
    private double height;
    private Image sprite;

    public Sprite(double width, double height, Image sprite) {
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public Sprite() {
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setSprite(String spriteFileName) {
        Image sprite = new Image(spriteFileName);
        setSpriteSize(sprite);
    }

    public void setSpriteSize(Image sprite) {
        this.sprite = sprite;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
    }
}
