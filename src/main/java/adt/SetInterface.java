package adt;

/**
 *
 * @author rttz159
 */
public interface SetInterface<T> extends Iterable<T> {

    boolean add(T item);
    boolean remove(T item);
    boolean contains(T item);
    boolean isSubSet(SetInterface anotherSet);
    void union(SetInterface anotherSet);
    SetInterface intersection(SetInterface anotherSet);
    <A> boolean isSubSetByAttribute(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor);
    int size();
    boolean isEmpty();
    void clear();
}
