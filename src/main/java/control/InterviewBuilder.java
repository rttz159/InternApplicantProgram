package control;

import com.fasterxml.uuid.Generators;
import entity.Interview;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author rttz159
 */
public class InterviewBuilder {
    private String interviewId;
    private LocalDate date;
    private LocalTime startTime;

    public InterviewBuilder() {
        this.interviewId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public InterviewBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public InterviewBuilder startTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Interview build() {
        return new Interview(interviewId, date, startTime);
    }
}