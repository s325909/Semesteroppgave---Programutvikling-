package gameCode;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Class which handles all assets used in the Game.
 * Each asset is listed as Strings and then methods are run to create usable objects of type Image, AudioClip and MediaPlayer.
 */
class AssetsHandler {
    private MediaPlayer mediaPlayer;

    private AudioClip[] weaponSounds;
    private AudioClip[] basicSounds;
    private Image[][][] playerImages;

    private AudioClip[] zombieAudioClips;
    private Image[][] zombieImages;

    private AudioClip[] dropSounds;
    private Image[] hpDropImages;
    private Image[] armorDropImages;
    private Image[] pistolDropImages;
    private Image[] rifleDropImages;
    private Image[] shotgunDropImages;
    private Image[] scoreDropAnimation;

    private Image[] pistolBulletImaqe;
    private Image[] rifleBulletImage;
    private Image[] shotgunPelletImage;

    private Image[] rockImage;

    private double musicVolume;
    private double soundVolume;

    /**
     * Constructor which simply sets the starting volume values for music and sounds,
     * and runs the method which starts retrieving and loading assets.
     */
    AssetsHandler() {
        musicVolume = 0.15;
        soundVolume = 0.5;
        loadAssets();
    }

    /**
     * Method which finds and loads all necessary assets from disk only once.
     * Once found and loaded into memory, these sets of assets are then turned
     * into usable arrays which allows easy access without re-reading from disk.
     */
    private void loadAssets() {

        String musicFile = "src/resources/Sound/Soundtrack/Doom2.mp3";
        mediaPlayer = loadMusic(musicFile, musicVolume);


        ////////// Create Player's sounds. Turns Strings into usable AudioClips //////////
        String[] playerSounds = {
                "/resources/Sound/Sound Effects/Player/player_breathing_calm.wav",
                "/resources/Sound/Sound Effects/Player/2footsteps.wav",
                "/resources/Sound/Sound Effects/Player/soft_grunt.wav",
                "/resources/Sound/Sound Effects/Player/rough_grunt.wav",
                "/resources/Sound/Sound Effects/Player/death_grunt.wav"
        };

        String[] weaponSounds = {
                "/resources/Sound/Sound Effects/Player/Knife/knife_swish.mp3",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_shot.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_shot.wav",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_shot.wav",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_reload.wav",
                "/resources/Sound/Sound Effects/Player/weapon_empty.mp3"
        };

        ////////// Create Player's animations. Combines several arrays into one //////////
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

        basicSounds = loadAudio(playerSounds, soundVolume);
        this.weaponSounds = loadAudio(weaponSounds, soundVolume);

        playerImages = new Image[all.length][][];
        for (int i = 0; i < all.length; ++i) {
            playerImages[i] = loadAnimation(all[i]);
        }


        // Create Zombie's animations
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walk.aiff",
                "/resources/Sound/Sound Effects/Zombie/zombie_hit_1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_hit_2.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_death.mp3"
        };

