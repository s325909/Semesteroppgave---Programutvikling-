package menuOptions;

import gameCode.DataHandler;
import gameCode.GameInitializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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

        if (checkFile(saveGame)) {
            goToGame(saveGame, true);
        } else {
            fileAlert(saveGame);
        }
    }

    private boolean checkFile(String saveGame) {
        DataHandler dataHandler = new DataHandler();
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        return dataHandler.readSaveFile(saveGame, gameCfg);
    }

    private void goToGame(String saveGame, boolean loadGame) {
        try{
            Stage stage = (Stage) loadBtn1.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gameCode/GameWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            GameInitializer controller = loader.getController();
            if (loadGame)
                controller.loadAndCreateGame(saveGame);

            scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    private void fileAlert(String saveGame) {

        ButtonType resume = new ButtonType("Resume", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType newGame = new ButtonType("New Game", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(resume, newGame);
        alert.setHeaderText(null);

        alert.setTitle("Loadgame Error");
        alert.contentTextProperty().set("Unable to load the save file." +
                "\n\nEither it doesn't exist, or it cannot be read.");

        alert.showAndWait().ifPresent(response -> {
            if (response == resume) {

            } else if (response == newGame) {
                goToGame(saveGame, false);
            }
        });
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

