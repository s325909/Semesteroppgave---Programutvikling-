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
    private List<Rock> rocks;
    private Pane gameWindow;
    private Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer;

    private int scoreNumber;
    private int secondsCounter;
    private int roundNumber = 1;

    private AnimationTimer timer;
    private GameInitializer gameInitializer;
    private boolean running;
    private boolean newRound;
    private boolean gameOver;
    private DataHandler dataHandler;
    private boolean chooseDiffculty;

    public Game(Player player, List <Zombie> zombies, Pane gameWindow, Label hudHP, Label hudArmor, Label hudWeapon, Label hudMag, Label hudPool, Label hudScore, Label hudTimer, List<Rock> rocks){

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
        this.rocks = rocks;

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
        if (drops.size() < 10) {
            int random = (int) Math.floor(Math.random() * 100);
            if (random < 4) {
                int x = (int) Math.floor(Math.random() * gameWindow.getWidth());
                int y = (int) Math.floor(Math.random() * gameWindow.getHeight());
                drops.add(new Drop(gameInitializer.getScoreDropAnimation(), x, y, Drop.DropType.SCORE));
            }
        }

        // Check collision between zombies and player
        for (Zombie zombie : zombies) {
            zombie.movement(player);
//            if (player.isColliding(zombie)) {
//                player.receivedDamage(0);
//            }
            if (zombie.getState() == Zombie.State.ATTACK) {
                player.receivedDamage(15);
            }
            if (!player.isAlive()) {
                //gameOver();
            }
        }

        // Draw bullets to the pane, adjust direction, and check collision with zombies
        for(Bullet bullet : bullets) {
            if(!bullet.isDrawn()) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().add(bullet.getNode());
                gameWindow.getChildren().add(bullet.getAnimationHandler().getImageView());
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
                gameWindow.getChildren().add(drop.getAnimationHandler().getImageView());
                drop.setDrawn();
            }
            if(drop.isColliding(player)) {
                drop.setAlive(false);
                drop.dropCollision(player, this);
            }
        }
