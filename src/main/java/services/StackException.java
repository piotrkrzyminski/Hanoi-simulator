package services;

/**
 * Autor: Piotr Krzymiński
 * Wyjątek rzucany kiedy następuje próba umieszczenia większego krążka na mniejszy
 */
public class StackException extends Exception {
    StackException(String msg) {
        super(msg);
    }
}
