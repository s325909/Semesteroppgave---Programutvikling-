package menuOptions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import notCurrentlyUsed.SaveLoadManager;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private Stage window_GameMenu;
    private Scene scene_GameMenu;
    private Parent root_GameMenu;

    @FXML
    private Button returnToMenu, loadBtn1, loadBtn2, loadBtn3;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    @FXML
    public void returnToMenu(ActionEvent event) {
        try {
            if (event.getSource() == returnToMenu) {
                window_GameMenu = (Stage) returnToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/GameMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
        window_GameMenu.setScene(scene_GameMenu);
        window_GameMenu.show();
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

