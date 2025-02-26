package adt;

/**
 *
 * @author rttz159
 */
public class IntervalTree<T> implements Tree<T> {

    IntervalNode root;

    public IntervalTree() {
        root = null;
    }

    @Override
    public void add(T newEntry) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(T entry) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean contains(T entry) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    class Interval<T extends Comparable<T>> {

        T start;
        T end;

        public Interval(T start, T end) {
            this.start = start;
            this.end = end;
        }

        public boolean overlaps(Interval<T> other) {
            return this.start.compareTo(other.end) < 0 && other.start.compareTo(this.end) < 0;
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }

    public class IntervalNode {

        Interval interval;
        T max;
        IntervalNode left, right;

        IntervalNode(T start, T end) {
            interval = new Interval(start, end);
            this.max = interval.end;
            right = left = null;
        }

    }
}
