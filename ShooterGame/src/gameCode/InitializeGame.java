package gameCode;

import entities.Enemy;
import entities.Player;
import entities.Zombie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitializeGame implements Initializable{

    @FXML Pane gameWindow;
    @FXML MenuBar topbar;
    @FXML Text hpCounter;

    Stage stage = new Stage();

    private Player player;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private Game game;
    private SceneSizeChangeListener sceneChange;

    SoundPlayer soundPlayer;

    final private boolean DEBUG = false;

    public void exit(){
        System.out.println("hello");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Play soundtrack
        try {
            soundPlayer = new SoundPlayer("src/resources/Sound/Soundtrack/Doom2.mp3", "track");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }

        // Create all Entity objects
        try {
            player = new Player("/resources/Art/Survivor/knife/idle/survivor-idle_knife_", ".png", 20, 500, 500, 100);
            player.playerAnimation("knife");
            player.setSpriteSize(250, 250);
        } catch (Exception e) {
            System.out.println("Error: Player did not load correctly");
        }

        try {
            for (int i = 0; i < 10; i++) {
                enemyList.add(new Zombie("/resources/Art/Zombie/skeleton-idle_", ".png", 17, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100));
                enemyList.get(i).setSpriteIdle("/resources/Art/Zombie/skeleton-idle_", ".png", 17);
                enemyList.get(i).setSpriteMoving("/resources/Art/Zombie/skeleton-move_", ".png", 17);
                enemyList.get(i).setSpriteMelee("/resources/Art/Zombie/skeleton-attack_", ".png", 9);
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        // Add Entities to the gameWindow
        // Enable DEBUG in order to view the Entities represented as Nodes (E.g. if Sprites fail to load correctly)
        if (DEBUG)
            gameWindow.getChildren().add(player.getNode());

        gameWindow.getChildren().add(player.getSprite().getImageView());

        for (Enemy enemy : enemyList) {
            if (DEBUG)
                gameWindow.getChildren().add(enemy.getNode());

            gameWindow.getChildren().add(enemy.getSprite().getImageView());
        }

        // Initialize the game
        game = new Game(player, enemyList, gameWindow);

        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);

    }

    /***
     * Method for handling player input via keyboard
     */
    public void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            if (e.getCode() == KeyCode.F12) {
                changeFullScreen();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            } else if (e.getCode() == KeyCode.P) {
                game.pauseGame();
                game.pauseDrops();
            } else if (e.getCode() == KeyCode.M) {
                soundPlayer.muteVolume();
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
        });
    }

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
}
