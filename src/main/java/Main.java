import controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        /* wczytuje plik opisujący wygląd aplikacji */
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXML/MainWindow.fxml"));

        Parent root = loader.load();
        MainWindowController controller = loader.getController(); /* wczytuje kontroler dla okna */

        /* tworzy nową scenę */
        if(root != null) {
            primaryStage.setScene(new Scene(root));
        }

        controller.setWindow(primaryStage); /* ustaw wartość obiektu okna w kontrolerze */

        primaryStage.setTitle("Wieże Hanoi - symulacja ruchów");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        primaryStage.show(); /* wyświetla onko na ekranie */
    }

    public static void main(String[] args) {
        launch(args);
    }
}