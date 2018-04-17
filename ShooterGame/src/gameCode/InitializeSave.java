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

public class InitializeSave implements Initializable{
    @FXML
    Button saveBtn, loadBtn;
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
        }

        @FXML
        public String writeName(){
            return fieldName.getText() + ".save";
        }

    @FXML
    public void loadGame() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
            Stage loadGame = new Stage();
            loadGame.setScene(new Scene(root, 600, 400));
            loadBtn.setOnAction(event->{
                try{
                    SaveData data = (SaveData) SaveLoadManager.load("1.save");
                    fieldName.setText(data.name);
                    fieldHP.setText(String.valueOf(data.hp));
                } catch(Exception e) {
                    System.out.println("Couldn't load saved data");
                }
            });
            loadGame.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
}
