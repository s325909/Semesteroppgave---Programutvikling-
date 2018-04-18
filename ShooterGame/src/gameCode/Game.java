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

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Pane gameWindow;
    private Player player;
    private List<Zombie> zombies;
    private Text playerHP, playerArmor, magazineSize, poolSize, score;

    private boolean isRunning = true;
    private boolean createDrops = true;
    private boolean rPressed = false;
    private boolean isGameOver = false;

    private InitializeGame controller;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Drop> drops = new ArrayList<>();
    //private ArrayList<Entity> entityList = new ArrayList<>();

    private Drop drop;

    private ArrayList<Rectangle> bonuses=new ArrayList<>();
    private ArrayList<Circle> bonuses2=new ArrayList<>();
    private int scoreNumber = 0;

    public Game(Player player, List <Zombie> zombies, Pane gameWindow, Text playerHP, Text playerArmor, Text magazineSize, Text poolSize, Text score){

        this.gameWindow = gameWindow;
        this.player = player;
        this.zombies = zombies;
        this.playerHP = playerHP;
        this.playerArmor = playerArmor;
        this.magazineSize = magazineSize;
        this.poolSize = poolSize;
        this.score = score;

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - startNanoTime) / 1000000000.0;
                onUpdate(time);
                bonus();
                updateHUD();
            }
        };
        timer.start();
    }

    public void setrPressed(boolean rPressed) {
        this.rPressed = rPressed;
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
        if (isGameOver && rPressed){
            restartGame();
            controller.gameState.setVisible(false);
            controller.pressKey.setVisible(false);
            controller.pressKey2.setVisible(false);
        }
        if (isRunning) {
            player.update(time);
            bullets = player.getBulletList();

            for (Bullet bullet : bullets) {
                if (!bullet.isDrawn()) {
                    gameWindow.getChildren().add(bullet.getNode());
                    bullet.setDrawn();
                }
            }


            for (Zombie zombie : zombies) {
                zombie.update(time);
                zombie.movement(player);
                if (player.isColliding(zombie)) {
                    player.damage(10);
                    if (!player.stillAlive()) {
                        System.out.println("Player is dead");
                        //gameOver();
                    }
                }
//                for (Zombie zombie2 : zombies) {
//                    if (zombie.isColliding(zombie2)) {
//                        zombie.setVelocityX(-0.5);
//                    }
//                }
//                for (Bullet bullet : bullets) {
////                    bullet.update(time);
//                    bullet.bulletDirection(player);
//                    if (bullet.isColliding(zombie)) {
//                        bullet.setAlive(false);
//                        zombie.setHealthPoints(zombie.getHealthPoints() - bullet.getDamage());
//                        if (!zombie.stillAlive()) {
//                            gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getIv());
//                            zombie.setAlive(false);
//                            this.scoreNumber = 100;
//                            drop = new Drop(zombie.getPositionX(), zombie.getPositionY());
//                            drops.add(drop);
//                        }
////                        if (!bullet.stillAlive())
////                            gameWindow.getChildren().removeAll(bullet.getNode());
//                    }
//                    for (Drop drop : drops) {
//                        if(drop.isColliding(player)) {
//                            drop.randomPickup(player);
//                            gameWindow.getChildren().removeAll(drop.getNode(), drop.getIv());
//                            drop.setAlive(false);
//                        }
//                    }
//                }
            }

            bullets.removeIf(Bullet::isDead);
            zombies.removeIf(Zombie::isDead);
            drops.removeIf(Drop::isDead);

            for (Shape shape : this.bonuses) {
                if (isColliding(player.getNode(), shape)) {
                    bonuses.remove(shape);
                    gameWindow.getChildren().remove(shape);
                    scoreNumber += 10;

                    player.healthPickup(25);
                    player.armorPickup(25);
                    player.getMagazinePistol().changeBulletNumber(15);
                    player.getMagazineRifle().changeBulletNumber(30);
                    player.getMagazineShotgun().changeBulletNumber(8);
                }
            }

            for (Shape shape : this.bonuses2) {
                if (isColliding(player.getNode(), shape)) {
                    bonuses2.remove(shape);
                    gameWindow.getChildren().remove(shape);
                    scoreNumber += 20;

                    player.healthPickup(50);
                    player.armorPickup(50);
                    player.getMagazinePistol().changeBulletNumber(15);
                    player.getMagazineRifle().changeBulletNumber(30);
                    player.getMagazineShotgun().changeBulletNumber(8);
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

    /***
     * Method for changing the boolean isRunning.
     * Method affects the onUdate() function.
     */
    public void pauseGame() {
        if (isRunning) {
            this.isRunning = false;
            this.createDrops = false;
            controller.setGameIsPausedLabel(true);
        } else {
            this.isRunning = true;
            this.createDrops = true;
            controller.setGameIsPausedLabel(false);
        }
    }

    /***
     * Method for stopping the game and displaying a message at a
     * point where the game is over.
     */
    public void gameOver() {
        if (isRunning) {
            this.isRunning = false;
            this.createDrops = false;
            controller.setGameOverLabel(true);
            this.isGameOver = true;
        }
    }

    public void restartGame() {

        for (Zombie zombie : zombies){
            gameWindow.getChildren().remove(zombie.getSprite().getImageView());
            gameWindow.getChildren().remove(zombie.getNode());
        }
       zombies.clear();

        player.getNode().setTranslateX(0);
        player.getNode().setTranslateY(0);
        player.setPositionX(0);
        player.setPositionY(0);

        player.setHealthPoints(100);
        //this.score = 0;

        try {
            for (int i = 0; i < 10; i++) {
                zombies.add(new Zombie("/resources/Art/Zombie/skeleton-idle_", ".png", 17, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100));
                zombies.get(i).setSpriteIdle("/resources/Art/Zombie/skeleton-idle_", ".png", 17);
                zombies.get(i).setSpriteMoving("/resources/Art/Zombie/skeleton-move_", ".png", 17);
                zombies.get(i).setSpriteMelee("/resources/Art/Zombie/skeleton-attack_", ".png", 9);
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        for (Zombie zombie : zombies){
            gameWindow.getChildren().addAll(zombie.getNode());
            gameWindow.getChildren().addAll(zombie.getSprite().getImageView());
        }

        this.isRunning = true;
        this.createDrops = true;

    }

    /***
     * Method for updating the datafields playerHP, magazineSize, poolSize of type Text.
     * These will in turn update the Text values on the HUD.
     */
    public void updateHUD() {
        String hpLevel = String.valueOf(player.getHealthPoints());
        String armorLevel = String.valueOf(player.getArmor());
        String magazineLevel = String.valueOf(player.getMagazineCount());
        String poolLevel = String.format("%02d", player.getAmmoPool());
        String score = String.format("%05d", this.scoreNumber);

        this.playerHP.setText(hpLevel);
        this.playerArmor.setText(armorLevel);
        this.magazineSize.setText(magazineLevel);
        this.poolSize.setText(poolLevel);
        this.score.setText(score);
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