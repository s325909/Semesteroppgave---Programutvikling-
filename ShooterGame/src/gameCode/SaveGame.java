package gameCode;

import entities.Bullet;
import entities.Drop;
import entities.Player;
import entities.Zombie;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;

public class SaveGame {

    private DataHandler dataHandler;

    private Player player;
    private Game game;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    private List<Drop> drops;
    private List<Drop> dropsExtra;

    public SaveGame(Player player, Game game, List<Zombie> zombies, List<Bullet> bullets, List<Drop> drops, List<Drop> dropsExtra) {
        this.player = player;
        this.game = game;
        this.zombies = zombies;
        this.bullets = bullets;
        this.drops = drops;
        this.dropsExtra = dropsExtra;
        this.dataHandler = new DataHandler();
    }

    public SaveGame() {this.dataHandler = new DataHandler();};

    /**
     *
     * @param filename f
     */
    public void save(String filename) {
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        retrieveData(gameCfg);

        System.out.println("Retrieve successful");
        System.out.println(gameCfg.player.posX);

        if (dataHandler.createSaveFile(filename, gameCfg)) {
            System.out.println("Save game");
            System.out.println("GameScore: " + game.getScoreNumber());
            System.out.println("Player HP: " + player.getHealthPoints());
            System.out.println("Player Armor: " + player.getArmor());
            System.out.println("Player X: " + player.getPositionX());
            System.out.println("Player Y: " + player.getPositionY());
            System.out.println("NbrZombies: " + zombies.size());
        } else {
            System.out.println("Could not save the game");
        }
    }

