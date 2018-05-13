package notCurrentlyUsed;

import entities.AnimationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Assets implements Initializable {

    private AudioClip[] weaponSounds;
    private AudioClip[] basicSounds;
    private Sprite[][] playerAnimation;
    private AudioClip[] zombieAudioClips;
    private Sprite[][] zombieAnimation;
    private AnimationHandler[] zombieAnimationer;
    private Image[][] zombieImages;
    private String[] dropImages;
    private Sprite[] bulletImages;
    private Sprite[] coin;

    @FXML
    Button continueToGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load all Player sounds and animations
        String[] playerSounds = {
                "/resources/Sound/Sound Effects/Player/player_breathing_calm.wav",
                "/resources/Sound/Sound Effects/Player/footsteps_single.wav"
        };

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

        SpriteParam[] knife = {
                new SpriteParam("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/move/survivor-move_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)
        };

        SpriteParam[] pistol = {
                new SpriteParam("/resources/Art/Player/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/move/survivor-move_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/handgun/reload/survivor-reload_handgun_", ".png", 15)
        };

        SpriteParam[] rifle = {
                new SpriteParam("/resources/Art/Player/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/move/survivor-move_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15),
                new SpriteParam("/resources/Art/Player/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new SpriteParam("/resources/Art/Player/rifle/reload/survivor-reload_rifle_", ".png", 20)
        };

        SpriteParam[] shotgun = {
                new SpriteParam("/resources/Art/Player/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/meleeattack/survivor-meleeattack_shotgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/shotgun/reload/survivor-reload_shotgun_", ".png", 20)
        };

        SpriteParam[][] all = {
                knife,
                pistol,
                rifle,
                shotgun
        };

        this.basicSounds = loadAudio(playerSounds);
        this.weaponSounds = loadAudio(weaponSounds);
        this.playerAnimation = loadSprites(all);

        // Load all Zombie animations
        loadZombiesAssets(10);

        // Load coin Drop animation
        ImageView iv = new ImageView();
        this.coin = new Sprite[]{new Sprite(iv, "/resources/Art/Icon/Coin/coin_rotate_", ".png", 6)};

        // Load all Drop images
        this.dropImages = new String[]{
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/mag_icon.png",
                "/resources/Art/Icon/pool_icon.png",
                "/resources/Art/Icon/speed_boost.png",
                "/resources/Art/Icon/Coin/coin_rotate_0.png"
        };


        // Load all Bullet images
        String[] bulletImages = {
                "/resources/Art/Icon/pistol_bullet.png"
        };

        this.bulletImages = loadSingleSprites(bulletImages);
    }

    @FXML
    public void continueToGame() {
        try{
            Stage stage = (Stage) continueToGame.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Method which finds and loads all necessary Zombie assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     *
     * @param nbrZombies Requires the number of Zombie objects to create.
     */
    public void loadZombiesAssets(int nbrZombies) {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"
        };

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)
        };

        this.zombieAudioClips = loadAudio(zombieSounds);
        this.zombieImages = loadAnimation(zombieAnimations);
        this.zombieAnimationer = loadAnimations(this.zombieImages, nbrZombies);
        //this.zombieAnimation = loadSprites(this.zombieImages, nbrZombies);

    }

    private Image[][] loadAnimation(SpriteParam[] sprites) {
        Image[][] images = new Image[sprites.length][];
        for (int i = 0; i < sprites.length; ++i) {
            images[i] = new Image[sprites[i].numberImages];
            for (int j = 0; j < sprites[i].numberImages; ++j) {
                try {
                    String filename = sprites[i].filename + Integer.toString(j) + sprites[i].extension;
                    String resource = getClass().getResource(filename).toURI().toString();
                    images[i][j] = new Image(resource, 75, 75, true, false);
                } catch (Exception e) {
                    System.out.println(sprites[i].filename + Integer.toString(j) + sprites[i].extension);
                    System.out.println("Error: Unable to find requested file(s) and the array Sprite.frames couldn't be created");
                }
            }
        }
        return images;
    }

    private AnimationHandler[] loadAnimations(Image[][] images, int nbr) {
        AnimationHandler[] animationHandler = new AnimationHandler[nbr];
        for (int i = 0; i < nbr; i++) {
            animationHandler[i] = new AnimationHandler(images);
        }
        return animationHandler;
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
     *
     * @param nbrZombies Requires the number of zombies that should be created.
     * @param sprites    Requires a set of animations created in type SpriteParam[].
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
     * @param files g
     * @return g
     */
    private Sprite[] loadSingleSprites(String[] files) {
        Sprite[] sprites = new Sprite[files.length];
        ImageView iv = new ImageView();
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(iv, files[i]);
        }
        return sprites;
    }

    /**
     * Method which is used for turning a SpriteParam array and ImageView into a Sprite array.
     *
     * @param spriteParam Requires an array of type SpriteParam.
     * @param iv          Requires an ImageView object.
     * @return Returns an array of type Sprite.
     */
    private Sprite[] prepareSprites(SpriteParam[] spriteParam, ImageView iv) {
        Sprite[] sprites = new Sprite[spriteParam.length];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(iv, spriteParam[i].filename, spriteParam[i].extension, spriteParam[i].numberImages);
        }
        return sprites;
    }

    /**
     * @param audioFiles f
     * @return f
     */
    private AudioClip[] loadAudio(String[] audioFiles) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for (int i = 0; i < clips.length; i++) {
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
            clips[i].setVolume(0.1);
        }
        return clips;
    }

    public AudioClip[] getWeaponSounds() {
        return weaponSounds;
    }

    public AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    public Sprite[][] getPlayerAnimation() {
        return playerAnimation;
    }

    public AudioClip[] getZombieAudioClips() {
        return zombieAudioClips;
    }

    public Sprite[][] getZombieAnimation() {
        return zombieAnimation;
    }

    public AnimationHandler[] getZombieAnimationer() {
        return zombieAnimationer;
    }

    public String[] getDropImages() {
        return dropImages;
    }

    public Sprite[] getBulletImages() {
        return bulletImages;
    }

    public Sprite[] getCoin() {
        return coin;
    }

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


