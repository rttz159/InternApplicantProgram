package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 */
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T>, Iterable<T> {

    private Node rootNode;

    public BinarySearchTree() {
        rootNode = null;
    }

    @Override
    public void add(T newEntry) {
        rootNode = addUtility(rootNode, newEntry);
    }

    private Node addUtility(Node node, T newEntry) {
        if (node == null) {
            return new Node(newEntry);
        }

        if (node.data.compareTo(newEntry) > 0) {
            node.left = addUtility(node.left, newEntry);
        } else if (node.data.compareTo(newEntry) < 0) {
            node.right = addUtility(node.right, newEntry);
        }

        return node;
    }

    @Override
    public void remove(T entry) {
        rootNode = removeUtility(rootNode, entry);
    }

    private Node removeUtility(Node node, T entry) {
        if (node == null) {
            return null;
        }

        if (node.data.compareTo(entry) > 0) {
            node.left = removeUtility(node.left, entry);
        } else if (node.data.compareTo(entry) < 0) {
            node.right = removeUtility(node.right, entry);
        }else{
            if(node.left == null){
                return node.right;
            }
            if(node.right == null){
                return node.left;
            }
            
            T min = findMin(node.right);
            node.data = min;
            node.right = removeUtility(node.right,min);    
        }
        return node;
    }

    public T findMax(Node node) {
        if (node.right == null) {
            return node.data;
        }

        return findMax(node.right);
    }

    public T findMin(Node node) {
        if (node.left == null) {
            return node.data;
        }

        return findMin(node.left);
    }

    @Override
    public void clear() {
        this.rootNode = null;
    }

    @Override
    public int getHeight() {
        return heightUtility(rootNode);
    }

    private int heightUtility(Node node) {
        if (node == null) {
            return -1; 
        }

        int leftHeight = heightUtility(node.left);
        int rightHeight = heightUtility(node.right);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    @Override
    public int getSize() {
        return sizeUtility(rootNode);
    }
    
    private int sizeUtility(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeUtility(node.left) + sizeUtility(node.right);
    }

    @Override
    public boolean contains(T entry) {
        return containsUtility(rootNode,entry);
    }
    
    private boolean containsUtility(Node node, T newEntry) {
        if (node == null) {
            return false;
        }

        if (node.data.compareTo(newEntry) > 0) {
            return containsUtility(node.right, newEntry);
        } else if (node.data.compareTo(newEntry) < 0) {
            return containsUtility(node.left, newEntry);
        }else{
            return true;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.rootNode == null;
    }

    public void printInOrderTree() {
        inOrderTraversal(rootNode);
    }

    public void printPreOrderTree() {
        preOrderTraversal(rootNode);
    }

    public void printIPostOrderTree() {
        postOrderTraversal(rootNode);
    }

    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.print(node.data + " ");
            inOrderTraversal(node.right);
        }
    }

    private void preOrderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    private void postOrderTraversal(Node node) {
        if (node != null) {
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            System.out.print(node.data + " ");
        }
    }
    
    public Node getRoot(){
        return this.rootNode;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }
    
    private class BinarySearchTreeIterator implements Iterator<T> {
        private ArrayCyclicQueue<T> queue = new ArrayCyclicQueue<>(getSize());

        public BinarySearchTreeIterator() {
            inOrderTraversal(rootNode);
        }

        private void inOrderTraversal(Node node) {
            if (node != null) {
                inOrderTraversal(node.left);
                queue.enqueue(node.data); 
                inOrderTraversal(node.right);
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return queue.dequeue();
        }
    }

    public class Node {

        T data;
        Node left;
        Node right;

        Node(T entry) {
            data = entry;
            this.left = null;
            this.right = null;
        }
    }

}
