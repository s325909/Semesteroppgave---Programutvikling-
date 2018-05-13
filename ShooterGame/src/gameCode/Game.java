package gameCode;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum Difficulty {
        NORMAL, HARD, INSANE
    }

    private Difficulty difficulty;

    private Player player;
    private List<Zombie> zombies;
    private List<Rock> rocks;
    private List<Drop> drops = new ArrayList<>();
    private Pane gameWindow;
    private Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer;

    private int scoreNumber;
    private int secondsCounter;
    private int roundNumber;

    private GameInitializer gameInitializer;
    private boolean running;
    private boolean newRound;
    private boolean gameOver;
    private DataHandler dataHandler;

    public Game(Player player, List<Rock> rocks, Pane gameWindow, Label hudHP, Label hudArmor, Label hudWeapon, Label hudMag, Label hudPool, Label hudScore, Label hudTimer){

        this.difficulty = Difficulty.NORMAL;
        this.roundNumber = 0;
        this.player = player;
        this.zombies = new ArrayList<>();
        this.rocks = rocks;
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
                updateHUD();
                if(running)
                    onUpdate(time);
            }
        };
        timer.start();
    }

    /***
     * Method which continuously run as long as this.running is set to true.
     * Method will keep updating all Entities' positions and check for collision.
     * Method is affected by pauseGame() method.
     * @param time Requires a double value which here continuously gets updated via the
     *             the AnimationTimer
     */
    private void onUpdate(double time) {
        secondsCounter = (int)time;
        List<Bullet> bullets = player.getBulletList();

        // Create Drop entities with random position
        if (drops.size() < 10) {
            int random = (int) Math.floor(Math.random() * 100);
            if (random < 4) {
                int x = (int) Math.floor(Math.random() * gameWindow.getWidth());
                int y = (int) Math.floor(Math.random() * gameWindow.getHeight());
                drops.add(new Drop(gameInitializer.getScoreDropAnimation(), x, y, Drop.DropType.SCORE));
            }
        }

        ////
        //Calculate new position for all objects
        List<Entity> playerObjectCollidingList = new ArrayList<>();
        playerObjectCollidingList.addAll(rocks);
        playerObjectCollidingList.addAll(zombies);
        player.move();
        player.movement();

        List<Entity> zombieObjectCollidingList = new ArrayList<>();
        zombieObjectCollidingList.add(player);
        zombieObjectCollidingList.addAll(rocks);
        for (Zombie zombie : zombies) {
            zombie.findDirection(player);
            zombie.move();
            zombie.movement();

            for (Bullet zombieAttack : zombie.getAttackList()) {
                zombieAttack.movement();
            }
        }

        for(Bullet bullet : bullets) {
            bullet.bulletDirection();
            bullet.movement();
        }

        ////
        //Check collision with edges
        if(player.getNode().getTranslateX() < 0 || player.getNode().getTranslateY() < 0 || player.getNode().getTranslateX() > (gameWindow.getWidth() - 60) || player.getNode().getTranslateY() > (gameWindow.getHeight() - 100)) {
            player.moveBack();
        }

        for (Zombie zombie : zombies) {
            if (zombie.getNode().getTranslateX() < 0 || zombie.getNode().getTranslateY() < 0 || zombie.getNode().getTranslateX() > (gameWindow.getWidth()) || zombie.getNode().getTranslateY() > (gameWindow.getHeight())) {
                zombie.moveBack();
            }
            for (Bullet zombieAttack : zombie.getAttackList()) {
                if (zombieAttack.getNode().getTranslateX() < 0 || zombieAttack.getNode().getTranslateY() < 0 || zombieAttack.getNode().getTranslateX() > (gameWindow.getWidth()) || zombieAttack.getNode().getTranslateY() > (gameWindow.getHeight())) {
                    zombieAttack.setAlive(false);
                }
            }
        }

        for (Bullet bullet : bullets) {
            if (bullet.getNode().getTranslateX() < 0 || bullet.getNode().getTranslateY() < 0 || bullet.getNode().getTranslateX() > (gameWindow.getWidth()) || bullet.getNode().getTranslateY() > (gameWindow.getHeight())) {
                bullet.setAlive(false);
            }
        }

        //Checking if the new positions is colliding with other objects, if this happens move back to previous position.
        for (Entity obj : playerObjectCollidingList) {
            if(obj.isColliding(player)) {
                player.moveBack();
            }
        }

        for (Zombie zombie : zombies) {
            for (Entity obj : zombieObjectCollidingList) {
                if (obj.isColliding(zombie)) {
                    zombie.moveBack();
                }
            }

            //Damage player if colliding with zombie attack
            for (Bullet zombieAttack : zombie.getAttackList()) {
                if(zombieAttack.isColliding(player)) {
                    player.receivedDamage(zombieAttack.getDamage());
                    zombieAttack.setAlive(false);
                }
            }
        }

        // Check for collision between Bullets and zombies, and Bullets and rocks
        for(Bullet bullet : bullets) {
            bullet.bulletCollision(zombies, rocks);
        }

        // Check for collision with Player
        for (Drop drop : drops) {
            drop.dropCollision(player, this);
        }

        ////
        // Draw zombies to the pane
        for (Zombie zombie : zombies) {
            if(!zombie.isDrawn()) {
                if (gameInitializer.isDEBUG())
                    gameWindow.getChildren().addAll(zombie.getNode());
                gameWindow.getChildren().addAll(zombie.getAnimationHandler().getImageView());
                zombie.setDrawn();
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
        }

        // Draw drops to the pane, place these furthest back to allow other Entities to pass over,
        // and check for collision with Player
        for (Drop drop : drops) {
            if (!drop.isDrawn()) {
                if(gameInitializer.isDEBUG())
                    gameWindow.getChildren().add(drop.getNode());
                gameWindow.getChildren().add(drop.getAnimationHandler().getImageView());
                drop.setDrawn();
                drop.getAnimationHandler().getImageView().toBack();
                drop.getNode().toBack();
            }
        }

        // Check collision between zombies and player
        for (Zombie zombie : zombies) {
            for (Bullet zombieAttack : zombie.getAttackList()) {
                if (!zombieAttack.isDrawn()) {
                    gameWindow.getChildren().add(zombieAttack.getNode());
                    zombieAttack.setDrawn();
                }
            }
        }

        ////////
        //      Methods for updating animation, position and velocity of Entities given that
        //      they are still alive. If not, remove ImageView, Node and finally the object itself.
        ////////

        // Update animation, position and velocity of player
        player.updateAnimation();
        player.update(time);

        // Check if Zombie is alive.
        // If alive, update animation, and position and velocity.
        // If not, give Player score points, remove the ImageView and Node, and create a Drop of random "quality".
        for(Zombie zombie : zombies) {
            zombie.updateAnimation();
            zombie.update(time);

            for (Bullet zombieAttack : zombie.getAttackList()) {
                zombieAttack.update(time);
            }
        }

        // Update Bullet's position, Image direction, and handling timeToLive expiration.
        for(Bullet bullet : bullets) {
            bullet.update(time);
        }

        // Check if Drop is alive.
        // If alive, update position.
        // If not, remove the ImageView and Node.
        for(Drop drop : drops) {
            drop.update(time);
        }

        ////
        //Remove images of object not longer active
        for(Zombie zombie : zombies) {
            if (!zombie.isAlive()) {
                this.scoreNumber += 100;
                gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getAnimationHandler().getImageView());
                drops.add(getRandomDropType(zombie));
            }

            for (Bullet zombieAttack : zombie.getAttackList()) {
                if (!zombieAttack.isAlive()) {
                    gameWindow.getChildren().remove(zombieAttack.getNode());
                }
            }
        }

        for(Bullet bullet : bullets) {
            if(!bullet.isAlive()){
                gameWindow.getChildren().removeAll(bullet.getNode(), bullet.getAnimationHandler().getImageView());
            }
        }

        for(Drop drop : drops) {
            if (!drop.isAlive()) {
                gameWindow.getChildren().removeAll(drop.getNode(), drop.getAnimationHandler().getImageView());
            }
        }

        // Finally remove the object itself if isDead() equals true.
        zombies.removeIf(Zombie::isDead);
        bullets.removeIf(Bullet::isDead);
        drops.removeIf(Drop::isDead);

        for(Zombie zombie : zombies) {
            zombie.getAttackList().removeIf(Bullet::isDead);
        }

        if (!player.isAlive()) {
            //gameOver();
        }

        if (zombies.isEmpty()) {
            spawnNewRound();
        }
    }

    private void spawnNewRound() {

        if (!isNewRound()) {

            setNewRound(true);

            switch (difficulty) {

                case NORMAL:
                    switch (roundNumber) {
                        case 0:
                            createZombies(1);
                            break;
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
                            setRunning(false);
                            setGameOver(true);
                            setNewRound(true);
                            gameInitializer.showGameLabel();
                            gameInitializer.roundNbr.setText("FINAL ROUND");
                            roundNumber = 0;
                    }
                    setNewRound(false);
                    roundNumber++;
                    break;

                case HARD:
                    switch (roundNumber) {
                        case 0:
                            createZombies(1);
                            break;
                        case 1:
                            createZombies(roundNumber + 2);
                            break;
                        case 2:
                            createZombies(roundNumber + 2);
                            break;
                        case 3:
                            createZombies(roundNumber + 2);
                            break;
                        case 4:
                            createZombies(roundNumber + 2);
                            break;
                        case 5:
                            createZombies(roundNumber + 2);
                            break;
                        case 6:
                            createZombies(roundNumber + 2);
                            break;
                        case 7:
                            createZombies(roundNumber + 2);
                            break;
                        case 8:
                            createZombies(roundNumber + 2);
                            break;
                        case 9:
                            createZombies(roundNumber + 2);
                            break;
                        case 10:
                            setRunning(false);
                            setGameOver(true);
                            setNewRound(true);
                            gameInitializer.showGameLabel();
                            gameInitializer.roundNbr.setText("FINAL ROUND");
                            roundNumber = 0;
                    }
                    setNewRound(false);
                    roundNumber++;
                    break;

                case INSANE:
                    switch (getRoundNumber()) {
                        case 0:
                            createZombies(1);
                            break;
                        case 1:
                            createZombies(roundNumber + 2);
                            break;
                        case 2:
                            createZombies(roundNumber + 3);
                            break;
                        case 3:
                            createZombies(roundNumber + 4);
                            break;
                        case 4:
                            createZombies(roundNumber + 5);
                            break;
                        case 5:
                            createZombies(roundNumber + 6);
                            break;
                        case 6:
                            createZombies(roundNumber + 7);
                            break;
                        case 7:
                            createZombies(roundNumber + 8);
                            break;
                        case 8:
                            createZombies(roundNumber + 9);
                            break;
                        case 9:
                            createZombies(roundNumber + 10);
                            break;
                        case 10:
                            setRunning(false);
                            setGameOver(true);
                            setNewRound(true);
                            gameInitializer.showGameLabel();
                            gameInitializer.roundNbr.setText("FINAL ROUND");
                            roundNumber = 0;
                    }
                    setNewRound(false);
                    roundNumber++;
                    break;
            }
        }else {
            gameOver();
        }

    }

    /**
     * Method which will create a random type of Drop on the position of a Zombie.
     * DropType has a corresponding Image, and can be used to decide which type of Player stat that should be increased.
     * @param zombie Requires an object of type Zombie to determine the position of which to spawn
     *               the newly created Drop.
     * @return Returns an object of type Drop with a random Image and DropType.
     */
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

        gameInitializer.roundNbr.setText("Round: " + roundNumber);
    }

    /***
     * Method for changing the boolean running.
     * Method affects the onUdate() function.
     */
    public void pauseGame() {
        if(!isGameOver()) {
            if (isRunning()) {
                setRunning(false);
                gameInitializer.showGameLabel();
            } else {
                setRunning(true);
                gameInitializer.showGameLabel();
            }
        }
    }

    /***
     * Method for stopping the game and displaying a message at a
     * point where the game is over.
     */
    public void gameOver() {
        setRunning(false);
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
        clearGame();
        player.resetPlayer(getDifficulty());
        setScoreNumber(0);
        setRoundNumber(0);
        setNewRound(false);
        setGameOver(false);
        setRunning(false);
    }

    public void clearGame() {
        removePlayerVisibility();
        removeZombies();
        removeBullets();
        removeDrops();
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
            System.out.println("Player Rotation: " + gameCfg.player.movementCfg.rotation);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());
            System.out.println("NbrZombies: " + gameCfg.player.bulletListCfg.size());
            for (DataHandler.BulletConfiguration bulletCfg : gameCfg.player.bulletListCfg)
                System.out.println("Bullet remaining time " + bulletCfg.remainingTime);
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
            System.out.println("Player Rotation: " + gameCfg.player.movementCfg.rotation);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());
            System.out.println("NbrZombies: " + gameCfg.player.bulletListCfg.size());
            for (DataHandler.BulletConfiguration bulletCfg : gameCfg.player.bulletListCfg)
                System.out.println("Bullet remaining time " + bulletCfg.remainingTime);

            setGameConfiguration(gameCfg);
        } else {
            fileAlert(true);
        }
    }

    private void fileAlert(boolean loadGame) {
        setRunning(false);

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
                setRunning(true);
            } else if (response == restart) {
                restartGame();
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
        gameCfg.difficulty = getDifficulty();
        gameCfg.gameScore = getScoreNumber();
        gameCfg.roundNbr = getRoundNumber();
        gameCfg.player = player.getPlayerConfiguration();

        List<DataHandler.MovementConfiguration> zombieCfg = new ArrayList<>();
        for (Zombie zombie : this.zombies)
            zombieCfg.add(zombie.getZombieConfiguration());
        gameCfg.zombies = zombieCfg;

        List<DataHandler.DropConfiguration> dropCfg = new ArrayList<>();
        for (Drop drop : this.drops)
            dropCfg.add(drop.getDropConfiguration());
        gameCfg.drops = dropCfg;

        return gameCfg;
    }

    private void setGameConfiguration(DataHandler.GameConfiguration gameCfg) {
        clearGame();

        setDifficulty(gameCfg.difficulty);
        setScoreNumber(gameCfg.gameScore);
        setRoundNumber(gameCfg.roundNbr);

        player.setPlayerConfiguration(gameCfg.player);

        for (int i = 0; i < gameCfg.zombies.size(); i++) {
            Zombie zombie = new Zombie(this.getGameInitializer().getZombieImages(), this.getGameInitializer().getZombieAudioClips(),
                    gameCfg.zombies.get(i).entityCfg.posX, gameCfg.zombies.get(i).entityCfg.posY, gameCfg.zombies.get(i).health);
            zombie.setZombieConfiguration(gameCfg.zombies.get(i));
            this.zombies.add(zombie);

            if(this.getGameInitializer().isDEBUG())
                gameWindow.getChildren().add(zombie.getNode());
            gameWindow.getChildren().add(zombie.getAnimationHandler().getImageView());
        }


//          Får et problem forhold til å hente riktig bilde
//        for (DataHandler.DropConfiguration dropCfg : gameCfg.drops) {
//            Drop drop = new Drop(getGameInitializer().getArmorDropImages())
//        }
    }

    /**
     * Method which will hide the Player in the gameWindow.
     */
    public void removePlayerVisibility() {
        gameWindow.getChildren().removeAll(player.getNode(), player.getAnimationHandler().getImageView());
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
     * Method for removing all Bullets in the Game.
     */
    public void removeBullets() {
        for (Bullet bullet : player.getBulletList()) {
            gameWindow.getChildren().removeAll(bullet.getAnimationHandler().getImageView(), bullet.getNode());
            bullet.setAlive(false);
        }
        player.getBulletList().removeIf(Bullet::isDead);
    }

    /**
     * Method for removing all Drops in the Game.
     */
    public void removeDrops() {
        for (Drop drop : this.drops) {
            gameWindow.getChildren().removeAll(drop.getAnimationHandler().getImageView(), drop.getNode());
            drop.setAlive(false);
        }
        this.drops.removeIf(Drop::isDead);
    }

    /**
     * Method for creating Zombies at random location.
     * @param nbrZombies Requires the number of Zombies to create.
     */
    public void createZombies(int nbrZombies) {
        int zombieHealth = 0;
        switch(getDifficulty()) {
            case NORMAL:
                zombieHealth = 100;
                break;
            case HARD:
                zombieHealth = 150;
                break;
            case INSANE:
                zombieHealth = 200;
                break;
        }

        try {
            if (this.zombies == null)
                this.zombies = new ArrayList<>();

            for (int i = 0; i < nbrZombies; i++) {
                Zombie zombie = new Zombie(gameInitializer.getZombieImages(), gameInitializer.getZombieAudioClips(), (int) (Math.random() * 1200), (int) (Math.random() * 650), zombieHealth);
                this.zombies.add(zombie);
            }
        } catch (Exception e) {
            System.out.println("Unable to reset zombies");
            System.out.println(e.getMessage());
            //stack trace
            System.exit(0);
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean isRunning) {
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

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}