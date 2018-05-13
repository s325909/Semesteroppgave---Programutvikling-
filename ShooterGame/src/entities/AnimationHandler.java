package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimationHandler {
    private ImageView iv;
    private Image[][][] frames;

    private int imageType;
    private int imageAction;

    private double width;
    private double height;

    private double duration;

    public AnimationHandler(Image[] allImages) {
        this.iv = new ImageView();
        this.frames = new Image[1][1][];
        this.frames[0][0] = allImages;
        this.imageType = 0;
        this.imageAction = 0;
        this.duration = 0.032;
        this.width = this.frames[imageType][imageAction][0].getWidth();
        this.height = this.frames[imageType][imageAction][0].getHeight();
        this.iv.setImage(this.frames[this.imageType][this.imageAction][0]);
    }

    public AnimationHandler(Image[][] allImages) {
        this.iv = new ImageView();
        this.frames = new Image[1][][];
        this.frames[0] = allImages;
        this.imageType = 0;
        this.imageAction = 0;
        this.duration = 0.032;
        this.width = this.frames[imageType][imageAction][0].getWidth();
        this.height = this.frames[imageType][imageAction][0].getHeight();
        this.iv.setImage(this.frames[imageType][imageAction][0]);
    }

    public AnimationHandler(Image[][][] allImages) {
        this.iv = new ImageView();
        this.frames = allImages;
        this.imageType = 0;
        this.imageAction = 0;
        this.duration = 0.032;
        this.width = this.frames[imageType][imageAction][0].getWidth();
        this.height = this.frames[imageType][imageAction][0].getHeight();
        this.iv.setImage(this.frames[imageType][imageAction][0]);
    }

    public void setImageType(int index) {
        if (index < frames.length)
            imageType = index;
    }

    public void setImageAction(int index) {
        if (index < frames[imageType].length)
            this.imageAction = index;
    }

    /***
     * Method for determining how quickly the array of Sprites should cycle
     * @param time
     */
    protected void setFrame(double time) {
        int index = (int) ((time % (frames[imageType][imageAction].length * duration)) / duration);
        this.iv.setImage(frames[imageType][imageAction][index]);
    }

    public ImageView getImageView() {
        return this.iv;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
