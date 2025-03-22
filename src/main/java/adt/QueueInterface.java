package adt;

/**
 *
 * @author Raymond
 */
public interface QueueInterface<T> extends Iterable<T>{
    void enqueue(T newEntry);
    T dequeue();
    boolean isEmpty();
    int getNumberOfEntries();
    void clear();
    T getFront();
}
