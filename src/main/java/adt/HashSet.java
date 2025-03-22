package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Raymond
 */
public class HashSet<T> implements SetInterface<T> {

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

    @Override
    public boolean isSubSet(SetInterface anotherSet) {
        if (anotherSet == null || !(anotherSet instanceof HashSet)) {
            return false;
        }

        HashSet<T> tempSet = (HashSet) anotherSet;
        if (this.size > tempSet.size) {
            return false;
        }

        boolean valid = true;
        for (var x : this) {
            boolean found = false;
            for (var y : tempSet) {
                if (y.equals(x)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    @Override
    public <A> boolean isSubSetByAttribute(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor) {
        if (anotherSet == null || !(anotherSet instanceof HashSet)) {
            return false;
        }

        HashSet<T> tempSet = (HashSet<T>) anotherSet;
        if (this.size > tempSet.size) {
            return false;
        }

        for (T x : this) {
            boolean found = false;
            A attributeX = extractor.extract(x);

            for (T y : tempSet) {
                A attributeY = extractor.extract(y);
                if (attributeX.equals(attributeY)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <A, B extends Comparable<B>> boolean isSupSetByAttributes(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor, AttributeExtractor<T, B> levelExtractor) {
        if (anotherSet == null || !(anotherSet instanceof HashSet)) {
            return false;
        }

        HashSet<T> tempSet = (HashSet<T>) anotherSet;
        if (this.size < tempSet.size) {
            return false;
        }

        for (T x : tempSet) {
            boolean found = false;
            A attributeX = extractor.extract(x);
            B levelX = levelExtractor.extract(x);

            for (T y : this) {
                A attributeY = extractor.extract(y);
                B levelY = levelExtractor.extract(y);
                if (attributeX.equals(attributeY) && levelY.compareTo(levelX) >= 0) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <A, B extends Comparable<B>> double fulfillmentScore(SetInterface<T> anotherSet, AttributeExtractor<T, A> extractor, AttributeExtractor<T, B> levelExtractor) {
        if (anotherSet.isEmpty()) {
            return 1.0;
        }
        int totalMatch = 0;
        for (T x : this) {
            A attributeX = extractor.extract(x);
            B levelX = levelExtractor.extract(x);
            for (T y : anotherSet) {
                A attributeY = extractor.extract(y);
                B levelY = levelExtractor.extract(y);
                if (attributeX.equals(attributeY) && levelX.compareTo(levelY) != -1) {
                    totalMatch++;
                    break;
                }
            }
        }
        return (double) totalMatch / anotherSet.size();
    }

    @Override
    public void union(SetInterface anotherSet) {
        if (anotherSet == null || !(anotherSet instanceof HashSet)) {
            return;
        }

        HashSet<T> tempSet = (HashSet) anotherSet;
        for (var x : tempSet) {
            this.add(x);
        }
    }

    @Override
    public SetInterface intersection(SetInterface anotherSet) {
        SetInterface tempSet = new HashSet<T>();
        HashSet<T> anotherHashSet = (HashSet) anotherSet;
        for (var y : anotherHashSet) {
            if (this.contains(y)) {
                tempSet.add(y);
            }
        }
        return tempSet;
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
