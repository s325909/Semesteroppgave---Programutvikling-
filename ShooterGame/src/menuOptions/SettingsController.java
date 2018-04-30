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

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {



        String path = new File("src/resources/Sound/Soundtrack/Doom2.mp3").getAbsolutePath();
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);



        musicVolumeSlider.setValue(mediaPlayer.getVolume() * 99);
        musicVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(musicVolumeSlider.getValue() / 99);
                musicVolumeNumber.textProperty().setValue(
                        String.valueOf((int) musicVolumeSlider.getValue())
                );
            }
        });

        




        /*
        musicVolumeSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                musicVolumeNumber.textProperty().setValue(
                        String.valueOf((int) musicVolumeSlider.getValue()));
            }
        });

        soundVolumeSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                soundVolumeNumber.textProperty().setValue(
                        String.valueOf((int) soundVolumeSlider.getValue()));
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
