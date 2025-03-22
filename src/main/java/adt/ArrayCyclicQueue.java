package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Raymond
 */
public class ArrayCyclicQueue<T> implements QueueInterface<T> {

    private int capacity;
    private int numberOfEntries;
    private T[] arrayInstance;
    private int frontidx;
    private int rearidx;

    public ArrayCyclicQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        capacity = initialCapacity;
        numberOfEntries = 0;
        arrayInstance = (T[]) new Object[capacity];
        frontidx = 0;
        rearidx = -1;
    }

    @Override
    public void enqueue(T newEntry) {
        if (isFull()) {
            resize();
        }
        numberOfEntries++;
        rearidx = (rearidx + 1) % capacity;
        arrayInstance[rearidx] = newEntry;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        numberOfEntries--;
        int oldFrontIdx = frontidx;
        frontidx = (frontidx + 1) % capacity;
        return arrayInstance[oldFrontIdx];
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        numberOfEntries = 0;
        capacity = 10;
        arrayInstance = (T[]) new Object[capacity];
        frontidx = 0;
        rearidx = -1;
    }

    @Override
    public T getFront() {
        if (isEmpty()) {
            return null;
        }
        return arrayInstance[frontidx];
    }

    private boolean isFull() {
        return numberOfEntries == capacity;
    }

    private void resize() {
        int newCapacity = (int) (capacity * 1.5);
        T[] newArrayInstance = (T[]) new Object[newCapacity];

        for (int i = 0; i < numberOfEntries; i++) {
            newArrayInstance[i] = arrayInstance[(frontidx + i) % capacity];
        }

        frontidx = 0;
        rearidx = numberOfEntries - 1;
        capacity = newCapacity;
        arrayInstance = newArrayInstance;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayCyclicQueueIterator();
    }

    private class ArrayCyclicQueueIterator implements Iterator<T> {

        private int cursor; 
        private int count;  

        public ArrayCyclicQueueIterator() {
            this.cursor = frontidx;
            this.count = 0;  
        }

        @Override
        public boolean hasNext() {
            return count < numberOfEntries;  
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T temp = arrayInstance[cursor];  
            cursor = (cursor + 1) % capacity;  
            count++;  
            return temp;
        }
    }

}
