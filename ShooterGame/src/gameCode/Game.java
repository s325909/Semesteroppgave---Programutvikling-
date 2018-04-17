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
    private List<Zombie> zombies;
    private Text playerHP;
    private Text magazineSize;
    private Text poolSize;

    private boolean isDead = false;
    private boolean isRunning = true;
    private boolean createDrops = true;

    InitializeGame controller;

    private List<Bullet> bullets = new ArrayList<>();
    //private ArrayList<Entity> entityList = new ArrayList<>();

    private ArrayList<Rectangle> bonuses=new ArrayList<>();
    private ArrayList<Circle> bonuses2=new ArrayList<>();
    private int score = 0;

    public Game(Player player, List <Zombie> zombies, Pane gameWindow, Text playerHP, Text magazineSize, Text poolSize){
        this.gameWindow = gameWindow;
        this.player = player;
        this.zombies = zombies;
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
                //playerDead();
            }
        };
        timer.start();
    }

    public void setController(InitializeGame controller) {
        this.controller = controller;
    }

    /***
     * Method which continuously run as long as isRunning is set to true.
     * Method will keep updating all Entities' positions and check for collision.
     * Method is affected by pauseGame() method.
     * @param time Requires a double value which here continuously gets updated via the
     *             the AnimationTimer
     */
    private void onUpdate(double time) {
        if (isRunning) {

//            entityList.add(player);
//            entityList.addAll(zombies);
//            entityList.addAll(bullets);

            player.update(time);

            bullets = player.getBulletList();

//            if (player.getShotFired()) {
//                for (int i = 0; i < bullets.size(); i++) {
//                    gameWindow.getChildren().add(bullets.get(i).getNode());
//                }
//            }


            for (Zombie zombie : zombies) {
                zombie.update(time);
                zombie.movement(player);
                if (player.isColliding(zombie)) {
                    player.setHealthPoints(player.getHealthPoints() - 10);
                    if (!player.stillAlive()) {
                        System.out.println("Player is dead");
                        // pauseGame();
                    }
                }
//                for (Zombie zombie2 : zombies) {
//                    if (zombie.isColliding(zombie2)) {
//                        zombie.setVelocityX(-0.5);
//                    }
//                }
//                for (Bullet bullet : bullets) {
//                    bullet.update(time);
//                    bullet.bulletDirection(player);
////                    if (bullet.isColliding(zombie)) {
////                        bullet.setAlive(false);
////                        zombie.setHealthPoints(zombie.getHealthPoints() - bullet.getDamage());
////                        if (!zombie.stillAlive())
////                            gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getIv());
//////                        if (!bullet.stillAlive())
//////                            gameWindow.getChildren().removeAll(bullet.getNode());
////                    }
//                }
            }

//            bullets.removeIf(Bullet::isDead);
            zombies.removeIf(Zombie::isDead);

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
            isDead = true;
            controller.setGameOverLabel(true);
            this.isRunning = false;
        }
    }

    public boolean isDead() {
        return isDead;
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