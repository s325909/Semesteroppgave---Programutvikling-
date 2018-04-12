package gameCode;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayer {

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    private boolean paused = false;
    private boolean muted = false;

    public MusicPlayer (String filename) {
        this.file = new File(filename);
        this.media = new Media(this.file.toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.mediaPlayer.setVolume(0.05);
        this.mediaPlayer.setAutoPlay(true);
    }

    public void changeSong(String filename) {
        this.file = new File(filename);
        this.media = new Media(this.file.toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.mediaPlayer.setVolume(0.05);
        this.mediaPlayer.setAutoPlay(true);
    }

    public void pauseMusic() {
        if (!paused) {
            this.mediaPlayer.pause();
            paused = true;
        } else {
            this.mediaPlayer.play();
            paused = false;
        }

    }

    public void changeVolume(double volume) {
        if (volume > 1 || volume < 0) {
            System.out.println("Volume needs to be a double between 1 and 0");
        } else {
            this.mediaPlayer.setVolume(volume);
        }
    }

    public void muteVolume() {
        if (!muted) {
            this.mediaPlayer.setMute(true);
            muted = true;
        } else {
            this.mediaPlayer.setMute(false);
            muted = false;
        }
    }

    public void repeatMusic() {
        this.mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }
}
