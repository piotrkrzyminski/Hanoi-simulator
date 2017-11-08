package model;

import controller.DisplayErrorWindow;
import controller.MainWindowController;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import services.Commands;
import services.InvalidCommandFormatException;
import services.StackException;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Piotr Krzyminski on 05.11.2017
 * Model wieży Hanoi odpowiadający za wszystkie mechanizmy przemieszczania się krążków po wieżach
 */
public class Hanoi {
    private Service<Void> service; /* wątek wykonujący instrukcje */
    private static MainWindowController controller; /* odnośnik do kontrolera */
    private Commands commandParser; /* obiekt odpowiadający za analizę komend z pola tekstowego */
    private static long delay = 500; /* przerwa jaką należy wykonać po każdej operacji przemieszczenia krążka wyrażona w milisekundach */

    private static Stage window; /* odnośnik do okna aplikacji */
    private static Pane pane;

    private int diskCount; /* number of Disks */
    private int towerCount; /* number of towers */
    private String commands; /* tekst komend pobranych z pola tekstowego */
    private long counter = 0; /* licznik wykonań ruchów */

    private Tower[] towersArray; /* tablica przechowująca wszystkie wieże */

    public static boolean wrongMove = false; /* flaga oznaczająca czy ruch do wykonania jest poprawny */

    /**
     * Konstruktor inicjalizujący najważniejsze pola obiektu
     * Inicjalizuje pierwszą wieżę dodając do niej wszystkie krążki
     * @param disks liczba dysków
     * @param towers liczba wież
     * @param commands ciąg instrukcji do wykonania
     * @throws StackException zwraca wyjątek jeżeli istrukcje są niepoprawne
     */
    public Hanoi(int disks, int towers, String commands) throws StackException {
        window = MainWindowController.getWindow();
        pane = controller.hanoiPane;
        diskCount = disks;
        towerCount = towers;
        this.commands = commands;

        initialize();
    }

    /**
     * Tworzy nowe wieże i rysuje je na ekranie
     */
    private void createTowers() {
        /* Pozycje x i y wieży na ekranie */
        double width = 10;
        double x = 150 - width/2;
        double y = pane.getHeight();

        /*
        Dodaje nowe wieże do tablicy i rysuje każą z nich na ekranie
        Koordynaty x każdej z nich są przesuwane o 145 jednostek względem siebie
         */
        for(int i=0; i<towerCount; i++) {
            Tower tower = new Tower(diskCount);
            Rectangle towerGraphic = tower.drawTower(x*(i+1), y);
            towersArray[i] = tower;
            controller.hanoiPane.getChildren().add(towerGraphic);
        }
    }

    /**
     * Tworzy obiekty wież i zapisuje je w tablicy
     * Do pierwszej wieży dodaje wszystkie krążki
     * @throws StackException zwraca wyjątek jeżeli dodawanie do stosu nowych krążków się nie powiedzie
     */
    private void initialize() throws StackException {
        towersArray = new Tower[towerCount];

        createTowers();

        double width = Disk.WIDTH;
        double resizeValue = 4; /* wartosć o jaką ma być zmnniejszana szerokość każdego krążka, aby układały się w piramidę */
        double x = 150 - width/2;
        double y = pane.getHeight();

        for(int i=0; i<diskCount; i++) {
            Disk disk = new Disk(i+1);
            width -= resizeValue; /* zmniejsza szerokość krążka */
            x += resizeValue/2; /* przesuwa krążek o połowę wartości zmniejszenia szerokości, aby był nadal wyśrodkowany */
            y -= Disk.HEIGHT; /* podnosi wartość y dla nowego krążka, aby były ułożone jeden na drugim */
            Rectangle rectangle = disk.drawDisk(x,y,width);

            towersArray[0].addDisk(disk); /* dodaje nowy krążek do pierwszej wieży */
            pane.getChildren().add(rectangle);
        }
    }

