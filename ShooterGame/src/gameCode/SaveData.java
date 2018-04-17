package gameCode;

import entities.Player;
import entities.Zombie;

import java.util.ArrayList;
import java.util.List;

public class SaveData implements java.io.Serializable {

    public static final long serialVersionUID = 1L;
    public String name;
    public int hp;

    private Player player;
    private List<Zombie> zombies;
}
