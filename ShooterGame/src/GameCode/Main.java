package GameCode;

import Entities.Enemy;
import Entities.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
            if (player.isColliding(enemy))
                System.out.println("Collision!");
    }

    private void initializeEntity() {
        //player = new Player(new Rectangle(100,100, Color.BLUE), 500, 500);
        try {
            //player = new Player(getClass().getResource("/resources/test_sprites.png").toURI().toString(), 500, 500);
            player = new Player("/resources/Top_Down_Survivor/rifle/move/survivor-move_rifle_", ".png", 20, 500, 500);
        } catch (Exception e) {

        }


        for (int i = 0; i < 5; i++) {
            //enemyList.add(new Enemy(new Circle(25,25,50,Color.RED), (int)(Math.random() * 1280), (int)(Math.random() * 720)));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        initializeEntity();

        primaryStage.setTitle("Topdown Shooter");

        Group root = new Group();

        Pane topMenu = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Pane gameWindow = new Pane();

        gameWindow.getChildren().add(player.getNode());
        //ImageView iv = new ImageView();
        //iv.setImage(player.getSprite().getImage());
       //iv.relocate(640, 360);

        gameWindow.getChildren().add(player.getSprite().getImageView());
        for (Enemy enemy : enemyList)
            gameWindow.getChildren().add(enemy.getNode());


        /** Merk: Her ligger menybaren over spillvinduet, dvs at spillet blir renderet
         * også litt under selve menybaren. Dette med tanke på level layout
         */

        root.getChildren().add(gameWindow);
        root.getChildren().add(topMenu);

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);

        /** Spillers input for kontroll
         *
         */
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                player.goLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.goRight();
            } else if (e.getCode() == KeyCode.UP) {
                player.goUp();
            } else if (e.getCode() == KeyCode.DOWN) {
                player.goDown();
            } else if (e.getCode() == KeyCode.F12) {
                if (primaryStage.isFullScreen())
                    primaryStage.setFullScreen(false);
                else
                    primaryStage.setFullScreen(true);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

        primaryStage.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                player.stopX();
            } else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                player.stopY();
            }
        });

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
