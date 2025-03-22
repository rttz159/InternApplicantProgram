package control;

import adt.ArrayList;
import adt.IntervalTree;
import adt.interval.Interval;
import adt.interval.TimeInterval;
import java.time.LocalTime;
import adt.ListInterface;

/**
 *
 * @author Raymond
 */
public class InterviewScheduler {

    private IntervalTree<LocalTime> bookedSlots;
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    private static final int SLOT_DURATION = 30; // 30 mins

    public InterviewScheduler() {
        this.bookedSlots = new IntervalTree<>();
    }

    public ListInterface<TimeInterval> showAvailableSlots() {
        ListInterface<TimeInterval> temp = new ArrayList<>();
        LocalTime current = START_TIME;
        while (current.plusMinutes(SLOT_DURATION).isBefore(END_TIME.plusMinutes(1))) {
            TimeInterval slot = new TimeInterval(current, current.plusMinutes(SLOT_DURATION));
            if (!bookedSlots.contains(slot)) {
                temp.append(slot);
            }
            current = current.plusMinutes(SLOT_DURATION);
        }
        return temp;
    }

    public boolean bookSlot(LocalTime start) {
        LocalTime end = start.plusMinutes(SLOT_DURATION);
        TimeInterval newSlot = new TimeInterval(start, end);
        if (!bookedSlots.contains(newSlot)) {
            bookedSlots.add(newSlot);
            System.out.println("Slot booked: " + newSlot);
            return true;
        }
        System.out.println("Slot already booked: " + newSlot);
        return false;
    }

    public boolean cancelBooking(LocalTime start) {
        LocalTime end = start.plusMinutes(SLOT_DURATION);
        TimeInterval slot = new TimeInterval(start, end);
        if (bookedSlots.contains(slot)) {
            bookedSlots.remove(slot);
            System.out.println("Booking canceled: " + slot);
            return true;
        }
        System.out.println("No booking found for: " + slot);
        return false;
    }

    public ListInterface<Interval<LocalTime>> showBookedSlots() {
        ListInterface<Interval<LocalTime>> temp = new ArrayList<>();
        for (Interval<LocalTime> slot : bookedSlots) {
            temp.append(slot);
        }
        return temp;
    }

    public static ListInterface<TimeInterval> showAllSlots() {
        ListInterface<TimeInterval> temp = new ArrayList<>();
        LocalTime current = START_TIME;
        while (current.plusMinutes(SLOT_DURATION).isBefore(END_TIME.plusMinutes(1))) {
            TimeInterval slot = new TimeInterval(current, current.plusMinutes(SLOT_DURATION));
            temp.append(slot);
            current = current.plusMinutes(SLOT_DURATION);
        }
        return temp;
    }
}
