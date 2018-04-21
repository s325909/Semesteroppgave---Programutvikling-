package notCurrentlyUsed;

import entities.Player;
import entities.Zombie;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class SaveData implements java.io.Serializable {

    public static final long serialVersionUID = 1L;
    private Player player;
    private List<Zombie> zombies;
    private String playerHP, playerArmor, magazineSize, poolSize, score;
    int positionX;
    int positionY;
    int healthPoints;
    int armor;

    public SaveData(int positionX, int positionY, int healthPoints, int armor, Text playerHP, Text playerArmor, Text magazineSize, Text poolSize, Text score){
        this.playerHP = playerHP.getText();
        this.playerArmor = playerArmor.getText();
        this.magazineSize = magazineSize.getText();
        this.poolSize = poolSize.getText();
        this.score = score.getText();
        this.positionX = positionX;
        this.positionY = positionY;
        this.healthPoints = healthPoints;
        this.armor = armor;
    }
}
