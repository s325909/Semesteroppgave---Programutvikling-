package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {

    private double width;
    private double height;

    private ImageView iv;
    private Image[] frames;
    private double duration;

    private double maxWidth;
    private double maxHeight;

    public Sprite(ImageView iv, String spriteFileName) {
        this.iv = iv;
        this.frames = new Image[1];
        try {
            String resource = getClass().getResource(spriteFileName).toURI().toString();
            this.frames[0] = new Image(resource, 25, 25, true, true);
        } catch (Exception e) {
            System.out.println("Error: Unable to find requested file and SingleImage couldn't be created");
        }
        this.width = this.frames[0].getWidth();
        this.height = this.frames[0].getHeight();
    }

    public Sprite(ImageView iv, String spriteFileName, String extension, int numberImages) {
        this.iv = iv;
        this.frames = new Image[numberImages];
        for (int i = 0; i < numberImages; ++i) {
            try{
                String filename = spriteFileName + Integer.toString(i) + extension;
                String resource = getClass().getResource(filename).toURI().toString();
                frames[i] = new Image(resource, 75, 75, true, false);
            }catch (Exception e) {
                System.out.println(spriteFileName + Integer.toString(i) + extension);
                System.out.println("Error: Unable to find requested file(s) and the array Sprite.frames couldn't be created");
            }
        }
        this.width = this.frames[0].getWidth();
        this.height = this.frames[0].getHeight();
        this.duration = 0.032;
        this.maxWidth = this.width;
        this.maxHeight = this.height;
    }

    /***
     * Method for determining how quickly the array of Sprites should cycle
     * @param time
     */
    protected void setFrame(double time) {
        int index = (int)((time % (frames.length * duration)) / duration);
        this.iv.setImage(frames[index]);
        //this.iv.setFitWidth(maxWidth);
        //this.iv.setFitHeight(maxHeight);
    }

    public void setMax(double imageWidth, double imageHeight) {
        this.maxWidth = imageWidth;
        this.maxHeight = imageHeight;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Image getFirstImage() {
        return this.frames[0];
    }

    public Image getImage(int i) {
        return this.frames[i];
    }

    public ImageView getImageView() {
        return this.iv;
    }
}