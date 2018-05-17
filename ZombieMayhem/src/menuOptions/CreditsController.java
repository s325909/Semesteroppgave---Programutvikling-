package menuOptions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * This class controls the Credits.fxml,
 * and is used to show credits and make a returnToMenu button
 */
public class CreditsController implements Initializable {

    @FXML private Button backToMenu;
    @FXML private TextArea creditsText;

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
//            Scanner file = new Scanner(new File(this.getClass().getResource("/resources/Credits.txt").toExternalForm()));
//            while(file.hasNextLine())
//                creditsText.appendText(file.nextLine() + "\n");

            FileReader fr = new FileReader(this.getClass().getResource("/resources/Credits.txt").toExternalForm());
            BufferedReader br = new BufferedReader(fr);

            String str;
            while((str = br.readLine()) != null) {
                creditsText.appendText(str + "\n");
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            creditsText.appendText("Unable to find credits file");
        }
    }

    /**
     * Method that makes it possible to return to the main menu
     */
    @FXML
    public void returnToMenu() {
        try{
            Stage stage = (Stage) backToMenu.getScene().getWindow();
            URL url = getClass().getResource("/main/MainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
