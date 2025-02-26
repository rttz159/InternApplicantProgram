package adt;

import java.util.Comparator;

/**
 *
 * @author rttz159
 */
public interface List <T> extends Iterable<T>{
    void append(T newEntry);
    boolean insert(Integer newPosition, T newEntry);
    T remove(Integer givenPosition);
    void clear();
    boolean replace(Integer givenPosition, T newEntry);
    T getEntry(Integer givenPosition);
    boolean contains(T anEntry);
    int getNumberOfEntries();
    boolean isEmpty();
    boolean isFull();
    T[] toArray();
    void sort();
    void sort(Comparator<T> comparator);
}
