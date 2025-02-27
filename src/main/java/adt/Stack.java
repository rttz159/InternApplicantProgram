package adt;

/**
 *
 * @author rttz159
 */
public interface Stack<T> {
    int getNumberOfEntries();
    T getFirst();
    T pop();
    void push(T newEntry);
    void clear();
    boolean isFull();
    boolean isEmpty();
}
