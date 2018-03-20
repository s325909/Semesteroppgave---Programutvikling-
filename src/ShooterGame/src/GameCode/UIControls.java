package GameCode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UIControls {

    @FXML private MenuBar menuBar;
    @FXML private Button fullScreen;
    @FXML private GridPane TopMenu;

    @FXML
    public void exitGame(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void fullScreen(ActionEvent event) {
        Stage stage = (Stage) fullScreen.getScene().getWindow();

        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            TopMenu.setVisible(true);
        }
        else {
            stage.setFullScreen(true);
            TopMenu.setVisible(false);
        }
    }
}
