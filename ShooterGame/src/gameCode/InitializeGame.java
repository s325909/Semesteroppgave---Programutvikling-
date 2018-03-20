package gameCode;

import entities.Enemy;
import entities.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitializeGame implements Initializable{

    @FXML
    Pane pane;

    private Player player;
    private List<Enemy> enemyList = new ArrayList<Enemy>();

    public void exit(){
        System.out.println("hello");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 500, 500, 100);
        } catch (Exception e) {
            System.out.println("Feilmelding");
        }

        for (int i = 0; i < 5; i++) {
            enemyList.add(new Enemy(new Circle(25,25,50, Color.RED), (int)(Math.random() * 1280), (int)(Math.random() * 720)));
        }

        player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 500, 500, 100);


        pane.getChildren().add(player.getNode());

        pane.getChildren().add(player.getSprite().getImageView());

        for (Enemy enemy : enemyList)
            pane.getChildren().add(enemy.getNode());

        Game game = new Game(player, enemyList);

        Platform.runLater(this::getKeyPressed);

    }



    public void getKeyPressed(){

        pane.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            Stage stage = (Stage) pane.getScene().getWindow();
            if (e.getCode() == KeyCode.F12) {
                if (stage.isFullScreen())
                    stage.setFullScreen(false);
                else
                    stage.setFullScreen(true);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
        pane.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
        });
    }
}
