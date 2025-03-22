package adt;

import adt.interval.Interval;
import java.util.Iterator;

/**
 *
 * @author Raymond
 */
public class IntervalTree<T extends Comparable<T>> implements TreeInterface<Interval<T>> {

    private class Node implements TreeInterface.TreeNode<Interval> {

        Interval<T> interval;
        T maxEnd;
        Node left, right;
        int height;

        Node(Interval<T> interval) {
            this.interval = interval;
            this.maxEnd = interval.end;
            this.height = 1;
        }

        @Override
        public Interval<T> getData() {
            return this.interval;
        }

        @Override
        public TreeNode getLeftChild() {
            return this.left;
        }

        @Override
        public TreeNode getRightChild() {
            return this.right;
        }

        @Override
        public int getHeight() {
            return this.height;
        }
    }

    private Node rootNode;

    @Override
    public void add(Interval<T> newEntry) {
        rootNode = addUtility(rootNode, newEntry);
    }

    private Node addUtility(Node node, Interval<T> interval) {
        if (node == null) {
            return new Node(interval);
        }
        if (interval.start.compareTo(node.interval.start) < 0) {
            node.left = addUtility(node.left, interval);
        } else {
            node.right = addUtility(node.right, interval);
        }

        node.maxEnd = max(node.maxEnd, max(getMaxEnd(node.left), getMaxEnd(node.right)));
        node.height = 1 + Math.max(height(node.left),
                height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public boolean contains(Interval<T> interval) {
        return contains(rootNode, interval);
    }

    private boolean contains(Node node, Interval<T> interval) {
        if (node == null) {
            return false;
        }
        if (node.interval.start.equals(interval.start) && node.interval.end.equals(interval.end)) {
            return true;
        }
        return contains(interval.start.compareTo(node.interval.start) < 0 ? node.left : node.right, interval);
    }

    @Override
    public void remove(Interval<T> interval) {
        rootNode = removeUtility(rootNode, interval);
    }

    private Node removeUtility(Node node, Interval<T> interval) {
        if (node == null) {
            return null;
        }
        if (interval.start.compareTo(node.interval.start) < 0) {
            node.left = removeUtility(node.left, interval);
        } else if (interval.start.compareTo(node.interval.start) > 0) {
            node.right = removeUtility(node.right, interval);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node minNode = findMin(node.right);
            node.interval = minNode.interval;
            node.right = removeUtility(node.right, minNode.interval);
        }
        node.maxEnd = max(node.interval.end, max(getMaxEnd(node.left), getMaxEnd(node.right)));
        node.height = Math.max(height(node.left),
                height(node.right)) + 1;

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public ListInterface<Interval<T>> searchForOverlapping(Interval<T> query) {
        ListInterface<Interval<T>> result = new ArrayList<>();
        searchForOverlapping(rootNode, query, result);
        return result;
    }

    private void searchForOverlapping(Node node, Interval<T> query, ListInterface<Interval<T>> result) {
        if (node == null || query.start.compareTo(node.maxEnd) > 0) {
            return;
        }
        if (node.interval.overlaps(query)) {
            result.append(node.interval);
        }
        searchForOverlapping(node.left, query, result);
        searchForOverlapping(node.right, query, result);
    }

    @Override
    public void clear() {
        rootNode = null;
    }

    @Override
    public int getHeight() {
        return rootNode.height;
    }

    @Override
    public int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.getLeftChild()), getHeight(node.getRightChild()));
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

    private int height(Node N) {
        if (N == null) {
            return 0;
        }
        return N.height;
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
    public boolean isEmpty() {
        return rootNode == null;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private T getMaxEnd(Node node) {
        return node == null ? null : node.maxEnd;
    }

    private T max(T a, T b) {
        return (a == null) ? b : (b == null) ? a : (a.compareTo(b) > 0 ? a : b);
    }

    @Override
    public Iterator<Interval<T>> iterator() {
        ListInterface<Interval<T>> list = new ArrayList<>();
        inorderTraversal(rootNode, list);
        return list.iterator();
    }

    private void inorderTraversal(Node node, ListInterface<Interval<T>> list) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left, list);
        list.append(node.interval);
        inorderTraversal(node.right, list);
    }
}
