package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //puts title on top of window
    public static void setTitle(String title) {
        Main.stage.setTitle(title);
    }

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("GameMenu.fxml"));
        primaryStage.setTitle("The Game");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}