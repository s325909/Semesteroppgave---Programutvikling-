package main;

import gameCode.GameInitializer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    Button newGame, loadGame, credits, howToPlay, exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Method that open the GameWindow whe the NewGame Button is clicked.
     * The scene also fetches a style sheet.
     */
    @FXML
    public void launchGame(){
        try{
            Stage stage = (Stage) newGame.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gameCode/GameWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            GameInitializer controller = loader.getController();
            controller.setDifficulty();
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
    @FXML
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
     * Method that opens the HowToPlay.fxml when the howToPlay Button is clicked.
     * @throws IOException when loading the fxml.
     */
    @FXML
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
     * Method that opens Settings.fxml when the options Button is clicked
     */
    @FXML
    public void openCredits() throws IOException{
        try {
            windowSettings = (Stage) credits.getScene().getWindow();
            rootSettings = FXMLLoader.load(getClass().getResource("../menuOptions/Credits.fxml"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Scene settingsScene = new Scene(rootSettings, 1280, 720);
        windowSettings.setScene(settingsScene);
        windowSettings.show();
    }

    /**
     * Method used to shut down the program when the exit Button is clicked.
     */
    @FXML
    public void exitGame(){

        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}