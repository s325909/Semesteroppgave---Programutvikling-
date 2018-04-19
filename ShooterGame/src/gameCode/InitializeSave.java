package gameCode;

import entities.Player;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
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

public class InitializeSave implements Initializable {
    @FXML
    Button saveBtn;
    @FXML
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /***
     * Method which takes user input and establishes save function by clicking save button.
     */

    //@FXML
    public void saveGame(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnIdValue = btn.idProperty().getValue();
        String saveGameName = null;
        if(btnIdValue == "loadBtn1") {
            saveGameName = "game1.save";
        } else if(btnIdValue == "loadBtn2") {
            saveGameName = "game2.save";
        } else if(btnIdValue == "loadBtn3") {
            saveGameName = "game3.save";
        }

        actionEvent.getSource();
        //saveBtn.setOnAction(event -> {
            SaveData data = new SaveData();
            data.name = fieldName.getText();
            data.hp = Integer.parseInt(fieldHP.getText());
            try {
                SaveLoadManager.save(data, saveGameName); //Bytt ut med saveGameName fra wrtieName og slett funksjon og InputField
            } catch (Exception e) {
                System.out.println("Couldn't save" + e.getMessage());
            }
        //});
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
}
