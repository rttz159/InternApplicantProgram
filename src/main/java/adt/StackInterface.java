package adt;

/**
 *
 * @author rttz159
 */
public interface StackInterface<T> extends Iterable<T>{
    int getNumberOfEntries();
    T getFirst();
    T pop();
    void push(T newEntry);
    void clear();
    boolean isFull();
    boolean isEmpty();
}
