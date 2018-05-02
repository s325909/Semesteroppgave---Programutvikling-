package menuOptions;

import gameCode.GameInitializer;
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

        /*

        try {
            MusicPlayer musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }



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

        */


        soundVolumeSlider.setValue(GameInitializer.weaponSounds[0].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[1].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[2].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[3].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[4].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[5].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[6].getVolume() * 10);
        soundVolumeSlider.setValue(GameInitializer.weaponSounds[7].getVolume() * 10);




        soundVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                GameInitializer.weaponSounds[0].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[1].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[2].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[3].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[4].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[5].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[6].setVolume(soundVolumeSlider.getValue() / 10);
                GameInitializer.weaponSounds[7].setVolume(soundVolumeSlider.getValue() / 10);

                soundVolumeNumber.textProperty().setValue(
                        String.valueOf((int) soundVolumeSlider.getValue())
                );
            }
        });

    }

    public void showReturnToMenu(boolean visible){
        returnToMenu.setVisible(visible);
    }

    @FXML
    public void returnToMenu(ActionEvent event) {

        MusicPlayer.mediaPlayer.stop();

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
