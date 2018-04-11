package entities;

import javafx.scene.Node;
import javafx.scene.media.AudioClip;

public class Enemy extends Movable {
    public Enemy(){}

    public Enemy(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    public void movement(double time) {
        int i = (int)(time % 10);

        setMoving();

        if (i < 2) {
            goUp();
            goRight();
        } else if (i < 4) {
            goRight();
            goDown();
        } else if (i < 6) {
            goDown();
            goLeft();
        } else if (i < 8){
            goLeft();
            goUp();
        } else {
            stopX();
            stopY();
            setIdle();
        }
    }

    public void idleSound(double time, String filename) {
        int i = (int)(time % 15);

        AudioClip audioClip = new AudioClip(getClass().getResource(filename).toExternalForm());
        audioClip.setVolume(0.15);

        if (i < 1) {
            audioClip.play();
        }
    }
}
