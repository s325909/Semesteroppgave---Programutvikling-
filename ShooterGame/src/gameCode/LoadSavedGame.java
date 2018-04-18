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
    Button loadBtn;
    @FXML
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /***
     * Method which establishes load function by clicking load button.
     */

    @FXML
    public void loadGame() {
            loadBtn.setOnAction(event->{
                try{
                    SaveData data = (SaveData) SaveLoadManager.load("1.save");
                    fieldName.setText(data.name);
                    fieldHP.setText(String.valueOf(data.hp));
                } catch(Exception e) {
                    System.out.println("Couldn't load saved data");
                }
            });
    }


}
