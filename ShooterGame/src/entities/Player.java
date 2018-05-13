package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Player extends Movable {

    public enum WeaponTypes {
        KNIFE, PISTOL, RIFLE, SHOTGUN
}

    private enum PlayerDirection {
        LEFT, RIGHT, UP, DOWN, FIRE, RELOAD
    }

    private WeaponTypes equippedWeapon;
    private State playerState;
    private int armor;
    private AudioClip[] weaponSounds;
    private Image[] bulletImages;
    private Magazine magazinePistol;
    private Magazine magazineRifle;
    private Magazine magazineShotgun;
    private List<Bullet> bulletList;
    private Queue<AnimationLengthPair> animationQueue;
    private long waitTime;
    private long invTime;
    private long fireWaitTime;
    private List<Rock> rocks;
    private List<PlayerDirection> directionButtonPressed;

    public Player(Image[][][] images, AudioClip[] basicSounds, AudioClip[] weaponSounds, Image[] bulletImages, int positionX, int positionY, int healthPoints, int armor) {
        super(new AnimationHandler(images), basicSounds, positionX, positionY, healthPoints, 5.0);
        this.weaponSounds = weaponSounds;
        this.bulletImages = bulletImages;
        this.animationQueue = new LinkedList<AnimationLengthPair>();
        this.waitTime = 0;
        setEquippedWeapon(WeaponTypes.KNIFE);
        setAnimation(0,0, 0);
        magazinePistol = new Magazine(15, 30);
        magazineRifle = new Magazine(30,90);
        magazineShotgun = new Magazine(8,32);
        this.armor = armor;
        this.bulletList = new ArrayList<Bullet>();
        playerState = State.NORMAL;
        setDirection(Direction.EAST);
        this.rocks = rocks;
        directionButtonPressed = new ArrayList<>();
    }

    public void move() {
        stopX();
        stopY();
        for (PlayerDirection playerDirection : directionButtonPressed) {
            switch (playerDirection) {
                case LEFT:
                    addVelocityX(-getMovementSpeed());
                    break;
                case RIGHT:
                    addVelocityX(getMovementSpeed());
                    break;
                case UP:
                    addVelocityY(-getMovementSpeed());
                    break;
                case DOWN:
                    addVelocityY(getMovementSpeed());
                    break;
                case FIRE:
                    fire();
                    break;
                case RELOAD:
                    reload();
                    break;
            }
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
    public void pressEvent(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            if (!directionButtonPressed.contains(PlayerDirection.LEFT))
                directionButtonPressed.add(PlayerDirection.LEFT);
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            if (!directionButtonPressed.contains(PlayerDirection.RIGHT))
                directionButtonPressed.add(PlayerDirection.RIGHT);
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            if (!directionButtonPressed.contains(PlayerDirection.UP))
                directionButtonPressed.add(PlayerDirection.UP);
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            if (!directionButtonPressed.contains(PlayerDirection.DOWN))
                directionButtonPressed.add(PlayerDirection.DOWN);
        }

        if (keyEvent.getCode() == KeyCode.SPACE) {
            if (!directionButtonPressed.contains(PlayerDirection.FIRE))
                directionButtonPressed.add(PlayerDirection.FIRE);
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != WeaponTypes.KNIFE) {
            if (!directionButtonPressed.contains(PlayerDirection.RELOAD))
                directionButtonPressed.add(PlayerDirection.RELOAD);
        } else if (keyEvent.getCode() == KeyCode.F) {
            setEquippedWeapon(WeaponTypes.KNIFE);
            setAnimation(0,0, 0);
        }

        if (keyEvent.getCode() == KeyCode.DIGIT1) {
            this.equippedWeapon = WeaponTypes.PISTOL;
            setAnimation(1,0, 0);
        }
        else if (keyEvent.getCode() == KeyCode.DIGIT2) {
            this.equippedWeapon = WeaponTypes.RIFLE;
            setAnimation(2,0, 0);
        }
        else if (keyEvent.getCode() == KeyCode.DIGIT3) {
            this.equippedWeapon = WeaponTypes.SHOTGUN;
            setAnimation(3,0, 0);
        }
        else if (keyEvent.getCode() == KeyCode.DIGIT4) {
            this.equippedWeapon = WeaponTypes.KNIFE;
            setAnimation(0,0, 0);
        }
    }

    /***
     * Method which handles key released events of the user input that affects the Player.
     * The animation set for each Weapon is set to the idle state, and put into the setAnimation().
     * As for visual bulletDirection, the player stops in the direction they were moving.
     * @param keyEvent Handles user input via the release of a key.
     */
    public void releasedEvent(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            directionButtonPressed.remove(PlayerDirection.LEFT);
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            directionButtonPressed.remove(PlayerDirection.RIGHT);
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            directionButtonPressed.remove(PlayerDirection.UP);
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S){
            directionButtonPressed.remove(PlayerDirection.DOWN);
        }

        if (keyEvent.getCode() == KeyCode.SPACE) {
            directionButtonPressed.remove(PlayerDirection.FIRE);
            switch(this.equippedWeapon) {
                case KNIFE:
                    setAnimation(0,0, 0);
                    break;
                case PISTOL:
                    setAnimation(1,0, 0);
                    break;
                case RIFLE:
                    setAnimation(2,0, 0);
                    break;
                case SHOTGUN:
                    setAnimation(3,0, 0);
                    break;
            }
        } else if (keyEvent.getCode() == KeyCode.R) {
            directionButtonPressed.remove(PlayerDirection.RELOAD);
            switch(this.equippedWeapon) {
                case KNIFE:
                    setAnimation(0, 0, 0);
                    break;
                case PISTOL:
                    setAnimation(1, 0, 0);
                    break;
                case RIFLE:
                    setAnimation(2, 0, 0);
                    break;
                case SHOTGUN:
                    setAnimation(3, 0, 0);
                    break;
            }
        } else if (keyEvent.getCode() == KeyCode.F) {
            setEquippedWeapon(WeaponTypes.KNIFE);
            setAnimation(0,0,0);
        }
    }

    /***
     * Method for running the changeBulletNumber() method in Magazine, and playing the appropriate sound.
     * Adds a check to ensure that the magazine isn't empty. This check ensures to correctly perform the
     * method for changing the number and playing the appropriate sound. If the magazine is empty, the
     * reloadMagazine() method will be run.
     */
    private void fire() {
        long currentTime = System.currentTimeMillis();
        int fireRate = 0;
        if (currentTime > this.fireWaitTime) {
            int posX = getPositionX();
            int posY = getPositionY();
            int knifePosX = getPositionX();
            int knifePosY = getPositionY();
            switch (this.getDirection()) {
                case EAST:
                    posX += this.getAnimationHandler().getImageView().getImage().getWidth();
                    posY += (this.getAnimationHandler().getImageView().getImage().getHeight() - 20);
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case NORTHEAST:
                    posX += this.getAnimationHandler().getImageView().getImage().getWidth();
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case WEST:
                    posY += 10;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    break;
                case NORTHWEST:
                    posX += 10;
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += 2*this.getAnimationHandler().getImageView().getImage().getHeight()/3;
                    break;
                case NORTH:
                    posX += (this.getAnimationHandler().getImageView().getImage().getWidth() - 20);
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case SOUTH:
                    posX += 10;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight();
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case SOUTHEAST:
                    posX += this.getAnimationHandler().getImageView().getImage().getWidth();
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight();
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case SOUTHWEST:
                    posX -= 10;
                    posY += this.getAnimationHandler().getImageView().getImage().getHeight();
                    knifePosX += 2*this.getAnimationHandler().getImageView().getImage().getWidth()/3;
                    knifePosY += this.getAnimationHandler().getImageView().getImage().getHeight()/2;
                    break;
                case IDLE:
                    break;
            }

            switch (this.equippedWeapon) {
                case KNIFE:
                    fireRate = 500;
                    Bullet knifeSlash = new Bullet(this.bulletImages, knifePosX, knifePosY, 0, 75, this.getDirection(), 10000);
                    knifeSlash.setNewRotation(getNewRotation());
                    Arc knifeArc = new Arc(0, 0, 25, 30.0, 90, 180);
                    knifeArc.setTranslateX(knifePosX);
                    knifeArc.setTranslateY(knifePosY);
                    knifeArc.getTransforms().add(new Rotate(180, 0, 0));
                    knifeSlash.setNode(knifeArc);
                    //knifeSlash.setDrawn();
                    bulletList.add(knifeSlash);
                    playWeaponSounds(0, 1);
                    setAnimation(0,2, 500);
                    break;
                case PISTOL:
                    fireRate = 500;
                    if (!magazinePistol.isMagazineEmpty()) {
                        magazinePistol.changeBulletNumber(-1);
                        Bullet bullet = new Bullet(this.bulletImages, posX, posY, 10, 50, this.getDirection(), 3000);
                        bulletList.add(bullet);
                        playWeaponSounds(1, 1);
                    } else {
                        playWeaponSounds(7, 1);
                    }
                    setAnimation(1,2, 100);
                    break;
                case RIFLE:
                    fireRate = 500;
                    if (!magazineRifle.isMagazineEmpty()) {
                        magazineRifle.changeBulletNumber(-1);
                        Bullet bullet = new Bullet(this.bulletImages, posX, posY, 10, 35, this.getDirection(), 3000);
                        bulletList.add(bullet);
                        playWeaponSounds(3, 1);
                    } else {
                        playWeaponSounds(7, 1);
                    }
                    setAnimation(2,2, 100);
                    break;
                case SHOTGUN:
                    fireRate = 500;
                    if (!magazineShotgun.isMagazineEmpty()) {
                        magazineShotgun.changeBulletNumber(-1);

                        double adjustVelX1 = 0, adjustVelY1 = 0;
                        double adjustVelX2 = 0, adjustVelY2 = 0;
                        switch (getDirection()) {
                            case NORTHWEST:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = -1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = -1;
                                break;
                            case SOUTHEAST:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = 1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = 1;
                                break;
                            case SOUTHWEST:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = 1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = 1;
                                break;
                            case NORTHEAST:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = -1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = -1;
                                break;
                            case SOUTH:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = 1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = 1;
                                break;
                            case NORTH:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = -1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = -1;
                                break;
                            case WEST:
                                adjustVelX1 = -0.5;
                                adjustVelY1 = 1;
                                adjustVelX2 = -0.5;
                                adjustVelY2 = -1;
                                break;
                            case EAST:
                                adjustVelX1 = 0.5;
                                adjustVelY1 = 1;
                                adjustVelX2 = 0.5;
                                adjustVelY2 = -1;
                                break;
                        }

                        Bullet bullet = new Bullet(this.bulletImages, posX, posY, 10, 20, this.getDirection(), 300);
                        this.bulletList.add(bullet);
                        bullet = new Bullet(this.bulletImages, posX, posY, 10, 20, this.getDirection(),300, adjustVelX1, adjustVelY1);
                        this.bulletList.add(bullet);
                        bullet = new Bullet(this.bulletImages, posX, posY, 10, 20, this.getDirection(),300, adjustVelX2, adjustVelY2);
                        this.bulletList.add(bullet);
                        playWeaponSounds(5, 1);
                    } else {
                        playWeaponSounds(7, 1);
                    }
                    setAnimation(3,2, 100);
                    break;
            }
            this.fireWaitTime = currentTime + fireRate;
        }
    }

    /***
     * Method for running the reloadMagazine() method in Magazine, and playing the appropriate sound.
     * Adds a check to ensure that the magazine isn't full and that there is ammunition left in the pool.
     * This check ensures to correctly perform the method for changing the number and playing the appropriate sound.
     */
    private void reload() {
        long currentTime = System.currentTimeMillis();
        int fireRate = 500;
        if (currentTime > this.fireWaitTime) {
            switch (this.equippedWeapon) {
                case PISTOL:
                    if (!magazinePistol.isPoolEmpty() && !magazinePistol.isMagazineFull()) {
                        magazinePistol.reloadMagazine();
                        playWeaponSounds(2, 3);
                        setAnimation(1, 3, 500);
                    }
                    break;
                case RIFLE:
                    if (!magazineRifle.isPoolEmpty() && !magazineRifle.isMagazineFull()) {
                        magazineRifle.reloadMagazine();
                        playWeaponSounds(4, 3);
                        setAnimation(2, 3, 500);
                    }
                    break;
                case SHOTGUN:
                    if (!magazineShotgun.isPoolEmpty() && !magazineShotgun.isMagazineFull()) {
                        magazineShotgun.reloadMagazine();
                        playWeaponSounds(6, 3);
                        setAnimation(3, 3, 500);
                    }
                    break;
            }
            this.fireWaitTime = currentTime + fireRate;
        }
    }

    /**
     *
     * @param animationType
     * @param animationAction
     * @param animationLength
     */
    private void setAnimation(int animationType, int animationAction, int animationLength) {
        boolean inQueue = false;

        for (AnimationLengthPair pair : animationQueue) {
            if (pair.animationType == animationType && pair.animationAction == animationAction)
                inQueue = true;
        }

        if (!inQueue) {
            animationQueue.add(new AnimationLengthPair(animationType, animationAction, animationLength, 0.032));
        }
    }

    /**
     *
     */
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        AnimationLengthPair pair = animationQueue.peek();
        if (pair != null) {
            if (currentTime > this.waitTime) {
                getAnimationHandler().setImageType(animationQueue.peek().animationType);
                getAnimationHandler().setImageAction(animationQueue.peek().animationAction);
                this.waitTime = currentTime + animationQueue.peek().time;
                animationQueue.remove();
            }
        }
    }

    /**
     * Method which will reset the Player's stats.
     * These include position, healthpoints, armor, and ammunition of each weaponSounds.
     */

    public void resetPlayer(Game.Difficulty difficulty) {
        setPosition(1280/2,720/2);
        setTranslateNode(1280/2, 720/2);
        setHealthPoints(100);
        setArmor(50);
        setEquippedWeapon(WeaponTypes.KNIFE);
        getMagazinePistol().setNumberBullets(0);
        getMagazinePistol().setCurrentPool(0);
        getMagazineRifle().setNumberBullets(0);
        getMagazineRifle().setCurrentPool(0);
        getMagazineShotgun().setNumberBullets(0);
        getMagazineShotgun().setCurrentPool(0);
        switch(difficulty) {
            case NORMAL:
                getMagazinePistol().setNumberBullets(15);
                getMagazinePistol().setCurrentPool(15);
                getMagazineRifle().setNumberBullets(30);
                getMagazineRifle().setCurrentPool(30);
                getMagazineShotgun().setNumberBullets(8);
                getMagazineShotgun().setCurrentPool(8);
                break;
            case HARD:
                setArmor(0);
                getMagazinePistol().setNumberBullets(15);
                getMagazinePistol().setCurrentPool(15);
                break;
            case INSANE:
                setHealthPoints(50);
                setArmor(0);
                break;
        }
    }

    /**
     * Method which will handle damage towards the Player.
     * This method will adjust Player's armor, whereas the armor will reduce the damage to healthpoints by half.
     * Method will ensure that both healthpoints and armor can not go below 0 points.
     * @param damage Requires a positive integer to represent damage received,
     *               which in turn will adjust Player healthpoints and armor.
     */
    public void receivedDamage(int damage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.invTime) {
            int diffArmor = this.getArmor() - damage;
            if (diffArmor < 0) {
                this.setArmor(0);
                int diffHealth = this.getHealthPoints() - damage;
                if (diffHealth < 0) {
                    this.setHealthPoints(0);
                    this.setAlive(false);
                } else {
                    this.setHealthPoints(this.getHealthPoints() - Math.abs(diffArmor));
                }
            } else {
                this.setArmor(diffArmor);
                this.setHealthPoints(this.getHealthPoints() - (damage/2));
            }
            playerState = State.DAMAGED;
            this.invTime = currentTime + 1000;
        }
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

    /**
     * Method which will retrieve and return requested information about a Player object.
     * Creates a new PlayerConfiguration object from the DataHandler class, and transfers
     * variables inherited from Movable, combined with variables specific to the Player class,
     * into the corresponding variables in playerCfg.
     * @return Returns the object playerCfg of type PlayerConfiguration.
     */
    public DataHandler.PlayerConfiguration getPlayerConfiguration() {
        DataHandler.PlayerConfiguration playerCfg = new DataHandler.PlayerConfiguration();
        playerCfg.movementCfg = super.getMovementConfiguration();
        playerCfg.armor = this.getArmor();
        playerCfg.equipped = this.getEquippedWeapon();
        playerCfg.magPistol = this.getMagazinePistol().getNumberBullets();
        playerCfg.poolPistol = this.getMagazinePistol().getCurrentPool();
        playerCfg.magRifle = this.getMagazineRifle().getNumberBullets();
        playerCfg.poolRifle = this.getMagazineRifle().getCurrentPool();
        playerCfg.magShotgun = this.getMagazineShotgun().getNumberBullets();
        playerCfg.poolShotgun = this.getMagazineShotgun().getCurrentPool();

        List<DataHandler.BulletConfiguration> bulletListCfg = new ArrayList<>();
        for (Bullet bullet : bulletList)
            bulletListCfg.add(bullet.getBulletConfiguration());
        playerCfg.bulletListCfg = bulletListCfg;

        return playerCfg;
    }

    /**
     * Method which will transfer provided playerCfg's variables into corresponding variables in Player.
     * Variables inherited from Movable are transferred and set through a super method call.
     * Further, variables specific to the Player class are transferred and set.
     * @param playerCfg Requires and object of type PlayerConfiguration.
     */
    public void setPlayerConfiguration(DataHandler.PlayerConfiguration playerCfg) {
        super.setMovementConfiguration(playerCfg.movementCfg);
        this.setArmor(playerCfg.armor);
        this.setEquippedWeapon(playerCfg.equipped);
        this.getMagazinePistol().setNumberBullets(playerCfg.magPistol);
        this.getMagazinePistol().setCurrentPool(playerCfg.poolPistol);
        this.getMagazineRifle().setNumberBullets(playerCfg.magRifle);
        this.getMagazineRifle().setCurrentPool(playerCfg.poolRifle);
        this.getMagazineShotgun().setNumberBullets(playerCfg.magShotgun);
        this.getMagazineShotgun().setCurrentPool(playerCfg.poolShotgun);

        bulletList = new ArrayList<>();
        for (DataHandler.BulletConfiguration bulletCfg : playerCfg.bulletListCfg)
            bulletList.add(new Bullet(bulletImages, bulletCfg.movementCfg.entityCfg.posX, bulletCfg.movementCfg.entityCfg.posY, bulletCfg.movementCfg.movementSpeed, bulletCfg.damage, bulletCfg.movementCfg.direction, bulletCfg.remainingTime));
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void playWeaponSounds(int i, double rate) {
        this.weaponSounds[i].setRate(rate);
        this.weaponSounds[i].play();
    }

    public List<Bullet> getBulletList() {
        return this.bulletList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
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