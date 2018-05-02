package menuOptions;

import gameCode.MusicPlayer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private Button returnToMenu;
    @FXML private Slider musicVolumeSlider, soundVolumeSlider;
    @FXML private Text musicVolumeNumber, soundVolumeNumber;
    @FXML private RadioButton select720, select1080, select1440;
    @FXML private TextField userWidth, userHeight;

    private Stage window_GameMenu;
    private Parent root_GameMenu;



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {



        musicVolumeSlider.setValue(MusicPlayer.mediaPlayer.getVolume() * 10);
        musicVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                MusicPlayer.mediaPlayer.setVolume(musicVolumeSlider.getValue() / 10);
                musicVolumeNumber.textProperty().setValue(
                        String.valueOf((int) musicVolumeSlider.getValue())
                );
            }
        });

        /*
        String path2 = new File("src/resources/Sound/Sound Effects/Player/player_breathing_calm.wav").getAbsolutePath();
        media2 = new Media(new File(path2).toURI().toString());
        mediaPlayer2 = new MediaPlayer(media2);
        mediaPlayer2.setAutoPlay(true);

        soundVolumeSlider.setValue(mediaPlayer2.getVolume() * 10);
        soundVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer2.setVolume(soundVolumeSlider.getValue() / 10);
                soundVolumeNumber.textProperty().setValue(
                        String.valueOf((int) soundVolumeSlider.getValue())
                );
            }
        });
        */
    }

    public void showReturnToMenu(boolean visible){
        returnToMenu.setVisible(visible);
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        try {
            if (event.getSource() == returnToMenu) {
                window_GameMenu = (Stage) returnToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/MainMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
        window_GameMenu.setScene(scene_GameMenu);
        window_GameMenu.show();
    }

    public Slider getMusicSlider() {
        return this.musicVolumeSlider;
    }
}
