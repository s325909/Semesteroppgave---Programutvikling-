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

    public Sprite(ImageView iv, String spriteFileName, String extension, int numberImages) {
        this.iv = iv;
        this.frames = new Image[numberImages];
        for (int i = 0; i < numberImages; ++i) {
            try{
                String filename = spriteFileName + Integer.toString(i) + extension;
                String resource = getClass().getResource(filename).toURI().toString();
                frames[i] = new Image(resource, 100, 100, true, false);
            }catch (Exception e) {

            }
        }
        this.width = this.frames[0].getWidth();
        this.height = this.frames[0].getHeight();
        this.duration = 0.016;
        this.maxWidth = this.width;
        this.maxHeight = this.height;
    }

    /***
     * Method for determining how quickly the array of Sprites should cycle
     * @param time
     */
    public void setFrame(double time) {
        int index = (int)((time % (frames.length * duration)) / duration);
        this.iv.setImage(frames[index]);
        //this.iv.setFitWidth(maxWidth);
        //this.iv.setFitHeight(maxHeight);
    }

    public void playAllFrames() {
        for(int i = 0; i < this.frames.length; i++) {
            this.iv.setImage(this.frames[i]);
        }
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