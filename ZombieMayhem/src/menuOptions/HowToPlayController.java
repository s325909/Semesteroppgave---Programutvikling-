package menuOptions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls the HowToPlay.fxml,
 * and is used to make a returnToMenu button.
 */

public class HowToPlayController implements Initializable {
    @FXML
    private Button returnToMenu;

    private Stage window_GameMenu;
    private Parent root_GameMenu;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /***
     * Method that makes it possible to return to the main menu
     * @param event that takes the button's id
     */
    @FXML
    public void returnToMenu(ActionEvent event) {
        try {
            if (event.getSource() == returnToMenu) {
                window_GameMenu = (Stage) returnToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/MainMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
        window_GameMenu.setScene(scene_GameMenu);
        window_GameMenu.show();
    }

}
