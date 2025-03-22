package adt;

import java.util.Iterator;

/**
 *
 * @author Raymond
 */
public class ArrayStack<T> implements StackInterface<T>{
    
    private int numberOfEntries;
    private int capacity;
    private T[] arrayInstance;
    
    @SuppressWarnings("unchecked")
    public ArrayStack(){
        numberOfEntries = 0;
        capacity = 10;
        arrayInstance = (T[]) new Object[capacity];
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public T getFirst() {
        if(!isEmpty()){
            return arrayInstance[numberOfEntries - 1];
        }
        return null;
    }

    @Override
    public T pop() {
        if(isEmpty()){
            return null;
        }
        
        T returnedObject = arrayInstance[numberOfEntries - 1];
        numberOfEntries--;
        return returnedObject;
    }

    @Override
    public void push(T newEntry) {
        if(isFull()){
            resize();
        }
        arrayInstance[numberOfEntries++] = newEntry;
    }

    @Override
    public void clear() {
        numberOfEntries = 0;
        capacity = 10;
        arrayInstance = (T[]) new Object[capacity];
    }

    @Override
    public boolean isFull() {
        return numberOfEntries == capacity;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }
    
    @SuppressWarnings("unchecked")
    private void resize(){
        int newCapacity = capacity + (capacity / 2);
        T[] newArrayInstance = (T[]) new Object[newCapacity];
        System.arraycopy(arrayInstance, 0, newArrayInstance, 0, numberOfEntries);
        arrayInstance = newArrayInstance;
        capacity = newCapacity;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayStackIterator();
    }
    
    private class ArrayStackIterator implements Iterator<T>{

        int currentIdx = 0;
        
        @Override
        public boolean hasNext() {
            return (currentIdx + 1) != numberOfEntries;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new java.util.NoSuchElementException();
            }
            
            return arrayInstance[currentIdx++];
        }
    
    }
    
}
