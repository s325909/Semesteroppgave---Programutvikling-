package menuOptions;

import gameCode.GameInitializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * The class is used to control the Loading.fxml,
 * and is used to make loadGame() and returnToMenu()
 */
public class LoadingController implements Initializable {

    @FXML private Button returnToMenu, loadBtn1, loadBtn2, loadBtn3;

    private Stage window_GameMenu;
    private Parent root_GameMenu;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /***
     * Method which establishes load function by clicking load button.
     */
    @FXML
    public void loadGame(ActionEvent event) {
        String saveGame = "save1";
        if (event.getSource() == loadBtn1) {
            saveGame = "Savegame1";
        } else if (event.getSource() == loadBtn2) {
            saveGame = "Savegame2";
        } else if (event.getSource() == loadBtn3) {
            saveGame = "Savegame3";
        }

        try{
            Stage stage = (Stage) loadBtn1.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gameCode/GameWindow.fxml"));
            Parent root = loader.load();
            GameInitializer controller = loader.<GameInitializer>getController();
            boolean succcess = controller.loadGame(saveGame);
            if (succcess) {
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }

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

