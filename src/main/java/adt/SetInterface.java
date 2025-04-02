package adt;

/**
 *
 * @author Raymond
 */
public interface SetInterface<T> extends Iterable<T> {

    boolean add(T item);
    boolean remove(T item);
    boolean contains(T item);
    boolean isSubSet(SetInterface anotherSet);
    void union(SetInterface anotherSet);
    SetInterface intersection(SetInterface anotherSet);
    <A> boolean isSubSetByAttribute(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor);
    <A,B extends Comparable<B>> boolean isSupSetByAttributes(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor, AttributeExtractor<T, B> levelExtractor);
    <B extends Comparable<B>> boolean isSupSetByLevelAttributes(SetInterface<T> anotherSet, AttributeExtractor<T, B> levelExtractor);
    <A,B extends Comparable<B>> double fulfillmentScore(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor, AttributeExtractor<T, B> levelExtractor);
    int size();
    boolean isEmpty();
    void clear();
}
