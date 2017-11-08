package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorWindowController {
    @FXML
    public Label label; /* pole gdzie wyświetany jest komunikat */

    /**
     * Zamyka onko
     */
    @FXML
    public void closeWindow() {
        Stage window = (Stage)label.getScene().getWindow();
        window.close();
    }
}
