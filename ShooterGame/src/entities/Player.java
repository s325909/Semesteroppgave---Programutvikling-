package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Player extends Movable {

    public enum WeaponTypes {
        KNIFE, PISTOL, RIFLE, SHOTGUN
    }

    private WeaponTypes equippedWeapon;
    private int armor;
    private AudioClip[] weaponSounds;
    private Sprite[][] allAnimation;
    private Magazine magazinePistol;
    private Magazine magazineRifle;
    private Magazine magazineShotgun;
    private List<Bullet> bulletList;
    private Queue<SpritePair> animationQueue;
    private long waitTime;

    public Player(Sprite[][] allAnimation, AudioClip[] basicSounds, AudioClip[] weaponSounds, int positionX, int positionY, int healthPoints, int armor) {
        super(allAnimation[0][0], basicSounds, positionX, positionY, healthPoints, 5.0);
        this.allAnimation = allAnimation;
        this.weaponSounds = weaponSounds;
        this.animationQueue = new LinkedList<SpritePair>();
        this.waitTime = 0;

        setEquippedWeapon(WeaponTypes.KNIFE);
        setAnimation(0,0);

        magazinePistol = new Magazine(15, 30);
        magazineRifle = new Magazine(30,90);
        magazineShotgun = new Magazine(8,32);
        this.armor = armor;
        this.bulletList = new ArrayList<Bullet>();
    }

    /***
     * Method which will switch between which set of weaponSounds animations that should be used based on a String value.
     * @param weaponType Requires a String value which is later compared in order to change equippedWeapon.
     */
    public void setWeaponTypeFromString(String weaponType) {
        switch(weaponType) {
            case "knife":
                this.equippedWeapon = WeaponTypes.KNIFE;
                break;
            case "pistol":
                this.equippedWeapon = WeaponTypes.PISTOL;
                break;
            case "rifle":
                this.equippedWeapon = WeaponTypes.RIFLE;
                break;
            case "shotgun":
                this.equippedWeapon = WeaponTypes.SHOTGUN;
                break;
            default:
                this.equippedWeapon = WeaponTypes.KNIFE;
        }
    }

    public String getWeaponTypeToString() {
        switch(this.equippedWeapon) {
            case KNIFE:
                return "knife";
            case PISTOL:
                return "pistol";
            case RIFLE:
                return "rifle";
            case SHOTGUN:
                return "shotgun";
            default:
                return "knife";
        }
    }

    public WeaponTypes getWeaponTypeFromString(String weaponType) {
        switch(weaponType) {
            case "knife":
                return WeaponTypes.KNIFE;
            case "pistol":
                return WeaponTypes.PISTOL;
            case "rifle":
                return WeaponTypes.RIFLE;
            case "shotgun":
                return WeaponTypes.SHOTGUN;
            default:
                return WeaponTypes.KNIFE;
        }
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
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            j = 1;
            goRight();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            j = 1;
            goUp();
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            j = 1;
            goDown();
        } else {
            j = 0;
        }

        if ((keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.SPACE) && equippedWeapon == WeaponTypes.KNIFE) {
            j = 2;
            playWeaponSounds(audioAction);
        } else if (keyEvent.getCode() == KeyCode.SPACE && equippedWeapon != WeaponTypes.KNIFE) {
            j = 3;
            fire(i, j, audioAction);
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != WeaponTypes.KNIFE) {
            j = 4;
            reload(i, j, audioReload);
        } else if (keyEvent.getCode() == KeyCode.F) {
            setEquippedWeapon(WeaponTypes.KNIFE);
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
    private void fire(int i, int j, int audioAction) {
        switch (this.equippedWeapon) {
            case PISTOL:
                if (!magazinePistol.isMagazineEmpty()) {
                    magazinePistol.changeBulletNumber(-1);
                    playWeaponSounds(audioAction);
                    setAnimation(i, j);
                    System.out.println("Pistol fired");
                    Bullet bullet = new Bullet("/resources/Art/pistol_bullet.png", getPositionX(), getPositionY(), 10, 20, this.getDirection());
                    this.bulletList.add(bullet);
                } else {
                    playWeaponSounds(7);
                }
                break;
            case RIFLE:
                if (!magazineRifle.isMagazineEmpty()) {
                    magazineRifle.changeBulletNumber(-1);
                    playWeaponSounds(audioAction);
                    setAnimation(i, j);
                    Bullet bullet = new Bullet("/resources/Art/pistol_bullet.png", getPositionX(), getPositionY(), 10, 10, this.getDirection());
                    this.bulletList.add(bullet);
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
                    Bullet bullet = new Bullet("/resources/Art/pistol_bullet.png", getPositionX(), getPositionY(), 10, 5, this.getDirection());
                    this.bulletList.add(bullet);
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
    private void reload(int i, int j, int audioReload) {
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


    /**
     *
     * @param i
     * @param j
     */
    private void setAnimation(int i, int j) {
        long time = 0;
        if(j == 4) {
            time = 250;
        }
        animationQueue.add(new SpritePair(this.allAnimation[i][j], time));
    }

    /**
     *
     */
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        SpritePair pair = animationQueue.peek();
        if (pair != null) {
            if (currentTime > this.waitTime) {
                //System.out.println("Change animation!");
                super.setSprite(animationQueue.peek().sprite);
                this.waitTime = currentTime + animationQueue.peek().time;
                animationQueue.remove();
            }
        }
    }

    /**
     * Method which will reset the Player's stats.
     * These include position, healthpoints, armor, and ammunition of each weaponSounds.
     */
    public void resetPlayer() {
        setPosition(1280/2,720/2);
        setTranslateNode(1280/2, 720/2);
        setHealthPoints(100);
        setArmor(50);
        setEquippedWeapon(WeaponTypes.KNIFE);
        getMagazinePistol().setNumberBullets(15);
        getMagazinePistol().setCurrentPool(15);
        getMagazineRifle().setNumberBullets(30);
        getMagazineRifle().setCurrentPool(30);
        getMagazineShotgun().setNumberBullets(8);
        getMagazineShotgun().setCurrentPool(8);
    }

    /**
     * Method which will handle damage towards the Player.
     * This method will adjust Player's armor, whereas the armor will reduce the damage to healthpoints by half.
     * Method will ensure that both healthpoints and armor can not go below 0 points.
     * @param damage Requires a positive integer to represent damage received,
     *               which in turn will adjust Player healthpoints and armor.
     */
    public void receivedDamage(int damage) {
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

    /**
     * Method which will handle Player healthpoints upon picking up health booster in the game.
     * Method ensures that the Player's healthpoints can not exceed 100 points through pickups.
     * @param hpChange Requires a positive integer to represent the amount of
     *                 healthpoints that the Player has picked up.
     */
    public void healthPickup(int hpChange) {
        if (this.getHealthPoints() + hpChange <= 100)
            this.setHealthPoints(this.getHealthPoints() + hpChange);
        else
            this.setHealthPoints(100);
    }

    /**
     * Method which will handle Player armor upon picking up armor booster in the game.
     * Method ensures that the Player's armor can not exceed 200 points through pickups.
     * @param armorChange Requires a positive integer to represent the amount of
     *                    armor that the Player has picked up.
     */
    public void armorPickup(int armorChange) {
        if (this.getArmor() + armorChange <= 200)
            this.setArmor(this.getArmor() + armorChange);
        else
            this.setArmor(200);
    }

    /***
     * Method to get the current number of bullets in the magazine of each weaponSounds.
     * @return Returns the getNumberBullets() value associated with specific weaponSounds.
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
     * Method to get the current number of bullets in the pool for each weaponSounds.
     * @return Returns the getCurrentPool() value associated with specific weaponSounds.
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

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Sprite[][] getAllAnimation() {
        return allAnimation;
    }

    public void playWeaponSounds(int i) {
        this.weaponSounds[i].play();
    }

    public List<Bullet> getBulletList() {
        return this.bulletList;
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

    public WeaponTypes getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(WeaponTypes equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    /***
     * Inner class for handling magazine count and ammunition pool for the Player.
     * Controls whether a new Bullet can be created when a weaponSounds is fired.
     */
    public class Magazine {
        private int maxSize;
        private int numberBullets;
        private int maxPool;
        private int currentPool;

        private Magazine(int magazineSize, int maxPool) {
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
        private void reloadMagazine() {
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

        private boolean isMagazineEmpty() {
            return this.numberBullets <= 0;
        }

        private boolean isMagazineFull() {
            return this.numberBullets >= this.maxSize;
        }

        private boolean isPoolEmpty() {
            return this.currentPool <= 0;
        }

        private boolean isPoolFull() {
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