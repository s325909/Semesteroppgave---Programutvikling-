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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private Button returnToMenu, backGame;
    @FXML private Slider musicVolumeSlider, soundVolumeSlider;
    @FXML private Text musicVolumeNumber, soundVolumeNumber;
    @FXML private RadioButton select720, select1080, select1440;
    @FXML private TextField userWidth, userHeight;

    private Stage window_GameMenu;
    private Parent root_GameMenu;

    GameInitializer gameInitializer;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playMusic();

        //Multiply by 10 to get values between 1-10 instead of 0.1-1 to match the slider
        musicVolumeSlider.setValue(MusicPlayer.mediaPlayer.getVolume() * 10);

        //Shows the value of the musicVolumeSlider in text-format
        musicVolumeNumber.textProperty().setValue(
                String.valueOf((int) musicVolumeSlider.getValue())
        );

        //Changes the music volume based on the value of the slider
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

        //Loops ArrayList for weaponSound and multiplies each soundclip by 10 to match the slider
        for (int i = 0; i < GameInitializer.weaponSounds.length; i++){
            soundVolumeSlider.setValue(GameInitializer.weaponSounds[i].getVolume() * 10);
        }

        //Shows the value of the soundVolumeSlider in text-format
        soundVolumeNumber.textProperty().setValue(
                String.valueOf((int) soundVolumeSlider.getValue())
        );

        //Changes the sound volume based on the value of the slider
        soundVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (int i = 0; i < GameInitializer.weaponSounds.length; i++){
                    GameInitializer.weaponSounds[i].setVolume(soundVolumeSlider.getValue() / 10);
                }

                soundVolumeNumber.textProperty().setValue(
                        String.valueOf((int) soundVolumeSlider.getValue())
                );
            }
        });

        */

    }

    public void playMusic(){
        try {
            MusicPlayer musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }
    }

    public void showReturnToMenu(boolean visible){
        returnToMenu.setVisible(visible);
    }

    public void showReturnToGame(boolean visible) { backGame.setVisible(visible); }


    @FXML
    public void backToGame(){

        Stage stage = (Stage) backGame.getScene().getWindow();
        stage.close();


        /*
        try{
            Stage stage = (Stage) backGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }
        */
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
