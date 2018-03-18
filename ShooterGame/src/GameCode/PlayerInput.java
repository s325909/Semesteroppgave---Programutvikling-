package GameCode;

import Entities.Player;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PlayerInput {

    public PlayerInput(Stage stage, Player player) {
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                player.goLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.goRight();
            } else if (e.getCode() == KeyCode.UP && e.getCode() != KeyCode.DOWN) {
                player.goUp();
            } else if (e.getCode() == KeyCode.DOWN && e.getCode() != KeyCode.UP) {
                player.goDown();
            } else if (e.getCode() == KeyCode.F12) {
                if (stage.isFullScreen())
                    stage.setFullScreen(false);
                else
                    stage.setFullScreen(true);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

        stage.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                player.stopX();
            } else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                player.stopY();
            }
        });
    }

}
