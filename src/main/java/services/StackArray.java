package services;

import model.Disk;

/**
 * Created by Piotr Krzyminski on 05.11.2017
 *
 * Zarządza porządkowaniem krążków na wieżach
 * Jeżeli top wynosi -1 to stos jest pusty. Wskazuje ona indeks w tablicy na element, który ma zostać zdjęty jako pierwszy
 * Każde dodanie lub usunięcie elemntu zwiększa lub zmniejsza wartośc zmiennej top
 * @param <T>
 */
public class StackArray<T extends Disk> implements Stack<T> {
    private T[] array;

    private int maxSize;
    private int size;
    private int top;

    /**
     * Inicjalizuje tablicę obiektów
     * @param s maksymalny rozmiar stosu
     */
    public StackArray(int s) {
        maxSize = s;
        size = 0;
        top = -1;

        array = (T[]) new Disk[maxSize];
    }

    /**
     * Dodaje nowy obiekt do stosu
     * @param t Obiekt, który ma zostać dodany
     * @throws StackException stos jest pełny i nie można dodać nowego elementu
     */
    public void push(T t) throws StackException {
        if(isFull()) throw new StackException("Nie można dodać nowego elementu. Wieża jest pełny.");

        if(!isEmpty()) {
            if(t.getIndex()<array[top].getIndex()) throw new StackException("Instrukcja zawiera bład: Nie można umieszczać większego krążka na mniejszy");
        }

        array[++top] = t;
        size++;
    }

    /**
     * Pobiera element ze stosu
     * @return pobrany element ze stosu
     * @throws StackException stos jest pusty i nie można usunąć żadnego elementu
     */
    public T pop() throws StackException {
        if(isEmpty()) throw new StackException("Instrukcja zawiera bład: Nie można zdjąć elementu z wieży, ponieważ jest ona pusta.");

        size--;
        return array[top--];
    }

    /**
     * @return wielkość tablicy
     */
    public int size() {
        return size;
    }

    /**
     * @return stos jest pusty lub nie
     */
    public boolean isEmpty() {
        return(size==0);
    }

    /**
     * @return stos jest pełny lub nie
     */
    public boolean isFull() {
        return(size==maxSize);
    }
}