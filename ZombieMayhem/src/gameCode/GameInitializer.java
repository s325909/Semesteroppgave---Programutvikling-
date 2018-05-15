package gameCode;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class which handles the initial parts of the Game creation.
 * These include handling the GameWindow FXML and all its elements, including the HUD and the gameWindow Pane.
 * It handles interaction between the FXML and the Game itself, which include restarting and pausing.
 * It also handles the creation and loading of all Images, AudioClips, and MediaPlayer's soundtracks.
 * And it creates the initial parts of level design.
 *
 * As the Difficulty is selected, the Player and the Game is created, where the Entities Player and Rocks are parsed into Game,
 * together with FXML specific elements such as the gameWindow Pane and all Labels which represent the GameWindow's HUD.
 */
public class GameInitializer implements Initializable{

    @FXML private Pane gameWindow;
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, roundNbr, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp, ingameSave, ingameLoad, ingameSettings, ingameNormalDifficulty, ingameHardDifficulty, ingameInsaneDifficulty;
    @FXML private HBox ingameChooseDifficulty;
    @FXML private Button howToPlay, saveGame, loadGame, settings, backHelp, backSave, backLoad, backSettings;
    @FXML private Button saveBtn1, saveBtn2, saveBtn3, loadBtn1, loadBtn2, loadBtn3;
    @FXML private Button normalDifficulty, hardDifficulty, insaneDifficulty;
    @FXML private Slider musicSlider, soundSlider;
    @FXML private Text musicNbr, soundNbr;

    private Game game;
    private boolean labelVisible;
    private boolean difficultyVisisble;
    private boolean menuElementVisible;

    private AssetsHandler assetsHandler;

    /***
     * Method which will load all assets used in the Game, create the level design, and allow the user to select a Difficulty.
     * Upon selecting the Difficulty, the rest of the Game is created and the game loop starts to run.
     * @param location Method is run upon GameWindow FXML being loaded.
     * @param resources Method is run upon GameWindow FXML being loaded.
     */

