package gameCode;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player player;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    private List<Drop> drops = new ArrayList<>();
    private List<Drop> dropsExtra = new ArrayList<>();
    private Pane gameWindow;
    private Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer;

    private int scoreNumber;
    private int secondsCounter;

    private AnimationTimer timer;
    private GameInitializer gameInitializer;
    private boolean running;
    private boolean gameOver;
    private DataHandler dataHandler;

    //private SaveGame saveGame;

    public Game(Player player, List <Zombie> zombies, Pane gameWindow, Label hudHP, Label hudArmor, Label hudWeapon, Label hudMag,Label hudPool, Label hudScore, Label hudTimer){

        this.player = player;
        this.zombies = zombies;
        this.gameWindow = gameWindow;
        this.hudHP = hudHP;
        this.hudArmor = hudArmor;
        this.hudWeapon = hudWeapon;
        this.hudMag = hudMag;
        this.hudPool = hudPool;
        this.hudScore = hudScore;
        this.hudTimer = hudTimer;
        this.dataHandler = new DataHandler();
        this.running = true;
        this.scoreNumber = 0;

        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - startNanoTime) / 1000000000.0;
                onUpdate(time);
                updateHUD();
            }
        };
        timer.start();
        this.timer = timer;
    }

    /***
     * Method which continuously run as long as running is set to true.
     * Method will keep updating all Entities' positions and check for collision.
     * Method is affected by pauseGame() method.
     * @param time Requires a double value which here continuously gets updated via the
     *             the AnimationTimer
     */
    private void onUpdate(double time) {
        secondsCounter = (int)time;
        bullets = player.getBulletList();

        // Create Drop entities with random position
        if (dropsExtra.size() < 5) {
            int random = (int) Math.floor(Math.random() * 100);
            if (random < 4) {
                int x = (int) Math.floor(Math.random() * gameWindow.getWidth());
                int y = (int) Math.floor(Math.random() * gameWindow.getHeight());

                dropsExtra.add(new Drop(gameInitializer.getDropImages()[5], x, y));
            }
        }

        // Check collision between zombies and player
        for (Zombie zombie : zombies) {
            zombie.movement(player);
            if (player.isColliding(zombie)) {
                player.receivedDamage(10);
                if (!player.isAlive()) {
                    //gameOver();
                }
            }
        }

        // Draw bullets to the pane, adjust direction, and check collision with zombies
        for(Bullet bullet : bullets) {
            if(!bullet.isDrawn()) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().add(bullet.getNode());
                gameWindow.getChildren().add(bullet.getSprite().getImageView());
                bullet.setDrawn();
            }
            bullet.bulletDirection();
            bullet.bulletCollision(zombies);
        }

        // Draw drops to the pane, and check for collision with player
        for (Drop drop : drops) {
            if (!drop.isDrawn()) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().add(drop.getNode());
                gameWindow.getChildren().add(drop.getSprite().getImageView());
                drop.setDrawn();
            }
            if(drop.isColliding(player)) {
                drop.setAlive(false);
                drop.dropCollision(player, this);
            }
        }

        // Draw dropsExtra to the pane, and check for collision with player
        for (Drop drop : dropsExtra) {
            if (!drop.isDrawn()) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().add(drop.getNode());
                gameWindow.getChildren().add(drop.getSprite().getImageView());
                drop.setDrawn();
            }
            if (drop.isColliding(player)) {
                drop.setAlive(false);
                drop.dropCollision(player, this);
            }
        }

        // Update animation, position and velocity of player
        player.updateAnimation();
        player.update(time);

        // Update zombie position and velocity.
        // Check if zombie is alive. If dead, create and draw a Drop entity
        for(Zombie zombie : zombies) {
            if(!zombie.isAlive()) {
                this.scoreNumber += 100;
                gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getIv());
                Drop drop = new Drop(gameInitializer.getDropImages()[3], zombie.getPositionX(), zombie.getPositionY());
                drops.add(drop);
            }
            zombie.updateAnimation();
            zombie.update(time);
        }

        // Check if Bullet is dead, and remove if so
        for(Bullet bullet : bullets) {
            if(!bullet.isAlive())
                gameWindow.getChildren().removeAll(bullet.getNode(), bullet.getSprite().getImageView());
            bullet.update(time);
        }

        // Check if Drop of drops is dead
        for(Drop drop : drops) {
            if(!drop.isAlive())
              gameWindow.getChildren().removeAll(drop.getNode(), drop.getSprite().getImageView());
        }

        // Check if Drop of dropsExtra is dead
        for(Drop drop : dropsExtra) {
            if(!drop.isAlive()) {
                gameWindow.getChildren().removeAll(drop.getNode(), drop.getSprite().getImageView());
            }
        }

        // Remove every dead Entity object from the ArrayLists
        bullets.removeIf(Bullet::isDead);
        zombies.removeIf(Zombie::isDead);
        drops.removeIf(Drop::isDead);
        dropsExtra.removeIf(Drop::isDead);
    }

    /**
     * Method for updating the datafields playerHP, magazineSize, poolSize of type Text.
     * These will in turn update the Text values on the HUD.
     */
    private void updateHUD() {
        String hpLevel = String.valueOf(player.getHealthPoints());
        String armorLevel = String.valueOf(player.getArmor());
        String weapon = player.getWeaponTypeToString().toUpperCase();
        String magazineLevel = String.valueOf(player.getMagazineCount());
        String poolLevel = String.format("%02d", player.getAmmoPool());
        String score = String.format("%05d", this.getScoreNumber());
        String timer = String.valueOf(this.secondsCounter);

        this.hudHP.setText(hpLevel);
        this.hudArmor.setText(armorLevel);
        this.hudWeapon.setText(weapon);
        this.hudMag.setText(magazineLevel);
        this.hudPool.setText(poolLevel);
        this.hudScore.setText(score);
        this.hudTimer.setText(timer);
    }

    /***
     * Method for changing the boolean running.
     * Method affects the onUdate() function.
     */
    public void pauseGame() {
        if(!isGameOver()) {
            if (isRunning()) {
                stopTimer();
                gameInitializer.showGameLabel();
            } else {
                startTimer();
                gameInitializer.showGameLabel();
            }
        }
    }

    /***
     * Method for stopping the game and displaying a message at a
     * point where the game is over.
     */
    public void gameOver() {
        stopTimer();
        setGameOver(true);
        gameInitializer.showGameLabel();
    }

    /***
     * Method for restarting the game when GameOver or Paused
     * The method will first try to remove all zombies and bonuses on the stage.
     * Then set the player's position equals to the player's original start position,
     * as well as resetting the player's hp, armor and score to it's original value.
     * The method will then try to respawn all the zombies.
     * Then set both "running" and "createDrops" equals "true"
     * as well as setting both "gameOver" and "gameIsPaused" equals "false",
     * which allows this method to run again after restarting the game
     */
    protected void restartGame() {
        removeZombies();
        removeBullets();
        removeDrops();
        removeDropsExtra();
        player.resetPlayer();
        setScoreNumber(0);
        createZombies(gameInitializer.getNbrZombies());
        gameInitializer.showGameLabel();
        gameInitializer.showMenu();
        setGameOver(false);
        startTimer();
        setRunning(true);
    }

    public void saveGame() {
        //saveGame = new SaveGame(this.player, this, this.zombies, this.bullets, this.drops, this.dropsExtra);
        //saveGame.save("testsave");
        SaveGame saveGame = new SaveGame();
        DataHandler.GameConfiguration gameCfg = getGameConfiguration();
        saveGame.save("testsave", gameCfg);
    }

    private DataHandler.GameConfiguration getGameConfiguration() {
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        gameCfg.gameScore = this.getScoreNumber();
        gameCfg.player = this.player.getConfiguration();

        List<DataHandler.Configuration> zombieCfg = new ArrayList<>();
        for (Zombie zombie : this.zombies)
            zombieCfg.add(zombie.getConfiguration());
        gameCfg.zombies = zombieCfg;

        List<DataHandler.Configuration> bulletCfg = new ArrayList<>();
        for (Bullet bullet : this.player.getBulletList())
            bulletCfg.add(bullet.getConfiguration());
        gameCfg.bullets = bulletCfg;

        List<DataHandler.Configuration> dropCfg = new ArrayList<>();
        for (Drop drop : this.drops)
            dropCfg.add(drop.getConfiguration());
        gameCfg.drops = dropCfg;

        List<DataHandler.Configuration> dropExtraCfg = new ArrayList<>();
        for (Drop dropExtra : this.dropsExtra)
            dropExtraCfg.add(dropExtra.getConfiguration());
        gameCfg.dropsExtra = dropExtraCfg;

        return gameCfg;
    }

    public void loadGame() {
        SaveGame saveGame = new SaveGame(this.player, this, this.zombies, this.bullets, this.drops, this.dropsExtra);
        saveGame.load("testsave");
    }


    /**
     * Method for removing all Zombies in the Game.
     */
    public void removeZombies() {
        for (Zombie zombie : this.zombies) {
            gameWindow.getChildren().removeAll(zombie.getSprite().getImageView(), zombie.getNode());
        }

        for (Zombie zombie : this.zombies) {
            zombie.setAlive(false);
        }
        this.zombies.removeIf(Zombie::isDead);
    }

    /**
     *
     */
    public void removeBullets() {
        for (Bullet bullet : this.bullets) {
            gameWindow.getChildren().removeAll(bullet.getSprite().getImageView(), bullet.getNode());
        }

        for (Bullet bullet : this.bullets) {
            bullet.setAlive(false);
        }
        this.bullets.removeIf(Bullet::isDead);
    }

    /**
     *
     */
    public void removeDrops() {
        for (Drop drop : this.drops) {
            gameWindow.getChildren().removeAll(drop.getSprite().getImageView(), drop.getNode());
        }

        for (Drop drop : this.drops) {
            drop.setAlive(false);
        }
        this.drops.removeIf(Drop::isDead);
    }

    // Kun nødvendig inntil videre
    public void removeDropsExtra() {
        for (Drop drop : this.dropsExtra) {
            gameWindow.getChildren().removeAll(drop.getSprite().getImageView(), drop.getNode());
        }

        for (Drop drop : this.dropsExtra) {
            drop.setAlive(false);
        }
        this.dropsExtra.removeIf(Drop::isDead);
    }

    /**
     * Method for creating Zombies at random location.
     * @param nbrZombies Requires the number of Zombies to create.
     */
    public void createZombies(int nbrZombies) {
        try {
            this.zombies = new ArrayList<>();
            for (int i = 0; i < nbrZombies; i++) {
                Zombie zombie = new Zombie(gameInitializer.getZombieAnimation()[i], gameInitializer.getZombieAudioClips(), (int) (Math.random() * 1280), (int) (Math.random() * 720), 100);
                this.zombies.add(zombie);
            }

            for (Zombie zombie : zombies) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().addAll(zombie.getNode());
                gameWindow.getChildren().addAll(zombie.getSprite().getImageView());
            }
        } catch (Exception e) {
            System.out.println("Unable to reset zombies");
        }
    }

    public void startTimer() {
        this.timer.start();
        setRunning(true);
    }

    public void stopTimer() {
        this.timer.stop();
        setRunning(false);
    }

    public Pane getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(Pane gameWindow) {
        this.gameWindow = gameWindow;
    }

    public GameInitializer getGameInitializer() {
        return gameInitializer;
    }

    public int getScoreNumber() {
        return scoreNumber;
    }

    public void setScoreNumber(int scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    private boolean isRunning() {
        return running;
    }

    private void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameInitializer(GameInitializer gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    public void setZombies(List<Zombie> zombieList) {
        this.zombies = zombieList;
    }
}