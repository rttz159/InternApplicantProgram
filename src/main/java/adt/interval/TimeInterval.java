package adt.interval;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Raymond
 */
public class TimeInterval extends Interval<LocalTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public TimeInterval(LocalTime start, LocalTime end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return "[" + start.format(FORMATTER) + " - " + end.format(FORMATTER) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeInterval && ((TimeInterval) (obj)).start.equals(this.start) && ((TimeInterval) (obj)).end.equals(this.end)) {
            return true;
        }
        return false;
    }
}
