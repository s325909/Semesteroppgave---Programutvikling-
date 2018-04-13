package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

enum WeaponTypes {
    KNIFE, PISTOL, RIFLE, SHOTGUN
}

public class Player extends Movable {

    private WeaponTypes equippedWeapon;

    private AudioClip[] weapon;

    private Sprite[][] allAnimation;

    public Player(){}

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);

        String[] footsteps = {
                "/resources/Sound/Sound Effects/footsteps_single.wav"};
        String[] weaponSounds = {
                "/resources/Sound/Sound Effects/knife_swish.mp3",
                "/resources/Sound/Sound Effects/pistol_shot.wav",
                "/resources/Sound/Sound Effects/pistol_reload.mp3",
                "/resources/Sound/Sound Effects/rifle_shot.wav",
                "/resources/Sound/Sound Effects/rifle_reload.mp3",
                "/resources/Sound/Sound Effects/shotgun_shot.wav",
                "/resources/Sound/Sound Effects/shotgun_reload.wav"};

        SpriteParam[] knife = {
                new SpriteParam("/resources/Art/Survivor/knife/idle/survivor-idle_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/knife/move/survivor-move_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)};
        SpriteParam[] pistol = {
                new SpriteParam("/resources/Art/Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/handgun/move/survivor-move_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15),
                new SpriteParam("/resources/Art/Survivor/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new SpriteParam("/resources/Art/Survivor/handgun/reload/survivor-reload_handgun_", ".png", 15)};
        SpriteParam[] rifle = {
                new SpriteParam("/resources/Art/Survivor/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/rifle/move/survivor-move_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15),
                new SpriteParam("/resources/Art/Survivor/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new SpriteParam("/resources/Art/Survivor/rifle/reload/survivor-reload_rifle_", ".png", 20)};
        SpriteParam[] shotgun = {
                new SpriteParam("/resources/Art/Survivor/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Survivor/shotgun/meleeattack/survivor-meleeattack_shotgun_", ".png", 15),
                new SpriteParam("/resources/Art/Survivor/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new SpriteParam("/resources/Art/Survivor/shotgun/reload/survivor-reload_shotgun_", ".png", 20)};
        SpriteParam[][] all = {knife, pistol, rifle, shotgun};

        loadFootsteps(footsteps);
        loadWeaponSounds(weaponSounds);
        loadWeaponSprite(all);

        playerAnimation("knife");
        setAnimation(0,0);
    }

    private void loadWeaponSprite(SpriteParam[][] sprites) {
        Sprite[][] outerSprite = new Sprite[sprites.length][];

        for (int i = 0; i < sprites.length; i++) {
            outerSprite[i] = loadSprites(sprites[i]);
        }

        this.allAnimation = outerSprite;

        double maxWidth = -1;
        double maxHeight = -1;

        for(int i = 0; i < this.allAnimation.length; i++) {
            for (int j = 0; j < this.allAnimation[i].length; j++) {
                if (this.allAnimation[i][j].getWidth() > maxWidth && this.allAnimation[i][j].getHeight() > maxHeight) {
                    maxWidth = this.allAnimation[i][j].getWidth();
                    maxHeight = this.allAnimation[i][j].getHeight();
                }
            }
        }

        for(int i = 0; i < this.allAnimation.length; i++) {
            for (int j = 0; j < this.allAnimation[i].length; j++) {
                this.allAnimation[i][j].setMax(maxWidth, maxHeight);
            }
        }

        super.getSprite().setMax(maxWidth,maxHeight);
        System.out.println(maxWidth);
        System.out.println(maxHeight);
    }

    public void playerAnimation(String animationWanted) {
        if (animationWanted == "knife") {
            this.equippedWeapon = WeaponTypes.KNIFE;
        } else if (animationWanted == "pistol") {
            this.equippedWeapon = WeaponTypes.PISTOL;
        } else if (animationWanted == "rifle") {
            this.equippedWeapon = WeaponTypes.RIFLE;
        } else if (animationWanted == "shotgun") {
            this.equippedWeapon = WeaponTypes.SHOTGUN;
        } else {
            this.equippedWeapon = WeaponTypes.KNIFE;
        }
    }

    public void loadWeaponSounds(String[] audioFiles) {
        this.weapon = loadAudio(audioFiles);
    }

    public void playWeapon(int i) {
        this.weapon[i].play();
    }

    public void setAnimation(int i, int j) {
        super.setSprite(this.allAnimation[i][j]);
    }

    /***
     * Method for turning player keyboard input into movement on the screen
     * @param keyEvent
     */
    public void movePlayer(KeyEvent keyEvent){
        int i,j, audioAction, audioReload;

        switch (this.equippedWeapon) {
            case KNIFE:
                i = 0;
                audioAction = 0;
                audioReload = 0;
                break;
            case PISTOL:
                i = 1;
                audioAction = 1;
                audioReload = 2;
                break;
            case RIFLE:
                i = 2;
                audioAction = 3;
                audioReload = 4;
                break;
            case SHOTGUN:
                i = 3;
                audioAction = 5;
                audioReload = 6;
                break;
            default:
                i = 0;
                audioAction = 0;
                audioReload = 0;
        }

        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            j = 1;
            goLeft();
            playFootStep();
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            j = 1;
            goRight();
            playFootStep();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            j = 1;
            goUp();
            playFootStep();
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            j = 1;
            goDown();
            playFootStep();
        } else {
            j = 0;
        }

        if (keyEvent.getCode() == KeyCode.E || (keyEvent.getCode() == KeyCode.SPACE && equippedWeapon == WeaponTypes.KNIFE)) {
            j = 2;
        } else if (keyEvent.getCode() == KeyCode.SPACE && equippedWeapon != WeaponTypes.KNIFE) {
            playWeapon(audioAction);
            j = 3;
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != WeaponTypes.KNIFE) {
            playWeapon(audioReload);
            j = 4;
        }
        setAnimation(i, j);
    }

    /***
     * Method for stopping movement upon key released
     * @param keyEvent
     */
    public void releasedPlayer(KeyEvent keyEvent){
        int i,j=0;
        switch (this.equippedWeapon) {
            case KNIFE:
                i = 0;
                break;
            case PISTOL:
                i = 1;
                break;
            case RIFLE:
                i = 2;
                break;
            case SHOTGUN:
                i = 3;
                break;
            default:
                i = 0;
        }
        setAnimation(i, j);
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT
                || keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.D) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN
                || keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.S) {
            stopY();
        }
    }
}