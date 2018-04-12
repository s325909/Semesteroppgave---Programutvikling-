package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsMenu implements Initializable{

    @FXML
    Button GameOptions, VideoOptions, SoundOptions, Controls;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    @FXML
   public void GameOptions(){
       Parent root;

       try {
           root = FXMLLoader.load(getClass().getResource("gameOptions.fxml"));
           Stage gameOptions = new Stage();
           gameOptions.setScene(new Scene(root, 500, 500));
           gameOptions.show();
       } catch (Exception e) {
           System.out.println("Error");
           System.out.println(e.getMessage());
       }
   }

   @FXML
    public void VideoOptions(){
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("videoOptions.fxml"));
            Stage videoOptions = new Stage();
            videoOptions.setScene(new Scene(root, 500, 500));
            videoOptions.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void SoundOptions(){
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("soundOptions.fxml"));
            Stage soundOptions = new Stage();
            soundOptions.setScene(new Scene(root, 500, 500));
            soundOptions.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void Controls(){
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("controls.fxml"));
            Stage controls = new Stage();
            controls.setScene(new Scene(root, 500, 500));
            controls.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

}
