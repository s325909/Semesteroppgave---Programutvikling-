package menuOptions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/***
 * This class controls the Credits.fxml,
 * and is used to show credits and make a returnToMenu button
 */
public class CreditsController implements Initializable {

    @FXML private Button backToMenu;
    @FXML private TextArea creditsText;
    private Stage window_GameMenu;
    private Parent root_GameMenu;

    /**
     * Fetches a text document containing credits,
     * which is shown upon opening the Credits.fxml
     * @param url Method is run upon Credits FXML being loaded.
     * @param resourceBundle Method is run upon Credits FXML being loaded.
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            creditsText.setEditable(false);
            Scanner file = new Scanner(new File("src/resources/Credits.txt"));
            while(file.hasNextLine())
                creditsText.appendText(file.nextLine() + "\n");
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
            creditsText.appendText("Unable to find credits file");
        }
    }

    /**
     * Method that makes it possible to return to the main menu
     * @param event that takes the Button's id
     * @throws IOException
     */
    @FXML
    public void returnToMenu(ActionEvent event) throws IOException {
        try {
            if (event.getSource() == backToMenu) {
                window_GameMenu = (Stage) backToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/MainMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
        window_GameMenu.setScene(scene_GameMenu);
        window_GameMenu.show();
    }
}
