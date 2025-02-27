package control;

/**
 *
 * @author rttz159
 */
import adt.ArrayList;
import adt.HashMap;
import adt.interval.Interval;
import adt.IntervalTree;
import adt.List;
import adt.Map;
import adt.interval.TimeInterval;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.NoSuchElementException;

public class InterviewManager {

    private Map<LocalDate, InterviewScheduler> bookingRecords;

    public InterviewManager() {
        this.bookingRecords = new HashMap<>();
    }

    public Map<LocalDate, InterviewScheduler> getBookingRecords() {
        return bookingRecords;
    }

    public void interviewBooking(LocalDate date, LocalTime start) {
        if (!this.bookingRecords.containsKey(date)) {
            this.bookingRecords.put(date, new InterviewScheduler());
        }

        InterviewScheduler tempScheduler = this.bookingRecords.get(date);
        tempScheduler.bookSlot(start);
    }

    public void interviewCancelled(LocalDate date, LocalTime start) {
        if (!this.bookingRecords.containsKey(date)) {
            throw new NoSuchElementException();
        }

        InterviewScheduler tempScheduler = this.bookingRecords.get(date);
        tempScheduler.cancelBooking(start);
    }

    public InterviewScheduler getParticularDaySchedule(LocalDate date) {
        return bookingRecords.get(date);
    }
}

class InterviewScheduler {

    private IntervalTree<LocalTime> bookedSlots;
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    private static final int SLOT_DURATION = 60; // 30 mins

    public InterviewScheduler() {
        this.bookedSlots = new IntervalTree<>();
    }

    public List<TimeInterval> showAvailableSlots() {
        List<TimeInterval> temp = new ArrayList<>();
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

    public List<Interval<LocalTime>> showBookedSlots() {
        List<Interval<LocalTime>> temp = new ArrayList<>();
        for (Interval<LocalTime> slot : bookedSlots) {
            temp.append(slot);
        }
        return temp;
    }
}
