package GameCode;

import Entities.Enemy;
import Entities.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    Player player;
    List<Enemy> enemyList = new ArrayList<Enemy>();

    private void onUpdate(double time) {
        player.update(enemyList, time);

        for (Enemy enemy : enemyList)
            if (player.isColliding(enemy)) {
                System.out.println("Collision!");
                System.out.println("Current health is: " + player.getHealthpoints());
            }
    }

    private void initializeEntity() {
        try {
            player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 0.000005, 500, 500, 100);
        } catch (Exception e) {

        }


        for (int i = 0; i < 5; i++) {
            enemyList.add(new Enemy(new Circle(25,25,50, Color.RED), (int)(Math.random() * 1280), (int)(Math.random() * 720)));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        initializeEntity();

        primaryStage.setTitle("Topdown Shooter");

        Group root = new Group();

        Pane topMenu = FXMLLoader.load(getClass().getResource("GameUI.fxml"));

        Pane gameWindow = new Pane();

        gameWindow.getChildren().add(player.getNode());

        gameWindow.getChildren().add(player.getSprite().getImageView());

        for (Enemy enemy : enemyList)
            gameWindow.getChildren().add(enemy.getNode());

        root.getChildren().add(gameWindow);
        root.getChildren().add(topMenu);

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);

        PlayerInput playerInput = new PlayerInput(primaryStage, player);

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = (now - startNanoTime);
                onUpdate(t);
            }
        };

        timer.start();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
