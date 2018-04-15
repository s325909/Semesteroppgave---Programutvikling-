package entities;

public class Bullet extends Movable {

    private int damage;
    private WeaponTypes currentWeapon;


    public Bullet(int positionX, int positionY, double velocityX, double velocityY, double movementSpeed, int damage, WeaponTypes currentWeapon) {
        super(positionX, positionY, velocityX, velocityY, movementSpeed);
        this.damage = damage;
        this.currentWeapon = currentWeapon;
    }

    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, double movementSpeed, int damage, WeaponTypes currentWeapon) {
        super(filename, positionX, positionY, velocityX, velocityY, movementSpeed);
        this.damage = damage;
        this.currentWeapon = currentWeapon;
    }

    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, int damage, WeaponTypes currentWeapon) {
        super(filename, positionX, positionY, velocityX, velocityY, 10);
        this.damage = damage;
        this.currentWeapon = currentWeapon;
    }

//    public void setBulletType(String weaponWanted) {
//        if (this.bulletType == "pistol")
//            this.currentWeapon = WeaponTypes.PISTOL;
//        if (this.bulletType == "rifle")
//            this.currentWeapon = WeaponTypes.RIFLE;
//        if (this.bulletType == "shotgun")
//            this.currentWeapon = WeaponTypes.SHOTGUN;
//    }
//
//    public void outOfBullets() {
//
//    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public WeaponTypes getCurrentWeapon() {
        return currentWeapon;
    }
}
