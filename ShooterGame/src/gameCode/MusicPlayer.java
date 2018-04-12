package gameCode;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {

    MediaPlayer mediaPlayer;
    Media media;

    public MusicPlayer (String filename) {
        this.media = new Media(getClass().getResource(filename).toExternalForm());
        this.mediaPlayer = new MediaPlayer(this.media);
        mediaPlayer.setAutoPlay(true);
    }

    public void pauseMusic() {
        this.mediaPlayer.pause();
    }

    public void playMusic() {
        this.mediaPlayer.play();
    }

    public void changeVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }

    public void muteVolume() {
        this.mediaPlayer.muteProperty();
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
