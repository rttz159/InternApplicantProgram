package adt;

/**
 *
 * @author rttz159
 */
public interface MapInterface<K, V> extends Iterable<MapInterface.Entry<K, V>>{

    void put(K key, V value);
    V get(K key);
    V remove(K key);
    boolean containsKey(K key);
    boolean containsValue(V value);
    int size();
    boolean isEmpty();
    SetInterface<K> keySet();
    ListInterface<V> values();
    SetInterface<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V value);
    }
}
