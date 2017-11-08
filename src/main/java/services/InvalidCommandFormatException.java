package services;

/**
 * Autor: Piotr Krzymiński
 * Wyjątek rzucany kiedy instrukcja ma niepoprawny format, to znaczy ma nieodpowiedni format lub indeks wieży
 * Uniemożliwia wykonanie instrukcji niezgodnej z zasadami wieży Hanoi
 */
public class InvalidCommandFormatException extends Exception {
    InvalidCommandFormatException(String msg) {
        super(msg);
    }
}
