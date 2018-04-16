package gameCode;

import entities.Player;
import entities.Zombie;
import javafx.application.Platform;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitializeGame implements Initializable{

    @FXML Pane gameWindow;
    @FXML MenuBar topbar;
    @FXML Text playerHP;
    @FXML Text magazineSize;
    @FXML Text poolSize;
    @FXML Label pause, gameOver;
    @FXML Button saveBtn, loadBtn;
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies = new ArrayList<>();
    private Game game;
    private SceneSizeChangeListener sceneChange;

    MusicPlayer musicPlayer;

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
            System.out.println("Error: Player did not load correctly");
        }

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
        game = new Game(player, zombies, gameWindow, playerHP, magazineSize, poolSize);

        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
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
            } else if (e.getCode() == KeyCode.ESCAPE) {
                pause.setVisible(true);
                gameOver.setVisible(true);
                game.pauseGame();
                game.pauseDrops();
            } else if (e.getCode() == KeyCode.P) {
                pause.setVisible(true);
                gameOver.setVisible(true);
                game.pauseGame();
                game.pauseDrops();
            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
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

    public void startNewGame() {
//        game.pauseGame();
//        player.
//        enemyList.removeAll();

        //game = null;
        //game = new Game(player, enemyList, gameWindow, playerHP);
    }

    /***
     * Method which will exit the application.
     */

    @FXML
    public void saveGame() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("saveGame.fxml"));
            Stage saveGame = new Stage();
            saveGame.setScene(new Scene(root, 300, 200));
            saveBtn.setOnAction(event -> {
                SaveData data = new SaveData();
                data.name = fieldName.getText();
                data.hp = Integer.parseInt(fieldHP.getText());
                try {
                    SaveLoadManager.save(data, "1.save");
                } catch (Exception e) {
                    System.out.println("Couldn't save" + e.getMessage());
                }
            });
            saveGame.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadGame() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
            Stage loadGame = new Stage();
            loadGame.setScene(new Scene(root, 300, 200));
            loadBtn.setOnAction(event->{
                try{
                    SaveData data = (SaveData) SaveLoadManager.load("1.save");
                    fieldName.setText(data.name);
                    fieldHP.setText(String.valueOf(data.hp));
                } catch(Exception e) {
                    System.out.println("Couldn't load saved data");
                }
            });
            loadGame.show();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    public void exitGame() {
        System.exit(0);
    }
}