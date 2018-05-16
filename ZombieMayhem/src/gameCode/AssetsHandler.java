package gameCode;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AssetsHandler {
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

    public AssetsHandler() {
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

        String[] dropImageStrings = new String[] {
                "/resources/Art/Icon/hp_icon.png",
                "/resources/Art/Icon/armor_icon.png",
                "/resources/Art/Icon/pistol_ammo.png",
                "/resources/Art/Icon/rifle_ammo.png",
                "/resources/Art/Icon/shotgun_ammo.png"
        };

        this.dropSounds = loadAudio(dropSounds, soundVolume);
        scoreDropAnimation = loadAnimation(scoreDrop);
        hpDropImages = createImage(dropImageStrings[0], 25, 25);
        armorDropImages = createImage(dropImageStrings[1], 25, 25);
        pistolDropImages = createImage(dropImageStrings[2], 25, 25);
        rifleDropImages = createImage(dropImageStrings[3], 25, 25);
        shotgunDropImages = createImage(dropImageStrings[4], 25, 25);



        ////////// Create all of Bullet's images //////////
        String[] bulletImageStrings = {
                "/resources/Art/Icon/pistol_bullet.png",
                "/resources/Art/Icon/single_rifle_bullet.png"
        };

        pistolBulletImaqe = createImage(bulletImageStrings[0], 25, 25);
        rifleBulletImage = createImage(bulletImageStrings[1], 25 ,25);



        ////////// Create all of Rock's images //////////
        String[] rockImageStrings = {
                "/resources/Art/Icon/rock.png"
        };

        rockImage = createImage(rockImageStrings[0], 50, 50);
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
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
            clips[i].setVolume(volume);
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

    public Image[] createImage(String imageFile, int width, int height) {
        Image[] image = {new Image((imageFile), width, height, true, false)};
        return image;
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
        this.pistolDropImages = new Image[1];
        this.pistolDropImages[0] = new Image(images[2], 25, 25, true, false);
        this.rifleDropImages = new Image[1];
        this.rifleDropImages[0] = new Image(images[3], 25, 25, true, false);
        this.shotgunDropImages = new Image[1];
        this.shotgunDropImages[0] = new Image(images[4], 25, 25, true, false);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }



    public AudioClip[] getBasicSounds() {
        return basicSounds;
    }

    public AudioClip[] getWeaponSounds() {
        return weaponSounds;
    }

    public Image[][][] getPlayerImages() {
        return playerImages;
    }

    AudioClip[] getZombieAudioClips() {
        return zombieAudioClips;
    }

    Image[][] getZombieImages() {
        return this.zombieImages;
    }

    public Image[] getPistolBulletImaqe() {
        return pistolBulletImaqe;
    }

    public Image[] getRifleBulletImage() {
        return rifleBulletImage;
    }

    public Image[] getShotgunPelletImage() {
        return shotgunPelletImage;
    }

    public AudioClip[] getDropSounds() {
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

    public Image[] getPistolDropImages() {
        return pistolDropImages;
    }

    public Image[] getRifleDropImages() {
        return rifleDropImages;
    }

    public Image[] getShotgunDropImages() {
        return shotgunDropImages;
    }

    public Image[] getRockImage() { return rockImage; }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(double soundVolume) {
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
