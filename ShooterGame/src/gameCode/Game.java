package gameCode;

import entities.Enemy;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    Player player;
    List<Enemy> enemyList;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ArrayList<Rectangle> bonuses=new ArrayList<>();
    public static ArrayList<Circle>bonuses2=new ArrayList<>();
    static Pane root = new Pane();

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

    public void bonus(){
        int random = (int)Math.floor(Math.random()*100);
        int x = (int)Math.floor(Math.random()*600);
        int y = (int)Math.floor(Math.random()*600);
        if(random==5){
            Rectangle rect = new Rectangle (70, 70, Color.RED);
            rect.setX(x);
            rect.setY(y);
            bonuses.add(rect);
            root.getChildren().addAll(rect);
        }

        if(random == 4){
            Circle circle = new Circle (50, Color.BLUE);
            circle.setCenterX(200);
            circle.setCenterY(200);
            circle.setRadius(50);
            bonuses2.add(circle);
            root.getChildren().addAll(circle);
        }
    }
}