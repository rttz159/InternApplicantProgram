package control;

import adt.HashMap;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import adt.MapInterface;

/**
 *
 * @author Raymond
 */
public class InterviewManager {

    private MapInterface<LocalDate, InterviewScheduler> bookingRecords;

    public InterviewManager() {
        this.bookingRecords = new HashMap<>();
    }

    public MapInterface<LocalDate, InterviewScheduler> getBookingRecords() {
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
