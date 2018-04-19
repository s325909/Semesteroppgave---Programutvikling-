package entities;

import gameCode.Game;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;

import gameCode.Game;

public class Player extends Movable {

    private enum WeaponTypes {
        KNIFE, PISTOL, RIFLE, SHOTGUN
    }

    private WeaponTypes equippedWeapon;
    private int armor;
    private AudioClip[] weapon;
    private AudioClip[] basicSounds;
    private Sprite[][] allAnimation;

    private Magazine magazinePistol;
    private Magazine magazineRifle;
    private Magazine magazineShotgun;

    private List<Bullet> bulletList = new ArrayList<>();

    public Player(){}

    public Player(int positionX, int positionY) {
        super(positionX, positionY);
    }

    public Player(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints, int armor) {
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

        loadBasicSounds(playerSounds);
        loadWeaponSounds(weaponSounds);
        loadWeaponSprite(all);

        playerAnimation("knife");
        setAnimation(0,0);

        magazinePistol = new Magazine(15, 30);
        magazineRifle = new Magazine(30,90);
        magazineShotgun = new Magazine(8,32);
        this.armor = armor;
    }

    public int[] getPlayerInfo() {
        int[] info = {
                getPositionX(),
                getPositionY(),
                getHealthPoints(),
                getArmor(),
                getMagazinePistol().getNumberBullets(),
                getMagazinePistol().getCurrentPool(),
                getMagazineRifle().getNumberBullets(),
                getMagazineRifle().getCurrentPool(),
                getMagazineShotgun().getNumberBullets(),
                getMagazineShotgun().getCurrentPool()};
        return info;
    }

    public void resetPlayer() {
        int[] values = {0,0,100,50,15,15,30,30,8,8};
        setPlayerInfo(values);
    }

    public void setPlayerInfo(int[] playerInfo) {
        setPosition(playerInfo[0], playerInfo[1]);
        setTranslateNode(playerInfo[0], playerInfo[1]);
        setHealthPoints(playerInfo[2]);
        setArmor(playerInfo[3]);
        getMagazinePistol().setNumberBullets(playerInfo[4]);
        getMagazinePistol().setCurrentPool(playerInfo[5]);
        getMagazineRifle().setNumberBullets(playerInfo[6]);
        getMagazineRifle().setCurrentPool(playerInfo[7]);
        getMagazineShotgun().setNumberBullets(playerInfo[8]);
        getMagazineShotgun().setCurrentPool(playerInfo[9]);
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

    public void setAnimation(int i, int j) {
        super.setSprite(this.allAnimation[i][j]);
    }

    public Sprite[][] getAllAnimation() {
        return allAnimation;
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

    public void damage(int damage) {
        if (this.getArmor() > 0) {
            this.setArmor(this.getArmor() - damage);
            this.setHealthPoints(this.getHealthPoints() - damage / 2);
        } else {
            this.setHealthPoints(this.getHealthPoints() - damage);
        }

        if (this.getArmor() < 0)
            this.setArmor(0);

        if (this.getHealthPoints() > 0)
            this.setHealthPoints(this.getHealthPoints());
        else
            this.setHealthPoints(0);
    }

    public void healthPickup(int hpChange) {
        if (this.getHealthPoints() + hpChange <= 100)
            this.setHealthPoints(this.getHealthPoints() + hpChange);
        else
            this.setHealthPoints(100);
    }

    public void armorPickup(int armorChange) {
        if (this.getArmor() + armorChange <= 200)
            this.setArmor(this.getArmor() + armorChange);
        else
            this.setArmor(200);
    }

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
            j = 3;
            fire(i,j, audioAction);
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != WeaponTypes.KNIFE) {
            j = 4;
            reload(i,j, audioReload);
        }

        if (keyEvent.getCode() == KeyCode.DIGIT1)
            this.equippedWeapon = WeaponTypes.PISTOL;
        else if (keyEvent.getCode() == KeyCode.DIGIT2)
            this.equippedWeapon = WeaponTypes.RIFLE;
        else if (keyEvent.getCode() == KeyCode.DIGIT3)
            this.equippedWeapon = WeaponTypes.SHOTGUN;
        else if (keyEvent.getCode() == KeyCode.DIGIT4)
            this.equippedWeapon = WeaponTypes.KNIFE;

        setAnimation(i, j);
    }

