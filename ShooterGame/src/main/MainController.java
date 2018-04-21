package main;

import javafx.event.ActionEvent;
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

public class MainController implements Initializable{

    private Stage windowLoading, windowSettings, windowHowToPlay;
    private Parent rootLoading, rootSettings, rootHowToPlay;

    @FXML
    Button newGame, loadGame, options, help, exit;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void launchGame(){
        try{
            Stage stage = (Stage) newGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));/* Exception */
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../menuOptions/MenuStyle.css").toExternalForm());
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
                rootLoading = FXMLLoader.load(getClass().getResource("../menuOptions/LoadMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene loadScene = new Scene(rootLoading, 1280, 720);
        windowLoading.setScene(loadScene);
        windowLoading.show();
    }

    public void openSettings(ActionEvent event) throws IOException{

        try {
            if (event.getSource() == options){
                windowSettings = (Stage) options.getScene().getWindow();
                rootSettings = FXMLLoader.load(getClass().getResource("../menuOptions/Settings.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene optionsScene = new Scene(rootSettings, 1280, 720);
        windowSettings.setScene(optionsScene);
        windowSettings.show();
    }

    public void openHowToPlay(ActionEvent event) throws IOException{

        try {
            if (event.getSource() == help){
                windowHowToPlay = (Stage) help.getScene().getWindow();
                rootHowToPlay = FXMLLoader.load(getClass().getResource("../menuOptions/HowToPlay.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene helpScene = new Scene(rootHowToPlay, 1280, 720);
        windowHowToPlay.setScene(helpScene);
        windowHowToPlay.show();
    }

    public void exitGame(){

        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}