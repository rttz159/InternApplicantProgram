package adt;

/**
 *
 * @author rttz159
 */
public interface Queue<T> extends Iterable<T>{
    void enqueue(T newEntry);
    T dequeue();
    boolean isEmpty();
    int getNumberOfEntries();
    void clear();
    T getFront();
}
