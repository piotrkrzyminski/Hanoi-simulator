package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Hanoi;
import services.StackException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Autor : Piotr Krymiński
 * Zarządzanie interfejsem
 */
public class MainWindowController implements Initializable {
    @FXML
    public Pane hanoiPane; /* panel gdzie rysowane są wieże i krążki */
    @FXML
    public TextArea textArea; /* pole tekstowe do podawania komend */
    @FXML
    public ChoiceBox<String> numberOfDisksBox; /* pole wyboru ilości krążków */
    @FXML
    public ChoiceBox<String> numberOfTowersBox; /* pole wyboru ilości wież */
    @FXML
    public Button button; /* przycisk rozpoczęcia pracy */
    @FXML
    public Button resetButton; /* przycisk czyszczenia danych */
    @FXML
    public Slider delaySlider; /* suwak czasy opóźnienia */
    @FXML
    public Label operationCounterLabel; /* pole wyświetlające ilość wykonanych euchów */

    private Hanoi hanoi;
    private static Stage window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addChoiceBoxvalues(numberOfDisksBox, 1, 20); /* nadaje polu ilości krążków wartości możliwe do wybrania */
        addChoiceBoxvalues(numberOfTowersBox, 3, 8); /* nadaje polu ilości wież wartości możliwe do wybrania */
        numberOfDisksBox.getSelectionModel().select(2); /* ustawia wartość domyślną ilości dysków */
        numberOfTowersBox.getSelectionModel().selectFirst(); /* ustawia wartość domyślną ilości wież */

        Hanoi.setController(this); /* ustawia wartość dla obiektu kontrolera w klasie Hanoi */

        /*
        * Włącz przycisk start jeżeli wartość w polu tekstowym została zmieniona
         */
        textArea.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(false));

        /*
        * Ustaw wartośc opóźnienia w klasie Hanoi po każdej zmianie wartości suwaka. Umożliwia to dynamiczną zmianę szybkości wykonwania przesuwań
         */
        delaySlider.valueChangingProperty().addListener((observable, oldValue, newValue) -> Hanoi.setDelay((long)delaySlider.getValue()));
    }

    /**
     * Akcja dla przycisku startu
     * Pobiera dane o wybranej ilości krążków i wież, a także instrukcji zawartych w polu tekstowym
     * wyjącza możliwość ponownego użycia przycisku start i reset
     * Wywołuje metodę przesywania krążków po wieżach
     * Jeżeli zostanie rzucony wyjątek to wyświetl komunikat o błędzie i przerwij działanie przesunięć
     * Po zakończeniu pracy włącz przycisk resetu
     */
    @FXML
    public void onClickStart() {
        resetButton.setDisable(true); /* wyłącza możliwość wciśnięcia przycisku resetu jeżeli nie zostały zamieszczone żadne instrukcje */
        button.setDisable(true); /* wyłącza możliwość wciśnięcia przycisku startu jeżeli nie zostały zamieszczone żadne instrukcje */
        Stage window = (Stage) hanoiPane.getScene().getWindow();
        int disks = Integer.valueOf(numberOfDisksBox.getSelectionModel().getSelectedItem());
        int towers = Integer.valueOf(numberOfTowersBox.getSelectionModel().getSelectedItem());
        String commands = textArea.getText();

        try {
            hanoi = new Hanoi(disks, towers, commands);
            hanoi.execute();
        } catch (StackException e) {
            Hanoi.wrongMove = true;
            try {
                DisplayErrorWindow.displayWindow(window, e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        resetButton.setDisable(false);
    }

    /**
     * Anuluje działanie wątku wykonującego instrukcje i resetuje dane o poprzednim działaniu algorytmu, usuwa instrukcje z pola tekstowego
     */
    @FXML
    public void onClickReset() {
        hanoi.clear(); /* czyści dane o poprzednim zadaniu */
        hanoi.getService().cancel(); /* anuluje pracę wątku wykonującego instrukcje */
        operationCounterLabel.setText("0"); /* zeruje licznik wykonań */

        textArea.setText("");
        button.setDisable(true);
    }

    /**
     * Tworzy listę wartości, a następnie dodaje wszystkie te wartości do pola wyboru
     * @param choiceBox pole wyboru, do którego mają być dodane wartości
     * @param min minimalna wartość
     * @param max maksymalna wartość
     */
    private void addChoiceBoxvalues(ChoiceBox<String> choiceBox, int min, int max) {
        List<String> options = new ArrayList<>();

        for(int i=min; i<=max; i++)
            options.add(String.valueOf(i));

        choiceBox.setItems(FXCollections.observableArrayList(options));
    }

    /**
     * Pobiera obiekt reprezentujący okno aplikacji z klasy głównej
     * @param window okno aplikacji
     */
    public void setWindow(Stage window) {
        MainWindowController.window = window;
    }

    /**
     * @return okno aplikacji
     */
    public static Stage getWindow() {
        return window;
    }
}
