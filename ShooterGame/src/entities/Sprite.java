package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {

    private double width;
    private double height;
    private Image image;
    private ImageView iv;

    private Image[] frames;
    private double duration;

    /*public Sprite(String spriteFileName, int x, int y) {
        this.image = new Image(spriteFileName);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.iv = new ImageView();
        iv.setImage(this.image);
        iv.relocate(x, y);
    }*/

    public Sprite(ImageView iv, String spriteFileName, String extension, int numberImages) {
        this.iv = iv;
        this.frames = new Image[numberImages];
        for (int i = 0; i < numberImages; ++i) {
            try{
                String filename = spriteFileName + Integer.toString(i) + extension;
                String resource = getClass().getResource(filename).toURI().toString();
                frames[i] = new Image(resource, 100, 100, true, true);
            }catch (Exception e) {

            }
        }
        this.width = this.frames[0].getWidth();
        this.height = this.frames[0].getHeight();
        this.duration = 0.016;
        //this.iv = new ImageView();
        //this.iv.setImage(this.frames[0]);
    }

    /***
     * Method for determining how quickly the array of Sprites should cycle
     * @param time
     */
    public void setFrame(double time) {
        int index = (int)((time % (frames.length * duration)) / duration);
        this.iv.setImage(frames[index]);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Image getImage() {
        return this.frames[0];//this.image;
    }

    public ImageView getImageView() {
        return this.iv;
    }
}
