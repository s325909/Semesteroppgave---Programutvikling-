package menuOptions;

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

    @FXML private Button backToMenu;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method that makes it possible to return to the main menu
     */
    @FXML
    public void returnToMenu() {
        try{
            Stage stage = (Stage) backToMenu.getScene().getWindow();
            URL url = getClass().getResource("/main/MainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
