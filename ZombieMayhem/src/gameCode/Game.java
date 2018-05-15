package gameCode;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;

/**
 * Core class of the application, which controls everything regarding Entities.
 * The class handles the creation and removal of Entities.
 * It checks for collision and what this collision entails.
 * It handles drawing of their Image, and looping the sets of Images to create animation.
 * It handles saving and loading of each Entity to an .xml file.
 * It updates certain values and pass them to the GameWindow FXML.
 */
public class Game {

    public enum Difficulty {
        NORMAL, HARD, INSANE
    }

    private Difficulty difficulty;
    private int scoreMod;
    private double healthMod;
    private int damageMod;
    private int spawnMod;

    private Player player;
    private List<Zombie> zombies;
    private List<Drop> drops = new ArrayList<>();
    private List<Rock> rocks;
    private Pane gameWindow;
    private Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer;

    private int scoreNumber;
    private int secondsCounter;
    private int roundNumber;
    private int maxRound;
    private boolean running;
    private boolean newRound;
    private boolean gameOver;
    private boolean muted;

    private GameInitializer gameInitializer;

    private AssetsHandler assetsHandler;
    private DataHandler dataHandler;

    final private boolean DEBUG = true;

    /**
     * Constructor which sets all starting attributes, and then creates and starts an AnimationTimer.
     * This AnimationTimer continuously run and loop through the methods that controls the Game.
     * These methods include one which updates animations, updates positions and checks collision,
     * and another which updates the HUD elements of the GameWindow FXML.
     * @param difficulty Requires a set Difficulty.
     * @param gameWindow Requires a Pane to draw Images to.
     * @param hudHP requires a Label which represents Player's healthpoints, and will be updated.
     * @param hudArmor requires a Label which represents Player's armor, and will be updated.
     * @param hudWeapon requires a Label which represents Player's equipped weapon, and will be updated.
     * @param hudMag requires a Label which represents Player's current weapon magazine count, and will be updated.
     * @param hudPool requires a Label which represents Player's current weapon ammopool, and will be updated.
     * @param hudScore requires a Label which represents Game's score value, and will be updated.
     * @param hudTimer requires a Label which represents Game's timer value, and will be updated.
     */
    public Game(GameInitializer gameInitializer, AssetsHandler assetsHandler, Difficulty difficulty, Pane gameWindow, Label hudHP, Label hudArmor, Label hudWeapon, Label hudMag, Label hudPool, Label hudScore, Label hudTimer){
        this.gameInitializer = gameInitializer;
        this.assetsHandler = assetsHandler;
        this.difficulty = difficulty;
        setDifficultyModifiers(difficulty);
        this.zombies = new ArrayList<>();
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
        this.roundNumber = 0;
        this.maxRound = 10;

        createRocks();
        createPlayer(difficulty);

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

    /**
     * Method for updating the datafields playerHP, magazineSize, poolSize of type Label.
     * These will in turn update the Label values in GameWindow FXML.
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

        if (roundNumber < maxRound)
            gameInitializer.roundNbr.setText("Round: " + roundNumber);
        else
            gameInitializer.roundNbr.setText("FINAL ROUND!");
    }

    /***
     * Method which continuously run as long as running is set to true.
     * Method will create Entities, draw them to the Pane, update their position and animation, and finally
     * remove them if an Entity is set to dead. Method only runs if running is set to true.
     * @param time Requires a double value which here continuously gets updated via the
     *             the AnimationTimer
     */
    private void onUpdate(double time) {
        secondsCounter = (int)time;
        List<Bullet> bullets = player.getBulletList();

        // Create Drop entities with random position
        randomDropCreation(10);

        //////////////////////////////////////////////////////////

        //Calculate new position for all objects
        List<Entity> playerObjectCollidingList = new ArrayList<>();
        playerObjectCollidingList.addAll(rocks);
        playerObjectCollidingList.addAll(zombies);
        player.action();
        player.movement();

        List<Entity> zombieObjectCollidingList = new ArrayList<>();
        zombieObjectCollidingList.add(player);
        zombieObjectCollidingList.addAll(rocks);
        for (Zombie zombie : zombies) {
            zombie.chasePlayer(player);
            zombie.action(damageMod);
            zombie.movement();

            for (Bullet zombieAttack : zombie.getAttackList()) {
                zombieAttack.movement();
            }
        }

        for(Bullet bullet : bullets) {
            bullet.bulletDirection();
            bullet.movement();
        }

        ////////// Check collision with edges //////////

        // Add Player and Zombies to a List for collision check with edges
        List<Movable> moveableList = new ArrayList<>();
        moveableList.add(player);
        moveableList.addAll(zombies);
        for (Movable moveable : moveableList) {
            if (moveable.getNode().getTranslateX() < 0 || moveable.getNode().getTranslateY() < 0 || moveable.getNode().getTranslateX() > (gameWindow.getWidth() - 60) || moveable.getNode().getTranslateY() > (gameWindow.getHeight() - 100)) {
                moveable.moveBack();
            }
        }

        // Add all Zombie attacks to the list of Bullets in the Game
        List<Bullet> bulletList = new ArrayList<>();
        bulletList.addAll(bullets);
        for (Zombie zombie : zombies) {
            bulletList.addAll(zombie.getAttackList());
        }

        // Check Bullet collision with edges, and if true, set to dead
        for (Bullet bullet : bulletList) {
            if (bullet.getNode().getTranslateX() < 0 || bullet.getNode().getTranslateY() < 0 || bullet.getNode().getTranslateX() > (gameWindow.getWidth()) || bullet.getNode().getTranslateY() > (gameWindow.getHeight())) {
                bullet.setAlive(false);
            }
        }

        ////////// Check for collision between Entities //////////

        // Check Player collision with other entities
        for (Entity obj : playerObjectCollidingList) {
            if(obj.isColliding(player)) {
                player.moveBack();
            }
        }

        // Check Zombie collision with other entities
        for (Zombie zombie : zombies) {
            for (Entity obj : zombieObjectCollidingList) {
                if (obj.isColliding(zombie)) {
                    zombie.moveBack();
                }
            }

            // Damage Player if colliding with zombie attack
            for (Bullet zombieAttack : zombie.getAttackList()) {
                if(zombieAttack.isColliding(player)) {
                    player.receivedDamage(zombieAttack.getDamage());
                    zombieAttack.setAlive(false);
                }
            }
        }

        // Check for Bullet collision with zombies and rocks
        for(Bullet bullet : bullets) {
            bullet.bulletCollision(zombies, rocks);
        }

        // Check for Drop collision with Player
        for (Drop drop : drops) {
            drop.dropCollision(player, this);
        }

        ////////// Draw Entities //////////

        // Add all Entities to a List for drawing
        List<Entity> wholeWorld = new ArrayList<>();
        wholeWorld.add(player);
        wholeWorld.addAll(zombies);
        wholeWorld.addAll(bullets);
        wholeWorld.addAll(drops);
        wholeWorld.addAll(rocks);
        for (Zombie zombie : zombies) {
            wholeWorld.addAll(zombie.getAttackList());
        }

        // Draw all Entities in List
        for (Entity entity : wholeWorld) {
            entity.drawImage(this);
        }

        ////////// Update animation, position and Image rotation //////////

        moveableList.addAll(bulletList); // Update Bullet's position, Image rotation, and handling timeToLive expiration
        for (Movable moveable : moveableList) {
            moveable.updateAnimation();
            moveable.update(time);
        }

        for(Drop drop : drops) {
            drop.update(time);
        }

        ////////// Remove Entities' Node and Image //////////

        for (Entity remove : wholeWorld) {
            remove.removeImage(this);
        }

        ////////// Remove the objects of dead Entities //////////

        zombies.removeIf(Zombie::isDead);
        bullets.removeIf(Bullet::isDead);
        drops.removeIf(Drop::isDead);

        for(Zombie zombie : zombies) {
            zombie.getAttackList().removeIf(Bullet::isDead);
        }

        ////////// End game if Player is dead //////////

        if (!player.isAlive()) {
            gameOver();
        }

        ////////// Create new Zombies each round //////////

        if (zombies.isEmpty()) {
            spawnNewRound();
        }
    }

    /***
     * Method which handles user input.
     * pressEvent() method call in Player handles movement of the Player object itself.
     */
    public void getKeyPressed(){


        gameWindow.getScene().setOnKeyPressed(e -> {
            player.pressEvent(e);
            if (e.getCode() == KeyCode.BACK_SPACE) {
                removeZombies();

            } else if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.P) {
                if (!gameInitializer.getDifficultyVisible()) {
                    pauseGame();
                    gameInitializer.showGameLabel();
                    gameInitializer.showMenu();
                    gameInitializer.hideMenuElements();
                }

            } else if (e.getCode() == KeyCode.M) {
                muteMediaPlayer();

            } else if (e.getCode() == KeyCode.F5){
                saveGame("Quicksave");
            } else if (e.getCode() == KeyCode.F9) {
                if(!loadGame("Quicksave"))
                    fileAlert(true);
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedEvent(e);
        });
    }

    /**
     * Method which creates and draws the Player object for use in the Game.
     */
    private void createPlayer(Game.Difficulty difficulty) {
        try {
            player = new Player(assetsHandler.getPlayerImages(), assetsHandler.getBasicSounds(), assetsHandler.getWeaponSounds(), assetsHandler.getPistolBulletImaqe(), (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50);
            player.resetPlayer(difficulty);
        } catch (Exception e) {
            System.out.println("Error: Player did not load correctly");
        }
    }

    /**
     * Method which creates and draws the Rock objects for use in the Game.
     */
    private void createRocks() {
        DataHandler.GameConfiguration rockCfg = new DataHandler.GameConfiguration();
        rocks = new ArrayList<>();
        if(dataHandler.readEnvironment(rockCfg)) {
            rocks = new ArrayList<>();
            for (int i = 0; i < rockCfg.rocks.size(); i++)
                rocks.add(new Rock(assetsHandler.getRockImage(), rockCfg.rocks.get(i).posX, rockCfg.rocks.get(i).posY));
        } else {
            System.out.println("Unable to create Rocks");
        }
    }

    /**
     * Method which handles spawning of Zombie's in a round-based design.
     * Killing the current Zombies will run this method, and the roundNumber will increase, and
     * a new set of Zombies will be created. The number is dependent on the round and Difficulty.
     */
    private void spawnNewRound() {

        if (!isNewRound()) {
            if (roundNumber < maxRound) {
                roundNumber++;
                createZombies(roundNumber * spawnMod);
            } else {
                running = false;
                setGameOver(true);
                setNewRound(true);
                gameInitializer.showGameLabel();
                roundNumber = 0;
            }
        } else {
            gameOver();
        }

    }

    /**
     * Method for creating Zombies at random location.
     * Healthpoints of each Zombie is dependent on the Difficulty.
     * @param nbrZombies Requires a number to determine how many Zombies to create.
     */
    private void createZombies(int nbrZombies) {
        int zombieHealth = (int)(100 * healthMod);

        try {
            if (this.zombies == null)
                this.zombies = new ArrayList<>();

            // Checks for collision with the Game's Rocks before placing the Zombie for good
            // The check is basic and requires improvement, but works mostly as desired
            for (int i = 0; i < nbrZombies; i++) {
                int[] randomPosition = getRandomPosition();
                Zombie zombie = new Zombie(assetsHandler.getZombieImages(), assetsHandler.getZombieAudioClips(), randomPosition[0], randomPosition[1], zombieHealth);

                int nbrClearedRocks = 0;
                while (nbrClearedRocks < rocks.size()) {
                    for (Rock rock : rocks) {
                        if (rock.isColliding(zombie)) {
                            randomPosition = getRandomPosition();
                            zombie = new Zombie(assetsHandler.getZombieImages(), assetsHandler.getZombieAudioClips(), randomPosition[0], randomPosition[1], zombieHealth);
                        } else {
                            nbrClearedRocks++;
                        }
                    }
                }
                this.zombies.add(zombie);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //stack trace
            System.exit(0);
        }
    }

    /**
     * Method which checks if Player is at the location.
     * @return Returins an array which contains position
     */
    private int[] getRandomPosition() {
        int[] randomPosition = new int[2];
        double distance;

        do {
            randomPosition[0] = (int) (Math.random() * gameWindow.getWidth());
            randomPosition[1] = (int) (Math.random() * gameWindow.getHeight());
            double diffx = (player.getPositionX()) - randomPosition[0];
            double diffy = (player.getPositionY()) - randomPosition[1];
            distance = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
        } while(distance < 100);

        return randomPosition;
    }

    /**
     * Method which creates a Drop of type Score at a random location.
     * @param number Requires a number to determine how many Drops to create.
     */
    private void randomDropCreation(int number) {
        if (drops.size() < number) {
            int random = (int) Math.floor(Math.random() * 100);
            if (random < 4) {
                int[] randomPosition = getRandomPosition();
                Drop drop = new Drop(assetsHandler.getScoreDropAnimation(), randomPosition[0], randomPosition[1], Drop.DropType.SCORE);

                int nbrClearedRocks = 0;
                while (nbrClearedRocks < rocks.size()) {
                    for (Rock rock : rocks) {
                        if (rock.isColliding(drop)) {
                            randomPosition = getRandomPosition();
                            drop = new Drop(assetsHandler.getScoreDropAnimation(), randomPosition[0], randomPosition[1], Drop.DropType.SCORE);
                        } else {
                            nbrClearedRocks++;
                        }
                    }
                }
                drops.add(drop);
            }
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
                return new Drop(assetsHandler.getHpDropImages(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.HP);
            case 1:
                return new Drop(assetsHandler.getArmorDropImages(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.ARMOR);
            case 2:
                return new Drop(assetsHandler.getPistolDropImages(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.PISTOLAMMO);
            case 3:
                return new Drop(assetsHandler.getRifleDropImages(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.RIFLEAMMO);
            case 4:
                return new Drop(assetsHandler.getShotgunDropImages(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.SHOTGUNAMMO);
            default:
                return new Drop(assetsHandler.getScoreDropAnimation(), zombie.getPositionX() + 25, zombie.getPositionY() + 25, Drop.DropType.SCORE);
        }
    }

    /**
     * Method which updates the Game's score value based on the Difficulty.
     */
    public void scorePerKill() {
        switch(difficulty) {
            case NORMAL:
                setScoreNumber(getScoreNumber() + 100 * scoreMod);
                break;
            case HARD:
                setScoreNumber(getScoreNumber() + 100 * scoreMod);
                break;
            case INSANE:
                setScoreNumber(getScoreNumber() + 100 * scoreMod);
                break;
        }
    }

    /***
     * Method for pausing the Game.
     * Will only run if the Game state isn't set to over.
     * Stops the onUpdate() method, and a Label is shown through the GameWindow FXML.
     */
    void pauseGame() {
        if(!isGameOver()) {
            if (running) {
                running = false;
                gameInitializer.showGameLabel();
            } else {
                running = true;
                gameInitializer.showGameLabel();
            }
        }
    }

    /***
     * Method which stops the Game upon Player death.
     * Sets the Game to over and a Label is shown through the GameWindow FXML.
     */
    private void gameOver() {
        running = false;
        setGameOver(true);
        gameInitializer.showGameLabel();
    }

    /**
     * Method which restarts the Game.
     * Player Image is removed, and all other entities are deleted.
     * Resets the Player's stats based on Difficulty, and resets Game's Score and RoundNumber.
     * Resets any Game states, and pauses the onUpdate() method.
     */
    void restartGame() {
        clearGame(true);
        player.resetPlayer(getDifficulty());
        setScoreNumber(0);
        setRoundNumber(0);
        setNewRound(false);
        setGameOver(false);
        running = false;
    }

    /**
     * Method which handles saving of the Game.
     * Creates a GameConfiguration from the DataHandler class and calls the getGameConfiguration() method
     * in order to retrieve values about the Game, and turn these into an .xml file through DataHandler class.
     * Method call createSaveFile() will return a boolean based on whether there was an Exception during creation,
     * and a false return will display an Alert to the user.
     * @param filename Requires a String to represent the name of the file.
     */
    public boolean saveGame(String filename) {
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
            System.out.println("NbrBullets: " + gameCfg.player.bulletListCfg.size());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which handles loading of a Game.
     * Creates a new GameConfiguration object from the DataHandler class.
     * Calls the method readSaveFile() which will transfer each field in the .xml file into variables
     * in GameConfiguration, which then can be used to set the values in the Game.
     * This method returns a false boolean if an Exception occurred, and this will in turn
     * display an Alert to the user.
     * @param filename Requires a String which represents the name of the file.
     */
    public boolean loadGame(String filename) {
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
            System.out.println("NbrBullets: " + gameCfg.player.bulletListCfg.size());

            setGameConfiguration(gameCfg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which will pause the Game and display an Alert to the user.
     * The displayed message is dependent on whether it was during a load, and
     * allows the user to either resume the game or restart the game.
     * @param loadGame Requires a boolean to determine whether it is during loading of a file.
     */
    private void fileAlert(boolean loadGame) {
        running = false;

        ButtonType resume = new ButtonType("Resume Game", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(resume);
        alert.setHeaderText(null);

        if (loadGame) {
            alert.setTitle("Loadgame Error");
            alert.contentTextProperty().set("Unable to load the save file." +
                    "\n\nEither it doesn't exist, or it cannot be read.");
        } else {
            alert.setTitle("Savegame Error");
            alert.contentTextProperty().set("Unable to create the save file.");

        }

        alert.showAndWait().ifPresent(response -> {
            if (response == resume) {
                running = true;
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
        for (Zombie zombie : zombies)
            zombieCfg.add(zombie.getZombieConfiguration());
        gameCfg.zombies = zombieCfg;

        List<DataHandler.DropConfiguration> dropCfg = new ArrayList<>();
        for (Drop drop : drops)
            dropCfg.add(drop.getDropConfiguration());
        gameCfg.drops = dropCfg;

        return gameCfg;
    }

    /**
     * Method for setting all saved information about all saved objects in the gameWindow.
     * Method clears the Game of current Entities. Further, the attributes of Game and Player
     * are set, and every Entity that was saved is created according to the saved attributes.
     * @param gameCfg Requires an object of type GameConfiguration in DataHandler, containing
     *                information about the saved Game.
     */
    private void setGameConfiguration(DataHandler.GameConfiguration gameCfg) {
        clearGame(false);

        setDifficulty(gameCfg.difficulty);
        setScoreNumber(gameCfg.gameScore);
        setRoundNumber(gameCfg.roundNbr);

        player.setPlayerConfiguration(gameCfg.player);

        for (int i = 0; i < gameCfg.zombies.size(); i++) {
            Zombie zombie = new Zombie(assetsHandler.getZombieImages(), assetsHandler.getZombieAudioClips(),
                    gameCfg.zombies.get(i).entityCfg.posX, gameCfg.zombies.get(i).entityCfg.posY, gameCfg.zombies.get(i).health);
            zombie.setZombieConfiguration(gameCfg.zombies.get(i));
            zombies.add(zombie);
        }

        for (int i = 0; i < gameCfg.drops.size(); i++) {
            Image[] images;
            switch (gameCfg.drops.get(i).dropType) {
                case SCORE:
                    images = assetsHandler.getScoreDropAnimation();
                    break;
                case HP:
                    images = assetsHandler.getHpDropImages();
                    break;
                case ARMOR:
                    images = assetsHandler.getArmorDropImages();
                    break;
                case PISTOLAMMO:
                    images = assetsHandler.getPistolDropImages();
                    break;
                case RIFLEAMMO:
                    images = assetsHandler.getRifleDropImages();
                    break;
                case SHOTGUNAMMO:
                    images = assetsHandler.getShotgunDropImages();
                    break;
                default:
                    images = assetsHandler.getScoreDropAnimation();
            }
            Drop drop = new Drop(images, gameCfg.drops.get(i).entityCfg.posX, gameCfg.drops.get(i).entityCfg.posY, gameCfg.drops.get(i).dropType);
            drops.add(drop);
            drop.setDropConfiguration(gameCfg.drops.get(i));
        }
    }

    /**
     * Method which clears the Game of a select type of Entities.
     */
    private void clearGame(boolean clearPlayer) {
        if (clearPlayer)
            removePlayerVisibility();
        removeZombies();
        removeBullets();
        removeDrops();
    }

    /**
     * Method which will hide the Player in the gameWindow.
     */
    private void removePlayerVisibility() {
        gameWindow.getChildren().removeAll(player.getNode(), player.getAnimationHandler().getImageView());
    }

    /**
     * Method for removing all Zombies in the Game.
     */
    void removeZombies() {
        for (Zombie zombie : zombies) {
            gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getAnimationHandler().getImageView());
            zombie.setAlive(false);
        }
        zombies.removeIf(Zombie::isDead);
    }

    /**
     * Method for removing all Bullets in the Game.
     */
    private void removeBullets() {
        for (Bullet bullet : player.getBulletList()) {
            gameWindow.getChildren().removeAll(bullet.getAnimationHandler().getImageView(), bullet.getNode());
            bullet.setAlive(false);
        }
        player.getBulletList().removeIf(Bullet::isDead);
    }

    /**
     * Method for removing all Drops in the Game.
     */
    private void removeDrops() {
        for (Drop drop : drops) {
            gameWindow.getChildren().removeAll(drop.getAnimationHandler().getImageView(), drop.getNode());
            drop.setAlive(false);
        }
        drops.removeIf(Drop::isDead);
    }

    /**
     * Method for removing all Rocks in the Game.
     */
    private void removeRocks() {
        for (Rock rock : rocks) {
            gameWindow.getChildren().removeAll(rock.getAnimationHandler().getImageView(), rock.getNode());
            rock.setAlive(false);
        }
        rocks.removeIf(Rock::isDead);
    }

    /**
     * Method which alters values regarding Zombie attributes and reward for killing a Zombie, according to the set Difficulty.
     * @param difficulty Requires an enum of type Difficulty to set difficultModifier.
     */
    private void setDifficultyModifiers(Difficulty difficulty) {
        scoreMod = 0;   // INT - Times 100 for score per Zombie killed
        healthMod = 0;  // DOUBLE - Times 100 for health per Zombie spawned
        damageMod = 0;  // INT - Times 10 for damage per Zombie attack
        spawnMod = 0;   // INT - Times roundNumber for number of Zombies to spawn each round

        switch (difficulty) {
            case NORMAL:
                scoreMod = 1;
                healthMod = 1;
                damageMod = 2;
                spawnMod = 1;
                break;
            case HARD:
                scoreMod = 2;
                healthMod = 1.5;
                damageMod = 3;
                spawnMod = 2;
                break;
            case INSANE:
                scoreMod = 3;
                healthMod = 2;
                damageMod = 4;
                spawnMod = 3;
                break;
        }
    }

    public AudioClip[] getAllAudioClips() {
        int basicLength = assetsHandler.getBasicSounds().length;
        int weaponLength = assetsHandler.getWeaponSounds().length;
        int zombieLength = assetsHandler.getZombieAudioClips().length;
        AudioClip[] sounds = new AudioClip[basicLength + weaponLength + zombieLength];

        for(int i = 0; i < basicLength; i++) {
            sounds[i] = assetsHandler.getBasicSounds()[i];
        }

        for(int i = 0; i < weaponLength; i++) {
            sounds[i + basicLength] = assetsHandler.getWeaponSounds()[i];
        }

        for(int i = 0; i < zombieLength; i++) {
            sounds[i + basicLength + weaponLength] = assetsHandler.getZombieAudioClips()[i];
        }
        return sounds;
    }

    /**
     * Method which mutes/unmutes the MediaPlayer each time M key is pressed during Game.getKeyPressed().
     */
    public void muteMediaPlayer() {
        if(!muted) {
            assetsHandler.getMediaPlayer().setMute(true);
            muted = true;
        } else {
            assetsHandler.getMediaPlayer().setMute(false);
            muted = false;
        }
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    public Pane getGameWindow() {
        return gameWindow;
    }

    private Difficulty getDifficulty() {
        return difficulty;
    }

    private void setDifficulty(Difficulty difficulty) {
        setDifficultyModifiers(difficulty);
        this.difficulty = difficulty;
    }

    public int getScoreNumber() {
        return scoreNumber;
    }

    public void setScoreNumber(int scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    boolean isNewRound() {
        return newRound;
    }

    private void setNewRound(boolean newRound) {
        this.newRound = newRound;
    }

    boolean isGameOver() {
        return gameOver;
    }

    private void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    private void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    private int getRoundNumber() {
        return roundNumber;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<Drop> getDrops() {
        return drops;
    }
}