package entities;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        Player player = new Player(4,3);

        try {
            FileOutputStream fos = new FileOutputStream(new File("./savegame.xml"));
            XMLEncoder encoder = new XMLEncoder(fos);

            encoder.writeObject(player);
            encoder.close();
            fos.close();

//            FileInputStream fis = new FileInputStream(new File("./savegame.xml"));
//            XMLDecoder decoder = new XMLDecoder(fis);
//            Player p = (Player)decoder.readObject();
//            decoder.close();
//            fis.close();



            //System.out.println(player.getArmor());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
