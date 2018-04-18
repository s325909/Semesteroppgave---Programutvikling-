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

public class InitializeSave implements Initializable {
    @FXML
    Button saveBtn;
    @FXML
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void saveGame() {
        saveBtn.setOnAction(event -> {
            SaveData data = new SaveData();
            data.name = fieldName.getText();
            data.hp = Integer.parseInt(fieldHP.getText());
            try {
                SaveLoadManager.save(data, writeName());

            } catch (Exception e) {
                System.out.println("Couldn't save" + e.getMessage());
            }
        });
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public String writeName() {
        return fieldName.getText() + ".save";
    }
}
