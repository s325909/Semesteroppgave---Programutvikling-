package gameCode;

import entities.Player;
import entities.Movable;
import javafx.scene.control.Alert;
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

public class DataHandler {

    static class GameConfiguration {
        int gameScore;
        Configuration player;
        List<Configuration> zombies;
        List<Configuration> bullets;
        List<Configuration> drops;
        List<Configuration> dropsExtra;
    }

    static class Configuration {
        int health;
        int armor;
        int posX;
        int posY;
        double velX;
        double velY;
        double movementSpeed;
        Movable.Direction direction;
        Player.WeaponTypes equipped;
        int damage;
        int magPistol;
        int poolPistol;
        int magRifle;
        int poolRifle;
        int magShotgun;
        int poolShotgun;
    }

    public static class GameSettings {
        Settings audio;
        Settings video;
    }

    static class Settings {
        int soundVolume;
        int musicVolume;
        int windowWidth;
        int windowHeight;
    }

    public boolean createSettingsFile(GameSettings settings) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException pce) {
            //System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return false;
        }

        Element gameSettings = doc.createElement("GameSettings");
        doc.appendChild(gameSettings);

        Element volume = doc.createElement("Volume");
        gameSettings.appendChild(volume);

        Element sound = doc.createElement("Sound");
        sound.appendChild(doc.createTextNode(String.valueOf(settings.audio.soundVolume)));
        volume.appendChild(sound);

        Element music = doc.createElement("Music");
        music.appendChild(doc.createTextNode(String.valueOf(settings.audio.musicVolume)));
        volume.appendChild(music);

        Element window = doc.createElement("Size");
        gameSettings.appendChild(window);

        Element width = doc.createElement("Width");
        width.appendChild(doc.createTextNode(String.valueOf(settings.video.windowWidth)));
        window.appendChild(width);

        Element height = doc.createElement("Height");
        height.appendChild(doc.createTextNode(String.valueOf(settings.video.windowHeight)));
        window.appendChild(height);

        try {
            TransformerFactory trf = TransformerFactory.newInstance();
            Transformer tr = trf.newTransformer();

            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            File directory = new File("./Data/");
            directory.mkdir();
            File file = new File("./Data/Settings.xml");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            tr.transform(source, result);
        } catch (TransformerException e) {
            System.out.println("TransformerException");
            return false;
        }

        return true;
    }

    public boolean readSettingsFile(GameSettings settings) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse("./Data/Settings.xml");
        } catch (IOException ioe) {
            System.out.println("Caught IOException when reading file: " + ioe.getMessage());
            return false;
        } catch (SAXException sax) {
            System.out.println("Caught SAXException when reading file: " + sax.getMessage());
            return false;
        } catch (ParserConfigurationException pce) {
            System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return false;
        }

        doc.getDocumentElement().normalize();

        //Parse volume settings
        NodeList volumeList = doc.getElementsByTagName("Volume");
        Node volumeNode = volumeList.item(0);
        if (volumeNode.getNodeType() == Node.ELEMENT_NODE) {
            Element volumeElement = (Element)volumeNode;
            settings.audio.soundVolume = Integer.valueOf(volumeElement.getElementsByTagName("Sound").item(0).getTextContent());
            settings.audio.musicVolume = Integer.valueOf(volumeElement.getElementsByTagName("Music").item(0).getTextContent());
        } else {
            return false;
        }

        //Parse window size settings
        NodeList windowList = doc.getElementsByTagName("Size");
        Node windowNode = windowList.item(0);
        if (windowNode.getNodeType() == Node.ELEMENT_NODE) {
            Element windowElement = (Element)windowNode;
            settings.video.windowWidth = Integer.valueOf(windowElement.getElementsByTagName("Width").item(0).getTextContent());
            settings.video.windowWidth = Integer.valueOf(windowElement.getElementsByTagName("Height").item(0).getTextContent());
        } else {
            return false;
        }

        return true;
    }

    /**
     * Method for creating a .xml save file.
     * Turns the data retrieved during save() method in Game class into a structured .xml file.
     * Each field corresponds to the values of the same name for each Entity, and are turned into String
     * values before storing.
     * @param fileName Requires a filename of type String, which in turn will be the name for .xml file.
     * @param configuration Requires an object of type GameConfiguration, which in turn contains all
     *                      the retrieved data for each type of Entity.
     * @return Returns a boolean based on whether the savefile is created successfully, or an exception occurred.
     */
    boolean createSaveFile(String fileName, GameConfiguration configuration) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException pce) {
            //System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return false;
        }



        // Store player information in a XML structure
        Element savegame = doc.createElement("Savegame");
        doc.appendChild(savegame);

        Element gameInfo = doc.createElement("Game");
        savegame.appendChild(gameInfo);

        Element gameScore = doc.createElement("ScorePoints");
        gameScore.appendChild(doc.createTextNode(String.valueOf(configuration.gameScore)));
        gameInfo.appendChild(gameScore);

        Element playerInfo = doc.createElement("Player");
        savegame.appendChild(playerInfo);

        Element playerHP = doc.createElement("HealthPoints");
        playerHP.appendChild(doc.createTextNode(String.valueOf(configuration.player.health)));
        playerInfo.appendChild(playerHP);

        Element armor = doc.createElement("ArmorPoints");
        armor.appendChild(doc.createTextNode(String.valueOf(configuration.player.armor)));
        playerInfo.appendChild(armor);

        Element playerPosX = doc.createElement("PositionX");
        playerPosX.appendChild(doc.createTextNode(String.valueOf(configuration.player.posX)));
        playerInfo.appendChild(playerPosX);

        Element playerPosY = doc.createElement("PositionY");
        playerPosY.appendChild(doc.createTextNode(String.valueOf(configuration.player.posY)));
        playerInfo.appendChild(playerPosY);

        Element playerVelX = doc.createElement("VelocityX");
        playerVelX.appendChild(doc.createTextNode(String.valueOf(configuration.player.velX)));
        playerInfo.appendChild(playerVelX);

        Element playerVelY = doc.createElement("VelocityY");
        playerVelY.appendChild(doc.createTextNode(String.valueOf(configuration.player.velY)));
        playerInfo.appendChild(playerVelY);

        Element playerMovement = doc.createElement("MovementSpeed");
        playerMovement.appendChild(doc.createTextNode(String.valueOf(configuration.player.movementSpeed)));
        playerInfo.appendChild(playerMovement);

        Element playerDirection = doc.createElement("Direction");
        playerDirection.appendChild(doc.createTextNode(String.valueOf(configuration.player.direction)));
        playerInfo.appendChild(playerDirection);

        Element eqWep = doc.createElement("EquippedWep");
        eqWep.appendChild(doc.createTextNode(String.valueOf(configuration.player.equipped)));
        playerInfo.appendChild(eqWep);

        Element magPistol = doc.createElement("MagPistol");
        magPistol.appendChild(doc.createTextNode(String.valueOf(configuration.player.magPistol)));
        playerInfo.appendChild(magPistol);

        Element poolPistol = doc.createElement("PoolPistol");
        poolPistol.appendChild(doc.createTextNode(String.valueOf(configuration.player.poolPistol)));
        playerInfo.appendChild(poolPistol);

        Element magRifle = doc.createElement("MagRifle");
        magRifle.appendChild(doc.createTextNode(String.valueOf(configuration.player.magRifle)));
        playerInfo.appendChild(magRifle);

        Element poolRifle = doc.createElement("PoolRifle");
        poolRifle.appendChild(doc.createTextNode(String.valueOf(configuration.player.poolRifle)));
        playerInfo.appendChild(poolRifle);

        Element magShotgun = doc.createElement("MagShotgun");
        magShotgun.appendChild(doc.createTextNode(String.valueOf(configuration.player.magShotgun)));
        playerInfo.appendChild(magShotgun);

        Element poolShotgun = doc.createElement("PoolShotgun");
        poolShotgun.appendChild(doc.createTextNode(String.valueOf(configuration.player.magShotgun)));
        playerInfo.appendChild(poolShotgun);



        // Store zombie information in a XML structure
        Element zombies = doc.createElement("Zombies");
        savegame.appendChild(zombies);

        for(int i = 0; i < configuration.zombies.size(); i++) {
            Element zombieInfo = doc.createElement("Zombie");
            zombies.appendChild(zombieInfo);

            Element zombieHP = doc.createElement("HealthPoints");
            zombieHP.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).health)));
            zombieInfo.appendChild(zombieHP);

            Element zombiePosX = doc.createElement("PositionX");
            zombiePosX.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).posX)));
            zombieInfo.appendChild(zombiePosX);

            Element zombiePosY = doc.createElement("PositionY");
            zombiePosY.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).posY)));
            zombieInfo.appendChild(zombiePosY);

            Element zombieVelX = doc.createElement("VelocityX");
            zombieVelX.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).velX)));
            zombieInfo.appendChild(zombieVelX);

            Element zombieVelY = doc.createElement("VelocityY");
            zombieVelY.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).velY)));
            zombieInfo.appendChild(zombieVelY);

            Element zombieMovement = doc.createElement("MovementSpeed");
            zombieMovement.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).movementSpeed)));
            zombieInfo.appendChild(zombieMovement);

            Element zombieDirection = doc.createElement("Direction");
            zombieDirection.appendChild(doc.createTextNode(String.valueOf(configuration.zombies.get(i).direction)));
            zombieInfo.appendChild(zombieDirection);
        }



        // Store bullet information in a XML structure
        Element bullets = doc.createElement("Bullets");
        savegame.appendChild(bullets);

        for(int i = 0; i < configuration.bullets.size(); i++) {
            Element bulletInfo = doc.createElement("Bullet");
            bullets.appendChild(bulletInfo);

            Element bulletPosX = doc.createElement("PositionX");
            bulletPosX.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).posX)));
            bulletInfo.appendChild(bulletPosX);

            Element bulletPosY = doc.createElement("PositionY");
            bulletPosY.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).posY)));
            bulletInfo.appendChild(bulletPosY);

            Element bulletVelX = doc.createElement("VelocityX");
            bulletVelX.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).velX)));
            bulletInfo.appendChild(bulletVelX);

            Element bulletVelY = doc.createElement("VelocityY");
            bulletVelY.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).velY)));
            bulletInfo.appendChild(bulletVelY);

            Element bulletMovement = doc.createElement("MovementSpeed");
            bulletMovement.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).movementSpeed)));
            bulletInfo.appendChild(bulletMovement);

            Element bulletDirection = doc.createElement("Direction");
            bulletDirection.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).direction)));
            bulletInfo.appendChild(bulletDirection);

            Element damage = doc.createElement("Damage");
            damage.appendChild(doc.createTextNode(String.valueOf(configuration.bullets.get(i).damage)));
            bulletInfo.appendChild(damage);
        }



        // Store drop information in a XML structure
        Element drops = doc.createElement("Drops");
        savegame.appendChild(drops);

        for(int i = 0; i < configuration.drops.size(); i++) {
            Element dropInfo = doc.createElement("Drop");
            drops.appendChild(dropInfo);

            Element dropPosX = doc.createElement("PositionX");
            dropPosX.appendChild(doc.createTextNode(String.valueOf(configuration.drops.get(i).posX)));
            dropInfo.appendChild(dropPosX);

            Element dropPosY = doc.createElement("PositionY");
            dropPosY.appendChild(doc.createTextNode(String.valueOf(configuration.drops.get(i).posY)));
            dropInfo.appendChild(dropPosY);
        }



        // Turn the information into a file named according to the given fileName String.
        try {
            TransformerFactory trf = TransformerFactory.newInstance();
            Transformer tr = trf.newTransformer();

            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            File directory = new File("./Data/Savegames/");
            directory.mkdirs();
            File file = new File("./Data/Savegames/" + fileName + ".xml");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            tr.transform(source, result);
        } catch (TransformerException tre) {
            System.out.println("TransformerException");
            System.out.println(tre.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Method for reading a .xml save file.
     * It goes through each requested line in the file, takes the String value and transforms it into the appropriate
     * variable type, and finally adds this value to the corresponding variable of each Configuration object.
     * @param fileName Requires a String value that represents the name of the file to read.
     * @param configuration Requires an object of type GameConfiguration, which in turn contains all the retrieved
     *                      data for each type of Entity.
     * @return Returns a boolean value based on whether there were any exceptions during file search, read, or parsing.
     */
    boolean readSaveFile(String fileName, GameConfiguration configuration) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse("./Data/Savegames/" + fileName + ".xml");
        } catch (IOException ioe) {
            System.out.println("Caught IOException when reading file: " + ioe.getMessage());
            return false;
        } catch (SAXException sax) {
            System.out.println("Caught SAXException when reading file: " + sax.getMessage());
            return false;
        } catch (ParserConfigurationException pce) {
            System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return false;
        }

        doc.getDocumentElement().normalize();

        //Parse game
        NodeList gameList = doc.getElementsByTagName("Game");
        Node gameNode = gameList.item(0);
        if (gameNode.getNodeType() == Node.ELEMENT_NODE) {
            Element gameElement = (Element)gameNode;
            configuration.gameScore = Integer.valueOf(gameElement.getElementsByTagName("ScorePoints").item(0).getTextContent());
        } else {
            return false;
        }

        //Parse player
        NodeList playerList = doc.getElementsByTagName("Player");
        Node playerNode = playerList.item(0);
        configuration.player = new Configuration();
        if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
            Element playerElement = (Element) playerNode;
            configuration.player.health = Integer.valueOf(playerElement.getElementsByTagName("HealthPoints").item(0).getTextContent());
            configuration.player.armor = Integer.valueOf(playerElement.getElementsByTagName("ArmorPoints").item(0).getTextContent());
            configuration.player.posX = Integer.valueOf(playerElement.getElementsByTagName("PositionX").item(0).getTextContent());
            configuration.player.posY = Integer.valueOf(playerElement.getElementsByTagName("PositionY").item(0).getTextContent());
            configuration.player.velX = Double.valueOf(playerElement.getElementsByTagName("VelocityX").item(0).getTextContent());
            configuration.player.velY = Double.valueOf(playerElement.getElementsByTagName("VelocityY").item(0).getTextContent());
            configuration.player.movementSpeed = Double.valueOf(playerElement.getElementsByTagName("MovementSpeed").item(0).getTextContent());
            configuration.player.direction = Movable.Direction.valueOf(playerElement.getElementsByTagName("Direction").item(0).getTextContent());
            configuration.player.equipped =  Player.WeaponTypes.valueOf(playerElement.getElementsByTagName("EquippedWep").item(0).getTextContent());
            configuration.player.magPistol = Integer.valueOf(playerElement.getElementsByTagName("MagPistol").item(0).getTextContent());
            configuration.player.poolPistol = Integer.valueOf(playerElement.getElementsByTagName("PoolPistol").item(0).getTextContent());
            configuration.player.magRifle = Integer.valueOf(playerElement.getElementsByTagName("MagRifle").item(0).getTextContent());
            configuration.player.poolRifle = Integer.valueOf(playerElement.getElementsByTagName("PoolRifle").item(0).getTextContent());
            configuration.player.magShotgun = Integer.valueOf(playerElement.getElementsByTagName("MagShotgun").item(0).getTextContent());
            configuration.player.poolShotgun = Integer.valueOf(playerElement.getElementsByTagName("PoolShotgun").item(0).getTextContent());
        } else {
            return false;
        }

        //Parse zombies
        NodeList zombieList = doc.getElementsByTagName("Zombie");
        configuration.zombies = new ArrayList<>();
        for (int i = 0; i < zombieList.getLength(); i++) {
            Node zombieNode = zombieList.item(i);
            if (zombieNode.getNodeType() == Node.ELEMENT_NODE) {
                Element zombieElement = (Element) zombieNode;
                Configuration zombieCfg = new Configuration();
                zombieCfg.health = Integer.valueOf(zombieElement.getElementsByTagName("HealthPoints").item(0).getTextContent());
                zombieCfg.posX = Integer.valueOf(zombieElement.getElementsByTagName("PositionX").item(0).getTextContent());
                zombieCfg.posY = Integer.valueOf(zombieElement.getElementsByTagName("PositionY").item(0).getTextContent());
                zombieCfg.velX = Double.valueOf(zombieElement.getElementsByTagName("VelocityX").item(0).getTextContent());
                zombieCfg.velY = Double.valueOf(zombieElement.getElementsByTagName("VelocityY").item(0).getTextContent());
                zombieCfg.movementSpeed = Double.valueOf(zombieElement.getElementsByTagName("MovementSpeed").item(0).getTextContent());
                zombieCfg.direction = Movable.Direction.valueOf(zombieElement.getElementsByTagName("Direction").item(0).getTextContent());
                configuration.zombies.add(zombieCfg);
            } else {
                return false;
            }
        }

        //Parse bullets
        NodeList bulletList = doc.getElementsByTagName("Bullet");
        configuration.bullets = new ArrayList<>();
        for (int i = 0; i < bulletList.getLength(); i++) {
            Node bulletNode = bulletList.item(i);
            if (bulletNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bulletElement = (Element) bulletNode;
                Configuration bulletCfg = new Configuration();
                bulletCfg.posX = Integer.valueOf(bulletElement.getElementsByTagName("PositionX").item(0).getTextContent());
                bulletCfg.posY = Integer.valueOf(bulletElement.getElementsByTagName("PositionY").item(0).getTextContent());
                bulletCfg.velX = Double.valueOf(bulletElement.getElementsByTagName("VelocityX").item(0).getTextContent());
                bulletCfg.velY = Double.valueOf(bulletElement.getElementsByTagName("VelocityY").item(0).getTextContent());
                bulletCfg.movementSpeed = Double.valueOf(bulletElement.getElementsByTagName("MovementSpeed").item(0).getTextContent());
                bulletCfg.direction = Movable.Direction.valueOf(bulletElement.getElementsByTagName("Direction").item(0).getTextContent());
                bulletCfg.damage = Integer.valueOf(bulletElement.getElementsByTagName("Damage").item(0).getTextContent());
                configuration.bullets.add(bulletCfg);
            } else {
                return false;
            }
        }

        //Parse drops
        NodeList dropList = doc.getElementsByTagName("Drop");
        configuration.drops = new ArrayList<>();
        for (int i = 0; i < dropList.getLength(); i++) {
            Node dropsNode = dropList.item(i);
            if (dropsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dropsElement = (Element) dropsNode;
                Configuration dropCfg = new Configuration();
                dropCfg.posX = Integer.valueOf(dropsElement.getElementsByTagName("PositionX").item(0).getTextContent());
                dropCfg.posY = Integer.valueOf(dropsElement.getElementsByTagName("PositionY").item(0).getTextContent());
                configuration.drops.add(dropCfg);
            } else {
                return false;
            }
        }
        return true;
    }
}