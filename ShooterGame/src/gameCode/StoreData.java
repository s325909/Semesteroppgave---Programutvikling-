package gameCode;

import entities.Bullet;
import entities.Drop;
import entities.Player;
import entities.Zombie;
import javafx.scene.layout.Pane;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoreData {
    private Player player;
    private List<Zombie> zombies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<Drop> drops = new ArrayList<>();
    private List<Drop> dropsExtra = new ArrayList<>();
    private Pane gameWindow;
    private Game game;

    public void retrieveData(Player player, List<Zombie> zombies, List<Bullet> bullets, List<Drop> drops, List<Drop> dropsExtra, Pane gameWindow, Game game) {
        this.player = player;
        this.zombies = zombies;
        this.bullets = bullets;
        this.drops = drops;
        this.dropsExtra = dropsExtra;
        this.gameWindow = gameWindow;
        this.game = game;
    }

    /**
     * Method for creating a save file.
     * Requests the current information about the game and all entities, such as score, healthpoints, positions,
     * and so on, and stores these systematically in a .xml file.
     * @param fileName Requires a fileName of String type, which will be used as the name for .xml file.
     * @param isQuick Requires a boolean to define whether this is the quicksave slot. If true, fileName
     *                gets set to "quicksave", regardless of parameter input, and is saved as quicksave.xml.
     */
    public void createSaveFile(String fileName, Boolean isQuick) {
        Object[] options = {"Resume game"};

        if (isQuick) {
            fileName = "quicksave";
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();

            Document doc = db.newDocument();

            Element element = doc.createElement("Savefile");
            doc.appendChild(element);

            Element gInfo = doc.createElement("Game");
            element.appendChild(gInfo);

            Element score = doc.createElement("Score");
            score.appendChild(doc.createTextNode(String.valueOf(game.getScoreNumber())));
            gInfo.appendChild(score);

            Element pInfo = doc.createElement("Player");
            element.appendChild(pInfo);

            Element playerHP = doc.createElement("HP");
            playerHP.appendChild(doc.createTextNode(String.valueOf(player.getHealthPoints())));
            pInfo.appendChild(playerHP);

            Element armor = doc.createElement("Armor");
            armor.appendChild(doc.createTextNode(String.valueOf(player.getArmor())));
            pInfo.appendChild(armor);

            Element playerPosX = doc.createElement("PosX");
            playerPosX.appendChild(doc.createTextNode(String.valueOf(player.getPositionX())));
            pInfo.appendChild(playerPosX);

            Element playerPosY = doc.createElement("PosY");
            playerPosY.appendChild(doc.createTextNode(String.valueOf(player.getPositionY())));
            pInfo.appendChild(playerPosY);

            Element playerDirection = doc.createElement("Direction");
            playerDirection.appendChild(doc.createTextNode(String.valueOf(player.getDirection())));
            pInfo.appendChild(playerDirection);

            Element eqWep = doc.createElement("Equipped");
            eqWep.appendChild(doc.createTextNode(String.valueOf(player.getEquippedWeapon())));
            pInfo.appendChild(eqWep);

            Element magPistol = doc.createElement("MagPistol");
            magPistol.appendChild(doc.createTextNode(String.valueOf(player.getMagazinePistol().getNumberBullets())));
            pInfo.appendChild(magPistol);

            Element poolPistol = doc.createElement("PoolPistol");
            poolPistol.appendChild(doc.createTextNode(String.valueOf(player.getMagazinePistol().getCurrentPool())));
            pInfo.appendChild(poolPistol);

            Element magRifle = doc.createElement("MagRifle");
            magRifle.appendChild(doc.createTextNode(String.valueOf(player.getMagazineRifle().getNumberBullets())));
            pInfo.appendChild(magRifle);

            Element poolRifle = doc.createElement("PoolRifle");
            poolRifle.appendChild(doc.createTextNode(String.valueOf(player.getMagazineRifle().getCurrentPool())));
            pInfo.appendChild(poolRifle);

            Element magShotgun = doc.createElement("MagShotgun");
            magShotgun.appendChild(doc.createTextNode(String.valueOf(player.getMagazineShotgun().getNumberBullets())));
            pInfo.appendChild(magShotgun);

            Element poolShotgun = doc.createElement("PoolShotgun");
            poolShotgun.appendChild(doc.createTextNode(String.valueOf(player.getMagazineShotgun().getCurrentPool())));
            pInfo.appendChild(poolShotgun);

            Element zInfo = doc.createElement("Zombies");
            element.appendChild(zInfo);

            for(int i = 0; i < zombies.size(); i++) {
                Element zInfoNbr = doc.createElement("Zombie");
                Attr nbrZombie = doc.createAttribute("id");
                nbrZombie.setValue(String.valueOf(i));
                zInfoNbr.setAttributeNode(nbrZombie);
                zInfo.appendChild(zInfoNbr);

                Element zombieHP = doc.createElement("HP");
                zombieHP.appendChild(doc.createTextNode(String.valueOf(zombies.get(i).getHealthPoints())));
                zInfoNbr.appendChild(zombieHP);

                Element zombiePosX = doc.createElement("PosX");
                zombiePosX.appendChild(doc.createTextNode(String.valueOf(zombies.get(i).getPositionX())));
                zInfoNbr.appendChild(zombiePosX);

                Element zombiePosY = doc.createElement("PosY");
                zombiePosY.appendChild(doc.createTextNode(String.valueOf(zombies.get(i).getPositionY())));
                zInfoNbr.appendChild(zombiePosY);

                Element zombieDirection = doc.createElement("Direction");
                zombieDirection.appendChild(doc.createTextNode(String.valueOf(zombies.get(i).getDirection())));
                zInfoNbr.appendChild(zombieDirection);
            }

            Element bulletInfo = doc.createElement("Bullets");
            element.appendChild(bulletInfo);

            for(int i = 0; i < game.getBulletList().size(); i++) {
                Element bulletInfoNbr = doc.createElement("Bullet");
                Attr nbrBullet = doc.createAttribute("id");
                nbrBullet.setValue(String.valueOf(i));
                bulletInfoNbr.setAttributeNode(nbrBullet);
                bulletInfo.appendChild(bulletInfoNbr);

                Element bulletPosX = doc.createElement("PosX");
                bulletPosX.appendChild(doc.createTextNode(String.valueOf(game.getBulletList().get(i).getPositionX())));
                bulletInfoNbr.appendChild(bulletPosX);

                Element bulletPosY = doc.createElement("PosY");
                bulletPosY.appendChild(doc.createTextNode(String.valueOf(game.getBulletList().get(i).getPositionY())));
                bulletInfoNbr.appendChild(bulletPosY);

                Element damage = doc.createElement("Damage");
                damage.appendChild(doc.createTextNode(String.valueOf(game.getBulletList().get(i).getDamage())));
                bulletInfoNbr.appendChild(damage);

                Element bulletDirection = doc.createElement("Direction");
                bulletDirection.appendChild(doc.createTextNode(String.valueOf(game.getBulletList().get(i).getDirection())));
                bulletInfoNbr.appendChild(bulletDirection);
            }

            try {
                TransformerFactory trf = TransformerFactory.newInstance();
                Transformer tr = trf.newTransformer();

                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                DOMSource source = new DOMSource(doc);

//                StreamResult result = new StreamResult(new File("savegame.xml"));
                StreamResult result = new StreamResult(new File("./savegame/" + fileName + ".xml"));

                tr.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
                System.out.println("TransformerException");
            }
//            catch (IOException ioe) {
//                System.out.println(ioe.getMessage());
//            Object[] options = {"Resume game"};
//            int n = JOptionPane.showOptionDialog(null, "Unable to create save file. " +
//                    "\n \n Try to name it something else", "Saving error", JOptionPane.DEFAULT_OPTION,
//                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("ParseConfigurationException");
        }
    }

    /**
     * Method for loading a save file.
     * Turns the information stored in the .xml file into values that can be set for the game and all entities,
     * in order to restore the game to the previous state. Game score and Player's stats and position are reset to
     * the appropriate values, whilst all zombies, if any, are first removed, then one by one is recreated
     * according the the saved values regarding number of zombies and their respective stats.
     * @param fileName Requires a fileName of String type, which will be used to find the correct .xml file.
     * @param isQuick Requires a boolean to define whether this is the quicksave slot. If true, fileName
     *                gets set to "quicksave", regardless of parameter input, and quicksave.xml will load.
     */
    public void readSaveFile(String fileName, Boolean isQuick) {
        Object[] options = {"Resume game"};

        if (isQuick) {
            fileName = "quicksave";
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("./savegame/" + fileName + ".xml");
            doc.getDocumentElement().normalize();

            NodeList gameList = doc.getElementsByTagName("Game");

            Node nodeG = gameList.item(0);

            if (nodeG.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodeG;
                game.setScoreNumber(Integer.valueOf(e.getElementsByTagName("Score").item(0).getTextContent()));
            }

            NodeList playerList = doc.getElementsByTagName("Player");

            Node nodeP = playerList.item(0);

            if (nodeP.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodeP;
                player.setHealthPoints(Integer.valueOf(e.getElementsByTagName("HP").item(0).getTextContent()));
                player.setArmor(Integer.valueOf(e.getElementsByTagName("Armor").item(0).getTextContent()));
                player.setPosition(
                        Integer.valueOf(e.getElementsByTagName("PosX").item(0).getTextContent()),
                        Integer.valueOf(e.getElementsByTagName("PosY").item(0).getTextContent()));
                player.setTranslateNode(
                        Integer.valueOf(e.getElementsByTagName("PosX").item(0).getTextContent()),
                        Integer.valueOf(e.getElementsByTagName("PosY").item(0).getTextContent()));
                player.setDirection(Player.Direction.valueOf(e.getElementsByTagName("Direction").item(0).getTextContent()));
                player.setEquippedWeapon(Player.WeaponTypes.valueOf(e.getElementsByTagName("Equipped").item(0).getTextContent()));
                player.getMagazinePistol().setNumberBullets(Integer.valueOf(e.getElementsByTagName("MagPistol").item(0).getTextContent()));
                player.getMagazinePistol().setCurrentPool(Integer.valueOf(e.getElementsByTagName("PoolPistol").item(0).getTextContent()));
                player.getMagazineRifle().setNumberBullets(Integer.valueOf(e.getElementsByTagName("MagRifle").item(0).getTextContent()));
                player.getMagazineRifle().setCurrentPool(Integer.valueOf(e.getElementsByTagName("PoolRifle").item(0).getTextContent()));
                player.getMagazineShotgun().setNumberBullets(Integer.valueOf(e.getElementsByTagName("MagShotgun").item(0).getTextContent()));
                player.getMagazineShotgun().setCurrentPool(Integer.valueOf(e.getElementsByTagName("PoolShotgun").item(0).getTextContent()));
            }

            // Remove the current number of Zombie objects
            for(Zombie zombie : zombies) {
                gameWindow.getChildren().removeAll(zombie.getIv(), zombie.getNode());
                zombie.setAlive(false);
            }
            zombies.removeIf(Zombie::isDead);


            NodeList zombieList = doc.getElementsByTagName("Zombie");

//            if (!getInitialized())
//                loadAssets(zombieList.getLength());

            for (int i = 0; i < zombieList.getLength(); i++) {

                Node nodeZ = zombieList.item(i);

//                if (nodeZ.getNodeType() == Node.ELEMENT_NODE) {
//                    Element e = (Element) nodeZ;
//                    zombies.add(new Zombie(this.zombieAnimation[i], this.zombieAudioClips,
//                            Integer.valueOf(e.getElementsByTagName("PosX").item(0).getTextContent()),
//                            Integer.valueOf(e.getElementsByTagName("PosY").item(0).getTextContent()),
//                            Integer.valueOf(e.getElementsByTagName("HP").item(0).getTextContent())));
//                    gameWindow.getChildren().addAll(zombies.get(i).getNode(), zombies.get(i).getIv());
//                }
            }
        }
        catch (IOException ioe) {
//            int n = JOptionPane.showOptionDialog(null, "Unable to load save file", "Loading error", JOptionPane.DEFAULT_OPTION,
//                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException saxe) {
            System.out.println(saxe.getMessage());
        }
    }
}