package adt;

/**
 *
 * @author rttz159
 */
@FunctionalInterface
public interface AttributeExtractor<T, A> {
    A extract(T item);
}
