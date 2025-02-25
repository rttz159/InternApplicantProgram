package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 * @param <T>
 */
public class SinglyLinkList<T> implements List<T>, Iterable<T>{
    
    private Node headNode;
    private int numberOfNodes;
    
    public SinglyLinkList(){
        numberOfNodes = 0;
    }

    @Override
    public void append(T newEntry) {
        
        if(isEmpty()){
            headNode = new Node(newEntry);
            numberOfNodes++;
            return;
        }
        
        Node currentNode = headNode;
        while(currentNode.next != null){
            currentNode = currentNode.next;
        }
        Node newNode = new Node(newEntry);
        currentNode.next = newNode;
        numberOfNodes++;
    }

    @Override
    public boolean insert(Integer newPosition, T newEntry) {
        if(newPosition < 0 || newPosition > numberOfNodes){
            throw new ArrayIndexOutOfBoundsException();
        }
        
        if(newPosition == 0){
            Node newNode = new Node(newEntry);
            newNode.next = headNode;
            headNode = newNode;
            numberOfNodes++;
            return true;
        }
        
        if(newPosition == numberOfNodes){
            append(newEntry);
            return true;
        }
        
        Node previousNode = headNode;
        for(int i = 0; i < newPosition - 1; i++){
            previousNode = previousNode.next;
        }
        
        Node currentNode = previousNode.next;
        Node newNode = new Node(newEntry);
        newNode.next = currentNode;
        previousNode.next = newNode;
        numberOfNodes++;
        return true;
    }

    @Override
    public T remove(Integer givenPosition) {
        if(isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)){
            throw new ArrayIndexOutOfBoundsException();
        }
        
        if(givenPosition == 0){
            Node removedNode = headNode;
            headNode = headNode.next;
            numberOfNodes--;
            return removedNode.data;
        }
        
        Node previousNode = headNode;       
        for(int i = 0; i < (givenPosition - 1); i++){
            previousNode = previousNode.next;
        }
        
        Node removedNode = previousNode.next;
        previousNode.next = removedNode.next;
        numberOfNodes--;
        return removedNode.data;
    }

    public T remove(T item) {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        
        if(headNode.data.equals(item)){
            Node removedNode = headNode;
            headNode = headNode.next;
            numberOfNodes--;
            return removedNode.data;
        }
        
        Node previousNode = headNode;
        Node currentNode = headNode.next;
        while (currentNode != null && !currentNode.data.equals(item)) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        if (currentNode == null) {
            throw new NoSuchElementException();
        }
        
        Node removedNode = previousNode.next;
        previousNode.next = removedNode.next;
        numberOfNodes--;
        return removedNode.data;
    }

    @Override
    public void clear() {
        headNode = null;
        numberOfNodes = 0;
    }

    @Override
    public boolean replace(Integer givenPosition, T newEntry) {
        if(isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)){
            throw new ArrayIndexOutOfBoundsException();
        }
        
        Node currentNode = headNode;
        for(int i = 0; i < givenPosition; i++){
            currentNode = currentNode.next;
        }
        
        currentNode.data = newEntry;
        return true;
    }

    @Override
    public T getEntry(Integer givenPosition) {
        if(isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)){
            throw new ArrayIndexOutOfBoundsException();
        }

        Node currentNode = headNode;
        for(int i = 0; i < givenPosition; i++){
            currentNode = currentNode.next;
        }
        
        return currentNode.data;
    }

    @Override
    public boolean contains(T anEntry) {
        
        if(isEmpty()){
            return false;
        }
        
        Node currentNode = headNode;
        
        while(currentNode != null){
            if(currentNode.data.equals(anEntry)){
                return true;
            }
            currentNode = currentNode.next;
        }
        
        return false;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfNodes;
    }

    @Override
    public boolean isEmpty() {
        return headNode == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new SinglyLinkListIterator();
    }
    
    private class SinglyLinkListIterator implements Iterator<T>{

        Node currentNode = headNode;
        
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new java.util.NoSuchElementException();
            }
            
            T currentData = currentNode.data;
            currentNode = currentNode.next;
            return currentData;
        }
        
    }
    
    public class Node{
        T data;
        Node next;

        public Node(T data){
            this.data = data;
        }
        
        public Node(T data, Node next){
            this.data = data;
            this.next = next;
        }
    }
    
}
