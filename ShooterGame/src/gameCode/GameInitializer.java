package gameCode;

import entities.Player;
import entities.Rock;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GameInitializer implements Initializable{

    @FXML private Pane gameWindow;
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, roundNbr, gameState, pressKey;
    @FXML private VBox gamePaused, ingameMenu, ingameHelp, ingameNormalDifficulty, ingameHardDifficulty, ingameInsaneDifficulty;
    @FXML private HBox ingameChooseDifficulty;
    @FXML private Button back_Help, settings;
    @FXML private Button normalDifficulty, hardDifficulty, insaneDifficulty;

    private Player player;
    private List<Rock> rocks;
    private Game game;
    private MusicPlayer musicPlayer;
    private boolean menuVisible;
    private boolean helpVisible;
    private boolean labelVisible;

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
    private Image[] scoreDropAnimation;
    private Image[] pistolBulletImaqe;
    private Image[] rockImage;

    final private boolean DEBUG = true;

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

        loadAssets();
        createRocks();
        selectDifficulty();
    }

    /**
     * Method for selecting the Game's difficulty, through startGame() method call, which then creates the other elements in the Game.
     */
    @FXML
    private void selectDifficulty(){
        normalDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.NORMAL);
        });
        hardDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.HARD);
        });
        insaneDifficulty.setOnAction(event->{
            startGame(Game.Difficulty.INSANE);
        });
    }

    /**
     * Method which creates the Player, and finally the Game can be instantiated.
     * @param difficulty Requires the user selected difficulty in order to adjust the Game.
     */
    private void startGame(Game.Difficulty difficulty) {
        createPlayer(difficulty);
        game = new Game(difficulty, player, rocks, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer);
        game.setGameInitializer(this);
        showDifficulty(false);
        Platform.runLater(this::getKeyPressed);
    }

    /**
     * Method which creates and draws the Player object for use in the Game.
     */
    private void createPlayer(Game.Difficulty difficulty) {
        try {
            player = new Player(this.playerImages, this.basicSounds, this.weaponSounds, this.pistolBulletImaqe, (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50);
            player.resetPlayer(difficulty);
        } catch (Exception e) {
            System.out.println("Error: Player did not load correctly");
        }

        // Draw Player to the Pane
        if(isDEBUG())
            gameWindow.getChildren().add(player.getNode());
        gameWindow.getChildren().add(player.getAnimationHandler().getImageView());
    }

    /**
     * Method which creates and draws the Rock objects for use in the Game.
     */
    private void createRocks() {
        try {
            rocks = new ArrayList<>();
            rocks.add(new Rock(rockImage, 240, 400));
            rocks.add(new Rock(rockImage, 300, 500));
            rocks.add(new Rock(rockImage, 151, 151));
            rocks.add(new Rock(rockImage, 500, 500));
            rocks.add(new Rock(rockImage, 800, 100));
            rocks.add(new Rock(rockImage, 700, 300));
            rocks.add(new Rock(rockImage, 1000, 500));
            rocks.add(new Rock(rockImage, 900, 800));
            rocks.add(new Rock(rockImage, 1200, 1000));
            rocks.add(new Rock(rockImage, 600, 450));
            rocks.add(new Rock(rockImage, 1200, 100));
            rocks.add(new Rock(rockImage, 940, 400));
            rocks.add(new Rock(rockImage, 400, 250));
            rocks.add(new Rock(rockImage, 200, 40));
            rocks.add(new Rock(rockImage, 500, 40));
        } catch (Exception e) {
            System.out.println("Error: Rocks did not load correctly");
        }

        // Draw rocks to the Pane
        for (Rock rock : rocks) {
            if (isDEBUG())
                gameWindow.getChildren().add(rock.getNode());
            gameWindow.getChildren().add(rock.getAnimationHandler().getImageView());
        }
    }

    /***
     * Method which handles user input.
     * pressEvent() method call in Player handles movement of the Player object itself.
     */
    private void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.pressEvent(e);
            if (e.getCode() == KeyCode.BACK_SPACE) {
                game.removeZombies();

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
            player.releasedEvent(e);
        });
    }

    /**
     * Method which displays Labels to the user based on the appropriate state.
     * These include Game Over, Game Won, and Paused.
     */
    protected void showGameLabel() {
        if(!labelVisible) {
            gamePaused.setVisible(true);
            gameState.setVisible(true);
            labelVisible = true;
            if(game.isGameOver() && !game.isNewRound()) {
                gameState.setText("GAME OVER!");
                gameState.setTextFill(Color.INDIANRED);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            } else if (game.isGameOver() && game.isNewRound()) {
                gameState.setText("GAME WON!");
                gameState.setTextFill(Color.DARKGREEN);
                pressKey.setVisible(true);
                pressKey.setText("Press ESC to continue");
            }else {
                gameState.setText("GAME IS PAUSED");
                gameState.setTextFill(Color.WHITE);
            }
        } else {
            gamePaused.setVisible(false);
            gameState.setVisible(false);
            pressKey.setVisible(false);
            labelVisible = false;
        }
    }

    /**
     * Method which will open the in-game menu.
     * It sets a hidden VBox to visible.
     */
    protected void showMenu() {
        if(!menuVisible) {
            ingameMenu.setVisible(true);
            menuVisible = true;
        } else {
            ingameMenu.setVisible(false);
            menuVisible = false;
        }
    }

    public void hideHelp(){
        ingameHelp.setVisible(false);
        ingameMenu.setVisible(true);
        gameState.setVisible(true);
    }

    /**
     * Method which will resume the game
     */
    @FXML
    public void resumeGame(){
        game.pauseGame();
        showMenu();
    }

    @FXML
    public void restartGame() {
        game.restartGame();
        showMenu();
        showGameLabel();
        showDifficulty(true);
    }

    @FXML
    public void exitGame() {
        System.exit(0);
    }

    public void showDifficulty(boolean show){
        if (show){
            ingameChooseDifficulty.setVisible(true);
            ingameNormalDifficulty.setVisible(true);
            ingameHardDifficulty.setVisible(true);
            ingameInsaneDifficulty.setVisible(true);
        } else {
            ingameChooseDifficulty.setVisible(false);
            ingameNormalDifficulty.setVisible(false);
            ingameHardDifficulty.setVisible(false);
            ingameInsaneDifficulty.setVisible(false);
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

        //musicPlayer.muteVolume();

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

    @FXML Button loadGame;
    Stage windowLoading;
    Parent rootLoading;
    Scene sceneLoading;
    public void showLoadMenu(ActionEvent event) throws IOException {
        try {
            if(event.getSource() == loadGame) {
                windowLoading = (Stage) loadGame.getScene().getWindow();
                rootLoading = FXMLLoader.load(getClass().getResource("../menuOptions/Loading.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene loadScene = new Scene(rootLoading, 1280, 720);
        windowLoading.setScene(loadScene);
        windowLoading.show();
    }



    /**
     * Method which finds and loads all necessary assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     */
    private void loadAssets() {

        // Create Player's sounds. Turns Strings into usable AudioClips
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

        // Create Player's animations. Combines several arrays into one
        FileParam[] knife = {
                new FileParam("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20),
                new FileParam("/resources/Art/Player/knife/move/survivor-move_knife_", ".png", 20),
                new FileParam("/resources/Art/Player/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)
        };

        FileParam[] pistol = {
                new FileParam("/resources/Art/Player/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new FileParam("/resources/Art/Player/handgun/move/survivor-move_handgun_", ".png", 20),
                new FileParam("/resources/Art/Player/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new FileParam("/resources/Art/Player/handgun/reload/survivor-reload_handgun_", ".png", 15)
        };

        FileParam[] rifle = {
                new FileParam("/resources/Art/Player/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new FileParam("/resources/Art/Player/rifle/move/survivor-move_rifle_", ".png", 20),
                new FileParam("/resources/Art/Player/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new FileParam("/resources/Art/Player/rifle/reload/survivor-reload_rifle_", ".png", 20)
        };

        FileParam[] shotgun = {
                new FileParam("/resources/Art/Player/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new FileParam("/resources/Art/Player/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new FileParam("/resources/Art/Player/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new FileParam("/resources/Art/Player/shotgun/reload/survivor-reload_shotgun_", ".png", 20)
        };

        FileParam[][] all = {
                knife,
                pistol,
                rifle,
                shotgun
        };

        this.playerImages = new Image[all.length][][];
        for (int i = 0; i < all.length; ++i) {
            this.playerImages[i] = loadAnimation(all[i]);
        }

        // Create Zombie's animations
        loadZombiesAssets();

        // Create Drop's animations
        FileParam scoreDrop = new FileParam("/resources/Art/Icon/Coin/coin_rotate_", ".png", 6);
        this.scoreDropAnimation = loadAnimation(scoreDrop);

        // Create all of Drop's images
        String[] dropImageStrings = new String[] {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_boost.png"
        };
        createDropImages(dropImageStrings);

        // Create all of Bullet's images
        String[] bulletImageStrings = {
                "/resources/Art/pistol_bullet.png"
        };
        this.pistolBulletImaqe = new Image[1];
        this.pistolBulletImaqe[0] = new Image(bulletImageStrings[0], 25, 25, true, false);

        // Create all of Rock's images
        String[] rockImageStrings = {
                "/resources/Art/rock.png"
        };
        this.rockImage = new Image[1];
        this.rockImage[0] = new Image(rockImageStrings[0], 50, 50, true, false);
    }

    /**
     *
     * @param files
     * @return
     */
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

    /**
     * Method which
     * @param files
     * @return
     */
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

    /**
     * Method which finds and loads all necessary Zombie assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     */
    private void loadZombiesAssets() {
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

    /**
     * Method which turns an array of type String into sets of Images, where each Image is put into single arrays of length 1.
     * @param images Requires an array of type String, and uses each of these Strings to create an Image.
     */
    private void createDropImages(String[] images) {
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
    }

    /**
     * Method which turns an array of Strings into AudioClips, and adjusts the volume.
     * @param audioFiles Requires an array of type String, which must contain a valid sound files.
     * @return Returns an array of type AudioClip, which may then be applied to each Entity.
     */
    private AudioClip[] loadAudio(String[] audioFiles) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for(int i = 0; i < clips.length; i++) {
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
            clips[i].setVolume(0.1);
        }
        return clips;
    }

    public Image[][] getZombieImages() {
        return this.zombieImages;
    }

    public Image[] getPistolBulletImaqe() {
        return pistolBulletImaqe;
    }

    public Image[] getScoreDropAnimation() {
        return scoreDropAnimation;
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