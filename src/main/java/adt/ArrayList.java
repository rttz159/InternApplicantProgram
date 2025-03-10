package adt;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 * @param <T>
 */
public class ArrayList<T> implements ListInterface<T>{

    private int numberOfEntries;
    private int capacity;
    private T[] arrayInstance;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        numberOfEntries = 0;
        capacity = 10;
        arrayInstance = (T[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        numberOfEntries = 0;
        this.capacity = capacity;
        arrayInstance = (T[]) new Object[capacity];
    }

    @Override
    public void append(T newEntry) {
        if (isFull()) {
            this.expandCapacity();
        }
        this.arrayInstance[numberOfEntries] = newEntry;
        this.numberOfEntries++;
    }

    @Override
    public boolean insert(Integer newPosition, T newEntry) {
        if (newPosition < 0 || newPosition > numberOfEntries) {
            return false;
        }

        if (isFull()) {
            this.expandCapacity();
        }

        if (newPosition == numberOfEntries) {
            append(newEntry);
        } else {
            int numberOfItemsToCopy = numberOfEntries - newPosition;
            System.arraycopy(arrayInstance, newPosition, arrayInstance, newPosition + 1, numberOfItemsToCopy);
            arrayInstance[newPosition] = newEntry;
            numberOfEntries++;
        }
        return true;
    }

    @Override
    public T remove(Integer givenPosition) {
        if (givenPosition < 0 || givenPosition > (this.numberOfEntries - 1) || isEmpty()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (givenPosition == this.numberOfEntries - 1) {
            T entry = this.arrayInstance[givenPosition];
            this.arrayInstance[givenPosition] = null;
            this.numberOfEntries--;
            return entry;
        }

        T entry = this.arrayInstance[givenPosition];
        int numberOfItemsNeedToCopy = this.numberOfEntries - (givenPosition + 1);
        System.arraycopy(this.arrayInstance, givenPosition + 1, this.arrayInstance, givenPosition, numberOfItemsNeedToCopy);
        this.numberOfEntries--;
        return entry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        if (!isEmpty()) {
            this.numberOfEntries = 0;
            this.capacity = 10;
            this.arrayInstance = (T[]) new Object[this.capacity];
        }
    }

    @Override
    public boolean replace(Integer givenPosition, T newEntry) {
        if (givenPosition < 0 || givenPosition > (this.numberOfEntries - 1)) {
            return false;
        }

        this.arrayInstance[givenPosition] = newEntry;
        return true;
    }

    @Override
    public T getEntry(Integer givenPosition) {
        if (givenPosition < 0 || givenPosition > (this.numberOfEntries - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return this.arrayInstance[givenPosition];
    }

    @Override
    public boolean contains(T anEntry) {
        if (isEmpty()) {
            return false;
        }

        for (int i = 0; i < numberOfEntries; i++) {
            if (arrayInstance[i].equals(anEntry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNumberOfEntries() {
        return this.numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return this.numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return this.numberOfEntries == this.capacity;
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        int newCapacity = this.capacity + (this.capacity / 2);
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        T[] newArrayInstance = (T[]) new Object[newCapacity];
        System.arraycopy(arrayInstance, 0, newArrayInstance, 0, numberOfEntries);
        this.capacity = newCapacity;
        this.arrayInstance = newArrayInstance;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    @Override
    public T[] toArray() {
        T[] temp = (T[]) new Object[numberOfEntries];
        System.arraycopy(arrayInstance, 0, temp, 0, numberOfEntries);
        return temp;
    }

    @Override
    public void sort() {
        mergeSort(0,numberOfEntries - 1, arrayInstance, null);
    }

    @Override
    public void sort(Comparator<T> comparator) {
        mergeSort(0,numberOfEntries - 1, arrayInstance, comparator);
    }

    public void mergeSort(int left, int right, T[] array, Comparator<T> comparator) {
        if (left < right) {
            int mid = (int) array.length / 2;
            mergeSort(left, mid, array, comparator);
            mergeSort(mid + 1, right, array, comparator);

            merge(array, left, mid, right, comparator);
        }
    }

    private void merge(T[] array, int left, int mid, int right, Comparator<T> comparator) {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        T[] leftArray = (T[]) new Object[leftSize];
        T[] rightArray = (T[]) new Object[rightSize];

        System.arraycopy(array, left, leftArray, 0, leftSize);
        System.arraycopy(array, mid + 1, rightArray, 0, rightSize);

        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize) {
            if (comparator == null) {
                if (((Comparable<T>) leftArray[i]).compareTo(rightArray[j]) <= 0) {
                    array[k++] = leftArray[i++];
                } else {
                    array[k++] = rightArray[j++];
                }
            } else {
                if (comparator.compare(leftArray[i], rightArray[j]) <= 0) {
                    array[k++] = leftArray[i++];
                } else {
                    array[k++] = rightArray[j++];
                }
            }
        }

        while (i < leftSize) {
            array[k++] = leftArray[i++];
        }

        while (j < rightSize) {
            array[k++] = rightArray[j++];
        }
    }

    private class ArrayListIterator implements Iterator<T> {

        private int cursor;

        public ArrayListIterator() {
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < numberOfEntries;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arrayInstance[cursor++];
        }
    }
}
