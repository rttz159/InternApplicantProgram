package adt.interval;

/**
 *
 * @author Raymond
 */
public class Interval<T extends Comparable<T>> {

    public T start, end;

    public Interval(T start, T end) {
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Start must be <= end");
        }
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(Interval<T> other) {
        return this.start.compareTo(other.end) < 0 && this.end.compareTo(other.start) > 0;
    }

    @Override
    public String toString() {
        return "[" + start + ", " + end + "]";
    }
}
