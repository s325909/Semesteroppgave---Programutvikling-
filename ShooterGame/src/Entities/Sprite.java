package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {

    private double width;
    private double height;
    private Image image;
    private ImageView iv;

    private Image[] frames;
    private double duration;

    public Sprite(String spriteFileName, int x, int y) {
        this.image = new Image(spriteFileName);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.iv = new ImageView();
        iv.setImage(this.image);
        iv.relocate(x, y);
    }

    public Sprite(String spriteFileName, String extension, int n, int x, int y) {
        this.frames = new Image[n];
        for (int i = 0; i < n; ++i) {
            try{
                String filename = spriteFileName + Integer.toString(i) + extension;
                String resource = getClass().getResource(filename).toURI().toString();
                frames[i] = new Image(resource);
            }catch (Exception e) {

            }
        }
        this.width = this.frames[0].getWidth();
        this.height = this.frames[0].getHeight();
        this.duration = 0.07;
        this.iv = new ImageView();
        iv.setImage(this.frames[0]);
    }

    public void getFrame(double time) {
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
