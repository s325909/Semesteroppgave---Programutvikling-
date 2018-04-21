package gameCode;

import entities.Player;
import entities.Zombie;
import entities.Sprite;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import notCurrentlyUsed.SaveData;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class InitializeGame implements Initializable{

    @FXML private Pane gameWindow;
    @FXML private MenuBar topbar;
    @FXML private Text playerHP, playerArmor, magazineSize, poolSize, score, equippedWeapon;
    @FXML protected Label gameState, pressKey, pressKey2, pressKey3;
    int positionX, positionY, healthPoints, armor;

    Stage stage = new Stage();

    private Player player;
    private List<Zombie> zombies = new ArrayList<>();
    private Game game;
    private SceneSizeChangeListener sceneChange;
    private MusicPlayer musicPlayer;
    private AudioClip[] weapon;
    private AudioClip[] basicSounds;
    private Sprite[][] playerAnimation;
    private AudioClip[] zombieAudioClips;
    private Sprite[][] zombieAnimation;

    private boolean initialized = false;

    final private boolean DEBUG = true;

    /***
     * Method which will create every Entity and add these to the gameWindow.
     * Method will also initialize the first soundtrack.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initialized = true;

        // Create an object of MusicPlayer, which includes what file to play and automatically starts playing
//        try {
//            musicPlayer = new MusicPlayer("src/resources/Sound/Soundtrack/Doom2.mp3");
//        } catch (Exception e) {
//            System.out.println("Error: Could not find sound file");
//        }

        int nbrZombies = 10;
        loadAssets(nbrZombies);

        // Create the Player upon starting a new game
        try {
            player = new Player(this.playerAnimation, this.weapon, this.basicSounds, (int)gameWindow.getPrefWidth()/2, (int)gameWindow.getPrefHeight()/2, 100,50);
            player.setWeaponTypeFromString("knife");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element);
            }
            System.out.println("Error: Player did not load correctly");
        }

        // Create every Zombie upon starting a new game
        try {
            for (int i = 0; i < nbrZombies; i++) {
                zombies.add(new Zombie(this.zombieAnimation[i], this.zombieAudioClips, (int) (Math.random() * 1280), (int) (Math.random() * 720), 100));
            }
        } catch (Exception e) {
            System.out.println("Error: Enemies did not load correctly");
        }

        // Simple debug tool which adds the Node of every Entity to the gameWindow upon a new game
        if (DEBUG) {
            gameWindow.getChildren().add(player.getNode());
            for (Zombie zombie : zombies)
                gameWindow.getChildren().add(zombie.getNode());
        }

        // Add the ImageView of every Entity to the gameWindow pane
        gameWindow.getChildren().add(player.getSprite().getImageView());
        for (Zombie zombie : zombies) {
            gameWindow.getChildren().add(zombie.getSprite().getImageView());
        }

        // Initialize the Game object, and thus start the game
        game = new Game(player, zombies, gameWindow, playerHP, playerArmor, magazineSize, poolSize, score, equippedWeapon);
        game.setController(this);
        Platform.runLater(this::getKeyPressed);

        sceneChange = new SceneSizeChangeListener(stage.getScene(), 1.6, 1280, 720, gameWindow);
    }

    /***
     * Method which takes in user keyboard input.
     * Some input is handled in the movePlayer() method.
     */
    private void getKeyPressed(){

        gameWindow.getScene().setOnKeyPressed(e -> {
            player.movePlayer(e);
            if (e.getCode() == KeyCode.F12) {
                changeFullScreen();

            } else if (e.getCode() == (KeyCode.P) || e.getCode() == KeyCode.ESCAPE) {
                game.pauseGame();

            } else if (e.getCode() == KeyCode.R) {
                game.setRestartable(true);

            } else if (e.getCode() == KeyCode.M) {
                musicPlayer.muteVolume();
            } else if (e.getCode() == KeyCode.F5){
                System.out.println("Game is saved");
                //saveGame(null);
                //quickSave();
                createSaveFile("",true);
            } else if (e.getCode() == KeyCode.F9) {
                System.out.println("Load game");
                //quickLoad();
                readSaveFile("", true);
            }
        });
        gameWindow.getScene().setOnKeyReleased(e -> {
            player.releasedPlayer(e);
            if (e.getCode() == KeyCode.R)
                game.setRestartable(false);
        });

        /*gameWindow.getScene().setOnKeyPressed(event -> {
            player.movePlayer(event);
            if (event.getCode() == KeyCode.F5){
                System.out.println("Game is saved");
                saveGame();
            }
        });*/
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

            if (!getInitialized())
                loadAssets(zombieList.getLength());

            for (int i = 0; i < zombieList.getLength(); i++) {

                Node nodeZ = zombieList.item(i);

                if (nodeZ.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nodeZ;
                    zombies.add(new Zombie(this.zombieAnimation[i], this.zombieAudioClips,
                            Integer.valueOf(e.getElementsByTagName("PosX").item(0).getTextContent()),
                            Integer.valueOf(e.getElementsByTagName("PosY").item(0).getTextContent()),
                            Integer.valueOf(e.getElementsByTagName("HP").item(0).getTextContent())));
                    gameWindow.getChildren().addAll(zombies.get(i).getNode(), zombies.get(i).getIv());
                }
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

    /***
     * Method which will open a new window with Save option.
     */
    @FXML
    public void saveGame() {
        Parent root;
        try {
            game.pauseGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("saveGame.fxml"));
            root = loader.load();
            InitializeSave initializeSave = loader.getController();
            initializeSave.saveData = new SaveData(positionX, positionY, healthPoints, armor, playerHP, playerArmor, magazineSize, poolSize, score); //.setText(this.playerHP.getText());
            //root = FXMLLoader.load(getClass().getResource("saveGame.fxml"));
            Stage saveGame = new Stage();
            saveGame.setScene(new Scene(root, 600, 400));
            saveGame.show();
        } catch (Exception e) {
            System.out.println("Open SavePane Error");
        }
    }
    /***
     * Method which will open a new window with Load option.
     */
    @FXML
    public void loadGame() {
        Parent root;
        try {
            game.pauseGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menuOptions/LoadMenu.fxml"));
            root = loader.load();
            LoadSavedGame loadSavedGame = loader.getController();
            //root = FXMLLoader.load(getClass().getResource("LoadMenu.fxml"));
            Stage loadGame = new Stage();
            loadGame.setScene(new Scene(root, 600, 400));
            loadGame.show();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Method which finds and loads all necessary assets from disk only once.
     * @param nbrZombies Requires the number of Zombie objects that should be created.
     */
    private void loadAssets(int nbrZombies) {
        String[] playerSounds = {
                "/resources/Sound/Sound Effects/Player/player_breathing_calm.wav",
                "/resources/Sound/Sound Effects/Player/footsteps_single.wav"};
        String[] weaponSounds = {
                "/resources/Sound/Sound Effects/Player/Knife/knife_swish.mp3",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_shot.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_shot.wav",
                "/resources/Sound/Sound Effects/Player/Rifle/rifle_reload.mp3",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_shot.wav",
                "/resources/Sound/Sound Effects/Player/Shotgun/shotgun_reload.wav",
                "/resources/Sound/Sound Effects/Player/Pistol/pistol_empty.mp3"};

        SpriteParam[] knife = {
                new SpriteParam("/resources/Art/Player/knife/idle/survivor-idle_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/move/survivor-move_knife_", ".png", 20),
                new SpriteParam("/resources/Art/Player/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15)};
        SpriteParam[] pistol = {
                new SpriteParam("/resources/Art/Player/handgun/idle/survivor-idle_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/move/survivor-move_handgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/handgun/shoot/survivor-shoot_handgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/handgun/reload/survivor-reload_handgun_", ".png", 15)};
        SpriteParam[] rifle = {
                new SpriteParam("/resources/Art/Player/rifle/idle/survivor-idle_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/move/survivor-move_rifle_", ".png", 20),
                new SpriteParam("/resources/Art/Player/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15),
                new SpriteParam("/resources/Art/Player/rifle/shoot/survivor-shoot_rifle_", ".png", 3),
                new SpriteParam("/resources/Art/Player/rifle/reload/survivor-reload_rifle_", ".png", 20)};
        SpriteParam[] shotgun = {
                new SpriteParam("/resources/Art/Player/shotgun/idle/survivor-idle_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/move/survivor-move_shotgun_", ".png", 20),
                new SpriteParam("/resources/Art/Player/shotgun/meleeattack/survivor-meleeattack_shotgun_", ".png", 15),
                new SpriteParam("/resources/Art/Player/shotgun/shoot/survivor-shoot_shotgun_", ".png", 3),
                new SpriteParam("/resources/Art/Player/shotgun/reload/survivor-reload_shotgun_", ".png", 20)};
        SpriteParam[][] all = {knife, pistol, rifle, shotgun};

        this.basicSounds = loadAudio(playerSounds);
        this.weapon = loadAudio(weaponSounds);
        this.playerAnimation = loadSprites(all);

        String[] zombieSounds = {
                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};

        SpriteParam[] zombieAnimations = {
                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};

        this.zombieAudioClips = loadAudio(zombieSounds);
        this.zombieAnimation = loadSprites(nbrZombies, zombieAnimations);

        //loadZombies(nbrZombies);
    }

//    private void loadZombies(int nbrZombies) {
//        String[] zombieSounds = {
//                "/resources/Sound/Sound Effects/Zombie/zombie_grunt1.wav",
//                "/resources/Sound/Sound Effects/Zombie/zombie_walking_concrete.wav"};
//
//        SpriteParam[] zombieAnimations = {
//                new SpriteParam("/resources/Art/Zombie/skeleton-idle_", ".png", 17),
//                new SpriteParam("/resources/Art/Zombie/skeleton-move_", ".png", 17),
//                new SpriteParam("/resources/Art/Zombie/skeleton-attack_", ".png", 9)};
//
//        this.zombieAudioClips = loadAudio(zombieSounds);
//        this.zombieAnimation = loadSprites(nbrZombies, zombieAnimations);
//    }

    /***
     * Method which is used for loading all the various weapon sprites into a 2-dimensional array.
     * @param sprites Requires a 2-dimensional array of type Sprite
     */
    private Sprite[][] loadSprites(SpriteParam[][] sprites) {
        ImageView iv = new ImageView();
        Sprite[][] outerSprite = new Sprite[sprites.length][];

        for (int i = 0; i < sprites.length; i++) {
            outerSprite[i] = loadSprites(sprites[i], iv);
        }
        return outerSprite;
    }

    /**
     *
     * @param nbrZombies
     * @param sprites
     * @return
     */
    private Sprite[][] loadSprites(int nbrZombies, SpriteParam[] sprites) {
        Sprite[][] outerSprite = new Sprite[nbrZombies][];
        for (int i = 0; i < nbrZombies; i++) {
            ImageView iv = new ImageView();
            outerSprite[i] = loadSprites(sprites, iv);
        }
        return outerSprite;
    }

    /**
     *
     * @param spriteParam
     * @param iv
     * @return
     */
    private Sprite[] loadSprites(SpriteParam[] spriteParam, ImageView iv) {
        Sprite[] sprites = new Sprite[spriteParam.length];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(iv, spriteParam[i].filename, spriteParam[i].extension, spriteParam[i].numberImages);
        }
        return sprites;
    }

    /**
     *
     * @param audioFiles
     * @return
     */
    private AudioClip[] loadAudio(String[] audioFiles) {
        AudioClip[] clips = new AudioClip[audioFiles.length];
        for(int i = 0; i < clips.length; i++) {
            clips[i] = new AudioClip(this.getClass().getResource(audioFiles[i]).toExternalForm());
            clips[i].setVolume(0.1);
        }
        return clips;
    }

    /**
     *
     * @param info
     */
    private void setGameInfoZombie(int[][] info) {
        for (Zombie zombie : zombies) {
            gameWindow.getChildren().removeAll(zombie.getNode(), zombie.getIv());
            zombie.setAlive(false);
        }
        zombies.removeIf(Zombie::isDead);

        //loadZombies(info.length);
        for (int i = 0; i < info.length; i++) {
            zombies.add(new Zombie(this.zombieAnimation[i], this.zombieAudioClips, info[i][0], info[i][1], info[i][2]));
            gameWindow.getChildren().addAll(zombies.get(i).getNode(), zombies.get(i).getIv());
        }
    }

    /***
     *
     * @param visible Requires
     */
    protected void setGameOverLabel(boolean visible) {
        gameState.setVisible(visible);
        gameState.setText("GAME OVER!");
        gameState.setTextFill(Color.INDIANRED);
        pressKey.setVisible(visible);
        pressKey.setText("Press R to Restart");
        pressKey2.setVisible(visible);
        pressKey2.setText("Press ESC to pop up in game Menu...IDK");
        pressKey3.setVisible(visible);
    }

    /***
     *
     * @param visible Requires
     */
    protected void setGameIsPausedLabel(boolean visible){
        gameState.setVisible(visible);
        gameState.setText("GAME IS PAUSED");
        gameState.setTextFill(Color.WHITE);
        pressKey.setVisible(visible);
        pressKey.setText("Press P to Continue");
        pressKey2.setVisible(visible);
        pressKey2.setText("Press R to Restart");
        pressKey3.setVisible(visible);
        pressKey3.setText("Press ESC to pop up in game Menu...IDK");
    }

    /***
     * Method which will change the FullScreen state of the application.
     */
    public void changeFullScreen() {
        Stage stage = (Stage) gameWindow.getScene().getWindow();
        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
            topbar.setVisible(true);
        } else {
            stage.setFullScreen(true);
            topbar.setVisible(false);
        }
    }


    public void exitGame() {
        System.exit(0);
    }

    public boolean getInitialized() {
        return this.initialized;
    }

    /***
     * Inner class used in combination with creating a 2-dimensional Sprite array.
     */
    private class SpriteParam {
        String filename;
        String extension;
        int numberImages;

        private SpriteParam(String filename, String extension, int numberImages) {
            this.filename = filename;
            this.extension = extension;
            this.numberImages = numberImages;
        }
    }

    // For resizing the images, might use
    //        double maxWidth = -1;
//        double maxHeight = -1;
//
//        for(int i = 0; i < this.allAnimation.length; i++) {
//            for (int j = 0; j < this.allAnimation[i].length; j++) {
//                if (this.allAnimation[i][j].getWidth() > maxWidth && this.allAnimation[i][j].getHeight() > maxHeight) {
//                    maxWidth = this.allAnimation[i][j].getWidth();
//                    maxHeight = this.allAnimation[i][j].getHeight();
//                }
//            }
//        }
//
//        for(int i = 0; i < this.allAnimation.length; i++) {
//            for (int j = 0; j < this.allAnimation[i].length; j++) {
//                this.allAnimation[i][j].setMax(maxWidth, maxHeight);
//            }
//        }
//
//        super.getSprite().setMax(maxWidth,maxHeight);
//        //System.out.println(maxWidth);
//        //System.out.println(maxHeight);
}