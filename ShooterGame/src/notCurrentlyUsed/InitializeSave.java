package notCurrentlyUsed;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import notCurrentlyUsed.SaveData;
import notCurrentlyUsed.SaveLoadManager;

import java.net.URL;
import java.util.ResourceBundle;

public class InitializeSave implements Initializable {
    @FXML
    Button saveBtn;
    @FXML
    SaveData saveData = null;
    //@FXML private Button OkBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /***
     * Method which establishes save function by clicking save button.
     */

    //@FXML
    public void saveGame(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnIdValue = btn.idProperty().getValue();
        String saveGameName = null;
        if(btnIdValue.equals("loadBtn1")) {
            saveGameName = "game1.save";
        } else if(btnIdValue.equals("loadBtn2")) {
            saveGameName = "game2.save";
        } else if(btnIdValue.equals("loadBtn3")) {
            saveGameName = "game3.save";
        }

        actionEvent.getSource();
        //saveBtn.setOnAction(event -> {
            try {
                SaveLoadManager.save(saveData, saveGameName); //Bytt ut med saveGameName fra writeName og slett funksjon og InputField
            } catch (Exception e) {
                System.out.println("Couldn't save" + e.getMessage());
            }
        //});
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
        System.out.println("Game is saved");
        //FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(getClass().getResource("GameIsSaved.fxml"));
    }

    /*@FXML
    public void gameIsSaved(){
        Stage stage = (Stage) OkBtn.getScene().getWindow();
        stage.close();
    }*/


}
