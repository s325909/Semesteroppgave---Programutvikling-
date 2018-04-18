package gameCode;

import entities.Player;
import entities.Zombie;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SaveData implements java.io.Serializable {

    public static final long serialVersionUID = 1L;
    public String name;
    public int hp;

    private Pane gameWindow;
    private Player player;
    private List<Zombie> zombies;
    private Text playerHP, magazineSize, poolSize, score;
}
