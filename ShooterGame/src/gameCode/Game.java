package gameCode;

import entities.Enemy;
import entities.Player;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    Pane gameWindow;
    Player player;
    List<Enemy> enemyList;

    private ArrayList<Rectangle> bonuses=new ArrayList<>();
    private ArrayList<Circle> bonuses2=new ArrayList<>();
    private int score = 0;

    public Game(Player player, List <Enemy> enemy, Pane gameWindow){
        this.gameWindow = gameWindow;
        this.player = player;
        this.enemyList = enemy;

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            double time = (now - startNanoTime) / 1000000000.0;
            onUpdate(time);
            bonus();
            }
        };

        timer.start();
    }

    public void bonus(){
        int random = (int)Math.floor(Math.random()*100);
        int x = (int)Math.floor(Math.random()*600);
        int y = (int)Math.floor(Math.random()*600);

        if(random==5){
            Rectangle rect = new Rectangle (70, 70, Color.RED);
            rect.setX(x);
            rect.setY(y);
            bonuses.add(rect);
            gameWindow.getChildren().addAll(rect);
        }

        if(random == 4){
            Circle circle = new Circle (50, Color.BLUE);
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setRadius(50);
            bonuses2.add(circle);
            gameWindow.getChildren().addAll(circle);
        }
    }


    public boolean isColliding(Node player, Node otherShape) {
        return player.getBoundsInParent().intersects(otherShape.getBoundsInParent());
    }

    private void onUpdate(double time) {
        player.update(enemyList, time);

        for (Shape shape : this.bonuses) {
            if (isColliding(player.getNode(), shape)) {
                bonuses.remove(shape);
                gameWindow.getChildren().remove(shape);
                score += 1;
                System.out.println("You got 1 point! New score equals: " + score);

                Main.SetTitle("The Game... Score: " + score);
            }
        }

        for (Shape shape : this.bonuses2) {
            if (isColliding(player.getNode(), shape)) {
                bonuses2.remove(shape);
                gameWindow.getChildren().remove(shape);
                score += 2;
                System.out.println("You got 2 points! New score equals: " + score);

                Main.SetTitle("The Game... Score: " + score);
            }
        }

        for (Enemy enemy : enemyList)
            if (player.isColliding(enemy)) {
                System.out.println("Collision!");
                System.out.println("Player hit for " + player.getHealthPoints());
            }
    }

}