        FileParam[] zombieAnimations = {
                new FileParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new FileParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new FileParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)
        };

        zombieAudioClips = loadAudio(zombieSounds, soundVolume);
        zombieImages = loadAnimation(zombieAnimations);


        ////////// Create all of Drop's images and animations //////////
        String[] dropSounds = {
                "/resources/Sound/Sound Effects/Drop/coin_pickup.wav",
                "/resources/Sound/Sound Effects/Drop/health_pickup.wav",
                "/resources/Sound/Sound Effects/Drop/armor_pickup.wav",
                "/resources/Sound/Sound Effects/Drop/ammo_pickup.wav"
        };

        FileParam scoreDrop = new FileParam(
                "/resources/Art/Icon/Coin/coin_rotate_", ".png", 6
        );

        String[] dropImageStrings = new String[]{
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/pistol_ammo.png",
                "/resources/Art/Icon/rifle_ammo.png",
                "/resources/Art/Icon/shotgun_ammo.png"
        };

        this.dropSounds = loadAudio(dropSounds, soundVolume);
        scoreDropAnimation = loadAnimation(scoreDrop);

        hpDropImages = new Image[1];
        hpDropImages[0] = new Image(dropImageStrings[0], 25, 25, true, false);
        armorDropImages = new Image[1];
        armorDropImages[0] = new Image(dropImageStrings[1], 25, 25, true, false);
        pistolDropImages = new Image[1];
        pistolDropImages[0] = new Image(dropImageStrings[2], 25, 25, true, false);
        rifleDropImages = new Image[1];
        rifleDropImages[0] = new Image(dropImageStrings[3], 25, 25, true, false);
        shotgunDropImages = new Image[1];
        shotgunDropImages[0] = new Image(dropImageStrings[4], 25, 25, true, false);



        ////////// Create all of Bullet's images //////////
        String[] bulletImageStrings = {
                "/resources/Art/Icon/pistol_bullet.png",
                "/resources/Art/Icon/single_rifle_bullet.png"
        };

        pistolBulletImaqe = new Image[1];
        pistolBulletImaqe[0] = new Image(bulletImageStrings[0], 25, 25, true, false);
        rifleBulletImage = new Image[1];
        rifleBulletImage[0] = new Image(bulletImageStrings[1], 25, 25, true, false);


        ////////// Create all of Rock's images //////////
        String[] rockImageStrings = {
                "/resources/Art/Icon/rock.png"
        };

        rockImage = new Image[1];
        rockImage[0] = new Image(rockImageStrings[0], 50, 50, true, false);
    }

    private MediaPlayer loadMusic(String fileName, double volume) {
        MediaPlayer mp = null;
        try {
            File file = new File(fileName);
            Media media = new Media(file.toURI().toString());
            mp = new MediaPlayer(media);
            mp.setVolume(volume);
            return mp;
        } catch (Exception e) {
            System.out.println("Error: Could not find sound file");
        }
        return mp;
    }

    /**
     * Method which turns an array of Strings into AudioClips, and adjusts the volume.
     * @param audioFiles Requires an array of type String, which must contain a valid sound files.
     * @return Returns an array of type AudioClip, which may then be applied to each Entity.
     */
    private AudioClip[] loadAudio(String[] audioFiles, double volume) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for(int i = 0; i < clips.length; i++) {
            try {
                clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
                clips[i].setVolume(volume);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return clips;
    }

    /**
     * Method which turns a temporary FileParam object into an Image array.
     * The object contains the start of the filename, the number of Images to create, and the file extension.
     * This is then used to create a new Image of each of these files according to the number of Images that the object provides,
     * and each Image is placed within an array. numberImages variable is used to set number of iterations, and each iteration
     * is used a a suffix to complete the Images full filenames together with the extension.
     * @param files Requires a FileParam object.
     * @return Returns an array of type Image.
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
     * Method which turns a temporary FileParam array into a 2-dimensional Image array.
     * Each FileParam object contains the start of the filename, the number of Images to create, and the file extension.
     * This is then used to create a new Image of each of these files according to the number of Images that the object provides,
     * and each Image is placed within an array. numberImages variable is used to set number of iterations, and each iteration
     * is used a a suffix to complete the Images full filenames together with the extension.
     * @param files Requires a FileParam array.
     * @return Returns a 2-dimensional array of type Image.
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
     * Method for creating Image arrays and filling them with a single Image.
     * @param imageFile Requires the name of the file to create an Image of.
     * @param width Requires the desired width of the Image.
     * @param height Requires the desired height of the Image.
     * @return Returns an array of type Image and length 1.
     */
    private Image[] createImage(String imageFile, int width, int height) {
        Image[] image = {new Image((imageFile), width, height, true, false)};
        return image;
    }

    /**
     * Method for displaying a simple message to the user during an Exception when retrieving files.
     */
    private void fileAlert() {

        ButtonType resume = new ButtonType("Resume", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(resume);
        alert.setHeaderText(null);

        alert.setTitle("Assets loading error");
        alert.contentTextProperty().set("Unable to find assets");

        alert.showAndWait().ifPresent(response -> {
            if (response == resume) {
                System.out.println("Game resumed");
            }
        });
    }

    MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    AudioClip[] getWeaponSounds() {
        return weaponSounds;
    }

    Image[][][] getPlayerImages() {
        return playerImages;
    }

    AudioClip[] getZombieAudioClips() {
        return zombieAudioClips;
    }

    Image[][] getZombieImages() {
        return this.zombieImages;
    }

    Image[] getPistolBulletImaqe() {
        return pistolBulletImaqe;
    }

    Image[] getRifleBulletImage() {
        return rifleBulletImage;
    }

    Image[] getShotgunPelletImage() {
        return shotgunPelletImage;
    }

    AudioClip[] getDropSounds() {
        return dropSounds;
    }

    Image[] getScoreDropAnimation() {
        return scoreDropAnimation;
    }

    Image[] getHpDropImages() {
        return hpDropImages;
    }

    Image[] getArmorDropImages() {
        return armorDropImages;
    }

    Image[] getPistolDropImages() {
        return pistolDropImages;
    }

    Image[] getRifleDropImages() {
        return rifleDropImages;
    }

    Image[] getShotgunDropImages() {
        return shotgunDropImages;
    }

    Image[] getRockImage() { return rockImage; }

    double getMusicVolume() {
        return musicVolume;
    }

    void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    double getSoundVolume() {
        return soundVolume;
    }

    void setSoundVolume(double soundVolume) {
        this.soundVolume = soundVolume;
    }

    /**
     * Inner class used in conjunction with creating Image arrays for use in AnimationHandler.
     * It contains the part of the filename, the file extension, and the number of Images.
     * The number is then used for iteration, whereas the iteration number serves as a suffix
     * to complete the full filename combined with the extension (as in file_(index).png).
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
