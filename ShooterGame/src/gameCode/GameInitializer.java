package gameCode;

import entities.AnimationHandler;
import entities.Player;
import entities.Rock;
import entities.Zombie;
import entities.Sprite;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import menuOptions.SettingsController;

import java.io.IOException;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GameInitializer implements Initializable{

    @FXML private Pane gameWindow;
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp, ingameNormalDifficulty, ingameHardDifficulty, ingameInsaneDifficulty;

    @FXML HBox ingameChooseDifficulty;

    @FXML private Button back_Help, settings;

    @FXML private Button normalDifficulty, hardDifficulty, insaneDifficulty;

    private Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies;
    private List<Rock> rocks;
    private int nbrZombies;
    private Game game;
    private MusicPlayer musicPlayer;
    private boolean menuVisible;
    private boolean helpVisible;
    private boolean difficultyVisible;
    private boolean labelVisible;

    private SceneSizeChangeListener sceneChange;

    public static AudioClip[] weaponSounds;
    private AudioClip[] basicSounds;
    private Image[][][] playerImages;
    private AudioClip[] zombieAudioClips;
    private Image[][] zombieImages;
    private Image[] hpDropImages;
    private Image[] armorDropImages;
    private Image[] magDropImages;
    private Image[] poolDropImages;
    private Image[] speedDropImages;
    private Image[] coinDropImages;
    private Image[] coinDrop;
    private Image[] bulletImages;

    final private boolean DEBUG = false;

    /***
     * Method which will create all the initial Entities and other objects to present when a new game starts.
     * @param location f
     * @param resources f
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create an object of MusicPlayer, which includes what file to play and automatically starts playing
//        try {
//            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
//        } catch (Exception e) {
//            System.out.println("Error: Could not find sound file");
//        }

        // Select number of zombies to create, and load all assets
        setNbrZombies(0);
        loadAssets();

        this.rocks = new ArrayList<Rock>();
        rocks.add(new Rock("/resources/Art/rock.png", 240, 400));
        rocks.add(new Rock("/resources/Art/rock.png", 300, 500));
        rocks.add(new Rock("/resources/Art/rock.png", 151, 151));
        rocks.add(new Rock("/resources/Art/rock.png", 500, 500));

        // Create the Player upon starting a new game
        try {
            player = new Player(this.playerImages, this.basicSounds, this.weaponSounds, this.bulletImages, (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50, this.rocks);
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
                zombies.add(new Zombie(this.zombieImages, this.zombieAudioClips, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100, rocks));
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        // Create the Node representation of these entities to the gameWindow if DEBUG is set to true
        if (isDEBUG()) {
            gameWindow.getChildren().add(player.getNode());
            for (Zombie zombie : zombies)
                gameWindow.getChildren().add(zombie.getNode());
            for (Rock rock : rocks)
                gameWindow.getChildren().add(rock.getSprite().getImageView());
        }

        // Add the ImageView of every Entity to the gameWindow pane
        gameWindow.getChildren().add(player.getAnimationHandler().getImageView());
        for (Zombie zombie : zombies) {
            gameWindow.getChildren().add(zombie.getAnimationHandler().getImageView());
        }

        // Initialize the Game object, and thus start the game
        game = new Game(player, zombies, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, rocks);
        game.setGameInitializer(this);
        //Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);


        //Gjør dette for å riktig kunne trykke esc for ingameMenu...
        game.restartGame();
        gameState.setVisible(false);
        ingameMenu.setVisible(false);

        game.clearGame();
    }

    public void launchNormalDifficulty(){
        hideDifficulty();
        game.restartNormalGame();
        ingameMenu.setVisible(false);
        gameState.setVisible(false);

        Platform.runLater(this::getKeyPressed);

    }

    public void launchHardDifficulty(){
        hideDifficulty();
        game.restartHardGame();
        ingameMenu.setVisible(false);
        gameState.setVisible(false);

        Platform.runLater(this::getKeyPressed);

    }

    public void launchInsaneDifficulty(){
        hideDifficulty();
        game.restartInsaneGame();
        ingameMenu.setVisible(false);
        gameState.setVisible(false);

        Platform.runLater(this::getKeyPressed);

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

            } else if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.E) {
                game.pauseGame();
                showMenu();

            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();

            } else if (e.getCode() == KeyCode.F5){
                game.saveGame("quicksave");
            } else if (e.getCode() == KeyCode.F9) {
                game.loadGame("quicksave");
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
     */
    private void loadAssets() {

        // Load all Player sounds and animations
        String[] playerSounds = {
                "/resources/Sound/Sound Effects/Player/player_breathing_calm.wav",
                "/resources/Sound/Sound Effects/Player/footsteps_single.wav"
        };

        this.basicSounds = loadAudio(playerSounds);

        String[] weaponSounds = {
                "/resources/Sound/Sound Effects/Player/Knife/knife_swish.mp3",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_shot.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_shot.wav",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_shot.wav",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_reload.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_empty.mp3"
        };

        this.weaponSounds = loadAudio(weaponSounds);

        FileParam[] knife = {
                new FileParam("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20),
                new FileParam("/resources/Art/Player/knife/move/survivor-move_knife_", ".png", 20),
                new FileParam("/resources/Art/Player/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)
        };

        FileParam[] pistol = {
                new FileParam("/resources/Art/Player/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new FileParam("/resources/Art/Player/handgun/move/survivor-move_handgun_", ".png", 20),
                new FileParam("/resources/Art/Player/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15),
                new FileParam("/resources/Art/Player/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new FileParam("/resources/Art/Player/handgun/reload/survivor-reload_handgun_", ".png", 15)
        };

        FileParam[] rifle = {
                new FileParam("/resources/Art/Player/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new FileParam("/resources/Art/Player/rifle/move/survivor-move_rifle_", ".png", 20),
                new FileParam("/resources/Art/Player/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15),
                new FileParam("/resources/Art/Player/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new FileParam("/resources/Art/Player/rifle/reload/survivor-reload_rifle_", ".png", 20)
        };

        FileParam[] shotgun = {
                new FileParam("/resources/Art/Player/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new FileParam("/resources/Art/Player/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new FileParam("/resources/Art/Player/shotgun/meleeattack/survivor-meleeattack_shotgun_", ".png", 15),
                new FileParam("/resources/Art/Player/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new FileParam("/resources/Art/Player/shotgun/reload/survivor-reload_shotgun_", ".png", 20)
        };

        FileParam[][] all = {knife, pistol, rifle, shotgun};

        this.playerImages = new Image[all.length][][];
        for (int i = 0; i < all.length; ++i) {
            this.playerImages[i] = loadAnimation(all[i]);
        }

        // Load all Zombie animations
        loadZombiesAssets();

        // Load coin Drop animation
        FileParam coin = new FileParam("/resources/Art/Icon/Coin/coin_rotate_", ".png", 6);

        this.coinDrop = loadAnimation(coin);

        // Load all Drop images
        String[] dropImageStrings = new String[] {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_boost.png",
                "/resources/Art/Icon/Coin/coin_rotate_0.png"};

        loadDropImages(dropImageStrings);

        // Load all Bullet images
        String[] bulletImageStrings = {
                "/resources/Art/pistol_bullet.png"};

        this.bulletImages = new Image[bulletImageStrings.length];

        for (int i = 0; i < bulletImageStrings.length; i++) {
            this.bulletImages[i] = new Image(bulletImageStrings[i], 25, 25, true, false);
        }
    }

    public void loadDropImages(String[] images) {
        this.hpDropImages = new Image[1];
        this.hpDropImages[0] = new Image(images[0], 25, 25, true, false);
        this.armorDropImages = new Image[1];
        this.armorDropImages[0] = new Image(images[1], 25, 25, true, false);
        this.magDropImages = new Image[1];
        this.magDropImages[0] = new Image(images[2], 25, 25, true, false);
        this.poolDropImages = new Image[1];
        this.poolDropImages[0] = new Image(images[3], 25, 25, true, false);
        this.speedDropImages = new Image[1];
        this.speedDropImages[0] = new Image(images[4], 25, 25, true, false);
        this.coinDropImages = new Image[1];
        this.coinDropImages[0] = new Image(images[5], 25, 25, true, false);
    }

    public Image[] getCoinDrop() {
        return coinDrop;
    }

    public Image[] getHpDropImages() {
        return hpDropImages;
    }

    public Image[] getArmorDropImages() {
        return armorDropImages;
    }

    public Image[] getMagDropImages() {
        return magDropImages;
    }

    public Image[] getPoolDropImages() {
        return poolDropImages;
    }

    public Image[] getSpeedDropImages() {
        return speedDropImages;
    }

    public Image[] getBulletImages() {
        return bulletImages;
    }

    private Image[] loadAnimation(FileParam files) {
        Image[] images = new Image[files.numberImages];
        for(int i = 0; i < files.numberImages; i++) {
            try {
                String filename = files.filename + Integer.toString(i) + files.extension;
                String resource = getClass().getResource(filename).toURI().toString();
                images[i] = new Image(resource, 25, 25, true, false);
            } catch (Exception e) {
                System.out.println(files.filename + Integer.toString(i) + files.extension);
                System.out.println("Error: Unable to find requested file(s) and the array Sprite.frames couldn't be created");
            }
        }
        return images;
    }

    private Image[][] loadAnimation(FileParam[] files) {
        Image[][] images = new Image[files.length][];
        for (int i = 0; i < files.length; ++i) {
            images[i] = new Image[files[i].numberImages];
            for (int j = 0; j < files[i].numberImages; ++j) {
                try {
                    String filename = files[i].filename + Integer.toString(j) + files[i].extension;
                    String resource = getClass().getResource(filename).toURI().toString();
                    images[i][j] = new Image(resource, 75, 75, true, false);
                } catch (Exception e) {
                    System.out.println(files[i].filename + Integer.toString(j) + files[i].extension);
                    System.out.println("Error: Unable to find requested file(s) and the array Sprite.frames couldn't be created");
                }
            }
        }
        return images;
    }

    private AnimationHandler[] loadMultipleAnimationHandler(Image[][] images, int nbr) {
        AnimationHandler[] animationHandler = new AnimationHandler[nbr];
        for (int i = 0; i < nbr; i++) {
            animationHandler[i] = new AnimationHandler(images);
        }
        return animationHandler;
    }

    /**
     * Method which finds and loads all necessary Zombie assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     */
    public void loadZombiesAssets() {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        FileParam[] zombieAnimations = {
                new FileParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new FileParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new FileParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        this.zombieAudioClips = loadAudio(zombieSounds);
        this.zombieImages = loadAnimation(zombieAnimations);
    }

    public Image[][] getZombieImages() {
        return this.zombieImages;
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

    public void hideDifficulty() {
        ingameChooseDifficulty.setVisible(false);
        ingameNormalDifficulty.setVisible(false);
        ingameHardDifficulty.setVisible(false);
        ingameInsaneDifficulty.setVisible(false);
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
        ingameMenu.setVisible(false);
        gameState.setVisible(false);
        game.clearGame();
        showDifficulty();
    }


    public void showDifficulty(){
        if (!difficultyVisible){
            ingameChooseDifficulty.setVisible(true);
            ingameNormalDifficulty.setVisible(true);
            ingameHardDifficulty.setVisible(true);
            ingameInsaneDifficulty.setVisible(true);
        }
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



    Stage windowSettings;
    Parent rootSettings;
    Scene sceneSettings;

    public void showSettings(ActionEvent event) throws IOException {

        musicPlayer.muteVolume();

        try {
            if (event.getSource() == settings){
                windowSettings = (Stage) settings.getScene().getWindow();
                rootSettings = FXMLLoader.load(getClass().getResource("../menuOptions/Settings.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        sceneSettings = new Scene(rootSettings, 1280, 720);
        windowSettings.setScene(sceneSettings);
        windowSettings.show();


        /*

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

        */
    }

    public void exitGame() {
        System.exit(0);
    }

    public boolean isMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(boolean menuVisible) {
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

    public AudioClip[] getZombieAudioClips() {
        return zombieAudioClips;
    }

    public AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    public AudioClip[] getWeaponSounds() {
        return weaponSounds;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    /**
     * Inner class used in conjunction with creating a 2-dimensional array of type Sprite.
     * Used to create smaller portions of the array, which then are combined with an
     * ImageView in order to create an object of type Sprite.
     */
    private class FileParam {
        String filename;
        String extension;
        int numberImages;

        private FileParam(String filename, String extension, int numberImages) {
            this.filename = filename;
            this.extension = extension;
            this.numberImages = numberImages;
        }
    }
}