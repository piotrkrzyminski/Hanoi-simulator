package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Autor: Piotr Krzymiński
 * Wyświetla nowe okno z komunikatem o błędzie
 */
public class DisplayErrorWindow {
    public static  void displayWindow(Stage parentStage, String text) throws IOException {
        /* wczytuje plik opisujący wygląd aplikacji */
        FXMLLoader loader = new FXMLLoader(DisplayErrorWindow.class.getResource("/FXML/errorWindow.fxml"));

        Parent root = loader.load();
        ErrorWindowController controller = loader.getController(); /* wczytuje kontroler dla okna  */

        Stage stage = new Stage();

        if (root != null)
            stage.setScene(new Scene(root)); /* Tworzy nowe okno */

        controller.label.setText(text); /* nadaje polu tekstowemu wartość podaną w parametrze funkcji */
        stage.setResizable(false);
        stage.setTitle("Błąd");

        stage.initOwner(parentStage); /* Ustala okno nadrzędne*/
        stage.initModality(Modality.APPLICATION_MODAL); /* Uniemożliwia używanie głównego okna jeżeli wyświetlone jest to okno */
        stage.showAndWait(); /* wyświetla okno na ekranie i czekaj na zamknięcie */
    }
}