    public void initialize(URL location, ResourceBundle resources) {

        assetsHandler = new AssetsHandler();
        assetsHandler.getMediaPlayer().play();

        // Sets the values of the Settings slider and number equivalent to the MediaPlayer and all AudioClips volume values.
        musicSlider.setValue((int)(assetsHandler.getMusicVolume() * 10));
        musicNbr.setText(
                String.valueOf((int)(assetsHandler.getMusicVolume() * 10))
        );
        soundSlider.setValue((int)(assetsHandler.getSoundVolume() * 10));
        soundNbr.setText(
                String.valueOf((int)(assetsHandler.getSoundVolume() * 10))
        );

        // Listener which detects slider value change, and updates both the number next to the slider and the volume of the MediaPlayer
        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicNbr.setText(String.valueOf((int)musicSlider.getValue()));
            assetsHandler.getMediaPlayer().setVolume(musicSlider.getValue() / 10);
        });

        // Listener which detects slider value change, and updates both the number next to the slider and the volume of the Game's AudioClips
        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundNbr.setText(String.valueOf((int)soundSlider.getValue()));
            for(int i = 0; i < game.getAllAudioClips().length; i++) {
                game.getAllAudioClips()[i].setVolume(soundSlider.getValue() / 10);
            }
        });

        selectDifficulty();
        showDifficulty(true);
    }

    private boolean checkFile(String saveGame) {
        DataHandler dataHandler = new DataHandler();
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        return dataHandler.readSaveFile(saveGame, gameCfg);
    }

    /**
     * Method for selecting the Game's difficulty, through startGame() method call.
     */
    @FXML
    private void selectDifficulty(){
        normalDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.NORMAL);
        });
        hardDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.HARD);
        });
        insaneDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.INSANE);
        });
    }

    /**
     * Method which creates the Player, and finally the Game can be instantiated.
     * @param difficulty Requires the user selected difficulty in order to adjust the Game.
     */
    private void startGame(Game.Difficulty difficulty) {
        game = new Game(this, assetsHandler, difficulty, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore);
        showDifficulty(false);

        // Method getKeyPressed() is run continuously, and monitors user input
        Platform.runLater(game::getKeyPressed);
    }

    /**
     * Method for loading a saved game file from outside this controller.
     * @param saveGame Requires the string name of the file.
     */
    public void loadAndCreateGame(String saveGame) {
        if (checkFile(saveGame)) {
            ingameChooseDifficulty.setVisible(false);
            startGame(Game.Difficulty.NORMAL);
            game.loadGame(saveGame);
        } else {
            fileAlert(true);
        }
    }

    /**
     * Method which displays Labels to the user based on the appropriate state.
     * These include Game Over, Game Won, and Paused.
     */
    void showGameLabel() {
        if(!labelVisible) {
            if(game.isGameOver() && !game.isNewRound()) {
                setAndShowGameStateLabel("GAME OVER!", Color.INDIANRED);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            } else if (game.isGameOver() && game.isNewRound()) {
                setAndShowGameStateLabel("GAME WON!", Color.DARKGREEN);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            }
        } else {
            gamePaused.setVisible(false);
            gameState.setVisible(false);
            pressKey.setVisible(false);
            labelVisible = false;
        }
    }

    private void setAndShowGameStateLabel(String labelText, Color textColor) {
        gamePaused.setVisible(true);
        gameState.setVisible(true);
        labelVisible = true;
        gameState.setText(labelText);
        gameState.setTextFill(textColor);
    }

    /**
     * Method which will show the Difficulty selection screen.
     * @param show Requires a boolean to switch between the visibility.
     */
    private void showDifficulty(boolean show){
        if (show){
            ingameChooseDifficulty.setVisible(true);
            ingameNormalDifficulty.setVisible(true);
            ingameHardDifficulty.setVisible(true);
            ingameInsaneDifficulty.setVisible(true);
            difficultyVisisble = true;
        } else {
            ingameChooseDifficulty.setVisible(false);
            ingameNormalDifficulty.setVisible(false);
            ingameHardDifficulty.setVisible(false);
            ingameInsaneDifficulty.setVisible(false);
            difficultyVisisble = false;
        }
    }

    /**
     * Method which will open the in-game menu.
     * It sets a hidden VBox to visible.
     */
    public void showMenu() {
        if (!ingameMenu.isVisible()){
            if (!menuElementVisible)
                ingameMenu.setVisible(true);
            else
                hideMenuElements();
        } else {
            ingameMenu.setVisible(false);
        }
    }

    /**
     * Method for switching between the various menu elements of the ingame menu.
     * @param event Requires a Button event in order to determine which element to show.
     */
    @FXML
    public void showMenuElement(ActionEvent event) {
        if (ingameMenu.isVisible()) {
            if (event.getSource() == howToPlay) {
                ingameHelp.setVisible(true);
                menuElementVisible = true;
                showMenu();
            } else if (event.getSource() == saveGame) {
                ingameSave.setVisible(true);
                menuElementVisible = true;
                showMenu();
            } else if (event.getSource() == loadGame) {
                ingameLoad.setVisible(true);
                menuElementVisible = true;
                showMenu();
            } else if (event.getSource() == settings) {
                ingameSettings.setVisible(true);
                menuElementVisible = true;
                showMenu();
            }
        } else {
            if (event.getSource() == backHelp) {
                ingameHelp.setVisible(false);
                menuElementVisible = false;
                showMenu();
            } else if (event.getSource() == backSave) {
                ingameSave.setVisible(false);
                menuElementVisible = false;
                showMenu();
            } else if (event.getSource() == backLoad) {
                ingameLoad.setVisible(false);
                menuElementVisible = false;
                showMenu();
            } else if (event.getSource() == backSettings) {
                ingameSettings.setVisible(false);
                menuElementVisible = false;
                showMenu();
            }
        }
    }

    /**
     * Method that will hide the sub-menu elements.
     * Used in Game's getKeyPressed() to resume game with esc from every sub-menu.
     */
    public void hideMenuElements() {
        ingameSave.setVisible(false);
        ingameLoad.setVisible(false);
        ingameHelp.setVisible(false);
        ingameSettings.setVisible(false);
        menuElementVisible = false;
    }


    /**
     * Method which will resume the Game.
     * It is run when pressing the resume button in the inGameMenu.
     */
    @FXML
    public void resumeGame(){
        game.pauseGame();
        showMenu();
    }

    /**
     * Method which will restart the Game.
     * It is run when pressing the restart button in the inGameMenu.
     */
    @FXML
    public void restartGame() {
        game.restartGame();
        showMenu();
        showGameLabel();
        showDifficulty(true);
    }

    /**
     * Method for saving the current game. See saveGame().
     * @param event Requires a Button event in order to determine which file to save to.
     */
    @FXML
    public void saveGame(ActionEvent event) {
        boolean success = false;
        if (event.getSource() == saveBtn1) {
            success = game.saveGame("Savegame1");
        } else if (event.getSource() == saveBtn2) {
            success = game.saveGame("Savegame2");
        } else if (event.getSource() == saveBtn3) {
            success = game.saveGame("Savegame3");
        }

        if(success) {
            ingameSave.setVisible(false);
            showGameLabel();
            hideMenuElements();
            game.setRunning(true);
        } else {
            fileAlert(false);
        }
    }

    /**
     * Method for loading the current game. See loadGame().
     * @param event Requires a Button event in order to determine which file to load from.
     */
    @FXML
    public void loadGame(ActionEvent event) {
        boolean success = false;
        if (event.getSource() == loadBtn1) {
            success = game.loadGame("Savegame1");
        } else if (event.getSource() == loadBtn2) {
            success = game.loadGame("Savegame2");
        } else if (event.getSource() == loadBtn3) {
            success = game.loadGame("Savegame3");
        }

        if(success) {
            ingameLoad.setVisible(false);
            showGameLabel();
            hideMenuElements();
            game.setRunning(true);
        } else {
            fileAlert(true);
        }
    }

    private void fileAlert(boolean loadGame) {

        ButtonType resume = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(resume);
        alert.setHeaderText(null);

        if (loadGame) {
            alert.setTitle("Loadgame Error");
            alert.contentTextProperty().set("Unable to load the save file." +
                    "\n\nEither it doesn't exist, or it cannot be read.");
        } else {
            alert.setTitle("Savegame Error");
            alert.contentTextProperty().set("Unable to create the save file.");
        }

        alert.showAndWait().ifPresent(response -> { });
    }

    /**
     * Method which will exit the Game.
     * It is run when pressing the exit button in the inGameMenu.
     */
    @FXML
    public void exitGame() {
        System.exit(0);
    }

    public HBox getIngameChooseDifficulty() {
        return ingameChooseDifficulty;
    }

    public VBox getIngameNormalDifficulty() {
        return ingameNormalDifficulty;
    }

    public VBox getIngameHardDifficulty() {
        return ingameHardDifficulty;
    }

    public VBox getIngameInsaneDifficulty() {
        return ingameInsaneDifficulty;
    }

    /**
     * Method for showing the select difficulty screen once GameWindow FXML is called during MainController.launchGame().
     */
    public void setDifficulty() {
        selectDifficulty();
    }

    public boolean isDifficultyVisible() {
        return difficultyVisisble;
    }

    public boolean isMenuElementVisible() {
        return menuElementVisible;
    }
}