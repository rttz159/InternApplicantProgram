package adt;

import adt.interval.Interval;
import java.util.Iterator;

/**
 *
 * @author rttz159
 */
public class IntervalTree<T extends Comparable<T>> implements Iterable<Interval<T>>,Tree<Interval<T>> {

    private class Node {
        Interval<T> interval;
        T maxEnd;
        Node left, right;

        Node(Interval<T> interval) {
            this.interval = interval;
            this.maxEnd = interval.end;
        }
    }

    private Node root;
    private int size;

    @Override
    public void add(Interval<T> newEntry) {
        root = insert(root, newEntry);
    }

    private Node insert(Node node, Interval<T> interval) {
        if (node == null) {
            size++;
            return new Node(interval);
        }
        if (interval.start.compareTo(node.interval.start) < 0)
            node.left = insert(node.left, interval);
        else
            node.right = insert(node.right, interval);
        
        node.maxEnd = max(node.maxEnd, max(getMaxEnd(node.left), getMaxEnd(node.right)));
        return node;
    }

    @Override
    public boolean contains(Interval<T> interval) {
        return contains(root, interval);
    }

    private boolean contains(Node node, Interval<T> interval) {
        if (node == null) return false;
        if (node.interval.start.equals(interval.start) && node.interval.end.equals(interval.end)) return true;
        return contains(interval.start.compareTo(node.interval.start) < 0 ? node.left : node.right, interval);
    }

    @Override
    public void remove(Interval<T> interval) {
        root = remove(root, interval);
    }

    private Node remove(Node node, Interval<T> interval) {
        if (node == null) return null;
        if (interval.start.compareTo(node.interval.start) < 0)
            node.left = remove(node.left, interval);
        else if (interval.start.compareTo(node.interval.start) > 0)
            node.right = remove(node.right, interval);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node minNode = findMin(node.right);
            node.interval = minNode.interval;
            node.right = remove(node.right, minNode.interval);
        }
        size--;
        node.maxEnd = max(node.interval.end, max(getMaxEnd(node.left), getMaxEnd(node.right)));
        return node;
    }

    public List<Interval<T>> searchForOverlapping(Interval<T> query) {
        List<Interval<T>> result = new ArrayList<>();
        searchForOverlapping(root, query, result);
        return result;
    }

    private void searchForOverlapping(Node node, Interval<T> query, List<Interval<T>> result) {
        if (node == null || query.start.compareTo(node.maxEnd) > 0) return;
        if (node.interval.overlaps(query)) result.append(node.interval);
        searchForOverlapping(node.left, query, result);
        searchForOverlapping(node.right, query, result);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
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
        List<Interval<T>> list = new ArrayList<>();
        inorderTraversal(root, list);
        return list.iterator();
    }

    private void inorderTraversal(Node node, List<Interval<T>> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.append(node.interval);
        inorderTraversal(node.right, list);
    }
}
