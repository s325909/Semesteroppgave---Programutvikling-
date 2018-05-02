package gameCode;

import entities.Player;
import entities.Zombie;
import entities.Sprite;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import menuOptions.SettingsController;

import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GameInitializer implements Initializable{

    @FXML private Pane gameWindow;
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp;

    @FXML private Button back_Help, settings;

    private Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies;
    private int nbrZombies;
    private Game game;
    private MusicPlayer musicPlayer;
    private boolean menuVisible;
    private boolean helpVisible;
    private boolean labelVisible;

    private SceneSizeChangeListener sceneChange;

    public static AudioClip[] weaponSounds;
    private AudioClip[] basicSounds;
    private Sprite[][] playerAnimation;
    private AudioClip[] zombieAudioClips;
    private Sprite[][] zombieAnimation;
    private Sprite[] hudIcons;
    private Sprite[] bulletImages;
    private Sprite[] coin;

    final private boolean DEBUG = false;

    /***
     * Method which will create all the initial Entities and other objects to present when a new game starts.
     * @param location f
     * @param resources f
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
        //Create an object of MusicPlayer, which includes what file to play and automatically starts playing
        try {
            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }
        */


        // Select number of zombies to create, and load all assets
        setNbrZombies(5);
        loadAssets(nbrZombies);

        // Create the Player upon starting a new game
        try {
            player = new Player(this.playerAnimation, this.basicSounds, this.weaponSounds, (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50);
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

        // Create the Node representation of these entities to the gameWindow if DEBUG is set to true
        if (isDEBUG()) {
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
        game = new Game(player, zombies, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer);
        game.setGameInitializer(this);
        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
    }

    /***
     * Method which takes in user keyboard input.
     * movePlayer() method in Player is called in order to transfer input into
     * movement of the Player object.
     */
    private void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            if (e.getCode() == KeyCode.F12) {
                changeFullScreen();

            } else if (e.getCode() == KeyCode.P) {
                game.pauseGame();

            } else if (e.getCode() == KeyCode.ESCAPE) {
                game.pauseGame();
                showMenu();

            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();

            } else if (e.getCode() == KeyCode.F5){
                game.saveTheGame("quicksave");

            } else if (e.getCode() == KeyCode.F9) {
                game.loadTheGame("quicksave");
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
        });
    }

    @FXML
    public void getMessage() {
        System.out.println("test");
    }


    /**
     * Method which finds and loads all necessary assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     * @param nbrZombies Requires the number of Zombie objects to create.
     */
    private void loadAssets(int nbrZombies) {

        // Load all Player sounds and animations
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
        this.weaponSounds = loadAudio(weaponSounds);
        this.playerAnimation = loadSprites(all);

        // Load all Zombie animations
        loadZombiesAssets(nbrZombies);

        // Load coin Drop animation
        ImageView iv = new ImageView();
        this.coin = new Sprite[]{new Sprite(iv,"/resources/Art/Icon/Coin/coin_rotate_", ".png", 6)};

        // Load all Drop images
        String[] dropImages = {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_boost.png"};

        this.hudIcons = loadSingleSprites(dropImages);

        // Load all Bullet images
        String[] bulletImages = {
                "/resources/Art/pistol_bullet.png"};

        this.bulletImages = loadSingleSprites(bulletImages);
    }

    /**
     * Method which finds and loads all necessary Zombie assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     * @param nbrZombies Requires the number of Zombie objects to create.
     */
    public void loadZombiesAssets(int nbrZombies) {
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
     * Method which is used for loading all of Player's animations.
     * It takes in a 2-dimensional array of type SpriteParam and combines
     * it with an ImageView. Through method call prepareSprites(), this is
     * then turned into a 2-dimensional array of type Sprite.
     * @param sprites Requires a 2-dimensional array of type SpriteParam.
     * @return Returns a combined 2-dimensional array of type Sprite.
     */
    private Sprite[][] loadSprites(SpriteParam[][] sprites) {
        ImageView iv = new ImageView();
        Sprite[][] outerSprite = new Sprite[sprites.length][];

        for (int i = 0; i < sprites.length; i++) {
            outerSprite[i] = prepareSprites(sprites[i], iv);
        }
        return outerSprite;
    }

    /**
     * Method which is used for loading all of Zombie's animations.
     * It takes in a 2-dimensional array of type SpriteParam and combines it
     * with an ImageView. Through method call prepareSprite(), this is then
     * turned into a 2-dimensional array of type Sprite. This action is repeated
     * equal to the set number of zombies.
     * @param nbrZombies Requires the number of zombies that should be created.
     * @param sprites Requires a set of animations created in type SpriteParam[].
     * @return Returns a combined 2-dimensional array of type Sprite.
     */
    private Sprite[][] loadSprites(int nbrZombies, SpriteParam[] sprites) {
        Sprite[][] outerSprite = new Sprite[nbrZombies][];
        for (int i = 0; i < nbrZombies; i++) {
            ImageView iv = new ImageView();
            outerSprite[i] = prepareSprites(sprites, iv);
        }
        return outerSprite;
    }

    /**
     *
     * @param files g
     * @return g
     */
    private Sprite[] loadSingleSprites(String[] files) {
        Sprite[] sprites = new Sprite[files.length];
        for(int i = 0; i < sprites.length; i++) {
            ImageView iv = new ImageView();
            sprites[i] = new Sprite(iv, files[i]);
        }
        return sprites;
    }

    /**
     * Method which is used for turning a SpriteParam array and ImageView into a Sprite array.
     * @param spriteParam Requires an array of type SpriteParam.
     * @param iv Requires an ImageView object.
     * @return Returns an array of type Sprite.
     */
    private Sprite[] prepareSprites(SpriteParam[] spriteParam, ImageView iv) {
        Sprite[] sprites = new Sprite[spriteParam.length];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(iv, spriteParam[i].filename, spriteParam[i].extension, spriteParam[i].numberImages);
        }
        return sprites;
    }

    /**
     *
     * @param audioFiles f
     * @return f
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
     * Method which will display a message to the Player upon pausing the game or game over.
     */
    protected void showGameLabel() {
        if(!isLabelVisible()) {
            gamePaused.setVisible(true);
            gameState.setVisible(true);
            setLabelVisible(true);
            if(game.isGameOver()) {
                gameState.setText("GAME OVER!");
                gameState.setTextFill(Color.INDIANRED);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            } else {
                gameState.setText("GAME IS PAUSED");
                gameState.setTextFill(Color.WHITE);
            }
        } else {
            gamePaused.setVisible(false);
            gameState.setVisible(false);
            pressKey.setVisible(false);
            setLabelVisible(false);
        }
    }

    /**
     * Method which will display the in-game menu.
     * Simply sets an object of type VBox to visible, and this VBox contains the
     * menu in form av buttons to interact with.
     */
    protected void showMenu() {
        if(!isMenuVisible()) {
            ingameMenu.setVisible(true);
            setMenuVisible(true);
        } else {
            ingameMenu.setVisible(false);
            setMenuVisible(false);
        }
    }


    public void hideHelp(){
        ingameHelp.setVisible(false);
        ingameMenu.setVisible(true);
        gameState.setVisible(true);
    }


    /***
     * Method which will change the FullScreen state of the application.
     */
    private void changeFullScreen() {
        Stage stage = (Stage) gameWindow.getScene().getWindow();
        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }
    }

    public void resumeGame(){
        game.pauseGame();
        showMenu();
    }

    public void restartGame() {
        game.restartGame();
    }
    
    public void showHelp() {
        if (!helpVisible){
            ingameMenu.setVisible(false);
            gameState.setVisible(false);
            ingameHelp.setVisible(true);
        }else {
            hideHelp();
        }
    }

    public void showSettings() {

        Parent root;

        try {
            Stage windowSettings = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/Settings.fxml"));
            root = loader.load();
            SettingsController controller = loader.getController();
            controller.showReturnToMenu(false);
            windowSettings.setScene(new Scene(root, 500, 500));
            windowSettings.show();
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
        }
    }

    public void exitGame() {
        System.exit(0);
    }

    private boolean isMenuVisible() {
        return menuVisible;
    }

    private void setMenuVisible(boolean menuVisible) {
        this.menuVisible = menuVisible;
    }

    public boolean isLabelVisible() {
        return labelVisible;
    }

    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public int getNbrZombies() {
        return nbrZombies;
    }

    public void setNbrZombies(int nbrZombies) {
        this.nbrZombies = nbrZombies;
    }

    public Sprite[][] getZombieAnimation() {
        return zombieAnimation;
    }

    public AudioClip[] getZombieAudioClips() {
        return zombieAudioClips;
    }

    public AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    public Sprite[][] getPlayerAnimation() {
        return playerAnimation;
    }

    public AudioClip[] getWeaponSounds() {
        return weaponSounds;
    }

    public Sprite[] getHudIcons() {
        return hudIcons;
    }

    public Sprite[] getBulletImages() {
        return bulletImages;
    }

    public Sprite[] getCoin() {
        return coin;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    /**
     * Inner class used in conjunction with creating a 2-dimensional array of type Sprite.
     * Used to create smaller portions of the array, which then are combined with an
     * ImageView in order to create an object of type Sprite.
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
}