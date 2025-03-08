package adt;

/**
 *
 * @author rttz159
 */
public interface TreeInterface<T> extends Iterable<T>{
    public void add(T newEntry);
    public void remove(T entry);
    public void clear();
    public int getHeight();
    public int getSize();
    public boolean contains(T entry);
    public boolean isEmpty();
}
