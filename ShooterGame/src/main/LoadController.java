package main;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadController implements Initializable {

    Stage loadWindow;
    Parent root;

    @FXML
    Button saveFile1, saveFile2, saveFile3, backBTN;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

    }


    public void loadFile1() {

        System.out.println("loading file 1");

    }

    public void loadFile2() {

        System.out.println("loading file 3");

    }

    public void loadFile3() {

        System.out.println("loading file 3");

    }


    public void backToMenu(ActionEvent event) throws IOException {

        try {
            if (event.getSource() == backBTN) {
                loadWindow = (Stage) backBTN.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("GameMenu.fxml"));
            }
            }catch (Exception e){
            System.out.println("Error");
            System.out.println(e.getMessage());
        }

        Scene scene = new Scene(root, 900, 900);
        loadWindow.setScene(scene);
        loadWindow.show();
    }

}