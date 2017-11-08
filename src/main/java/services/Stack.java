package services;

public interface Stack<T> {
    void push(T t) throws StackException;
    T pop() throws  StackException;
    int size();
    boolean isEmpty();
    boolean isFull();
}
