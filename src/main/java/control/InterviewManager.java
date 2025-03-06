package control;

/**
 *
 * @author rttz159
 */
import adt.HashMap;
import adt.Map;
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
