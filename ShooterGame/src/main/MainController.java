package main;

<<<<<<< HEAD
import gameCode.SceneSizeChangeListener;
import javafx.event.ActionEvent;
=======
>>>>>>> master
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
<<<<<<< HEAD
    @FXML
    public void openLoadMenu(){
=======



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
>>>>>>> master

    }
    @FXML
    public void openOptionsMenu(ActionEvent a) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("OptionsMenu.fxml"));
        Stage loadMenu = new Stage();
        loadMenu.setScene(new Scene(root, 600, 600));
        loadMenu.show();
    }
        catch(Exception e){

            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void openHelpMenu(){

        System.out.println("help me plz");
    }
    @FXML
    public void exitGame(){
        Stage stage = (Stage)
                exit.getScene().getWindow();
        stage.close();
    }
}
