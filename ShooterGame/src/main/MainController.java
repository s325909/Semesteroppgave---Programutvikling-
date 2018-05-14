package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import menuOptions.SettingsController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    private Stage windowLoading, windowSettings, windowHowToPlay;
    private Parent rootLoading, rootSettings, rootHowToPlay;

    @FXML
    Button newGame, loadGame, options, howToPlay, exit;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void launchGame(){
        try{
            Stage stage = (Stage) newGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }

    }

    public void openLoadMenu(ActionEvent event) throws IOException{

        try {
            if(event.getSource() == loadGame) {
                windowLoading = (Stage) loadGame.getScene().getWindow();
                rootLoading = FXMLLoader.load(getClass().getResource("../menuOptions/Loading.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene loadScene = new Scene(rootLoading, 1280, 720);
        windowLoading.setScene(loadScene);
        windowLoading.show();
    }

    public void openSettings(ActionEvent event) throws IOException {

        try {

            if (event.getSource() == options) {
                windowSettings = (Stage) options.getScene().getWindow();


                FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/Settings.fxml"));
                rootSettings = loader.load();
                SettingsController controller = loader.getController();
                controller.showReturnToGame(true);
                controller.playMusic();

                Scene settingsScene = new Scene(rootSettings, 1280, 720);
                windowSettings.setScene(settingsScene);
                windowSettings.show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void openHowToPlay(ActionEvent event) throws IOException{

        try {
            if (event.getSource() == howToPlay){
                windowHowToPlay = (Stage) howToPlay.getScene().getWindow();
                rootHowToPlay = FXMLLoader.load(getClass().getResource("../menuOptions/HowToPlay.fxml"));

            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene howToPlayScene = new Scene(rootHowToPlay, 1280, 720);
        windowHowToPlay.setScene(howToPlayScene);
        windowHowToPlay.show();
    }

    public void exitGame(){

        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}