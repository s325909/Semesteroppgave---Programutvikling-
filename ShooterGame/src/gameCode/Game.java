package gameCode;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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
    private int timer;

    private InitializeGame initGame;
    private boolean holdingButtonR;
    private boolean isRunning;

    private StoreData storeData;

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
        this.storeData = new StoreData();
        this.isRunning = true;
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
    }

    /***
     * Method which continuously run as long as isRunning is set to true.
     * Method will keep updating all Entities' positions and check for collision.
     * Method is affected by pauseGame() method.
     * @param time Requires a double value which here continuously gets updated via the
     *             the AnimationTimer
     */
    private void onUpdate(double time) {
        timer = (int)time;
        if (initGame.isLabelActive() && holdingButtonR){
            restartGame();
            initGame.showGameLabel(false, false);
        }
        if (isRunning) {

            bullets = player.getBulletList();

            // Create Drop entities with random position
            if (dropsExtra.size() < 10) {
                int random = (int) Math.floor(Math.random() * 100);
                if (random < 4) {
                    int x = (int) Math.floor(Math.random() * gameWindow.getWidth());
                    int y = (int) Math.floor(Math.random() * gameWindow.getHeight());

                    dropsExtra.add(new Drop("/resources/Art/Icon/Coin/coin_rotate_", ".png", 1, x, y));
                }
            }

            // Check collision between zombies and player
            for (Zombie zombie : zombies) {
                zombie.movement(player);
                if (player.isColliding(zombie)) {
                    player.receivedDamage(10);
                    if (!player.stillAlive()) {
                        System.out.println("Player is dead");
                        //gameOver();
                    }
                }
            }

            // Draw bullets to the pane, adjust direction, and check collision with zombies
            for (Bullet bullet : bullets) {
                if (!bullet.isDrawn()) {
                    gameWindow.getChildren().addAll(bullet.getSprite().getImageView(), bullet.getNode());
                    bullet.setDrawn();
                }
                bullet.bulletDirection();
                bullet.bulletCollision(zombies);
            }

            // Check collision between drops and player
            for (Drop drop : drops) {
                if(drop.isColliding(player)) {
                    drop.setAlive(false);
                }
            }

            // Draw dropsExtra to the pane, and check for collision with player
            for (Drop drop : dropsExtra) {
                if (!drop.isDrawn()) {
                    gameWindow.getChildren().addAll(drop.getSprite().getImageView(), drop.getNode());
                    drop.setDrawn();
                }
                if (drop.isColliding(player)) {
                    drop.setAlive(false);
                    scoreNumber += 25;
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
                    Drop drop = new Drop("/resources/Art/Icon/circle_icon.png", zombie.getPositionX(), zombie.getPositionY());
                    drops.add(drop);

                    if (!drop.isDrawn()) {
                        gameWindow.getChildren().add(drop.getNode());
                        drop.setDrawn();
                    }
                }
                zombie.update(time);
            }

            // Check if Bullet is dead, and remove if so
            for(Bullet bullet : bullets) {
                if(!bullet.isAlive())
                    gameWindow.getChildren().removeAll(bullet.getIv(), bullet.getNode());
                bullet.update(time);
            }

            // Check if Drop of drops is dead
            for(Drop drop : drops) {
                if(!drop.isAlive())
                  gameWindow.getChildren().removeAll(drop.getNode(), drop.getIv());
            }

            // Check if Drop of dropsExtra is dead
            for(Drop drop : dropsExtra) {
                if(!drop.isAlive()) {
                    gameWindow.getChildren().removeAll(drop.getNode(), drop.getIv());
                }
            }

            // Remove every dead Entity object from the ArrayLists
            bullets.removeIf(Bullet::isDead);
            zombies.removeIf(Zombie::isDead);
            drops.removeIf(Drop::isDead);
            dropsExtra.removeIf(Drop::isDead);
        }
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
        String timer = String.format("%03d", this.timer);

        this.hudHP.setText(hpLevel);
        this.hudArmor.setText(armorLevel);
        this.hudWeapon.setText(weapon);
        this.hudMag.setText(magazineLevel);
        this.hudPool.setText(poolLevel);
        this.hudScore.setText(score);
        this.hudTimer.setText("Survival time: " + timer);
    }

    /***
     * Method for changing the boolean isRunning.
     * Method affects the onUdate() function.
     */
    public void pauseGame() {
        if (isRunning) {
            setRunning(false);
            initGame.showGameLabel(false, true);
        } else {
            setRunning(true);
            initGame.showGameLabel(false, false);
        }
    }

    /***
     * Method for stopping the game and displaying a message at a
     * point where the game is over.
     */
    public void gameOver() {
        setRunning(false);
        initGame.showGameLabel(true, true);
    }

    /***
     * Method for restarting the game when GameOver or Paused
     * The method will first try to remove all zombies and bonuses on the stage.
     * Then set the player's position equals to the player's original start position,
     * as well as resetting the player's hp, armor and score to it's original value.
     * The method will then try to respawn all the zombies.
     * Then set both "isRunning" and "createDrops" equals "true"
     * as well as setting both "isGameOver" and "gameIsPaused" equals "false",
     * which allows this method to run again after restarting the game
     */
    protected void restartGame() {
        removeZombies();
        removeDrops();
        removeDropsExtra();
        player.resetPlayer();
        setScoreNumber(0);
        createZombies(initGame.getNbrZombies());
        setRunning(true);
    }

    private void saveTheGame(String filename) {
        StoreData.GameConfiguration gameCfg = new StoreData.GameConfiguration();
        if (storeData.createSaveFile(filename, gameCfg)) {
            System.out.println("Save game");
            System.out.println("GameScore: " + this.getScoreNumber());
            System.out.println("Player HP: " + player.getHealthPoints());
            System.out.println("Player X: " + player.getPositionX());
            System.out.println("Player Y: " + player.getPositionY());
            System.out.println("NbrZombies: " + zombies.size());

            saveGame(gameCfg);
        } else {
            System.out.println("Could not save the game");
        }
    }

    public void saveGame(StoreData.GameConfiguration gameCfg) {
        gameCfg.gameScore = getScoreNumber();
        gameCfg.player = savePlayer();
        //saveZombies(gameCfg.zombies);
    }

    public StoreData.Configuration savePlayer() {

        StoreData.Configuration playerCfg = new StoreData.Configuration();
        playerCfg.health = player.getHealthPoints();
        playerCfg.armor = player.getArmor();
        playerCfg.posX = player.getPositionX();
        playerCfg.posY = player.getPositionY();
        playerCfg.velX = player.getVelocityX();
        playerCfg.velY = player.getVelocityY();
        //playerCfg.movementSpeed = player.getMovementSpeed();
        playerCfg.direction = player.getDirection();
        playerCfg.equipped = player.getEquippedWeapon();
        playerCfg.magPistol = player.getMagazinePistol().getNumberBullets();
        playerCfg.poolPistol = player.getMagazinePistol().getCurrentPool();
        playerCfg.magPistol = player.getMagazinePistol().getNumberBullets();
        playerCfg.poolPistol = player.getMagazinePistol().getCurrentPool();
        playerCfg.magPistol = player.getMagazinePistol().getNumberBullets();
        playerCfg.poolPistol = player.getMagazinePistol().getCurrentPool();

        return playerCfg;
    }

    public List <StoreData.Configuration> saveZombies() {

        List<StoreData.Configuration> zombieList = new ArrayList<StoreData.Configuration>();
        for (int i = 0; i < zombies.size(); i++) {
            zombieList.get(i).health = zombies.get(i).getHealthPoints();
            zombieList.get(i).posX = zombies.get(i).getPositionX();
            zombieList.get(i).posY = zombies.get(i).getPositionY();
            zombieList.get(i).velX = zombies.get(i).getVelocityX();
            zombieList.get(i).velY = zombies.get(i).getVelocityY();
            //zombieList.get(i).movementSpeed = zombies.get(i).getMovementSpeed();
            zombieList.get(i).direction = zombies.get(i).getDirection();
        }
        return zombieList;
    }

    /**
     *  Method for loading a saved game file.
     * @param filename Requires the String which represents the name of the file.
     */
    private void loadSavegame(String filename) {
        StoreData.GameConfiguration gameCfg = new StoreData.GameConfiguration();
        if (storeData.readSaveFile(filename, gameCfg)) {
            System.out.println("Load game");
            System.out.println("GameScore: " + gameCfg.gameScore);
            System.out.println("Player HP: " + gameCfg.player.health);
            System.out.println("Player X: " + gameCfg.player.posX);
            System.out.println("Player Y: " + gameCfg.player.posY);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());

            loadGame(gameCfg);
        } else {
            System.out.println("Could not load quicksave!");
        }
    }

    /**
     *
     * @param gameCfg Requires
     */
    private void loadGame(StoreData.GameConfiguration gameCfg) {
        setScoreNumber(gameCfg.gameScore);
        loadPlayer(gameCfg.player);
        loadZombies(gameCfg.zombies);
        setZombies(this.zombies); // Nødvendig? er vel alt satt i laodZombies?
        loadBullets(gameCfg.bullets);
        loadDrops(gameCfg.drops);
    }

    /**
     *
     * @param playerCfg Requires
     */
    private void loadPlayer(StoreData.Configuration playerCfg) {
        this.player.setHealthPoints(playerCfg.health);
        this.player.setArmor(playerCfg.armor);
        this.player.setPosition(playerCfg.posX, playerCfg.posY);
        this.player.setTranslateNode(playerCfg.posX, playerCfg.posY);
        this.player.setVelocity(playerCfg.velX, playerCfg.velY);
        this.player.setMovementSpeed(playerCfg.movementSpeed);
        this.player.setDirection(playerCfg.direction);
        this.player.setEquippedWeapon(playerCfg.equipped);
        this.player.getMagazinePistol().setNumberBullets(playerCfg.magPistol);
        this.player.getMagazinePistol().setCurrentPool(playerCfg.poolPistol);
        this.player.getMagazineRifle().setNumberBullets(playerCfg.magRifle);
        this.player.getMagazineRifle().setCurrentPool(playerCfg.poolRifle);
        this.player.getMagazineShotgun().setNumberBullets(playerCfg.magShotgun);
        this.player.getMagazineShotgun().setCurrentPool(playerCfg.poolShotgun);
    }

    /**
     *
     * @param zombieList Requires
     */
    private void loadZombies(List<StoreData.Configuration> zombieList) {
        removeZombies();
        initGame.loadZombiesAssets(zombieList.size());

        this.zombies = new ArrayList<>();
        for (int i = 0; i < zombieList.size(); i++) {
            Zombie zombie = new Zombie(initGame.getZombieAnimation()[i], initGame.getZombieAudioClips(), zombieList.get(i).posX, zombieList.get(i).posY, zombieList.get(i).health);
            zombie.setVelocity(zombieList.get(i).velX, zombieList.get(i).velY);
            //zombie.setMovementSpeed(zombieList.get(i).movementSpeed);
            zombie.setDirection(zombieList.get(i).direction);
            this.zombies.add(zombie);
        }

        for (Zombie zombie : this.zombies) {
            gameWindow.getChildren().addAll(zombie.getSprite().getImageView(), zombie.getNode());
        }
    }

    private void loadBullets(List<StoreData.Configuration> bulletList) {
        removeBullets();

        this.bullets = new ArrayList<>();
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = new Bullet("animation", bulletList.get(i).posX, bulletList.get(i).posY, bulletList.get(i).movementSpeed, bulletList.get(i).damage, bulletList.get(i).direction);
            bullet.setVelocity(bulletList.get(i).velX, bulletList.get(i).velY);
            this.bullets.add(bullet);
        }

        for (Bullet bullet : this.bullets) {
            gameWindow.getChildren().addAll(bullet.getSprite().getImageView(), bullet.getNode());
        }
    }

    private void loadDrops(List<StoreData.Configuration> dropsList) {
        removeDrops();

        // Nødvendig?
        //this.drops = new ArrayList<>();
        for (int i = 0; i < dropsList.size(); i++) {
            Drop drop = new Drop("image", dropsList.get(i).posX, dropsList.get(i).posY);
            this.drops.add(drop);
        }

        for (Drop drop : this.drops) {
            gameWindow.getChildren().addAll(drop.getSprite().getImageView(), drop.getNode());
        }
    }

    /**
     * Method for removing all Zombies in the Game.
     */
    private void removeZombies() {
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
    private void removeBullets() {
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
    private void removeDrops() {
        for (Drop drop : this.drops) {
            gameWindow.getChildren().removeAll(drop.getSprite().getImageView(), drop.getNode());
        }

        for (Drop drop : this.drops) {
            drop.setAlive(false);
        }
        this.drops.removeIf(Drop::isDead);
    }

    // Kun nødvendig inntil videre
    private void removeDropsExtra() {
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
    private void createZombies(int nbrZombies) {
        try {
            this.zombies = new ArrayList<>();
            for (int i = 0; i < nbrZombies; i++) {
                Zombie zombie = new Zombie(initGame.getZombieAnimation()[i], initGame.getZombieAudioClips(), (int) (Math.random() * 1280), (int) (Math.random() * 720), 100);
                this.zombies.add(zombie);
            }

            for (Zombie zombie : zombies) {
                gameWindow.getChildren().addAll(zombie.getNode());
                gameWindow.getChildren().addAll(zombie.getSprite().getImageView());
            }
        } catch (Exception e) {
            System.out.println("Unable to reset zombies");
        }
    }

    public int getScoreNumber() {
        return scoreNumber;
    }

    public void setScoreNumber(int scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setHoldingButtonR(boolean holdingButtonR) {
        this.holdingButtonR = holdingButtonR;
    }

    public void setInitGame(InitializeGame initGame) {
        this.initGame = initGame;
    }

    public void setZombies(List<Zombie> zombieList) {
        this.zombies = zombieList;
    }
}