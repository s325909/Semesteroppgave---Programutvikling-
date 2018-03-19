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

public class Game {

    Player player;
    List<Enemy> enemyList;

    public Game(Player player, List <Enemy> enemy){
        this.player = player;
        this.enemyList = enemy;

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = (now - startNanoTime) / 1000000000.0;
                onUpdate(t);
            }
        };

        timer.start();
    }

    private void onUpdate(double time) {
        player.update(enemyList, time);

        for (Enemy enemy : enemyList)
            if (player.isColliding(enemy)) {
                System.out.println("Collision!");
                System.out.println("Player hit for " + player.getHealthPoints());
            }
    }
}