//        Ikke nødvendig med mindre du skal legge til nye underveis
//        for (Rock rock : rocks) {
//            if (!rock.isDrawn()) {
//                //if(gameInitializer.isDEBUG())
//                    gameWindow.getChildren().add(rock.getNode());
//                gameWindow.getChildren().add(rock.getSprite().getImageView());
//                rock.setDrawn();
//            }
//        }

        // Update animation, position and velocity of player
        player.updateAnimation();
        player.update(time);

        // Update zombie position and velocity.
        // Check if zombie is alive. If dead, create and draw a Drop entity
        for(Zombie zombie : zombies) {
            if(!zombie.isAlive()) {
                this.scoreNumber += 100;
                gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getIv());
                drops.add(getRandomDropType(zombie));
            }
            zombie.updateAnimation();
            zombie.update(time);
        }

        // Check if Bullet is dead, and remove if so
        for(Bullet bullet : bullets) {
            if(!bullet.isAlive())
                gameWindow.getChildren().removeAll(bullet.getNode(), bullet.getAnimationHandler().getImageView());
            bullet.update(time);
        }

        // Check if Drop of drops is dead
        for(Drop drop : drops) {
            if(!drop.isAlive()) {
                gameWindow.getChildren().removeAll(drop.getNode(), drop.getAnimationHandler().getImageView());
            }
            drop.update(time);
        }

        // Remove every dead Entity object from the ArrayLists
        bullets.removeIf(Bullet::isDead);
        zombies.removeIf(Zombie::isDead);
        drops.removeIf(Drop::isDead);


        gameInitializer.roundNbr.setText("Round: " + roundNumber);
        if (zombies.isEmpty()) {
            spawnNewRound();
        }

    }

    private void spawnNewRound() {

        if (!isNewRound()) {

            setNewRound(true);

            switch (roundNumber) {
                case 1:
                    createZombies(roundNumber + 1);
                    break;
                case 2:
                    createZombies(roundNumber + 1);
                    break;
                case 3:
                    createZombies(roundNumber + 1);
                    break;
                case 4:
                    createZombies(roundNumber + 1);
                    break;
                case 5:
                    createZombies(roundNumber + 1);
                    break;
                case 6:
                    createZombies(roundNumber + 1);
                    break;
                case 7:
                    createZombies(roundNumber + 1);
                    break;
                case 8:
                    createZombies(roundNumber + 1);
                    break;
                case 9:
                    createZombies(roundNumber + 1);
                    break;
                case 10:
                    createZombies(roundNumber + 1);
                    break;
                case 11:
                    stopTimer();
                    setGameOver(true);
                    setNewRound(true);
                    gameInitializer.showGameLabel();
                    roundNumber = 0;
            }
            setNewRound(false);
            roundNumber++;
        }else {
            gameOver();
        }

    }

    public Drop getRandomDropType(Zombie zombie) {
        int randomNumber = (int)(Math.random()*10) % 5;

        switch(randomNumber) {
            case 0:
                return new Drop(gameInitializer.getHpDropImages(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.HP);
            case 1:
                return new Drop(gameInitializer.getArmorDropImages(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.ARMOR);
            case 2:
                return new Drop(gameInitializer.getMagDropImages(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.PISTOLAMMO);
            case 3:
                return new Drop(gameInitializer.getPoolDropImages(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.RIFLEAMMO);
            case 4:
                return new Drop(gameInitializer.getSpeedDropImages(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.SHOTGUNAMMO);
            default:
                return new Drop(gameInitializer.getScoreDropAnimation(), zombie.getPositionX(), zombie.getPositionY(), Drop.DropType.SCORE);
        }
    }

    /**
     * Method for updating the datafields playerHP, magazineSize, poolSize of type Text.
     * These will in turn update the Text values on the HUD.
     */
    private void updateHUD() {
        String hpLevel = String.valueOf(player.getHealthPoints());
        String armorLevel = String.valueOf(player.getArmor());
        String weapon = player.getEquippedWeapon().toString().toUpperCase();
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
        //removeBullets();
        clearGame();
        player.resetPlayer();
        setScoreNumber(0);
        createZombies(gameInitializer.getNbrZombies());
        gameInitializer.showGameLabel();
        gameInitializer.showMenu();
        setNewRound(false);
        setGameOver(false);
        startTimer();
        setRunning(true);
    }

    public void restartNormalGame() {
        clearGame();
        player.resetNormalPlayer();
        setScoreNumber(0);
        createZombies(gameInitializer.getNbrZombies());
        gameInitializer.showGameLabel();
        gameInitializer.showMenu();
        setNewRound(false);
        setGameOver(false);
        startTimer();
        setRunning(true);
    }

    public void restartHardGame() {
        clearGame();
        player.resetHardPlayer();
        setScoreNumber(0);
        createZombies(gameInitializer.getNbrZombies());
        gameInitializer.showGameLabel();
        gameInitializer.showMenu();
        setNewRound(false);
        setGameOver(false);
        startTimer();
        setRunning(true);
    }

    public void restartInsaneGame() {
        clearGame();
        player.resetInsanePlayer();
        setScoreNumber(0);
        createZombies(gameInitializer.getNbrZombies());
        gameInitializer.showGameLabel();
        gameInitializer.showMenu();
        setNewRound(false);
        setGameOver(false);
        startTimer();
        setRunning(true);
    }

    public void clearGame() {
        removeZombies();
        //removeBullets();
        removeDrops();
        stopTimer();
    }


    /**
     *
     * @param filename f
     */
    public void saveGame(String filename) {
        DataHandler.GameConfiguration gameCfg = getGameConfiguration();

        if (dataHandler.createSaveFile(filename, gameCfg)) {
            System.out.println("Save game");
            System.out.println("GameScore: " + gameCfg.gameScore);
            System.out.println("Player HP: " + gameCfg.player.movementCfg.health);
            System.out.println("Player Armor: " + gameCfg.player.armor);
            System.out.println("Player X: " + gameCfg.player.movementCfg.entityCfg.posX);
            System.out.println("Player Y: " + gameCfg.player.movementCfg.entityCfg.posY);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());
        } else {
            fileAlert(false);
        }
    }

    public void loadGame(String filename) {
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();

        if (dataHandler.readSaveFile(filename, gameCfg)) {
            System.out.println("Load game");
            System.out.println("GameScore: " + gameCfg.gameScore);
            System.out.println("Player HP: " + gameCfg.player.movementCfg.health);
            System.out.println("Player Armor: " + player.getArmor());
            System.out.println("Player X: " + gameCfg.player.movementCfg.entityCfg.posX);
            System.out.println("Player Y: " + gameCfg.player.movementCfg.entityCfg.posY);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());

            setGameConfiguration(gameCfg);
        } else {
            fileAlert(true);
        }
    }

    private void fileAlert(boolean loadGame) {
        this.stopTimer();

        ButtonType resume = new ButtonType("Resume", ButtonBar.ButtonData.OK_DONE);
        ButtonType restart = new ButtonType("Restart", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(restart, resume);
        alert.setHeaderText(null);

        if (loadGame) {
            alert.contentTextProperty().set("Unable to load the savegame.\nIt's either lost, or corrupted.");
            alert.setTitle("Loadgame Error");
        } else {
            alert.contentTextProperty().set("Unable to save the game.");
            alert.setTitle("Savegame Error");
        }

        alert.showAndWait().ifPresent(response -> {
            if (response == resume) {
                this.startTimer();
            } else if (response == restart) {
                this.restartGame();
            }
        });
    }

    /**
     * Method for retrieving all information about all objects in the gameWindow and parsing these over to
     * DataHandler.GameConfiguration for use during saving.
     * @return Returns an object of type DataHandler.GameConfiguration.
     */
    private DataHandler.GameConfiguration getGameConfiguration() {
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        gameCfg.gameScore = this.getScoreNumber();
        gameCfg.player = this.player.getPlayerConfiguration();

        List<DataHandler.MovementConfiguration> zombieCfg = new ArrayList<>();
        for (Zombie zombie : this.zombies)
            zombieCfg.add(zombie.getZombieConfiguration());
        gameCfg.zombies = zombieCfg;

        List<DataHandler.BulletConfiguration> bulletCfg = new ArrayList<>();
        for (Bullet bullet : this.player.getBulletList())
            bulletCfg.add(bullet.getBulletConfiguration());
        gameCfg.bullets = bulletCfg;

        List<DataHandler.DropConfiguration> dropCfg = new ArrayList<>();
        for (Drop drop : this.drops)
            dropCfg.add(drop.getDropConfiguration());
        gameCfg.drops = dropCfg;

        return gameCfg;
    }

    private void setGameConfiguration(DataHandler.GameConfiguration gameCfg) {
        this.setScoreNumber(gameCfg.gameScore);
        this.player.setPlayerConfiguration(gameCfg.player);

        removeZombies();
        for (int i = 0; i < gameCfg.zombies.size(); i++) {
            Zombie zombie = new Zombie(this.getGameInitializer().getZombieImages(), this.getGameInitializer().getZombieAudioClips(),
                    gameCfg.zombies.get(i).entityCfg.posX, gameCfg.zombies.get(i).entityCfg.posY, gameCfg.zombies.get(i).health, this.rocks);
            zombie.setZombieConfiguration(gameCfg.zombies.get(i));
            this.zombies.add(zombie);

            if(this.getGameInitializer().isDEBUG())
                gameWindow.getChildren().add(zombie.getNode());
            gameWindow.getChildren().add(zombie.getAnimationHandler().getImageView());
        }

        removeBullets();
        for (DataHandler.BulletConfiguration bulletCfg : gameCfg.bullets) {
            //TO DO Fill in rotational angle?
            Bullet bullet = new Bullet(getGameInitializer().getPistolBulletImaqe(), bulletCfg.movementCfg.entityCfg.posX, bulletCfg.movementCfg.entityCfg.posY, bulletCfg.movementCfg.movementSpeed, bulletCfg.damage, bulletCfg.movementCfg.direction, this.rocks);
            this.bullets.add(bullet);
        }

        removeDrops();
//          Får et problem forhold til å hente riktig bilde
//        for (DataHandler.DropConfiguration dropCfg : gameCfg.drops) {
//            Drop drop = new Drop(getGameInitializer().getArmorDropImages())
//        }
    }

    /**
     * Method for removing all Zombies in the Game.
     */
    public void removeZombies() {
        for (Zombie zombie : this.zombies) {
            gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getAnimationHandler().getImageView());
            zombie.setAlive(false);
        }

        this.zombies.removeIf(Zombie::isDead);
    }

    /**
     *
     */
    public void removeBullets() {
        for (Bullet bullet : this.bullets) {
            gameWindow.getChildren().removeAll(bullet.getAnimationHandler().getImageView(), bullet.getNode());
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
            gameWindow.getChildren().removeAll(drop.getAnimationHandler().getImageView(), drop.getNode());
        }

        for (Drop drop : this.drops) {
            drop.setAlive(false);
        }
        this.drops.removeIf(Drop::isDead);
    }

    /**
     * Method for creating Zombies at random location.
     * @param nbrZombies Requires the number of Zombies to create.
     */
    public void createZombies(int nbrZombies) {
        try {
            if (this.zombies == null) this.zombies = new ArrayList<>();

            for (int i = 0; i < nbrZombies; i++) {
                Zombie zombie = new Zombie(gameInitializer.getZombieImages(), gameInitializer.getZombieAudioClips(), (int) (Math.random() * 1200), (int) (Math.random() * 650), 100, this.rocks);
                System.out.println("x"+zombie.getPositionX()+" y"+zombie.getPositionY());
                this.zombies.add(zombie);
            }

            for (Zombie zombie : zombies) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().addAll(zombie.getNode());
                gameWindow.getChildren().addAll(zombie.getAnimationHandler().getImageView());
            }
        } catch (Exception e) {
            System.out.println("Unable to reset zombies");
            System.out.println(e.getMessage());
            //stack trace
            System.exit(0);
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

    public boolean isNewRound() {
        return newRound;
    }

    private void setNewRound(boolean newRound) {
        this.newRound = newRound;
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