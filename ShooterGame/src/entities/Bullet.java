package entities;

public class Bullet extends Movable {

    private int damage;
    private String bulletType;
    private WeaponTypes currentWeapon;
    private Magazine magazinePistol = new Magazine(15, 30);
    private Magazine magazineRifle = new Magazine(30,90);
    private Magazine magazineShotgun = new Magazine(8,32);

    public Bullet(String filename, int positionX, int positionY, double velocityX, double velocityY, int damage, String bulletType) {
        super(filename, positionX, positionY, velocityX, velocityY);
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
                if (!magazinePistol.isMagazineEmpty()) {
                    magazinePistol.changeBulletNumber(-1);
                    this.damage = 10;
                }
                break;
            case RIFLE:
                if (!magazineRifle.isMagazineEmpty()) {
                    magazineRifle.changeBulletNumber(-1);
                    this.damage = 15;
                }
                break;
            case SHOTGUN:
                if (!magazineShotgun.isMagazineEmpty()) {
                    magazineShotgun.changeBulletNumber(-1);
                    this.damage = 20;
                }
                break;
        }
    }

    public void reload() {
        switch (this.currentWeapon) {
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

    public void outOfBullets() {

    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public WeaponTypes getCurrentWeapon() {
        return currentWeapon;
    }

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

        public void reloadMagazine() {
            if (!isMagazineFull() && !isPoolEmpty()) {
                if (maxPool > maxSize) {
                    setCurrentPool(getCurrentPool() - (maxSize - getNumberBullets()));
                    setNumberBullets(maxSize);
                } else if (maxPool <= maxSize) {
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
            return currentPool;
        }

        public void setCurrentPool(int currentPool) {
            this.currentPool = currentPool;
        }
    }
}
