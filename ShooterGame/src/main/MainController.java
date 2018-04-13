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

    Stage stage;
    Parent root;

    public void openLoadMenu(ActionEvent event) throws IOException{

        if(event.getSource() == loadGame){
            stage = (Stage) loadGame.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("loadFiles.fxml"));
        } else {
            System.out.println("Cannot enter load menu");
        }

        Scene scene = new Scene(root, 900, 900);
        stage.setScene(scene);
        stage.show();
    }


    public void openOptionsMenu(){

        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("optionsMenu.fxml"));
            Stage optionsMenu = new Stage();
            optionsMenu.setScene(new Scene(root, 500, 500));
            optionsMenu.show();
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