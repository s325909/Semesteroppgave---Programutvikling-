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

    public static ArrayList<Rectangle> bonuses=new ArrayList<>();
    public static ArrayList<Circle>bonuses2=new ArrayList<>();

    public void bonus(){
        int random = (int)Math.floor(Math.random()*100);
        int x = (int)Math.floor(Math.random()*600);
        int y = (int)Math.floor(Math.random()*600);
        System.out.println("Bonus!" + random);
        if(random==5){
            Rectangle rect = new Rectangle (70, 70, Color.RED);
            rect.setX(x);
            rect.setY(y);
            bonuses.add(rect);
            gameWindow.getChildren().addAll(rect);
        }

        if(random == 4){
            Circle circle = new Circle (50, Color.BLUE);
            circle.setCenterX(200);
            circle.setCenterY(200);
            circle.setRadius(50);
            bonuses2.add(circle);
            gameWindow.getChildren().addAll(circle);
        }
    }

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


        gameWindow.getChildren().add(player.getNode());

        gameWindow.getChildren().add(player.getSprite().getImageView());

        for (Enemy enemy : enemyList)
            gameWindow.getChildren().add(enemy.getNode());

        Game game = new Game(player, enemyList);

        Platform.runLater(this::getKeyPressed);

        SceneSizeChangeListener sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bonus();
            }

        };
        timer.start();
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
