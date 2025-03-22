package adt;

/**
 *
 * @author Raymond
 */
@FunctionalInterface
public interface AttributeExtractor<T, A> {
    A extract(T item);
}