    public void save(String filename, DataHandler.GameConfiguration gameCfg) {
        System.out.println("Retrieve successful");
        System.out.println(gameCfg.player.posX);

        if (dataHandler.createSaveFile(filename, gameCfg)) {
            System.out.println("Save game");
            System.out.println("GameScore: " + gameCfg.gameScore);
            System.out.println("Player HP: " + gameCfg.player.health);
            System.out.println("Player Armor: " + gameCfg.player.armor);
            System.out.println("Player X: " + gameCfg.player.posX);
            System.out.println("Player Y: " + gameCfg.player.posY);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());
        } else {
            System.out.println("Could not save the game");
        }
    }

    /**
     *
     * @param gameCfg f
     */
    private void retrieveData(DataHandler.GameConfiguration gameCfg) {
        gameCfg.gameScore = game.getScoreNumber();
        gameCfg.player = retrievePlayerInfo();
        gameCfg.zombies = retrieveZombieInfo();
        gameCfg.bullets = retrieveBulletInfo();
        gameCfg.drops = retrieveDropInfo();
    }

    /**
     *
     * @return f
     */
    private DataHandler.Configuration retrievePlayerInfo() {
        DataHandler.Configuration playerCfg = new DataHandler.Configuration();
        playerCfg.health = player.getHealthPoints();
        playerCfg.armor = player.getArmor();
        playerCfg.posX = player.getPositionX();
        playerCfg.posY = player.getPositionY();
        playerCfg.velX = player.getVelocityX();
        playerCfg.velY = player.getVelocityY();
        playerCfg.movementSpeed = player.getMovementSpeed();
        playerCfg.direction = player.getDirection();
        playerCfg.equipped = player.getEquippedWeapon();
        playerCfg.magPistol = player.getMagazinePistol().getNumberBullets();
        playerCfg.poolPistol = player.getMagazinePistol().getCurrentPool();
        playerCfg.magRifle = player.getMagazineRifle().getNumberBullets();
        playerCfg.poolRifle = player.getMagazineRifle().getCurrentPool();
        playerCfg.magShotgun = player.getMagazineShotgun().getNumberBullets();
        playerCfg.poolShotgun = player.getMagazineShotgun().getCurrentPool();
        return playerCfg;
    }

    /**
     *
     * @return f
     */
    private List<DataHandler.Configuration> retrieveZombieInfo() {
        List<DataHandler.Configuration> zombieList = new ArrayList<DataHandler.Configuration>();
        for (int i = 0; i < zombies.size(); i++) {
            DataHandler.Configuration zombieCfg = new DataHandler.Configuration();
            zombieCfg.health = zombies.get(i).getHealthPoints();
            zombieCfg.posX = zombies.get(i).getPositionX();
            zombieCfg.posY = zombies.get(i).getPositionY();
            zombieCfg.velX = zombies.get(i).getVelocityX();
            zombieCfg.velY = zombies.get(i).getVelocityY();
            zombieCfg.movementSpeed = zombies.get(i).getMovementSpeed();
            zombieCfg.direction = zombies.get(i).getDirection();
            zombieList.add(zombieCfg);
        }
        return zombieList;
    }

    /**
     *
     * @return f
     */
    private List <DataHandler.Configuration> retrieveBulletInfo() {
        List<DataHandler.Configuration> bulletList = new ArrayList<DataHandler.Configuration>();
        for (int i = 0; i < bullets.size(); i++) {
            DataHandler.Configuration bulletCfg = new DataHandler.Configuration();
            bulletCfg.posX = bullets.get(i).getPositionX();
            bulletCfg.posY = bullets.get(i).getPositionY();
            bulletCfg.velX = bullets.get(i).getVelocityX();
            bulletCfg.velY = bullets.get(i).getVelocityY();
            bulletCfg.movementSpeed = bullets.get(i).getMovementSpeed();
            bulletCfg.direction = bullets.get(i).getDirection();
            bulletCfg.damage = bullets.get(i).getDamage();
            bulletList.add(bulletCfg);
        }
        return bulletList;
    }

    /**
     *
     * @return f
     */
    private List <DataHandler.Configuration> retrieveDropInfo() {
        List<DataHandler.Configuration> dropList = new ArrayList<DataHandler.Configuration>();
        for (int i = 0; i < drops.size(); i++) {
            DataHandler.Configuration dropCfg = new DataHandler.Configuration();
            dropCfg.posX = drops.get(i).getPositionX();
            dropCfg.posY = drops.get(i).getPositionY();
            dropList.add(dropCfg);
        }
        return dropList;
    }

    /**
     *  Method for loading a saved game file.
     * @param filename Requires the String which represents the name of the file.
     */
    public void load(String filename) {
        DataHandler.GameConfiguration gameCfg = new DataHandler.GameConfiguration();
        if (dataHandler.readSaveFile(filename, gameCfg)) {
            System.out.println("Load game");
            System.out.println("GameScore: " + gameCfg.gameScore);
            System.out.println("Player HP: " + gameCfg.player.health);
            System.out.println("Player Armor: " + player.getArmor());
            System.out.println("Player X: " + gameCfg.player.posX);
            System.out.println("Player Y: " + gameCfg.player.posY);
            System.out.println("NbrZombies: " + gameCfg.zombies.size());
            loadGame(gameCfg);
        } else {
            fileAlert();
            System.out.println("Could not load quicksave!");
        }
    }

    private void fileAlert() {
        game.stopTimer();

        ButtonType resume = new ButtonType("Resume", ButtonBar.ButtonData.OK_DONE);
        ButtonType restart = new ButtonType("Restart", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Unable to load the savegame.\nIt's either lost, or corrupted.", restart, resume);

        alert.setTitle("Loadgame Error");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == resume) {
                game.startTimer();
            } else if (response == restart) {
                game.restartGame();
            }
        });
    }

    /**
     *
     * @param gameCfg Requires
     */
    private void loadGame(DataHandler.GameConfiguration gameCfg) {
        game.setScoreNumber(gameCfg.gameScore);
        loadPlayer(gameCfg.player);
        loadZombies(gameCfg.zombies);
        loadBullets(gameCfg.bullets);
        //loadDrops(gameCfg.drops);
    }

    /**
     *
     * @param playerCfg Requires
     */
    private void loadPlayer(DataHandler.Configuration playerCfg) {
        player.setHealthPoints(playerCfg.health);
        player.setArmor(playerCfg.armor);
        player.setPosition(playerCfg.posX, playerCfg.posY);
        player.setTranslateNode(playerCfg.posX, playerCfg.posY);
        player.setVelocity(playerCfg.velX, playerCfg.velY);
        player.setMovementSpeed(playerCfg.movementSpeed);
        player.setDirection(playerCfg.direction);
        player.setEquippedWeapon(playerCfg.equipped);
        player.getMagazinePistol().setNumberBullets(playerCfg.magPistol);
        player.getMagazinePistol().setCurrentPool(playerCfg.poolPistol);
        player.getMagazineRifle().setNumberBullets(playerCfg.magRifle);
        player.getMagazineRifle().setCurrentPool(playerCfg.poolRifle);
        player.getMagazineShotgun().setNumberBullets(playerCfg.magShotgun);
        player.getMagazineShotgun().setCurrentPool(playerCfg.poolShotgun);
    }

    /**
     *
     * @param zombieList Requires
     */
    private void loadZombies(List<DataHandler.Configuration> zombieList) {
        game.removeZombies();
        game.getGameInitializer().loadZombiesAssets(zombieList.size());

        this.zombies = new ArrayList<>();
        for (int i = 0; i < zombieList.size(); i++) {
            Zombie zombie = new Zombie(game.getGameInitializer().getZombieAnimation()[i], game.getGameInitializer().getZombieAudioClips(), zombieList.get(i).posX, zombieList.get(i).posY, zombieList.get(i).health);
            zombie.setVelocity(zombieList.get(i).velX, zombieList.get(i).velY);
            zombie.setMovementSpeed(zombieList.get(i).movementSpeed);
            zombie.setDirection(zombieList.get(i).direction);
            this.zombies.add(zombie);
        }

        for (Zombie zombie : this.zombies) {
            if(game.getGameInitializer().isDEBUG())
                game.getGameWindow().getChildren().add(zombie.getNode());
            game.getGameWindow().getChildren().add(zombie.getSprite().getImageView());
        }
    }

    /**
     *
     * @param bulletList f
     */
    private void loadBullets(List<DataHandler.Configuration> bulletList) {
        game.removeBullets();

        this.bullets = new ArrayList<>();
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = new Bullet("/resources/Art/pistol_bullet.png", bulletList.get(i).posX, bulletList.get(i).posY, bulletList.get(i).movementSpeed, bulletList.get(i).damage, bulletList.get(i).direction);
            bullet.setVelocity(bulletList.get(i).velX, bulletList.get(i).velY);
            this.bullets.add(bullet);
        }

        for (Bullet bullet : this.bullets) {
            if(game.getGameInitializer().isDEBUG())
                game.getGameWindow().getChildren().add(bullet.getNode());
            game.getGameWindow().getChildren().add(bullet.getSprite().getImageView());
        }
    }

    /**
     *
     * @param dropsList f
     */
//    private void loadDrops(List<DataHandler.Configuration> dropsList) {
//        removeDrops();
//        removeDropsExtra();
//
//        this.drops = new ArrayList<>();
//        for (int i = 0; i < dropsList.size(); i++) {
//            Drop drop = new Drop("image", dropsList.get(i).posX, dropsList.get(i).posY);
//            this.drops.add(drop);
//        }
//
//        for (Drop drop : this.drops) {
//            if(gameInitializer.isDEBUG())
//                gameWindow.getChildren().add(drop.getNode());
//            gameWindow.getChildren().add(drop.getSprite().getImageView());
//        }
//    }
}
