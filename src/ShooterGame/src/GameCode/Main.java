package GameCode;

import Entities.Enemy;
import Entities.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Scene menu_scene, game_scene, load_scene, options_scene, help_scene;
    Label menu_label, game_label, load_label, options_label, help_label;

    int btnWidth = 180;
    int btnHeight = 70;

    Button btn1, btn2, btn3, btn4, btn5;

    Font font1, font2;
    Font font3 = new Font("Ariel", 24);



    Player player;
    List<Enemy> enemyList = new ArrayList<Enemy>();

    private void onUpdate(double time) {
        player.update(enemyList, time);

        for (Enemy enemy : enemyList)
            if (player.isColliding(enemy)) {
                System.out.println("Collision!");
                System.out.println("Player hit for " + player.getHealthPoints());
            }
    }

    private void initializeEntity() {
        try {
            player = new Player("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20, 500, 500, 100);
        } catch (Exception e) {

        }


        for (int i = 0; i < 5; i++) {
            enemyList.add(new Enemy(new Circle(25,25,50, Color.RED), (int)(Math.random() * 1280), (int)(Math.random() * 720)));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{


        initializeEntity();

        Pane topMenu = FXMLLoader.load(getClass().getResource("GameUI.fxml"));




//The Game Window
        VBox game_layout = new VBox(25);
        game_layout.setAlignment(Pos.CENTER);
        game_label = new Label("Starting new game...");
        game_label.setFont(font2);


        for (Enemy enemy : enemyList)
            game_layout.getChildren().add(enemy.getNode());


        final long startNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = (now - startNanoTime) / 1000000000.0;
                onUpdate(t);
            }
        };


        PlayerInput playerInput = new PlayerInput(window, player);


        timer.start();


        game_layout.getChildren().addAll(topMenu, game_label, player.getNode(), player.getSprite().getImageView());
        game_scene = new Scene(game_layout, 900, 600);






        window = primaryStage;
        window.setTitle("The Issac Game");

        font1 = Font.font("Ariel", FontWeight.BOLD, 36);
        font2 = Font.font("Ariel", FontPosture.ITALIC, 24);


        //New Game
        btn1 = new Button("New Game");
        btn1.setMinSize(btnWidth, btnHeight);
        btn1.setOnAction( e -> window.setScene(game_scene) );

        //Load Game
        btn2 = new Button("Load Game");
        btn2.setMinSize(btnWidth, btnHeight);
        btn2.setOnAction( e -> window.setScene(load_scene) );

        //Options
        btn3 = new Button("Options");
        btn3.setMinSize(btnWidth, btnHeight);
        btn3.setOnAction( e -> window.setScene(options_scene) );

        //Help
        btn4 = new Button("Help");
        btn4.setMinSize(btnWidth, btnHeight);
        btn4.setOnAction( e -> window.setScene(help_scene) );

        //Exit Game
        btn5 = new Button("Exit Game");
        btn5.setMinSize(btnWidth, btnHeight);
        btn5.setOnAction( e -> window.close() );


        //The Game Menu
        VBox menu_layout = new VBox(25);
        menu_label = new Label("The Title of our Game");
        menu_label.setFont(font1);
        menu_layout.getChildren().addAll(menu_label, btn1, btn2, btn3, btn4, btn5);
        menu_layout.setAlignment(Pos.CENTER);
        menu_scene = new Scene(menu_layout, 900, 600);

        // Return to Game Menu
        Button return_btn1 = new Button("Return to Game Menu");
        return_btn1.setOnAction( e -> window.setScene(menu_scene));
        Button return_btn2 = new Button("Return to Game Menu");
        return_btn2.setOnAction( e -> window.setScene(menu_scene));
        Button return_btn3 = new Button("Return to Game Menu");
        return_btn3.setOnAction( e -> window.setScene(menu_scene));
        Button return_btn4 = new Button("Return to Game Menu");
        return_btn4.setOnAction( e -> window.setScene(menu_scene));

    /*
        //The Game Window
        VBox game_layout = new VBox(25);
        game_layout.setAlignment(Pos.CENTER);
        game_label = new Label("Starting new game...");
        game_label.setFont(font2);
        game_layout.getChildren().addAll(topMenu, game_label, player.getNode(), player.getSprite().getImageView(),return_btn1);
        game_scene = new Scene(game_layout, 900, 600);
    */


        //The Load menu
        VBox load_layout = new VBox(25);
        load_label = new Label("Load a game...");
        load_label.setFont(font3);
        Button game1 = new Button("Load File1");
        game1.setMinSize(btnWidth, btnHeight);
        Button game2 = new Button("Load File2");
        game2.setMinSize(btnWidth, btnHeight);
        Button game3 = new Button("Load File3");
        game3.setMinSize(btnWidth, btnHeight);
        load_layout.getChildren().addAll(load_label, game1, game2, game3, return_btn2);
        load_scene = new Scene(load_layout, 900, 600);

        //The Options Menu
        VBox options_layout = new VBox(25);
        options_label = new Label("Options...");
        options_layout.getChildren().addAll(options_label, return_btn3);
        options_scene = new Scene(options_layout);

        //The Help Menu
        VBox help_layout = new VBox(25);
        help_label = new Label("How to play:");
        Label help1 = new Label("Press W to move Forward");
        Label help2 = new Label("Press S to move Backward");
        Label help3 = new Label("Press A to move Left");
        Label help4 = new Label("Press D to move Right");
        Label help5 = new Label("Press Spacebar to Shoot enemies");
        help_layout.getChildren().addAll(help_label, help1, help2, help3, help4, help5, return_btn4);
        help_scene = new Scene(help_layout, 900, 600);

        //Window shown when starting the game
        StackPane layout1 = new StackPane();
        layout1.getChildren().add(menu_layout);
        Scene scene = new Scene(layout1, 900, 600);
        window.setScene(scene);
        window.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
