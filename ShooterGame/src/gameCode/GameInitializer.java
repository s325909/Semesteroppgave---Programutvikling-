package gameCode;

import entities.Player;
import entities.Rock;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import menuOptions.SettingsController;

import java.io.IOException;
import java.net.URL;
import java.lang.*;
import java.util.List;
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
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, roundNbr, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp, ingameNormalDifficulty, ingameHardDifficulty, ingameInsaneDifficulty;
    @FXML private HBox ingameChooseDifficulty;
    @FXML private Button back_Help, settings;
    @FXML private Button normalDifficulty, hardDifficulty, insaneDifficulty;

    private Game game;
    public MusicPlayer musicPlayer;
    private boolean menuVisible;
    private boolean helpVisible;
    private boolean labelVisible;

    /***
     * Method which will load all assets used in the Game, create the level design, and allow the user to select a Difficulty.
     * Upon selecting the Difficulty, the rest of the Game is created and the game loop starts to run.
     * @param location Method is run upon GameWindow FXML being loaded.
     * @param resources Method is run upon GameWindow FXML being loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Create an object of MusicPlayer, which includes what file to play and automatically starts playing
        try {
            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }

        musicPlayer.playMusic();
        musicPlayer.muteVolume();
    }

    public void setDifficulty() {
        selectDifficulty();
    }

    public void setSettings(SettingsHandler.SettingsParameters settings) {
        musicPlayer.setVolume(settings.musicVolume/10);
    }

    public void setLoad(String saveGame) {
        ingameChooseDifficulty.setVisible(false);
        startGame(Game.Difficulty.NORMAL);
        game.loadGame(saveGame);
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
        game = new Game(this, difficulty, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer);
        showDifficulty(false);

        // Method getKeyPressed() is run continuously, and monitors user input
        Platform.runLater(game::getKeyPressed);
    }

    /**
     * Method which displays Labels to the user based on the appropriate state.
     * These include Game Over, Game Won, and Paused.
     */
    void showGameLabel() {
        if(!labelVisible) {
            gamePaused.setVisible(true);
            gameState.setVisible(true);
            labelVisible = true;
            if(game.isGameOver() && !game.isNewRound()) {
                gameState.setText("GAME OVER!");
                gameState.setTextFill(Color.INDIANRED);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            } else if (game.isGameOver() && game.isNewRound()) {
                gameState.setText("GAME WON!");
                gameState.setTextFill(Color.DARKGREEN);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            }else {
                gameState.setText("GAME IS PAUSED");
                gameState.setTextFill(Color.WHITE);
            }
        } else {
            gamePaused.setVisible(false);
            gameState.setVisible(false);
            pressKey.setVisible(false);
            labelVisible = false;
        }
    }

    /**
     * Method which will open the in-game menu.
     * It sets a hidden VBox to visible.
     */
    public void showMenu() {
        if(!menuVisible) {
            ingameMenu.setVisible(true);
            menuVisible = true;
        } else {
            ingameMenu.setVisible(false);
            menuVisible = false;
        }
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
     * Method which will show the Help portion of the inGameMenu.
     * It is run when pressing the How to Play button in the inGameMenu.
     */
    @FXML
    public void showHelp() {
        if (!helpVisible){
            ingameMenu.setVisible(false);
            gameState.setVisible(false);
            ingameHelp.setVisible(true);
            helpVisible = true;
        }else {
            ingameHelp.setVisible(false);
            ingameMenu.setVisible(true);
            gameState.setVisible(true);
            helpVisible = false;
        }
    }

    /**
     * Method which will exit the Game.
     * It is run when pressing the exit button in the inGameMenu.
     */
    @FXML
    public void exitGame() {
        System.exit(0);
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
        } else {
            ingameChooseDifficulty.setVisible(false);
            ingameNormalDifficulty.setVisible(false);
            ingameHardDifficulty.setVisible(false);
            ingameInsaneDifficulty.setVisible(false);
        }
    }

    private Stage windowSettings;
    private Parent rootSettings;
    private Scene sceneSettings;

    public void showSettings(ActionEvent event) throws IOException {
        /*

        //musicPlayer.muteVolume();

        try {
            if (event.getSource() == settings){
                windowSettings = (Stage) settings.getScene().getWindow();
                rootSettings = FXMLLoader.load(getClass().getResource("../menuOptions/Settings.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        sceneSettings = new Scene(rootSettings, 1280, 720);
        windowSettings.setScene(sceneSettings);
        windowSettings.show();

        */

        Parent root;

        try {

            if (event.getSource() == settings) {

                windowSettings = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/Settings.fxml"));
                root = loader.load();
                SettingsController controller = loader.getController();
                controller.showReturnToMenu(false);
                controller.showReturnToGame(true);
                setSettings((new SettingsHandler()).loadSettings());
                windowSettings.setScene(new Scene(root, 1280, 720));
                windowSettings.show();
            }
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
        }


    }

    @FXML Button loadGame;
    private Stage windowLoading;
    private Parent rootLoading;
    private Scene sceneLoading;
    public void showLoadMenu(ActionEvent event) throws IOException {
        try {
            if(event.getSource() == loadGame) {
                windowLoading = (Stage) loadGame.getScene().getWindow();
                rootLoading = FXMLLoader.load(getClass().getResource("../menuOptions/Loading.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene loadScene = new Scene(rootLoading, 1280, 720);
        windowLoading.setScene(loadScene);
        windowLoading.show();
    }
}