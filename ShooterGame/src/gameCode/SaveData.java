package gameCode;

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
    private Text playerHP, playerArmor, magazineSize, poolSize, score;


    public SaveData(Player player, List<Zombie> zombies, Text playerHP, Text playerArmor, Text magazineSize, Text poolSize, Text score){
        this.player = player;
        this.zombies = zombies;
        this.playerHP = playerHP;
        this.playerArmor = playerArmor;
        this.magazineSize = magazineSize;
        this.poolSize = poolSize;
        this.score = score;
    }
}
