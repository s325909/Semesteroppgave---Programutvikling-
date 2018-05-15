package gameCode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

/**
 * Class for handling the Game's settings, so that the user may restart the application with the same settings, and
 * also grant the ability to set settings before the creation of a new Game. Albeit, this is not that dire as
 * adjusting the volume is more intuitive once you can hear the sounds anyhow.
 */
public class SettingsHandler {

    /**
     * Inner class used for handling each of the Game's settings.
     */
    public class SettingsParameters {
        public int horizontalResolution;
        public int verticalResolution;
        public double musicVolume;
        public double soundVolume;

        public SettingsParameters() {
            this.horizontalResolution = 1280;
            this.verticalResolution = 720;
            this.musicVolume = 5;
            this.soundVolume = 2;
        }
    }

    /**
     * Method for retrieving settings from a .xml file.
     * @return Returns an object of type SettingsParameters, which contains the Game's settings.
     */
    public SettingsParameters loadSettings() {
        SettingsParameters settings = new SettingsParameters();
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse("./Data/Settings.xml");
        } catch (IOException ioe) {
            System.out.println("Caught IOException when reading file: " + ioe.getMessage());
            return null;
        } catch (SAXException sax) {
            System.out.println("Caught SAXException when reading file: " + sax.getMessage());
            return null;
        } catch (ParserConfigurationException pce) {
            System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return null;
        }

        doc.getDocumentElement().normalize();

        // Parse volume settings
        NodeList volumeList = doc.getElementsByTagName("Volume");
        Node volumeNode = volumeList.item(0);
        if (volumeNode.getNodeType() == Node.ELEMENT_NODE) {
            Element volumeElement = (Element)volumeNode;
            settings.soundVolume = Double.valueOf(volumeElement.getElementsByTagName("Sound").item(0).getTextContent());
            settings.musicVolume = Double.valueOf(volumeElement.getElementsByTagName("Music").item(0).getTextContent());
        } else {
            return null;
        }

        // Parse window size settings
        NodeList windowList = doc.getElementsByTagName("Size");
        Node windowNode = windowList.item(0);
        if (windowNode.getNodeType() == Node.ELEMENT_NODE) {
            Element windowElement = (Element)windowNode;
            settings.horizontalResolution = Integer.valueOf(windowElement.getElementsByTagName("Width").item(0).getTextContent());
            settings.verticalResolution = Integer.valueOf(windowElement.getElementsByTagName("Height").item(0).getTextContent());
        } else {
            return null;
        }

        return settings;
    }

    /**
     * Method for storing the Game's settings.
     * @param horizontalResolution Requires desired horizontal resolution to store.
     * @param verticalResolution Requires desired vertical resolution to s+tore.
     * @param musicVolume Requires desired music volume to store.
     * @param soundVolume Requires desired sound volume to store.
     */
    public void storeSettings(int horizontalResolution, int verticalResolution, double musicVolume, double soundVolume) {
        System.out.println("Stroing " + musicVolume);
        SettingsParameters settings = new SettingsParameters();
        settings.horizontalResolution = horizontalResolution;
        settings.verticalResolution = verticalResolution;
        settings.musicVolume = musicVolume;
        settings.soundVolume = soundVolume;

        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException pce) {
            System.out.println("Caught ParserConfigurationException when reading file: " + pce.getMessage());
            return;
        }

        Element gameSettings = doc.createElement("GameSettings");
        doc.appendChild(gameSettings);

        Element volume = doc.createElement("Volume");
        gameSettings.appendChild(volume);

        Element sound = doc.createElement("Sound");
        sound.appendChild(doc.createTextNode(String.valueOf(soundVolume)));
        volume.appendChild(sound);

        Element music = doc.createElement("Music");
        music.appendChild(doc.createTextNode(String.valueOf(musicVolume)));
        volume.appendChild(music);

        Element window = doc.createElement("Size");
        gameSettings.appendChild(window);

        Element width = doc.createElement("Width");
        width.appendChild(doc.createTextNode(String.valueOf(horizontalResolution)));
        window.appendChild(width);

        Element height = doc.createElement("Height");
        height.appendChild(doc.createTextNode(String.valueOf(verticalResolution)));
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
            return;
        }
    }
}
