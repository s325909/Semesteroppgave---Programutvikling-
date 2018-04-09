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
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/TopMenu.fxml"));/* Exception */
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }

    }



    public void openLoadMenu(){


        // loadGame.getChildren().setAll(FXMLLoader.load("loadFiles.fxml"));

        Parent root;

        try {

            // Stage stage = (Stage) loadGame.getScene().getWindow();
            // Parent parent = FXMLLoader.load(LoadController.class.getResource("loadFiles.fxml"));

            root = FXMLLoader.load(getClass().getResource("loadFiles.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 450, 450));
            stage.show();

            /*
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loadFiles.fxml"));
            Parent loadMenu = (Parent) fxmlLoader.load();
            Stage stage2 = new Stage();
            stage2.setTitle("Load Menu");
            stage2.setScene(new Scene(loadMenu));
            stage2.show();
            */

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
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
