package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;

public class Player extends Movable {

    private WeaponTypes equippedWeapon;
    private AudioClip[] weapon;
    private AudioClip[] basicSounds;
    private Sprite[][] allAnimation;

    private List<Bullet> pistolBullets = new ArrayList<Bullet>();
    private List<Bullet> rifleBullets = new ArrayList<Bullet>();
    private List<Bullet> shotgunBullets = new ArrayList<Bullet>();

    private Magazine magazinePistol;
    private Magazine magazineRifle;
    private Magazine magazineShotgun;

    public Player(){}

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 5.0);

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
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_reload.wav"};

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

        loadBasicSounds(playerSounds);
        loadWeaponSounds(weaponSounds);
        loadWeaponSprite(all);

        playerAnimation("knife");
        setAnimation(0,0);

        magazinePistol = new Magazine(15, 30);
        magazineRifle = new Magazine(30,90);
        magazineShotgun = new Magazine(8,32);
    }

    /***
     * Method which is used for loading all the various weapon sprites into a 2-dimensional array.
     * @param sprites Requires a 2-dimensional array of type Sprite
     */
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
        //System.out.println(maxWidth);
        //System.out.println(maxHeight);
    }


    /***
     * Method which will switch between which set of weapon animations that should be used based on a String value.
     * @param animationWanted Requires a String value which is later compared in order to change equippedWeapon.
     */
    public void playerAnimation(String animationWanted) {
        if (animationWanted == "knife")
            this.equippedWeapon = WeaponTypes.KNIFE;
        else if (animationWanted == "pistol")
            this.equippedWeapon = WeaponTypes.PISTOL;
        else if (animationWanted == "rifle")
            this.equippedWeapon = WeaponTypes.RIFLE;
        else if (animationWanted == "shotgun")
            this.equippedWeapon = WeaponTypes.SHOTGUN;
        else
            this.equippedWeapon = WeaponTypes.KNIFE;
    }

    @Override
    public void loadBasicSounds(String[] audioFiles) {
        this.basicSounds = loadAudio(audioFiles);
    }

    public void loadWeaponSounds(String[] audioFiles) {
        this.weapon = loadAudio(audioFiles);
    }

    public void playWeaponSounds(int i) {
        this.weapon[i].play();
    }

    public void playBasicSounds(int i) {
        this.basicSounds[i].play();
    }

    public void setAnimation(int i, int j) {
        super.setSprite(this.allAnimation[i][j]);
    }

    public Sprite[][] getAllAnimation() {
        return allAnimation;
    }

    public WeaponTypes getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(WeaponTypes equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    /***
     * Method which sets current animation set to be used, together with the required sound clips necessary.
     * Upon WASD or Arrow Key input, the walking animation of each sprite set is selected.
     * When pressing E, the melee animation of the knife set is selected.
     * When pressing Space, the fire or knife animation of the set is selected.
     * When pressing 1-4, the user may switch between the various sets of animations.
     * Finally adds the now selected i and j int values as indexes in a 2-dimensional array.
     * @param keyEvent Handles user input via the pressing of a key.
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
            playBasicSounds(1);
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            j = 1;
            goRight();
            playBasicSounds(1);
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            j = 1;
            goUp();
            playBasicSounds(1);
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            j = 1;
            goDown();
            playBasicSounds(1);
        } else {
            j = 0;
        }

        if ((keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.SPACE) && equippedWeapon == WeaponTypes.KNIFE) {
            j = 2;
            playWeaponSounds(audioAction);
        } else if (keyEvent.getCode() == KeyCode.SPACE && equippedWeapon != WeaponTypes.KNIFE) {
            fire();
            playWeaponSounds(audioAction);
            j = 3;
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != WeaponTypes.KNIFE) {
            playWeaponSounds(audioReload);
            j = 4;
            reload();
        }

        if (keyEvent.getCode() == KeyCode.DIGIT1)
            this.equippedWeapon = WeaponTypes.KNIFE;
        else if (keyEvent.getCode() == KeyCode.DIGIT2)
            this.equippedWeapon = WeaponTypes.PISTOL;
        else if (keyEvent.getCode() == KeyCode.DIGIT3)
            this.equippedWeapon = WeaponTypes.RIFLE;
        else if (keyEvent.getCode() == KeyCode.DIGIT4)
            this.equippedWeapon = WeaponTypes.SHOTGUN;

        setAnimation(i, j);
    }

    /***
     * Method which handles key released events of the user input that affects the Player.
     * The animation set for each Weapon is set to the idle state, and put into the setAnimation().
     * As for visual movement, the player stops in the direction they were moving.
     * @param keyEvent Handles user input via the release of a key.
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

    public List getPistolBullets() {
        return pistolBullets;
    }

    public void fire() {
        switch (this.equippedWeapon) {
            case PISTOL:
                if (!magazinePistol.isMagazineEmpty()) {
                    magazinePistol.changeBulletNumber(-1);
                    pistolBullets.add(new Bullet(10,10,1.0,1.0,5.0,20,this.equippedWeapon));
                    System.out.println("Pistol fired");
                } else {
                    magazinePistol.reloadMagazine();
                }
                break;
            case RIFLE:
                if (!magazineRifle.isMagazineEmpty()) {
                    magazineRifle.changeBulletNumber(-1);
                    rifleBullets.add(new Bullet(10,10,1.0,1.0,5.0,20,this.equippedWeapon));
                    System.out.println("Rifle fired");
                } else {
                    magazineRifle.reloadMagazine();
                }
                break;
            case SHOTGUN:
                if (!magazineShotgun.isMagazineEmpty()) {
                    magazineShotgun.changeBulletNumber(-1);
                    shotgunBullets.add(new Bullet(10,10,1.0,1.0,5.0,20,this.equippedWeapon));
                    System.out.println("Shotgun fired");
                } else {
                    magazineShotgun.reloadMagazine();
                }
                break;
        }
    }

    public void reload() {
        switch (this.equippedWeapon) {
            case PISTOL:
                magazinePistol.reloadMagazine();
                break;
            case RIFLE:
                magazineRifle.reloadMagazine();
                break;
            case SHOTGUN:
                magazineShotgun.reloadMagazine();
                break;
        }
    }

    /***
     * Inner class for handling magazine count and ammunition pool for the Player.
     * Controls whether a new Bullet can be created when a weapon is fired.
     */
    public class Magazine {
        private int maxSize;
        private int numberBullets;
        private int maxPool;
        private int currentPool;

        public Magazine(int magazineSize, int maxPool) {
            this.maxSize = magazineSize;
            this.numberBullets = this.maxSize;
            this.maxPool = maxPool;
            this.currentPool = this.maxSize;
        }

        /***
         * Method for decreasing the number of bullets in the magazine upon a fire() call,
         * or to increase the number of bullets in the ammunition pool upon PowerUp(Ammunition) pickups.
         * @param number Int value which decides whether to decrease the number of bullets in the magazine
         *               if the value is negative and the magazine isn't empty, or if the value is positive,
         *               it'll be added to the ammunition pool as long as it isn't full.
         *
         */
        public void changeBulletNumber(int number) {
            if (number < 0 && !isMagazineEmpty()) {
                this.numberBullets += number;
            } else if (number > 0 && !isPoolFull()) {
                if ((number + getCurrentPool()) > maxPool)
                    this.currentPool = maxPool;
                else
                    this.currentPool += number;
            }
        }

        /***
         * Method for handling the reloading of a magazine, where it updates both the ammunition count in the magazine and in the pool.
         *
         * Requirements for the method is that the magazine isn't full, and the ammunition pool isn't empty.
         * If the pool is larger than the magazine size, simply fill the magazine and reduce the pool accordingly.
         * If the pool is equal or lower than the magazine size, accumulate these values, then check if this value is
         * larger than the maximum size of the magazine. If so, return the extra bullets to the pool and fill the magazine.
         * If not, then simply set the ammunition pool to zero, whilst the magazine already equals the accumulated value.
         */
        public void reloadMagazine() {
            if (!isMagazineFull() && !isPoolEmpty()) {
                if (getCurrentPool() > maxSize) {
                    setCurrentPool(getCurrentPool() - (maxSize - getNumberBullets()));
                    setNumberBullets(maxSize);
                } else if (getCurrentPool() <= maxSize) {
                    setNumberBullets(getNumberBullets() + getCurrentPool());
                    if (getNumberBullets() > maxSize) {
                        setCurrentPool(getNumberBullets() - maxSize);
                        setNumberBullets(maxSize);
                    } else {
                        setCurrentPool(0);
                    }
                }
            }
        }

        public boolean isMagazineEmpty() {
            return this.numberBullets <= 0;
        }

        public boolean isMagazineFull() {
            return this.numberBullets >= this.maxSize;
        }

        public boolean isPoolEmpty() {
            return this.currentPool <= 0;
        }

        public boolean isPoolFull() {
            return this.currentPool >= this.maxPool;
        }

        public void setNumberBullets(int numberBullets) {
            this.numberBullets = numberBullets;
        }

        public int getNumberBullets() {
            return this.numberBullets;
        }

        public int getCurrentPool() {
            return this.currentPool;
        }

        public void setCurrentPool(int currentPool) {
            this.currentPool = currentPool;
        }
    }
}