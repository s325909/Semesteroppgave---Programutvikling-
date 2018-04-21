package menuOptions;

import gameCode.InitializeGame;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import notCurrentlyUsed.SaveLoadManager;

import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    @FXML private Button returnToMenu, loadBtn1, loadBtn2, loadBtn3;

    private Stage window_GameMenu;
    private Parent root_GameMenu;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        try {
            if (event.getSource() == returnToMenu) {
                window_GameMenu = (Stage) returnToMenu.getScene().getWindow();
                root_GameMenu = FXMLLoader.load(getClass().getResource("../main/GameMenu.fxml"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        Scene scene_GameMenu = new Scene(root_GameMenu, 1280, 720);
        window_GameMenu.setScene(scene_GameMenu);
        window_GameMenu.show();
    }



    /***
     * Method which establishes load function by clicking load button.
     */
    @FXML
    public void loadGame(ActionEvent event) {

            try{
                Stage stage = (Stage) loadBtn1.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("../gameCode/GameWindow.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("../menuOptions/StylesMenu.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

                if (event.getSource() == loadBtn1 || event.getSource() == loadBtn2 || event.getSource() == loadBtn3) {

                }


            }catch (IOException io){
                io.printStackTrace();
            }

//            try{
//                SaveLoadManager.load("game1.save");
//            } catch(Exception e) {
//                System.out.println("Couldn't load saved data");
//            }

//
//        loadBtn2.setOnAction(event->{
//            try{
//                SaveLoadManager.load("game2.save");
//            } catch(Exception e) {
//                System.out.println("Couldn't load saved data");
//            }
//        });
//
//        loadBtn3.setOnAction(event->{
//            try{
//                SaveLoadManager.load("game3.save");
//            } catch(Exception e) {
//                System.out.println("Couldn't load saved data");
//            }
//        });
    }

}

