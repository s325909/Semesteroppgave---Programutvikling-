package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimationHandler {
    ImageView iv;
    Image[][] frames;

    int activeI;
    int activeJ;

    private double width;
    private double height;
    private double maxWidth;
    private double maxHeight;

    private double duration;

    public AnimationHandler(Image[][] allImages) {
        this.iv = new ImageView();
        this.frames = allImages;
        this.activeI = 0;
        this.activeJ = 0;
        this.duration = 0.032;
        this.width = this.frames[activeI][activeJ].getWidth();
        this.height = this.frames[activeI][activeJ].getHeight();
        this.maxWidth = this.width;
        this.maxHeight = this.height;
        this.iv.setImage(this.frames[activeI][activeJ]);
    }

    public void setI(int i) {
        if (i < frames.length)
            this.activeI = i;
    }

    public void setJ(int j) {
        if (j < frames[this.activeI].length)
            this.activeJ = j;
    }

    /***
     * Method for determining how quickly the array of Sprites should cycle
     * @param time
     */
    protected void setFrame(double time) {
        int index = (int) ((time % (frames[this.activeI].length * duration)) / duration);
        this.iv.setImage(frames[this.activeI][index]);
    }

    protected void setFrame() {
        this.iv.setImage(frames[this.activeI][this.activeJ]);
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
}
