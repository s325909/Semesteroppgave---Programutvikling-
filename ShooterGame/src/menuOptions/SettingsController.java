package menuOptions;

import gameCode.GameInitializer;
import gameCode.MusicPlayer;
import gameCode.SettingsHandler;
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

    @FXML private Button backToMenu, backToGame;
    @FXML private Slider musicVolumeSlider, soundVolumeSlider;
    @FXML private Text musicVolumeNumber, soundVolumeNumber;
    @FXML private RadioButton select720, select1080, select1440;
    @FXML private TextField userWidth, userHeight;

    private Stage window_GameMenu;
    private Parent root_GameMenu;
    private MusicPlayer musicPlayer;

    private int horizontalResolution = 1280;
    private int verticalResolution = 720;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SettingsHandler.SettingsParameters settings = (new SettingsHandler()).loadSettings();
        playMusic(settings.musicVolume);

        //Multiply by 10 to get values between 1-10 instead of 0.1-1 to match the slider
        musicVolumeSlider.setValue(musicPlayer.getVolume() * 10);

        //Shows the value of the musicVolumeSlider in text-format
        musicVolumeNumber.textProperty().setValue(
                String.valueOf((int) musicVolumeSlider.getValue())
        );

        //Changes the music volume based on the value of the slider
        musicVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                musicPlayer.setVolume(musicVolumeSlider.getValue() / 10);
                musicVolumeNumber.textProperty().setValue(
                        String.valueOf((int) musicVolumeSlider.getValue())
                );
            }
        });



        /*

        soundClip();


        //Shows the value of the soundVolumeSlider in text-format
        soundVolumeNumber.textProperty().setValue(
                String.valueOf((int) soundVolumeSlider.getValue())
        );

        //Changes the sound volume based on the value of the slider
        soundVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                changeSoundClip();

                soundVolumeNumber.textProperty().setValue(
                        String.valueOf((int) soundVolumeSlider.getValue())
                );
            }
        });

        */

    }

    public void playMusic(double volume){
        try {
            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
            musicPlayer.setVolume(volume/10);
            musicPlayer.playMusic();
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }
    }

    /*public void soundClip(){
        //Loops ArrayList for weaponSound and multiplies each soundclip by 10 to match the slider
        for (int i = 0; i < GameInitializer.weaponSounds.length; i++){
            soundVolumeSlider.setValue(GameInitializer.weaponSounds[i].getVolume() * 10);
        }
        for (int i = 0; i < GameInitializer.basicSounds.length; i++){
            soundVolumeSlider.setValue(GameInitializer.basicSounds[i].getVolume() * 10);
        }
        for (int i = 0; i < GameInitializer.zombieAudioClips.length; i++){
            soundVolumeSlider.setValue(GameInitializer.zombieAudioClips[i].getVolume() * 10);
        }
    }

    public void changeSoundClip(){
        for (int i = 0; i < GameInitializer.weaponSounds.length; i++){
            GameInitializer.weaponSounds[i].setVolume(soundVolumeSlider.getValue() / 10);
        }
        for (int i = 0; i < GameInitializer.basicSounds.length; i++){
            GameInitializer.basicSounds[i].setVolume(soundVolumeSlider.getValue() / 10);
        }
        for (int i = 0; i < GameInitializer.zombieAudioClips.length; i++){
            GameInitializer.zombieAudioClips[i].setVolume(soundVolumeSlider.getValue() / 10);
        }
    }*/

    public void showReturnToMenu(boolean visible){
        backToMenu.setVisible(visible);
    }

    public void showReturnToGame(boolean visible) { backToGame.setVisible(visible); }


    @FXML
    public void returnToGame(ActionEvent event) throws IOException{
        

        try {
            if (event.getSource() == backToGame) {
                (new SettingsHandler()).storeSettings(horizontalResolution, verticalResolution, musicVolumeSlider.getValue(), soundVolumeSlider.getValue());
                Stage stage = (Stage) backToGame.getScene().getWindow();
                stage.close();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



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
    public void returnToMenu(ActionEvent event) throws IOException {

        musicPlayer.stopMusic();
        (new SettingsHandler()).storeSettings(horizontalResolution, verticalResolution, musicVolumeSlider.getValue(), soundVolumeSlider.getValue());

        try {
            if (event.getSource() == backToMenu) {
                window_GameMenu = (Stage) backToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/MainMenu.fxml"));
                Scene scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
                window_GameMenu.setScene(scene_GameMenu);
                window_GameMenu.show();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Slider getMusicSlider() {
        return this.musicVolumeSlider;
    }
}
