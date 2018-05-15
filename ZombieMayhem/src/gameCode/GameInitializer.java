package gameCode;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
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
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, roundNbr, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp, ingameSave, ingameLoad, ingameSettings, ingameNormalDifficulty, ingameHardDifficulty, ingameInsaneDifficulty;
    @FXML private HBox ingameChooseDifficulty;
    @FXML private Button howToPlay, saveGame, loadGame, settings, backHelp, backSave, backLoad, backSettings;
    @FXML private Button saveBtn1, saveBtn2, saveBtn3, loadBtn1, loadBtn2, loadBtn3;
    @FXML private Button normalDifficulty, hardDifficulty, insaneDifficulty;
    @FXML private Slider musicSlider, soundSlider;
    @FXML private Text musicNbr, soundNbr;

    private Game game;
    public MediaPlayer mediaPlayer;
    private boolean labelVisible;
    private boolean menuVisible;
    private boolean menuElementVisible;
    private boolean muted;

    private boolean saveMenuVisible, loadMenuVisible, helpMenuVisible, settingsMenuVisible;

    /***
     * Method which will load all assets used in the Game, create the level design, and allow the user to select a Difficulty.
     * Upon selecting the Difficulty, the rest of the Game is created and the game loop starts to run.
     * @param location Method is run upon GameWindow FXML being loaded.
     * @param resources Method is run upon GameWindow FXML being loaded.
     */

    public void initialize(URL location, ResourceBundle resources) {

        //Create an object of MusicPlayer, which includes what file to play and automatically starts playing
        try {
            File file = new File("src/resources/Sound/Soundtrack/Doom2.mp3");
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }

        musicSlider.setValue(5);
        musicNbr.setText(
                String.valueOf(5)
        );

        soundSlider.setValue(1);
        soundNbr.setText(
                String.valueOf(1)
        );

        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicNbr.setText(String.valueOf((int)musicSlider.getValue()));
            mediaPlayer.setVolume(musicSlider.getValue()/10);
        });

        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            soundNbr.setText(String.valueOf((int)soundSlider.getValue()));

            for(int i = 0; i < game.getAllAudioClips().length; i++) {
                game.getAllAudioClips()[i].setVolume(soundSlider.getValue()/10);
            }
        });

        showDifficulty(true);
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

    public void loadGame(String saveGame) {
        ingameChooseDifficulty.setVisible(false);
        startGame(Game.Difficulty.NORMAL);
        game.loadGame(saveGame);
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

    void setAndShowGameStateLabel(String labelText, Color textColor) {
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
        } else {
            ingameChooseDifficulty.setVisible(false);
            ingameNormalDifficulty.setVisible(false);
            ingameHardDifficulty.setVisible(false);
            ingameInsaneDifficulty.setVisible(false);
        }
    }


    //TODO make seperate bool for each sub menu?

    private void showSaveMenu(){
        if (!saveMenuVisible){
            ingameSave.setVisible(true);
            menuVisible = true;
        } else {
            ingameSave.setVisible(false);
            menuVisible = false;
        }
    }

    private void showLoadMenu() {
        if (!loadMenuVisible){
            ingameLoad.setVisible(true);
        } else {
            ingameLoad.setVisible(false);
        }
    }

    private void showHelpMenu() {
        if (!helpMenuVisible){
            ingameHelp.setVisible(true);
        } else {
            ingameHelp.setVisible(false);
        }
    }

    private void showSettingsMenu() {
        if (!settingsMenuVisible){
            ingameSettings.setVisible(true);
        } else {
            ingameSettings.setVisible(false);
        }
    }


    /**
     * Method which will open the in-game menu.
     * It sets a hidden VBox to visible.
     */
    public void showMenu() {
        System.out.println("show menu "+ menuVisible);

        if (ingameMenu.isVisible() == false && menuVisible == false){
            ingameMenu.setVisible(true);
            menuVisible = true;
            //hideMenuElements();
        }else {
            ingameMenu.setVisible(false);
            menuVisible = false;
            //hideMenuElements();
        }




        /*
        if(ingameMenu.isVisible() == false && !menuElementVisible) {
            ingameMenu.setVisible(true);
            menuVisible = true;
        }
        */
    }

    //Hides the topp level menu
    public void hideInGameMenu() {
        if (ingameMenu.isVisible()) {
            ingameMenu.setVisible(false);
            menuVisible = true;
        }
    }

    @FXML
    public void showMenuElement(ActionEvent event) {
        //menuVisible = false;

        if (ingameMenu.isVisible()) {
            if (event.getSource() == howToPlay) {
                showHelpMenu();
                hideInGameMenu();
            } else if (event.getSource() == saveGame) {
                showSaveMenu();
                hideInGameMenu();
            } else if (event.getSource() == loadGame) {
                showLoadMenu();
                hideInGameMenu();
            } else if (event.getSource() == settings) {
                showSettingsMenu();
                hideInGameMenu();
            }


            //ingameMenu.setVisible(false);
            //menuElementVisible = true;

        } else {
            menuVisible = false;
            if (event.getSource() == backHelp) {
                ingameHelp.setVisible(false);
                showMenu();
            } else if (event.getSource() == backSave) {
                ingameSave.setVisible(false);
                showMenu();
            } else if (event.getSource() == backLoad) {
                ingameLoad.setVisible(false);
                showMenu();
            } else if (event.getSource() == backSettings) {
                ingameSettings.setVisible(false);
                showMenu();
            }

            menuVisible = false;

            //hideInGameMenu();

            //ingameMenu.setVisible(true);
            //menuElementVisible = false;

        }
        /*
        if (menuElementVisible){
            ingameMenu.setVisible(false);
        }
        */
    }



    public void hideMenuElements() {

        ingameSave.setVisible(false);
        ingameLoad.setVisible(false);
        ingameHelp.setVisible(false);
        ingameSettings.setVisible(false);

        /*
        if (!menuVisible){
            ingameSave.setVisible(false);
            ingameLoad.setVisible(false);
            ingameHelp.setVisible(false);
            ingameSettings.setVisible(false);
            hideInGameMenu();
        }
        */

        /*
        showSaveMenu();
        showLoadMenu();
        showHelpMenu();
        showSettingsMenu();
        */


        //ingameHelp.setVisible(false);
        //helpMenuVisible = false;


        //hideInGameMenu();

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
        showDifficulty(true);
    }

    @FXML
    public void saveGame(ActionEvent event) {
        if (event.getSource() == saveBtn1) {
            game.saveGame("Savegame1");
        } else if (event.getSource() == saveBtn2) {
            game.saveGame("Savegame2");
        } else if (event.getSource() == saveBtn3) {
            game.saveGame("Savegame3");
        }
        ingameSave.setVisible(false);
        menuVisible = false;
        menuElementVisible = false;
        game.setRunning(true);
    }

    @FXML
    public void loadGame(ActionEvent event) {
        if (event.getSource() == loadBtn1) {
            game.loadGame("Savegame1");
        } else if (event.getSource() == loadBtn2) {
            game.loadGame("Savegame2");
        } else if (event.getSource() == loadBtn3) {
            game.loadGame("Savegame3");
        }
        ingameLoad.setVisible(false);
        menuVisible = false;
        menuElementVisible = false;
        game.setRunning(true);
    }

    /**
     * Method which will exit the Game.
     * It is run when pressing the exit button in the inGameMenu.
     */
    @FXML
    public void exitGame() {
        System.exit(0);
    }

    public void muteMediaPlayer() {
        if(!muted) {
            mediaPlayer.setMute(true);
            muted = true;
        } else {
            mediaPlayer.setMute(false);
            muted = false;
        }
    }

    public void setDifficulty() {
        selectDifficulty();
    }
}