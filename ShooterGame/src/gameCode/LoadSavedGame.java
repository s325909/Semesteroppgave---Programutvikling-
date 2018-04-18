package gameCode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoadSavedGame {

    @FXML
    Button loadBtn;
    @FXML
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    @FXML
    public void loadGame() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
            Stage loadGame = new Stage();
            loadGame.setScene(new Scene(root, 600, 400));
            loadGame.show();
            loadBtn.setOnAction(event->{
                try{
                    SaveData data = (SaveData) SaveLoadManager.load("1.save");
                    fieldName.setText(data.name);
                    fieldHP.setText(String.valueOf(data.hp));
                } catch(Exception e) {
                    System.out.println("Couldn't load saved data");
                }
            });
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
