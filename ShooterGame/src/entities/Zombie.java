package entities;

public class Zombie extends Movable {

    Sprite[] animations;

    public Zombie(){}

    public Zombie(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints, 1.0);
        loadZombieAssets();
    }

    public void loadZombieAssets() {
        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        loadBasicSounds(zombieSounds);
//        Sprite[] sprites = new Sprite[zombieAnimations.length];
//        for(int i = 0; i < zombieAnimations.length; i++) {
//            sprites[i] = loadSprites(zombieAnimations[i]
//        }
    }

//    public void setAnimation(int i) {
//        super.setSprite(this.animations[i]);
//    }

}