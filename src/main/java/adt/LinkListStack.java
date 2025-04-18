package adt;

import java.util.Iterator;

/**
 *
 * @author Raymond
 */
public class LinkListStack<T> implements StackInterface<T>{
    
    private int numberOfEntries;
    private Node<T> headNode;

    public LinkListStack(){
        numberOfEntries = 0;
    }
    
    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public T getFirst() {
        return headNode.data;
    }

    @Override
    public T pop() {
        if(isEmpty()){
            return null;
        }
        
        T data = headNode.data;
        headNode = headNode.next;
        numberOfEntries--;
        return data;
    }

    @Override
    public void push(T newEntry) {
        if(isEmpty()){
            headNode = new Node(newEntry);
        }else{
            Node newNode = new Node(newEntry);
            newNode.next = headNode;
            headNode = newNode;
        }
        numberOfEntries++;
    }

    @Override
    public void clear() {
        numberOfEntries = 0;
        headNode = null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkListStackIterator();
    }
    
    private class LinkListStackIterator implements Iterator<T>{

        Node<T> currentNode = headNode;
        
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            T currentData = currentNode.data;
            currentNode = currentNode.next;
            return currentData;
        }
    
    } 
}

class Node<T>{
    T data;
    Node next;

    public Node(T data){
        this.data = data;
    }
}