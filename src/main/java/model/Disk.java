package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Piotr Krzyminski on 05.11.2017
 * Model dysku zawierający graficzną repreentację, jego pozycję x i y na ekranie, szerokość i wysokość
 */
public class Disk {
    private Rectangle rectangle; /* graficzna reprezentacja krążka */
    private double width; /* szerokość krążka */
    final static double HEIGHT = 10; /* wysokosć każdego krążka */
    final static double WIDTH = 120; /* domyślna szerokosć największego krążka */
    private int index; /* indeks krążka. 1 oznacza krążek najmniejszy. Uniemożliwia położenie krążka o indeksie mniejszym na ten o indeksie większym */

    /**
     * Ustawia indeks krążka
     * @param index indeks krążka
     */
    public Disk(int index) { this.index = index; }

    /**
     * Tworzy figurę prezentującą krążek na ekanie
     * Jest to prostokąt w kolorze odcieni zielonego z czarnymi obwódkami
     * @param x pozycja x krążka na ekranie
     * @param y pozycja y krążka na ekranie
     * @param width szerokość krążka
     * @return figura reprezentująca krążek o wartościach podanych w parametrze
     */
    Rectangle drawDisk(double x, double y, double width) {
        Color color = Color.rgb(0,50+(index*10),0);
        this.width = width;

        rectangle= new Rectangle(x,y,width,HEIGHT);
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);

        return rectangle;
    }

    /**
     * Zwraca indeks krążka
     * @return indeks krążka
     */
    public int getIndex() { return index; }

    /**
     * Zwraca szerokość krążka
     * @return szerokość krążka
     */
    double getWidth() { return width; }

    /**
     * Ustawia figurę krązka w pozycji zadanej przez x i y podane w parametrze
     * @param x nowa pozycja x krązka
     * @param y nowa pozycja y krazka
     */
    void setPosition(double x, double y) {
        rectangle.setX(x);
        rectangle.setY(y);
    }
}