    /***
     * Method which handles key released events of the user input that affects the Player.
     * The animation set for each Weapon is set to the idle state, and put into the setAnimation().
     * As for visual bulletDirection, the player stops in the direction they were moving.
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
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            stopY();
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S){
            stopY();
        }
    }

    /***
     * Method for running the changeBulletNumber() method in Magazine, and playing the appropriate sound.
     * Adds a check to ensure that the magazine isn't empty. This check ensures to correctly perform the
     * method for changing the number and playing the appropriate sound. If the magazine is empty, the
     * reloadMagazine() method will be run.
     * @param audioAction Requires an int value in order to select the correct sound clip via playWeaponSounds()
     */
    public void fire(int i, int j, int audioAction) {
        switch (this.equippedWeapon) {
            case PISTOL:
                if (!magazinePistol.isMagazineEmpty()) {
                    magazinePistol.changeBulletNumber(-1);
                    playWeaponSounds(audioAction);
                    setAnimation(i, j);
                    System.out.println("Pistol fired");
                    Bullet bullet = new Bullet(getPositionX(), getPositionY(), 20, 20);
                    bulletList.add(bullet);
                } else {
                    playWeaponSounds(7);
                }
                break;
            case RIFLE:
                if (!magazineRifle.isMagazineEmpty()) {
                    magazineRifle.changeBulletNumber(-1);
                    playWeaponSounds(audioAction);
                    setAnimation(i, j);
                    System.out.println("Rifle fired");
                } else {
                    playWeaponSounds(7);
                }
                break;
            case SHOTGUN:
                if (!magazineShotgun.isMagazineEmpty()) {
                    magazineShotgun.changeBulletNumber(-1);
                    playWeaponSounds(audioAction);
                    setAnimation(i, j);
                    System.out.println("Shotgun fired");
                } else {
                    playWeaponSounds(7);
                }
                break;
        }
    }

    /***
     * Method for running the reloadMagazine() method in Magazine, and playing the appropriate sound.
     * Adds a check to ensure that the magazine isn't full and that there is ammunition left in the pool.
     * This check ensures to correctly perform the method for changing the number and playing the appropriate sound.
     * @param audioReload Requires an int value in order to select the correct sound clip via playWeaponSounds()
     */
    public void reload(int i, int j, int audioReload) {
        switch (this.equippedWeapon) {
            case PISTOL:
                if (!magazinePistol.isPoolEmpty() && !magazinePistol.isMagazineFull()) {
                    magazinePistol.reloadMagazine();
                    playWeaponSounds(audioReload);
                    setAnimation(i, j);
                }
                break;
            case RIFLE:
                if (!magazineRifle.isPoolEmpty() && !magazineRifle.isMagazineFull()) {
                    magazineRifle.reloadMagazine();
                    playWeaponSounds(audioReload);
                    setAnimation(i, j);
                }
                break;
            case SHOTGUN:
                if (!magazineShotgun.isPoolEmpty() && !magazineShotgun.isMagazineFull()) {
                    magazineShotgun.reloadMagazine();
                    playWeaponSounds(audioReload);
                    setAnimation(i, j);
                }
                break;
        }
    }

    /***
     * Method to get the current number of bullets in the magazine of each weapon.
     * @return Returns the getNumberBullets() value associated with specific weapon.
     */
    public int getMagazineCount() {
        switch (this.equippedWeapon) {
            case PISTOL:
                return magazinePistol.getNumberBullets();
            case RIFLE:
                return magazineRifle.getNumberBullets();
            case SHOTGUN:
                return magazineShotgun.getNumberBullets();
            default:
                return 0;
        }
    }

    /***
     * Method to get the current number of bullets in the pool for each weapon.
     * @return Returns the getCurrentPool() value associated with specific weapon.
     */
    public int getAmmoPool() {
        switch (this.equippedWeapon) {
            case PISTOL:
                return magazinePistol.getCurrentPool();
            case RIFLE:
                return magazineRifle.getCurrentPool();
            case SHOTGUN:
                return magazineShotgun.getCurrentPool();
            default:
                return 0;
        }
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    public Magazine getMagazinePistol() {
        return magazinePistol;
    }

    public Magazine getMagazineRifle() {
        return magazineRifle;
    }

    public Magazine getMagazineShotgun() {
        return magazineShotgun;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
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
            if (number < 0) {
                this.numberBullets += number;
            } else if (number > 0) {
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

        public int getMaxSize() {
            return maxSize;
        }

        public int getMaxPool() {
            return maxPool;
        }
    }
}