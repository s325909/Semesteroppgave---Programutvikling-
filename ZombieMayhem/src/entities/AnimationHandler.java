package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class for handling Images and making these usable by the Entity class.
 * Each constructor handles a different type of Image array, but they are all
 * turned into the 3-dimensional Image array "frames".
 */
public class AnimationHandler {
    private ImageView iv;
    private Image[][][] frames;

    private int imageType;
    private int imageAction;

    private double width;
    private double height;

    // Determines how quickly images should cycle. A value of 0.016 equates to about 60 frames/second.
    private double duration;

    /**
     * Constructor which turns a one dimensional Image array into a 3-dimensional one.
     * Standard settings are set, the global width and height are set to the width and height of one of the images within the array,
     * and that Image is then applied to the ImageView, which further may be used in conjunction with the gameWindow Pane.
     * @param allImages Requires a one dimensional Image array.
     */
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

    /**
     * Constructor which turns a 2-dimensional Image array into a 3-dimensional one.
     * Standard settings are set, the global width and height are set to the width and height of one of the images within the array,
     * and that Image is then applied to the ImageView, which further may be used in conjunction with the gameWindow Pane.
     * @param allImages Requires a one dimensional Image array.
     */
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

    /**
     * Constructor which handles a 3-dimensional array and transfers it to the global frames.
     * Standard settings are set, the global width and height are set to the width and height of one of the images within the array,
     * and that Image is then applied to the ImageView, which further may be used in conjunction with the gameWindow Pane.
     * @param allImages Requires a one dimensional Image array.
     */
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

    /**
     * Method for deciding which Image type to display; the row in the 3-dimensional array.
     * This method is used in conjunction with Player, where Image type refers to which type of weapon to display;
     * knife, pistol, rifle, shotgun.
     *
     * Once type and action is set, the appropriate one-dimensional Image array can display.
     * @param index Requires index number.
     */
    void setImageType(int index) {
        if (index < frames.length)
            imageType = index;
    }

    /**
     * Method for deciding which Image action to display; the column in the 3-dimensional array.
     * This method is used in conjunction with Player, where Image action refers to which action the weapon Images should display;
     * idle, melee, fire, reload.
     *
     * Once type and action is set, the appropriate one-dimensional Image array can display.
     * @param index Requires index number.
     */
    void setImageAction(int index) {
        if (index < frames[imageType].length)
            this.imageAction = index;
    }

    /***
     * Method which handles the cycling of images in order to create an animation.
     * Duration may be adjusted in order to determine how they should cycle.
     * A duration value of 0.016 should equate to about 60 frames/second.
     * @param time Requires the Game's timer.
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
