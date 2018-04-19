package gameCode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadSavedGame implements Initializable{

    @FXML
    Button loadBtn1, loadBtn2, loadBtn3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /***
     * Method which establishes load function by clicking load button.
     */

    @FXML
    public void loadGame() {
        loadBtn1.setOnAction(event->{
            try{
                SaveLoadManager.load("game1.save");
            } catch(Exception e) {
                System.out.println("Couldn't load saved data");
            }
        });

        loadBtn2.setOnAction(event->{
            try{
                SaveLoadManager.load("game2.save");
            } catch(Exception e) {
                System.out.println("Couldn't load saved data");
            }
        });

        loadBtn3.setOnAction(event->{
            try{
                SaveLoadManager.load("game3.save");
            } catch(Exception e) {
                System.out.println("Couldn't load saved data");
            }
        });
    }


}
