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

    @FXML private Button backToMenu, loadBtn1, loadBtn2, loadBtn3;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /***
     * Method which establishes load function by clicking load button.
     */
    @FXML
    public void loadGame(ActionEvent event) {
        String saveGame = "save1";
        Stage stage = (Stage) backToMenu.getScene().getWindow();
        if (event.getSource() == loadBtn1) {
            saveGame = "Savegame1";
            stage = (Stage) loadBtn1.getScene().getWindow();
        } else if (event.getSource() == loadBtn2) {
            saveGame = "Savegame2";
            stage = (Stage) loadBtn2.getScene().getWindow();
        } else if (event.getSource() == loadBtn3) {
            saveGame = "Savegame3";
            stage = (Stage) loadBtn3.getScene().getWindow();
        }

        if (checkFile(saveGame)) {
            goToGame(stage, saveGame, true);
        } else {
            fileAlert(stage, saveGame);
        }
    }

    private boolean checkFile(String saveGame) {
        DataHandler dataHandler = new DataHandler();
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        return dataHandler.readSaveFile(saveGame, gameCfg);
    }

    /**
     * Method which changes the scene to the GameWindow upon loading a new save file.
     * If loadGame is set to true, a new game is created and then altered to the file settings.
     * If loadGame is set to false, GameInitializer controller runs as normal.
     * @param stage Requires the application's stage.
     * @param saveGame Requires a String which represents the name of the save file.
     * @param loadGame Requires a boolean to determine whether it is a load game situation.
     */
    private void goToGame(Stage stage, String saveGame, boolean loadGame) {
        try{
            URL url = getClass().getResource("/gameCode/GameWindow.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            if (loadGame) {
                GameInitializer controller = loader.getController();
                controller.loadAndCreateGame(saveGame);
            }

            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Alert displayed to the user if the save file cannot be found during load.
     * Provides the user with an option to simply resume to menu navigation, or to start a new game instead.
     * @param stage Requires the current stage, so it may be passed to goToGame() method.
     * @param saveGame Requires a String to represent the filename of the save file.
     */
    private void fileAlert(Stage stage, String saveGame) {

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType newGame = new ButtonType("New Game", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(cancel, newGame);
        alert.setHeaderText(null);

        alert.setTitle("Loadgame Error");
        alert.contentTextProperty().set("Unable to load the save file." +
                "\n\nEither it doesn't exist, or it cannot be read.");

        alert.showAndWait().ifPresent(response -> {
            if (response == newGame) {
                goToGame(stage, saveGame, false);
            }
        });
    }

    /**
     * Method which returns the user to the MainMenu.
     */
    @FXML
    public void returnToMenu() {
        try {
            Stage stage = (Stage) backToMenu.getScene().getWindow();
            URL url = getClass().getResource("/main/MainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

