package entities;

import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Entity{

    private int healthPoints;
    private int positionX;
    private int positionY;

    private boolean isAlive = true;

    private Node node;
    private ImageView iv;
    private Sprite sprite;
    private Sprite spriteDefault;

    public Entity() {}

    public Entity(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        this.iv = new ImageView();
        this.spriteDefault = new Sprite(this.iv, filename, extension, numberImages);
        this.sprite = this.spriteDefault;
        this.node = new Circle(this.sprite.getWidth()/2, this.sprite.getHeight()/2, 2*this.sprite.getHeight()/5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.healthPoints = healthPoints;
    }

    /***
     * Method for checking for collision between two objects of type Node
     * @param otherEntity
     * @return
     */
    public boolean isColliding(Entity otherEntity) {
        return this.node.getBoundsInParent().intersects(otherEntity.getNode().getBoundsInParent());
    }

    public void setSpriteSize(int width, int height) {
        this.sprite.setWidth(width);
        this.sprite.setHeight(height);
    }

    public boolean checkAlive() {
        if (this.healthPoints <= 0)
            return isAlive = false;
        else
            return isAlive;
    }

    public AudioClip[] loadAudio(String[] audioFiles) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for(int i = 0; i < clips.length; i++) {
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
        }
        return clips;
    }

    public class SpriteParam {
        String filename;
        String extension;
        int numberImages;

        public SpriteParam(String filename, String extension, int numberImages) {
            this.filename = filename;
            this.extension = extension;
            this.numberImages = numberImages;
        }
    }

    public Sprite[] loadSprites(SpriteParam[] spriteParam) {
        Sprite[] sprites = new Sprite[spriteParam.length];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(this.iv, spriteParam[i].filename, spriteParam[i].extension, spriteParam[i].numberImages);
        }
        return sprites;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int health) {
        this.healthPoints = health;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ImageView getIv() {
        return this.iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }
}