    /**Odczytuje instrukcje linia po linii
     * Uruchamia nowy wątek odpowiedzialny za analizowanie poleceń i przemieszczanie krążków między wieżami
     */
    public void execute() {
        service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        final CountDownLatch latch = new CountDownLatch(1);
                        commandParser = new Commands(towerCount);

                        /* oddziel polecenia na oddzielne linie */
                        for (String line : commands.split("\\n")) {
                            int[] command;

                            /* analizuje polecenia pod kątem błędów
                            *  jeżeli jakieś polecenie będzie błędne rzuca wyjątek
                            *  a następnie wyświetla okno z komunikatem o błędzie
                            */

                            try {
                                command = commandParser.parse(line);
                            } catch (InvalidCommandFormatException e) {
                                Platform.runLater(() -> {
                                    try {
                                        DisplayErrorWindow.displayWindow(window, e.getMessage());
                                    } catch (IOException e1) { e.printStackTrace(); }
                                });
                                return null;
                            }

                            /* pobiera dwie wieże. Jedna to ta, z której należy zabrać krążek, a druga to ta do której trzeba krążek dodać */
                            Tower from = towersArray[command[0]];
                            Tower to = towersArray[command[1]];

                            /*
                            * wykonuje przemieszczenie krążków z jednej wieży na drugą
                            * Jeżeli rzucony zostaje wyjątek wyświetl komunikat o błędzie
                             */
                            try {
                                move(from, to);
                            } catch (StackException e) {
                                Platform.runLater(() -> {
                                    try {
                                        DisplayErrorWindow.displayWindow(window, e.getMessage());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                });
                                return null;
                            }
                        }
                        latch.await();

                        return null;
                    }
                };
            }
        };

        service.start(); /* rozpoczyna wykonywanie zadania w innym wątku */
    }

    /**
     * Zdejmuje krążki ze stosu wież z parametrów
     * Odwołując się do wątku głównego zmień położenie x i y krążków w oknie
     * @param from Wieża, z której należy zabrać krążek
     * @param to wieża do której zostanie dodany krążek
     * @throws StackException zwraca wyjątek jeżeli zostanie wykonana próba położenia większego krążka na mniejszy
     * lub próba zabrania krążka z pustej wieży
     */
    private void move(Tower from, Tower to) throws InterruptedException, StackException {
        Thread.sleep(delay); /* uśpij wątek wykonujący przeniesienia na czas określony za pomocą suwaka */
        Disk disk = from.removeDisk(); /* zdejmij dysk ze stosu wieży */

        /* Oblicz nowe wartości x i y dla krążka */
        double diskXDestination = to.getX() - disk.getWidth()/2+Tower.WIDTH/2;
        double diskYDestination = pane.getHeight()-to.getyDiskPosition();


        /* Odwołaj się do wątku głównego rysującego aplikację
         * Wykonaj przeniesienie krążka z jednego słupka do drugiego jeżeli wykonywany ruch jest poprawny i nie powoduje błędów
         * Jeżeli ruch powoduje wywołanie wyjątku wyświetl komunikat w nowym oknie i przerwij wykonywanie algorytmu
         */
        Platform.runLater(() -> {
            if(!wrongMove) {
                disk.setPosition(diskXDestination, diskYDestination);
                controller.operationCounterLabel.setText(String.valueOf(++counter));
            } else {
                try {
                    DisplayErrorWindow.displayWindow(window, "Błąd!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        to.addDisk(disk); /* Dodaj zabrany krązek do nowej wieży */
    }

    /**
     * Ustawia opóźnienie pomiędzy kolejnymi przesunięciami
     * @param d wartość opóźnienia
     */
    public static void setDelay(long d) {
        delay = d;
    }

    /**
     * Pobiera obiekt kontrolera widoku
     * @param c obiekt klasy kontrolera
     */
    public static void setController(MainWindowController c) {
        controller = c;
    }

    /**
     * @return odnośnik do wątku wykonującego instrukcje
     */
    public Service<Void> getService() {
        return service;
    }

    public void clear() {
        for(int i=0; i<towersArray.length; i++) {
            towersArray[i] = null;
        }

        controller.hanoiPane.getChildren().clear();
    }
}
