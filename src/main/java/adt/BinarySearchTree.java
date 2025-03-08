package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rttz159
 */
public class BinarySearchTree<T extends Comparable<T>> implements TreeInterface<T>, Iterable<T> {

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

        if (newEntry.compareTo(node.data) < 0) {
            node.left = addUtility(node.left, newEntry);
        } else if (newEntry.compareTo(node.data) > 0) {
            node.right = addUtility(node.right, newEntry);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left),
                height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && newEntry.compareTo(node.left.data) < 0) {
            return rightRotate(node);
        }

        if (balance < -1 && newEntry.compareTo(node.right.data) > 0) {
            return leftRotate(node);
        }

        if (balance > 1 && newEntry.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && newEntry.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public void remove(T entry) {
        rootNode = removeUtility(rootNode, entry);
    }

    private Node removeUtility(Node root, T newEntry) {
        if (root == null) {
            return root;
        }

        if (newEntry.compareTo(root.data) < 0) {
            root.left = removeUtility(root.left, newEntry);
        } else if (newEntry.compareTo(root.data) > 0) {
            root.right = removeUtility(root.right, newEntry);
        } else {
            if ((root.left == null)
                    || (root.right == null)) {
                Node temp = root.left != null
                        ? root.left : root.right;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                T temp = findMin(root.right);

                root.data = temp;

                root.right = removeUtility(root.right, temp);
            }
        }

        root.height = Math.max(height(root.left),
                height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
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
        return height(rootNode);
    }

    private int height(Node N) {
        if (N == null) {
            return 0;
        }
        return N.height;
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = 1 + Math.max(height(y.left),
                height(y.right));
        x.height = 1 + Math.max(height(x.left),
                height(x.right));

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = 1 + Math.max(height(x.left),
                height(x.right));
        y.height = 1 + Math.max(height(y.left),
                height(y.right));

        return y;
    }

    private int getBalance(Node N) {
        if (N == null) {
            return 0;
        }
        return height(N.left) - height(N.right);
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
        return containsUtility(rootNode, entry);
    }

    private boolean containsUtility(Node node, T newEntry) {
        if (node == null) {
            return false;
        }

        if (node.data.compareTo(newEntry) > 0) {
            return containsUtility(node.right, newEntry);
        } else if (node.data.compareTo(newEntry) < 0) {
            return containsUtility(node.left, newEntry);
        } else {
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

    public Node getRoot() {
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
        int height;

        Node(T entry) {
            data = entry;
            this.left = null;
            this.right = null;
            height = 1;
        }
    }

}
