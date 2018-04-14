package entities;

public class Bullet extends Movable {

    private int damage;
    private String bulletType;
    private WeaponTypes currentWeapon;
    private Magazine magazinePistol = new Magazine(15, 15);
    private Magazine magazineRifle = new Magazine(30,30);
    private Magazine magazineShotgun = new Magazine(8,8);

    public Bullet(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints, int damage, String bulletType) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
        this.damage = damage;
        this.bulletType = bulletType;
    }

    public void setBulletType(String weaponWanted) {
        if (this.bulletType == "pistol")
            this.currentWeapon = WeaponTypes.PISTOL;
        if (this.bulletType == "rifle")
            this.currentWeapon = WeaponTypes.RIFLE;
        if (this.bulletType == "shotgun")
            this.currentWeapon = WeaponTypes.SHOTGUN;
    }

    public void fire() {
        switch (this.currentWeapon) {
            case PISTOL:
                magazinePistol.changeBulletNumber(-1);
                this.damage = 10;
                break;
            case RIFLE:
                magazineRifle.changeBulletNumber(-1);
                this.damage = 15;
                break;
            case SHOTGUN:
                magazineShotgun.changeBulletNumber(-1);
                this.damage = 20;
                break;
        }
    }

    public void reload() {
        switch (this.currentWeapon) {
            case PISTOL:
                magazinePistol.changeBulletNumber(1);
                break;
            case RIFLE:
                magazineRifle.changeBulletNumber(1);
                break;
            case SHOTGUN:
                magazineShotgun.changeBulletNumber(1);
                break;
        }
    }

    public class Magazine {
        private int maxSize;
        private int numberBullets;

        public Magazine() {}

        public Magazine(int magazineSize, int bulletsInMagazine) {
            this.maxSize = magazineSize;
            this.numberBullets = bulletsInMagazine;
        }

        public void changeBulletNumber(int number) {
            if (!isFull() && !isEmpty())
                this.numberBullets += number;
            else if (isFull() && number < 0)
                this.numberBullets += number;
            else if (isEmpty() && number > 0)
                this.numberBullets += number;
        }

        public int getNumberBullets() {
            return this.numberBullets;
        }

        public boolean isEmpty() {
            return this.numberBullets <= 0;
        }

        public boolean isFull() {
            return this.numberBullets >= this.maxSize;
        }
    }
}
