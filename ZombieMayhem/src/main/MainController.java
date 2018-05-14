package main;

import gameCode.GameInitializer;
import gameCode.SettingsHandler;
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

/***
 * Class that controls the fxml used for the main menu.
 */
public class MainController implements Initializable{

    private Stage windowLoading, windowSettings, windowHowToPlay;
    private Parent rootLoading, rootSettings, rootHowToPlay;

    @FXML
    Button newGame, loadGame, options, howToPlay, exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Method that open the GameWindow whe the NewGame Button is clicked.
     * The scene also fetches a style sheet.
     */
    public void launchGame(){
        try{
            Stage stage = (Stage) newGame.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gameCode/GameWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            GameInitializer controller = loader.<GameInitializer>getController();
            controller.setDifficulty();
            controller.setSettings((new SettingsHandler()).loadSettings());
            scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }

    }

    /**
     * Method that opens the Loading.fxml when the loadGame Button is clciked.
     */
    public void openLoadMenu(){

        try {
            windowLoading = (Stage) loadGame.getScene().getWindow();
            rootLoading = FXMLLoader.load(getClass().getResource("../menuOptions/Loading.fxml"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene loadScene = new Scene(rootLoading, 1280, 720);
        windowLoading.setScene(loadScene);
        windowLoading.show();
    }

    /**
     * Method that opens Settings.fxml when the options Button is clicked
     */
    public void openSettings(){

        try {
            windowSettings = (Stage) options.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/Settings.fxml"));
            rootSettings = loader.load();
            SettingsController controller = loader.getController();
            controller.showReturnToGame(false);

            Scene settingsScene = new Scene(rootSettings, 1280, 720);
            windowSettings.setScene(settingsScene);
            windowSettings.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method that opens the HowToPlay.fxml when the howToPlay Button is clicked.
     * @throws IOException when loading the fxml.
     */
    public void openHowToPlay() throws IOException{
        try {
            windowHowToPlay = (Stage) howToPlay.getScene().getWindow();
            rootHowToPlay = FXMLLoader.load(getClass().getResource("../menuOptions/HowToPlay.fxml"));

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene howToPlayScene = new Scene(rootHowToPlay, 1280, 720);
        windowHowToPlay.setScene(howToPlayScene);
        windowHowToPlay.show();
    }

    /**
     * Method used to shut down the program when the exit Button is clicked.
     */
    public void exitGame(){

        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}