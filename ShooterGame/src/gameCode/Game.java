package gameCode;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import main.Main;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Pane gameWindow;
    private Player player;
    private List<Enemy> enemyList;
    private Text playerHP;
    private Text magazineSize;
    private Text poolSize;

    private boolean isRunning = true;
    private boolean createDrops = true;

    private ArrayList<Entity> playerList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();
    private ArrayList<Entity> bulletList = new ArrayList<>();

    private ArrayList<Rectangle> bonuses=new ArrayList<>();
    private ArrayList<Circle> bonuses2=new ArrayList<>();
    private int score = 0;

    public Game(Player player, List <Enemy> enemy, Pane gameWindow, Text playerHP, Text magazineSize, Text poolSize){
        this.gameWindow = gameWindow;
        this.player = player;
        this.enemyList = enemy;
        this.playerHP = playerHP;
        this.magazineSize = magazineSize;
        this.poolSize = poolSize;

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - startNanoTime) / 1000000000.0;
                onUpdate(time);
                bonus();
                updateHP();
                updateAmmo();
                playerDead();
            }
        };
        timer.start();
    }

    /***
     * Method which continuously run as long as isRunning is set to true.
     * Method will keep updating all Entities' positions and check for collision.
     * Method is affected by pauseGame() method.
     * @param time Takes in a double which is used to determine how quickly updates should occur.
     */
    private void onUpdate(double time) {
        if (isRunning) {

            playerList.addAll(enemyList);

            player.update(playerList, time);

            entityList.add(player);
            entityList.addAll(enemyList);

            for (Enemy enemy : enemyList) {
                enemy.update(entityList, time);
                enemy.movement(player);
            }

            for (Enemy enemy : enemyList) {
                if (player.isColliding(enemy)) {
                    player.setHealthPoints(player.getHealthPoints() - 10);
                    enemy.setHealthPoints(enemy.getHealthPoints() - 25);
                    enemy.checkAlive();
                    System.out.println(enemy.getHealthPoints());
                    System.out.println(enemy.isDead());
//                    System.out.println("Collision!");
//                    System.out.println("Health remaining: " + player.getHealthPoints());
                }
            }

            entityList.removeIf(Entity::isDead);
            //bulletList.removeIf(Entity::isDead);

            //enemyList.removeIf(Entity::update);

            for (Shape shape : this.bonuses) {
                if (isColliding(player.getNode(), shape)) {
                    bonuses.remove(shape);
                    gameWindow.getChildren().remove(shape);
                    score += 1;

                    player.setHealthPoints(player.getHealthPoints() + 25);
                    double random = Math.random();
                    if (random < 0.5)
                        player.playerAnimation("pistol");
                    else if (random > 0.5)
                        player.playerAnimation("shotgun");

                    System.out.println("Current healthpoints: " + player.getHealthPoints());
                    System.out.println("You got 1 point! New score equals: " + score);
                    Main.setTitle("The Game... Score: " + score);
                }
            }

            for (Shape shape : this.bonuses2) {
                if (isColliding(player.getNode(), shape)) {
                    bonuses2.remove(shape);
                    gameWindow.getChildren().remove(shape);
                    score += 2;

                    player.setHealthPoints(player.getHealthPoints() + 50);
                    player.playerAnimation("rifle");

                    System.out.println("Current healthpoints: " + player.getHealthPoints());
                    System.out.println("You got 2 points! New score equals: " + score);
                    Main.setTitle("The Game... Score: " + score);
                }
            }
        }
    }

    /***
     * Method for checking for collision between two Nodes.
     * @param player Requires a Node to represent the Player.
     * @param otherShape Requires a Node to represent the object of collision.
     * @return Returns a boolean based on whether there is any collision or not.
     */
    public boolean isColliding(Node player, Node otherShape) {
        return player.getBoundsInParent().intersects(otherShape.getBoundsInParent());
    }


    public void playerDead(){
        if (player.getHealthPoints() <= 0){
            this.isRunning = false;
        }
    }


    /***
     * Method for changing the boolean isRunning.
     * Method affects the onUdate() function.
     */
    public void pauseGame() {
        if (isRunning) {
            this.isRunning = false;
        } else {
            this.isRunning = true;
        }
    }

    /***
     * Method for changing the boolean createDrops.
     * Method affects the bonus() method.
     */
    public void pauseDrops() {
        if (createDrops) {
            this.createDrops = false;
        } else {
            this.createDrops = true;
        }
    }

    /***
     * Method for updating the datafield playerHP of type Text.
     * This value is displayed on the HUD.
     */
    public void updateHP() {
        String hp_level = String.valueOf(player.getHealthPoints());
        this.playerHP.setText(hp_level);
    }

    /***
     * Method for updating the datafields magazineSize and poolSize of type Text.
     * These values are displayed on the HUD.
     */
    public void updateAmmo() {
        String magazineLevel = String.valueOf(player.getMagazineCount());
        String poolLevel = String.valueOf(player.getAmmoPool());
        this.magazineSize.setText(magazineLevel);
        this.poolSize.setText(poolLevel);
    }

    /***
     * Method for creating PowerUps which spawn randomly.
     * Method is affected by pauseDrops() method.
     */
    public void bonus(){

        if (createDrops) {
            if (bonuses.size() < 5 || bonuses2.size() < 5) {

                int random = (int) Math.floor(Math.random() * 100);
                int x = (int) Math.floor(Math.random() * gameWindow.getWidth());
                int y = (int) Math.floor(Math.random() * gameWindow.getHeight());


                if (random == 5 && bonuses.size() < 5) {
                    Rectangle rect = new Rectangle(40, 30, Color.PINK);
                    rect.setX(x);
                    rect.setY(y);
                    bonuses.add(rect);
                    gameWindow.getChildren().addAll(rect);
                }

                if (random == 4 && bonuses2.size() < 5) {
                    Circle circle = new Circle(50, Color.GRAY);
                    circle.setCenterX(x);
                    circle.setCenterY(y);
                    circle.setRadius(30);
                    bonuses2.add(circle);
                    gameWindow.getChildren().addAll(circle);
                }
            }
        }
    }
}