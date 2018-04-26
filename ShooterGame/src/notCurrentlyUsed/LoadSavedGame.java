package notCurrentlyUsed;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import notCurrentlyUsed.SaveLoadManager;

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
