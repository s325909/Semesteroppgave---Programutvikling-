package entities;

public class Zombie extends Enemy {

    public Zombie(){}

    public Zombie(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);


    }
}