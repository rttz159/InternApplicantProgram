package adt;

/**
 *
 * @author Raymond
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
