package entities;

public class Drop extends Player {

    public Drop() {
    }

    public Drop(int positionX, int positionY) {
        super(positionX, positionY);
    }

    public Drop(String filename, int positionX, int positionY) {
        super(filename, positionX, positionY);
    }

    public void pickUps(Player player) {
        int random = (int) Math.floor(Math.random() * 100);
    }
}
