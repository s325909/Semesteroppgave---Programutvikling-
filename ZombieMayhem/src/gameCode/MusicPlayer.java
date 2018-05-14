package gameCode;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;

/**
 * Class used to create a MediaPlayer object.
 * The volume is adjusted and it is set to play as soon as it is created.
 */
public class MusicPlayer {

    private File file;
    private Media media;
    public MediaPlayer mediaPlayer;

    private boolean paused = false;
    private boolean muted = false;
    private boolean playing = true;

    public MusicPlayer(String filename) {
        file = new File(filename);
        media = new Media(this.file.toURI().toString());
        mediaPlayer = new MediaPlayer(this.media);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    /**
     * Change the song being played
     * @param filename Requires a valid file.
     */
    public void changeSong(String filename) {
        file = new File(filename);
        media = new Media(this.file.toURI().toString());
        mediaPlayer = new MediaPlayer(this.media);
        mediaPlayer.setVolume(0.05);
    }

    public void playMusic() {
        mediaPlayer.play();
    }

    public void pauseMusic() {
        if (!paused) {
            mediaPlayer.pause();
            paused = true;
        } else {
            mediaPlayer.play();
            paused = false;
        }
    }

    public void stopMusic() {
        if (playing) {
            mediaPlayer.stop();
            playing = false;
        } else {
            mediaPlayer.play();
            playing = true;
        }
    }

    void muteVolume() {
        if (!muted) {
            mediaPlayer.setMute(true);
            muted = true;
        } else {
            mediaPlayer.setMute(false);
            muted = false;
        }
    }

    /**
     * Adjust the MusicPlayer's volume based on a double between 0 and 1.
     * @param volume
     */
    public void changeVolume(double volume) {
        if (volume > 1 || volume < 0) {
            System.out.println("Volume needs to be a double between 1 and 0");
        } else {
            mediaPlayer.setVolume(volume);
        }
    }
}
