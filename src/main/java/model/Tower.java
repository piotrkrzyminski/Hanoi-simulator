package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import services.*;

/**
 * Created by Piotr Krzyminski on 05.11.2017
 * Model wieży zawierający graficzną reprezentację obiektu na ekranie,
 * pozycję x i y wieży, wysokość, szerokość
 * Zawiera stos dysków
 */
class Tower {
    private double yDiskPosition; /* pozycja y gdzie mają być umieszczane krążki jeden na drugim */
    private double xPosition; /* pozycja x wieży na ekranie */
    private static final double HEIGHT = 200;
    static final double WIDTH = 10;

    private Stack<Disk> towerStack; /* Stos przechowujący krążki znajdujące się na wieży */

    /**
     * Kontruktor inicjalizujący stos z krążkami
     * @param size maksymalna ilość elementów jaką może pomieścić wieża
     */
    Tower(int size) {
        towerStack = new StackArray<>(size);
    }

    /**
     * Tworzy figurę reprezentującą wieżę na ekranie
     * @param x pozycja x wieży na ekranie
     * @param y pozycja y wieży na ekranie
     * @return figura reprezentująca wieżę
     */
    Rectangle drawTower(double x, double y) {
        xPosition = x;
        double yPosition = y - HEIGHT;
        yDiskPosition = Disk.HEIGHT;

        Rectangle rectangle = new Rectangle(xPosition, yPosition, WIDTH, HEIGHT);
        rectangle.setFill(Color.BROWN);

        return rectangle;
    }

    /**
     * @return pozycja x wieży na ekranie
     */
    double getX() {
        return xPosition;
    }

    /**
     * Zwraca pozycję y gdzie mogą być dodane nowe dyski
     * @return pozycja y wierzchołka stosu krążków
     */
    double getyDiskPosition() {
        return yDiskPosition;
    }

    /**
     * Usuwa dysk z wierzchołka stosu i obniża pozycję y gdzie dodane będą nowe krążki
     * @return obiekt dysku zdjętego ze stosu
     * @throws StackException zwraca wyjątek gdy zostanie podjęta próba zdjęcia stożka z pustego stosu
     */
    Disk removeDisk() throws StackException {
        yDiskPosition-=Disk.HEIGHT;
        return towerStack.pop();
    }

    /**
     * Dodaje nowy krążek do stosu i podwyższa pozycję y gdzie dodane będą nowe krążki
     * @param disk dysk który ma zostać dodany do stosu
     * @throws StackException zwraca wyjątek jeżeli zostanie podjęta próba dodania krążka do zapełnionego stosu
     */
    void addDisk(Disk disk) throws StackException {
        yDiskPosition+=Disk.HEIGHT;
        towerStack.push(disk);
    }

    /**
     * @return ilość stożków na wieży
     */
    int size() {
        return towerStack.size();
    }
}
