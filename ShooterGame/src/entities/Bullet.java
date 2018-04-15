package entities;

public class Bullet extends Movable {

    private int damage;
    private String bulletType;
//    private WeaponTypes currentWeapon;


    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, int damage, String bulletType) {
        super(filename, positionX, positionY, velocityX, velocityY);
        this.damage = damage;
        this.bulletType = bulletType;
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

//    public WeaponTypes getCurrentWeapon() {
//        return currentWeapon;
//    }


}
