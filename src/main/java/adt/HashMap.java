package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 */
public class HashMap<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>> {

    int INITIAL_CAPACITY = 10;
    ArrayList<SinglyLinkList<Entry<K, V>>> buckets;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;

    public HashMap() {
        buckets = new ArrayList<>(INITIAL_CAPACITY);
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets.append(null);
        }
    }

    private int getHashIdx(K key) {
        return Math.abs(key.hashCode() % buckets.getNumberOfEntries());
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / buckets.getNumberOfEntries() > LOAD_FACTOR) {
            rehash();
        }

        int idx = getHashIdx(key);
        if (buckets.getEntry(idx) == null) {
            buckets.replace(idx, new SinglyLinkList<>());
        }

        SinglyLinkList<Entry<K, V>> bucket = buckets.getEntry(idx);

        Entry<K, V> item = new Entry<>(key, value);

        if (!bucket.contains(item)) {
            bucket.append(item);
            size++;
        } else {
            for (int i = 0; i < bucket.getNumberOfEntries(); i++) {
                if (bucket.getEntry(i).key.equals(item.key)) {
                    bucket.getEntry(i).value = item.value;
                    break;
                }
            }
        }
    }

    @Override
    public V get(K key) {
        if (isEmpty() || buckets.getEntry(getHashIdx(key)) == null) {
            throw new NoSuchElementException();
        }

        int idx = getHashIdx(key);
        SinglyLinkList<Entry<K, V>> bucket = buckets.getEntry(idx);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public V remove(K key) {
        if (isEmpty() || buckets.getEntry(getHashIdx(key)) == null) {
            throw new NoSuchElementException();
        }

        int idx = getHashIdx(key);
        SinglyLinkList<Entry<K, V>> bucket = buckets.getEntry(idx);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                return bucket.remove(entry).value;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean containsKey(K key) {
        if (isEmpty() || buckets.getEntry(getHashIdx(key)) == null) {
            return false;
        }

        int idx = getHashIdx(key);
        SinglyLinkList<Entry<K, V>> bucket = buckets.getEntry(idx);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public SetInterface<K> keySet() {
        if (isEmpty()) {
            return null;
        }

        SetInterface<K> temp = new HashSet<>();

        for (var entry : this) {
            temp.add(entry.getKey());
        }

        return temp;
    }

    @Override
    public ListInterface<V> values() {
        if (isEmpty()) {
            return null;
        }
        ListInterface<V> temp = new ArrayList<>();

        for (var entry : this) {
            temp.append(entry.getValue());
        }

        return temp;
    }

    @Override
    public SetInterface<Map.Entry<K, V>> entrySet() {
        if (isEmpty()) {
            return null;
        }

        SetInterface<Map.Entry<K, V>> temp = new HashSet<>();

        for (var entry : this) {
            temp.add(entry);
        }

        return temp;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private void rehash() {
        ArrayList<SinglyLinkList<Entry<K, V>>> oldBuckets = buckets;
        buckets = new ArrayList<>(oldBuckets.getNumberOfEntries() * 2);
        for (int i = 0; i < oldBuckets.getNumberOfEntries() * 2; i++) {
            buckets.append(null);
        }
        size = 0;

        for (SinglyLinkList<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entries : bucket) {
                    put(entries.key, entries.value);
                }
            }
        }
    }

    private class Entry<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry<K, V> temp = (Entry) obj;
                if (this.key.equals(temp.key)) {
                    return true;
                }
            }
            return false;
        }

    }

    private class HashMapIterator implements Iterator<Map.Entry<K, V>> {

        private int currentBucketIdx;
        private int currentNodeIdx;
        private SinglyLinkList<Entry<K, V>> currentBucket;

        public HashMapIterator() {
            currentBucketIdx = 0;
            currentNodeIdx = 0;
            moveToNextValidBucket();
        }

        @Override
        public boolean hasNext() {
            return currentBucket != null && currentNodeIdx < currentBucket.getNumberOfEntries();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Entry<K, V> tempData = currentBucket.getEntry(currentNodeIdx);
            currentNodeIdx++;

            if (currentNodeIdx >= currentBucket.getNumberOfEntries()) {
                currentBucketIdx++;
                currentNodeIdx = 0;
                moveToNextValidBucket();
            }

            return tempData;
        }

        private void moveToNextValidBucket() {
            while (currentBucketIdx < buckets.getNumberOfEntries()) {
                currentBucket = buckets.getEntry(currentBucketIdx);
                if (currentBucket != null && !currentBucket.isEmpty()) {
                    return;
                }
                currentBucketIdx++;
            }
            currentBucket = null;
        }
    }

}
