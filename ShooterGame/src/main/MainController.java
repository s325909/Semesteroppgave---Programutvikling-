package main;

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
            Stage stage = (Stage) newGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));/* Exception */
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../gameCode/StyleGameWindow.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }

    }



    public void openLoadMenu(){

        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("loadFiles.fxml"));
            Stage loadMenu = new Stage();
            loadMenu.setScene(new Scene(root, 450, 450));
            loadMenu.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    public void openOptionsMenu(){

        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("optionsMenu.fxml"));
            Stage loadMenu = new Stage();
            loadMenu.setScene(new Scene(root, 500, 500));
            loadMenu.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
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