package adt;

/**
 *
 * @author rttz159
 */
public interface Map<K, V> extends Iterable<Map.Entry<K, V>>{

    void put(K key, V value);
    V get(K key);
    V remove(K key);
    boolean containsKey(K key);
    boolean containsValue(V value);
    int size();
    boolean isEmpty();
    Set<K> keySet();
    List<V> values();
    Set<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V value);
    }
}
