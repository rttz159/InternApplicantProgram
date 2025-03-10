package adt;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 * @param <T>
 */
public class SinglyLinkList<T> implements ListInterface<T> {

    private Node<T> headNode;
    private int numberOfNodes;

    public SinglyLinkList() {
        numberOfNodes = 0;
    }

    @Override
    public void append(T newEntry) {

        if (isEmpty()) {
            headNode = new Node(newEntry);
            numberOfNodes++;
            return;
        }

        Node<T> currentNode = headNode;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        Node<T> newNode = new Node(newEntry);
        currentNode.next = newNode;
        numberOfNodes++;
    }

    @Override
    public boolean insert(Integer newPosition, T newEntry) {
        if (newPosition < 0 || newPosition > numberOfNodes) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (newPosition == 0) {
            Node<T> newNode = new Node(newEntry);
            newNode.next = headNode;
            headNode = newNode;
            numberOfNodes++;
            return true;
        }

        if (newPosition == numberOfNodes) {
            append(newEntry);
            return true;
        }

        Node<T> previousNode = headNode;
        for (int i = 0; i < newPosition - 1; i++) {
            previousNode = previousNode.next;
        }

        Node<T> currentNode = previousNode.next;
        Node<T> newNode = new Node(newEntry);
        newNode.next = currentNode;
        previousNode.next = newNode;
        numberOfNodes++;
        return true;
    }

    @Override
    public T remove(Integer givenPosition) {
        if (isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (givenPosition == 0) {
            Node<T> removedNode = headNode;
            headNode = headNode.next;
            numberOfNodes--;
            return removedNode.data;
        }

        Node previousNode = headNode;
        for (int i = 0; i < (givenPosition - 1); i++) {
            previousNode = previousNode.next;
        }

        Node<T> removedNode = previousNode.next;
        previousNode.next = removedNode.next;
        numberOfNodes--;
        return removedNode.data;
    }

    public T remove(T item) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (headNode.data.equals(item)) {
            Node<T> removedNode = headNode;
            headNode = headNode.next;
            numberOfNodes--;
            return removedNode.data;
        }

        Node<T> previousNode = headNode;
        Node<T> currentNode = headNode.next;
        while (currentNode != null && !currentNode.data.equals(item)) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        if (currentNode == null) {
            throw new NoSuchElementException();
        }

        Node<T> removedNode = previousNode.next;
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
        if (isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        Node<T> currentNode = headNode;
        for (int i = 0; i < givenPosition; i++) {
            currentNode = currentNode.next;
        }

        currentNode.data = newEntry;
        return true;
    }

    @Override
    public T getEntry(Integer givenPosition) {
        if (isEmpty() || givenPosition < 0 || givenPosition > (numberOfNodes - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        Node<T> currentNode = headNode;
        for (int i = 0; i < givenPosition; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.data;
    }

    @Override
    public boolean contains(T anEntry) {

        if (isEmpty()) {
            return false;
        }

        Node<T> currentNode = headNode;

        while (currentNode != null) {
            if (currentNode.data.equals(anEntry)) {
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

    @Override
    public T[] toArray() {
        T[] temp = (T[]) new Object[numberOfNodes];
        Node currentNode = headNode;
        int i = 0;
        while (currentNode != null) {
            temp[i] = (T) currentNode.data;
            currentNode = currentNode.next;
            i++;
        }
        return temp;
    }

    @Override
    public void sort() {
        headNode = mergeSort(headNode, null);
    }

    @Override
    public void sort(Comparator<T> comparator) {
        headNode = mergeSort(headNode, comparator); 
    }

    private Node<T> mergeSort(Node<T> head, Comparator<T> comparator) {
        if (head == null || head.next == null) {
            return head;
        }

        Node<T> middle = getMiddle(head);
        Node<T> nextOfMiddle = middle.next;
        middle.next = null; 

        Node<T> left = mergeSort(head, comparator);
        Node<T> right = mergeSort(nextOfMiddle, comparator);

        return merge(left, right, comparator);
    }

    private Node<T> merge(Node<T> left, Node<T> right, Comparator<T> comparator) {
        Node<T> result = null;

        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (comparator == null) {
            if (((Comparable<T>) left.data).compareTo(right.data) <= 0) {
                result = left;
                result.next = merge(left.next, right, comparator);
            } else {
                result = right;
                result.next = merge(left, right.next, comparator);
            }
        } else {
            if (comparator.compare(left.data, right.data) <= 0) {
                result = left;
                result.next = merge(left.next, right, comparator);
            } else {
                result = right;
                result.next = merge(left, right.next, comparator);
            }
        }

        return result;
    }
    
    //Use slow-fast pointer to get the middle node
    private Node<T> getMiddle(Node<T> head) {
        if (head == null) {
            return null;
        }

        Node<T> slow = head;
        Node<T> fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private class SinglyLinkListIterator implements Iterator<T> {

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

    private class Node<T> {

        T data;
        Node next;

        public Node(T data) {
            this.data = data;
        }

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}
