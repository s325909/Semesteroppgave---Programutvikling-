package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Class that controls the fxml used for the main menu.
 */
public class MainController implements Initializable{

    @FXML Button newGame, loadGame, howToPlay, credits, exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Method that open the GameWindow whe the NewGame Button is clicked.
     * The scene also fetches a style sheet.
     */
    @FXML
    public void launchGame() {
        try{
            Stage stage = (Stage) newGame.getScene().getWindow();
            URL url = getClass().getResource("/gameCode/GameWindow.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that opens the Loading.fxml when the loadGame Button is clciked.
     */
    @FXML
    public void openLoadMenu() {
        try{
            Stage stage = (Stage) loadGame.getScene().getWindow();
            URL url = getClass().getResource("/menuOptions/Loading.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that opens the HowToPlay.fxml when the howToPlay Button is clicked.
     */
    @FXML
    public void openHowToPlay() {
        try{
            Stage stage = (Stage) howToPlay.getScene().getWindow();
            URL url = getClass().getResource("/menuOptions/HowToPlay.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that opens Settings.fxml when the options Button is clicked
     */
    @FXML
    public void openCredits() {
        try{
            Stage stage = (Stage) credits.getScene().getWindow();
            URL url = getClass().getResource("/menuOptions/Credits.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method used to shut down the program when the exit Button is clicked.
     */
    @FXML
    public void exitGame() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}