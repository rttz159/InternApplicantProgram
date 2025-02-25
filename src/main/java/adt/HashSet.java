package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 */
public class HashSet<T> implements Set<T>, Iterable<T> {

    private static final int INITIAL_CAPACITY = 10;
    private static final double LOAD_FACTOR = 0.75;
    private ArrayList<SinglyLinkList<T>> buckets;
    private int size = 0;

    public HashSet() {
        buckets = new ArrayList(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets.append(null);
        }
    }

    @Override
    public boolean add(T item) {
        if ((double) size / buckets.getNumberOfEntries() > LOAD_FACTOR) {
            rehash();
        }

        int index = getBucketIndex(item);
        if (buckets.getEntry(index) == null) {
            buckets.replace(index, new SinglyLinkList<>());
        }

        SinglyLinkList bucket = buckets.getEntry(index);

        if (!bucket.contains(item)) {
            bucket.append(item);
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(T item) {
        if (isEmpty()) {
            return false;
        }

        if (contains(item)) {
            int index = getBucketIndex(item);
            SinglyLinkList bucket = buckets.getEntry(index);
            bucket.remove(item);
            size--;
            return true;
        }

        return false;
    }

    @Override
    public boolean contains(T item) {
        int index = getBucketIndex(item);
        if (buckets.getEntry(index) == null) {
            return false;
        }

        SinglyLinkList bucket = buckets.getEntry(index);
        return bucket.contains(item);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        buckets = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets.append(null);
        }

        size = 0;
    }

    private void rehash() {
        ArrayList<SinglyLinkList<T>> oldBuckets = buckets;
        buckets = new ArrayList<>(oldBuckets.getNumberOfEntries() * 2);
        for (int i = 0; i < oldBuckets.getNumberOfEntries() * 2; i++) {
            buckets.append(null);
        }
        size = 0;

        for (SinglyLinkList<T> bucket : oldBuckets) {
            if (bucket != null) {
                for (T key : bucket) {
                    add(key);
                }
            }
        }
    }

    private int getBucketIndex(T key) {
        return Math.abs(key.hashCode() % buckets.getNumberOfEntries());
    }

    @Override
    public Iterator<T> iterator() {
        return new HashSetIterator();
    }

    private class HashSetIterator implements Iterator<T> {
        private int currentBucketIdx;
        private int currentNodeIdx;
        private SinglyLinkList<T> currentBucket;

        public HashSetIterator() {
            currentBucketIdx = 0;
            currentNodeIdx = 0;
            moveToNextValidBucket();
        }

        @Override
        public boolean hasNext() {
            return currentBucket != null && currentNodeIdx < currentBucket.getNumberOfEntries();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T tempData = currentBucket.getEntry(currentNodeIdx);
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
