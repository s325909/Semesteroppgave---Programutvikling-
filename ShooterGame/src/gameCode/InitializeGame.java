package gameCode;

import entities.Player;
import entities.Zombie;
import entities.Sprite;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class InitializeGame implements Initializable{

    @FXML private Pane gameWindow;
    @FXML protected Label hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer, gameState, pressKey, pressKey2, pressKey3;

    private Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies;
    private int nbrZombies;
    private Game game;
    private MusicPlayer musicPlayer;
    private boolean labelActive;

    private SceneSizeChangeListener sceneChange;

    private AudioClip[] weapon;
    private AudioClip[] basicSounds;
    private Sprite[][] playerAnimation;
    private AudioClip[] zombieAudioClips;
    private Sprite[][] zombieAnimation;
    private String[] hudIcons;
    private String[] bulletImages;

    final private boolean DEBUG = true;

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

        setNbrZombies(10);
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
        game = new Game(player, zombies, gameWindow, hudHP, hudArmor, hudWeapon, hudMag, hudPool, hudScore, hudTimer);
        game.setInitGame(this);
        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
    }

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
                game.saveTheGame("quicksave");

            } else if (e.getCode() == KeyCode.F9) {
                game.loadTheGame("quicksave");
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
            if (e.getCode() == KeyCode.R)
                game.setHoldingButtonR(false);
        });
    }

    /**
     * Method which finds and loads all necessary assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     * @param nbrZombies Requires the number of Zombie objects to create.
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

        String[] hudIcons = {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_boost.png"};

        //this.hudIcons = ;

        String[] bulletImages = {
                "/resources/Art/pistol_bullet.png"};

        loadZombiesAssets(nbrZombies);
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

//    private Sprite[] loadSingleSprites(String[] images) {
//        Sprite[] sprites =
//    }

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
    protected void showGameLabel(Boolean isGameOver, boolean visible) {
        if(visible) {
            setLabelActive(true);
            gameState.setVisible(true);
            pressKey.setVisible(true);
            pressKey2.setVisible(true);
            pressKey3.setVisible(true);
        } else {
            setLabelActive(false);
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
        } else {
            stage.setFullScreen(true);
        }
    }

    public void restartGame() {
        game.restartGame();
    }

    public void exitGame() {
        System.exit(0);
    }

    public boolean isLabelActive() {
        return labelActive;
    }

    public void setLabelActive(boolean labelActive) {
        this.labelActive = labelActive;
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

    public AudioClip[] getWeapon() {
        return weapon;
    }

    /***
     * Inner class used for taking smaller portions of Sprites
     * and combine these into a larger 2-dimensional array of type Sprite.
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