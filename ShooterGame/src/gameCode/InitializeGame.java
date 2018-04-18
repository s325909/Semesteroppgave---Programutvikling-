package gameCode;

import entities.Player;
import entities.Zombie;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.MainController;

import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class InitializeGame implements Initializable{

    @FXML Pane gameWindow;
    @FXML MenuBar topbar;
    @FXML Text playerHP, magazineSize, poolSize, score;
    @FXML Label gameState, pressKey, pressKey2;
  //  @FXML Button saveBtn, loadBtn;
  //  TextField fieldName = new TextField();
  //  TextField fieldHP = new TextField();

    Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies = new ArrayList<>();
    private Game game;
    private SceneSizeChangeListener sceneChange;

    private MusicPlayer musicPlayer;

    // Debug tool to view the Node representation of every entity, should images not load correctly.
    final private boolean DEBUG = true;

    /***
     * Method which will create every Entity and add these to the gameWindow.
     * Method will also initialize the first soundtrack.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }

        try {
            player = new Player("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20, (int)gameWindow.getHeight()/2, (int)gameWindow.getWidth()/2, 100);
            player.playerAnimation("knife");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element);
            }
            System.out.println("Error: Player did not load correctly");
        }

        System.out.println(gameWindow.getHeight());

        try {
            for (int i = 0; i < 10; i++) {
                zombies.add(new Zombie("/resources/Art/Zombie/skeleton-idle_", ".png", 17, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100));
                zombies.get(i).setSpriteIdle("/resources/Art/Zombie/skeleton-idle_", ".png", 17);
                zombies.get(i).setSpriteMoving("/resources/Art/Zombie/skeleton-move_", ".png", 17);
                zombies.get(i).setSpriteMelee("/resources/Art/Zombie/skeleton-attack_", ".png", 9);
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        if (DEBUG) {
            gameWindow.getChildren().add(player.getNode());
            for (Zombie zombie : zombies)
                gameWindow.getChildren().add(zombie.getNode());
        }

        gameWindow.getChildren().add(player.getSprite().getImageView());

        for (Zombie zombie : zombies) {
            gameWindow.getChildren().add(zombie.getSprite().getImageView());
        }

        // Initialize the game

        game = new Game(player, zombies, gameWindow, playerHP, magazineSize, poolSize, score);

        game.setController(this);

        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
    }

    /***
     *
     * @param visible
     */
    public void setGameOverLabel(boolean visible) {
        gameState.setVisible(visible);
        gameState.setText("GAME OVER!");
        gameState.setTextFill(Color.INDIANRED);
        pressKey.setVisible(visible);
        pressKey.setText("Press R to Restart");
        pressKey.setTextFill(Color.WHITE);
        pressKey2.setVisible(visible);
        pressKey2.setText("Press ESC to pop up in game Menu...IDK");
    }

    /***
     *
     * @param visible
     */
    public void setGameIsPausedLabel(boolean visible){
        gameState.setVisible(visible);
        gameState.setText("GAME IS PAUSED");
        gameState.setTextFill(Color.WHITE);
        pressKey.setVisible(visible);
        pressKey.setText("Press P to Continue");
        pressKey.setTextFill(Color.WHITE);
        pressKey.setTextFill(Color.WHITE);
        pressKey2.setVisible(visible);
        pressKey2.setText("Press ESC to pop up in game Menu...IDK");

    }

    /***
     * Method which takes in user keyboard input.
     * Some input is handled in the movePlayer() method.
     */
    public void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            if (e.getCode() == KeyCode.F12) {
                changeFullScreen();

            } else if (e.getCode() == (KeyCode.P) || e.getCode() == KeyCode.ESCAPE) {
                game.pauseGame();

            } else if (e.getCode() == KeyCode.R) {
                game.setrPressed(true);
                //game.restartGame();

                    //System.out.println("Starting New Game...");

            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
            if (e.getCode() == KeyCode.R)
                        game.setrPressed(false);
        });

        gameWindow.getScene().setOnKeyPressed(event -> {
            player.movePlayer(event);
            if (event.getCode() == KeyCode.F5){
                System.out.println("Game is saved");
                saveGame(null);
            }
        });
    }


    /***
     * Method which will change the FullScreen state of the application.
     */
    public void changeFullScreen() {
        Stage stage = (Stage) gameWindow.getScene().getWindow();
        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
            topbar.setVisible(true);
        } else {
            stage.setFullScreen(true);
            topbar.setVisible(false);
        }
    }

    public void exitGame() {
        System.exit(0);
    }

    public void saveGame(ActionEvent actionEvent) {
        Parent root;
        try {
            game.pauseGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("saveGame.fxml"));
            root = loader.load();
            InitializeSave initializeSave = loader.getController();
            initializeSave.fieldHP.setText(this.playerHP.getText());
            //root = FXMLLoader.load(getClass().getResource("saveGame.fxml"));
            Stage saveGame = new Stage();
            saveGame.setScene(new Scene(root, 600, 400));
            saveGame.show();
        } catch (Exception e) {
            System.out.println("Open SavePane Error");
        }
    }
}