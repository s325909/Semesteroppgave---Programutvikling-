package gameCode;

import entities.Player;
import entities.Zombie;
import entities.Sprite;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import notCurrentlyUsed.SaveData;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class InitializeGame implements Initializable{

    @FXML private Pane gameWindow;
    @FXML private MenuBar topbar;
    @FXML private Text playerHP, playerArmor, magazineSize, poolSize, score, equippedWeapon;
    @FXML protected Label gameState, pressKey, pressKey2, pressKey3;

    Stage stage = new Stage();

    private StoreData storeData;

    private Player player;
    private List<Zombie> zombies;
    private Game game;
    private SceneSizeChangeListener sceneChange;
    private MusicPlayer musicPlayer;
    private AudioClip[] weapon;
    private AudioClip[] basicSounds;
    private Sprite[][] playerAnimation;
    private AudioClip[] zombieAudioClips;
    private Sprite[][] zombieAnimation;

    final private boolean DEBUG = true;

    public InitializeGame() {
        storeData = new StoreData();
    }

    /***
     * Method which will create every Entity and add these to the gameWindow.
     * Method will also initialize the first soundtrack.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Create an object of MusicPlayer, which includes what file to play and automatically starts playing
//        try {
//            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
//        } catch (Exception e) {
//            System.out.println("Error: Could not find sound file");
//        }

        int nbrZombies = 10;
        loadAssets(nbrZombies);

        // Create the Player upon starting a new game
        try {
            player = new Player(this.playerAnimation, this.weapon, this.basicSounds, (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50);
            player.setWeaponTypeFromString("knife");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element);
            }
            System.out.println("Error: Player did not load correctly");
        }

        // Create every Zombie upon starting a new game
        try {
            zombies = new ArrayList<>();
            for (int i = 0; i < nbrZombies; i++) {
                zombies.add(new Zombie(this.zombieAnimation[i], this.zombieAudioClips, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100));
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        // Simple debug tool which adds the Node of every Entity to the gameWindow upon a new game
        if (DEBUG) {
            gameWindow.getChildren().add(player.getNode());
            for (Zombie zombie : zombies)
                gameWindow.getChildren().add(zombie.getNode());
        }

        // Add the ImageView of every Entity to the gameWindow pane
        gameWindow.getChildren().add(player.getSprite().getImageView());
        for (Zombie zombie : zombies) {
            gameWindow.getChildren().add(zombie.getSprite().getImageView());
        }

        // Initialize the Game object, and thus start the game
        game = new Game(player, zombies, gameWindow, playerHP, playerArmor, magazineSize, poolSize, score, equippedWeapon);
        game.setController(this);
        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
    }

    //menuOptions.LoadingController.getMusicSlider();

    /***
     * Method which takes in user keyboard input.
     * Some input is handled in the movePlayer() method.
     */
    private void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            if (e.getCode() == KeyCode.F12) {
                changeFullScreen();

            } else if (e.getCode() == (KeyCode.P) || e.getCode() == KeyCode.ESCAPE) {
                game.pauseGame();

            } else if (e.getCode() == KeyCode.R) {
                game.setHoldingButtonR(true);

            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();
            } else if (e.getCode() == KeyCode.F5){
                System.out.println("Game is saved");
                //saveGame(null);
            } else if (e.getCode() == KeyCode.F9) {
                loadSavegame("quicksave");
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
            if (e.getCode() == KeyCode.R)
                game.setHoldingButtonR(false);
        });

        /*gameWindow.getScene().setOnKeyPressed(event -> {
            player.movePlayer(event);
            if (event.getCode() == KeyCode.F5){
                System.out.println("Game is saved");
                saveGame();
            }
        });*/
    }

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

    private void loadGame(StoreData.GameConfiguration gameCfg) {
        game.setScoreNumber(gameCfg.gameScore);
        loadPlayer(gameCfg.player);
        loadZombies(gameCfg.zombies);
        game.setZombies(this.zombies);
    }

    private void loadPlayer(StoreData.Configuration playerCfg) {
        this.player.setHealthPoints(playerCfg.health);
        this.player.setArmor(playerCfg.armour);
        this.player.setPosition(playerCfg.posX, playerCfg.posY);
        this.player.setTranslateNode(playerCfg.posX, playerCfg.posY);
        this.player.setDirection(playerCfg.direction);
        this.player.setEquippedWeapon(playerCfg.equipped);
        //.......
    }

    private void loadZombies(List<StoreData.Configuration> zombieList) {
        killZombies();
        loadZombiesAssets(zombieList.size());

        this.zombies = new ArrayList<>();
        for (int i = 0; i < zombieList.size(); i++) {
            Zombie zombie = new Zombie(this.zombieAnimation[i], this.zombieAudioClips, zombieList.get(i).posX, zombieList.get(i).posY, zombieList.get(i).health);
            zombie.setDirection(zombieList.get(i).direction);
            this.zombies.add(zombie);
        }

        for (Zombie zombie : this.zombies) {
            if (DEBUG)
                gameWindow.getChildren().add(zombie.getNode());
            gameWindow.getChildren().add(zombie.getSprite().getImageView());
        }
    }

    private void killZombies() {
        for (Zombie zombie : this.zombies) {
            if (DEBUG)
                gameWindow.getChildren().remove(zombie.getNode());
            gameWindow.getChildren().remove(zombie.getSprite().getImageView());
        }

        for (Zombie zombie : this.zombies) {
            zombie.setAlive(false);
        }
        this.zombies.removeIf(Zombie::isDead);
    }

    /**
     * Method which finds and loads all necessary assets from disk only once.
     * @param nbrZombies Requires the number of Zombie objects that should be created.
     */
    private void loadAssets(int nbrZombies) {
        String[] playerSounds = {
                "/resources/Sound/Sound Effects/Player/player_breathing_calm.wav",
                "/resources/Sound/Sound Effects/Player/footsteps_single.wav"};
        String[] weaponSounds = {
                "/resources/Sound/Sound Effects/Player/Knife/knife_swish.mp3",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_shot.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_shot.wav",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_shot.wav",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_reload.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_empty.mp3"};

        SpriteParam[] knife = {
                new SpriteParam("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/move/survivor-move_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)};
        SpriteParam[] pistol = {
                new SpriteParam("/resources/Art/Player/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/move/survivor-move_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/handgun/reload/survivor-reload_handgun_", ".png", 15)};
        SpriteParam[] rifle = {
                new SpriteParam("/resources/Art/Player/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/move/survivor-move_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15),
                new SpriteParam("/resources/Art/Player/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new SpriteParam("/resources/Art/Player/rifle/reload/survivor-reload_rifle_", ".png", 20)};
        SpriteParam[] shotgun = {
                new SpriteParam("/resources/Art/Player/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/meleeattack/survivor-meleeattack_shotgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/shotgun/reload/survivor-reload_shotgun_", ".png", 20)};
        SpriteParam[][] all = {knife, pistol, rifle, shotgun};

        this.basicSounds = loadAudio(playerSounds);
        this.weapon = loadAudio(weaponSounds);
        this.playerAnimation = loadSprites(all);

//        String[] zombieSounds = {
//                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
//                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};
//
//        SpriteParam[] zombieAnimations = {
//                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
//                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
//                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};
//
//        this.zombieAudioClips = loadAudio(zombieSounds);
//        this.zombieAnimation = loadSprites(nbrZombies, zombieAnimations);

        loadZombiesAssets(nbrZombies);
    }

    private void loadZombiesAssets(int nbrZombies) {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        this.zombieAudioClips = loadAudio(zombieSounds);
        this.zombieAnimation = loadSprites(nbrZombies, zombieAnimations);
    }

    /***
     * Method which is used for loading all the various weapon sprites into a 2-dimensional array.
     * @param sprites Requires a 2-dimensional array of type Sprite
     */
    private Sprite[][] loadSprites(SpriteParam[][] sprites) {
        ImageView iv = new ImageView();
        Sprite[][] outerSprite = new Sprite[sprites.length][];

        for (int i = 0; i < sprites.length; i++) {
            outerSprite[i] = loadSprites(sprites[i], iv);
        }
        return outerSprite;
    }

    /**
     *
     * @param nbrZombies
     * @param sprites
     * @return
     */
    private Sprite[][] loadSprites(int nbrZombies, SpriteParam[] sprites) {
        Sprite[][] outerSprite = new Sprite[nbrZombies][];
        for (int i = 0; i < nbrZombies; i++) {
            ImageView iv = new ImageView();
            outerSprite[i] = loadSprites(sprites, iv);
        }
        return outerSprite;
    }

    /**
     *
     * @param spriteParam
     * @param iv
     * @return
     */
    private Sprite[] loadSprites(SpriteParam[] spriteParam, ImageView iv) {
        Sprite[] sprites = new Sprite[spriteParam.length];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(iv, spriteParam[i].filename, spriteParam[i].extension, spriteParam[i].numberImages);
        }
        return sprites;
    }

    /**
     *
     * @param audioFiles
     * @return
     */
    private AudioClip[] loadAudio(String[] audioFiles) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for(int i = 0; i < clips.length; i++) {
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
            clips[i].setVolume(0.1);
        }
        return clips;
    }

    /**
     * Method which will display a message to the player upon pausing the game, or if the game ends normally.
     * What message is displayed is dependent on which of these two states are present.
     * @param isGameOver Requires a boolean which will decide whether to display a message regarding
     *                   pause, or a message regarding the end of the game, commonly "game over".
     * @param visible Requires a boolean which will decide whether to display these messages.
     */
    protected void setGameLabel(Boolean isGameOver, boolean visible) {
        if(visible) {
            gameState.setVisible(true);
            pressKey.setVisible(true);
            pressKey2.setVisible(true);
            pressKey3.setVisible(true);
        } else {
            gameState.setVisible(false);
            pressKey.setVisible(false);
            pressKey2.setVisible(false);
            pressKey3.setVisible(false);
        }

        if (isGameOver) {
            gameState.setText("GAME OVER!");
            gameState.setTextFill(Color.INDIANRED);
            pressKey.setText("Press R to Restart");
            pressKey2.setText("Press ESC to pop up in game Menu...IDK");
        } else {
            gameState.setText("GAME IS PAUSED");
            gameState.setTextFill(Color.WHITE);
            pressKey.setText("Press P to Continue");
            pressKey2.setText("Press R to Restart");
            pressKey3.setText("Press ESC to pop up in game Menu...IDK");
        }
    }

    /***
     * Method which will change the FullScreen state of the application.
     */
    public void changeFullScreen() {
        Stage stage = (Stage) gameWindow.getScene().getWindow();
        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
            topbar.setVisible(true);
        } else {
            stage.setFullScreen(true);
            topbar.setVisible(false);
        }
    }

    public void exitGame() {
        System.exit(0);
    }

    /***
     * Inner class used in combination with creating a 2-dimensional Sprite array.
     */
    private class SpriteParam {
        String filename;
        String extension;
        int numberImages;

        private SpriteParam(String filename, String extension, int numberImages) {
            this.filename = filename;
            this.extension = extension;
            this.numberImages = numberImages;
        }
    }


    /***
     * Method which will open a new window with Save option.
     */
    @FXML
    public void saveGame() {
        Parent root;
        try {
            game.pauseGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("saveGame.fxml"));
            root = loader.load();
            InitializeSave initializeSave = loader.getController();
            initializeSave.saveData = new SaveData(player.getPositionX(), player.getPositionY(), player.getHealthPoints(), player.getArmor(), playerHP, playerArmor, magazineSize, poolSize, score); //.setText(this.playerHP.getText());
            //root = FXMLLoader.load(getClass().getResource("saveGame.fxml"));
            Stage saveGame = new Stage();
            saveGame.setScene(new Scene(root, 600, 400));
            saveGame.show();
        } catch (Exception e) {
            System.out.println("Open SavePane Error");
        }
    }
    /***
     * Method which will open a new window with Load option.
     */
    @FXML
    public void loadGame() {
        Parent root;
        try {
            game.pauseGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/Loading.fxml"));
            root = loader.load();
            LoadSavedGame loadSavedGame = loader.getController();
            //root = FXMLLoader.load(getClass().getResource("Loading.fxml"));
            Stage loadGame = new Stage();
            loadGame.setScene(new Scene(root, 600, 400));
            loadGame.show();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    // For resizing the images, might use
    //        double maxWidth = -1;
//        double maxHeight = -1;
//
//        for(int i = 0; i < this.allAnimation.length; i++) {
//            for (int j = 0; j < this.allAnimation[i].length; j++) {
//                if (this.allAnimation[i][j].getWidth() > maxWidth && this.allAnimation[i][j].getHeight() > maxHeight) {
//                    maxWidth = this.allAnimation[i][j].getWidth();
//                    maxHeight = this.allAnimation[i][j].getHeight();
//                }
//            }
//        }
//
//        for(int i = 0; i < this.allAnimation.length; i++) {
//            for (int j = 0; j < this.allAnimation[i].length; j++) {
//                this.allAnimation[i][j].setMax(maxWidth, maxHeight);
//            }
//        }
//
//        super.getSprite().setMax(maxWidth,maxHeight);
//        //System.out.println(maxWidth);
//        //System.out.println(maxHeight);
}