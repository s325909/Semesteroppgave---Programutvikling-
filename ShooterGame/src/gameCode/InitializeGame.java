package gameCode;

import entities.Enemy;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.MainController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitializeGame implements Initializable{

    @FXML Pane gameWindow;
    @FXML MenuBar topbar;

    Stage stage = new Stage();

    private Player player;
    private List<Enemy> enemyList = new ArrayList<Enemy>();

    public void exit(){
        System.out.println("hello");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 500, 500, 100);
            player.setSpriteIdle("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20);
            player.setSpriteMoving("/resources/Top_Down_Survivor/handgun/move/survivor-move_handgun_", ".png", 20);
            player.setSpriteAttack("/resources/Top_Down_Survivor/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15);
        } catch (Exception e) {
            System.out.println("Feilmelding");
        }

        for (int i = 0; i < 5; i++) {
            enemyList.add(new Enemy(new Circle(25,25,50, Color.RED), (int)(Math.random() * 1280), (int)(Math.random() * 720)));
        }

        //player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 500, 500, 100);


        gameWindow.getChildren().add(player.getNode());

        gameWindow.getChildren().add(player.getSprite().getImageView());

        for (Enemy enemy : enemyList)
            gameWindow.getChildren().add(enemy.getNode());

        Game game = new Game(player, enemyList, gameWindow);

        Platform.runLater(this::getKeyPressed);

        SceneSizeChangeListener sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);

    }

    public void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            Stage stage = (Stage) gameWindow.getScene().getWindow();
            if (e.getCode() == KeyCode.F12) {
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    topbar.setVisible(true);
                }
                else {
                    stage.setFullScreen(true);
                    topbar.setVisible(false);
                }
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
        });
    }

    public void changeFullscreen() {
        Stage stage = (Stage) gameWindow.getScene().getWindow();
        if (stage.isFullScreen())
            stage.setFullScreen(false);
        else
            stage.setFullScreen(true);
    }

    public void exitGame() {
        System.exit(0);
    }
}
