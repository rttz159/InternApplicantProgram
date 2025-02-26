/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author USER
 */
import adt.interval.Interval;
import adt.IntervalTree;
import adt.interval.TimeInterval;
import java.time.LocalTime;

public class InterviewScheduler {
    private IntervalTree<LocalTime> bookedSlots;
    private static final LocalTime START_TIME = LocalTime.of(8, 0);  // 8:00 AM
    private static final LocalTime END_TIME = LocalTime.of(17, 0);   // 5:00 PM
    private static final int SLOT_DURATION = 30; // 30 minutes

    public InterviewScheduler() {
        this.bookedSlots = new IntervalTree<>();
    }

    // Generate all available slots
    public void showAvailableSlots() {
        LocalTime current = START_TIME;
        System.out.println("Available Slots:");
        while (current.plusMinutes(SLOT_DURATION).isBefore(END_TIME.plusMinutes(1))) {
            TimeInterval slot = new TimeInterval(current, current.plusMinutes(SLOT_DURATION));
            if (!bookedSlots.contains(slot)) {
                System.out.println(slot);
            }
            current = current.plusMinutes(SLOT_DURATION);
        }
    }

    // Book a slot if available
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

    // Cancel a booking
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

    // Show booked slots
    public void showBookedSlots() {
        System.out.println("Booked Slots:");
        for (Interval<LocalTime> slot : bookedSlots) {
            System.out.println(slot);
        }
    }

    public static void main(String[] args) {
        InterviewScheduler scheduler = new InterviewScheduler();

        // Show initial availability
        scheduler.showAvailableSlots();

        // Book some slots
        scheduler.bookSlot(LocalTime.of(9, 0));  // Book 9:00 AM - 9:30 AM
        scheduler.bookSlot(LocalTime.of(10, 30)); // Book 10:30 AM - 11:00 AM

        // Show booked and available slots
        scheduler.showBookedSlots();
        scheduler.showAvailableSlots();

        // Cancel a slot
        scheduler.cancelBooking(LocalTime.of(9, 0));

        // Show updated availability
        scheduler.showBookedSlots();
        scheduler.showAvailableSlots();
    }
}
