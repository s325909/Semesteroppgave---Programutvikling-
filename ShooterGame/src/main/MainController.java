package main;

import gameCode.SceneSizeChangeListener;
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

    @FXML
    Button newGame, loadGame, options, help, exit;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void launchGame(){
        try{
            Stage stage = (Stage)
                    newGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/TopMenu.fxml"));/* Exception */
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }

    }

    public void openLoadMenu(){
        System.out.println("load menu");
    }

    public void openOptionsMenu(){
        System.out.println("settings");
    }

    public void openHelpMenu(){
        System.out.println("help me plz");
    }

    public void exitGame(){
        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}
