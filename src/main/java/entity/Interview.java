package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author rttz159
 */
public class Interview {

    private String interviewId;
    private LocalDate date;
    private LocalTime start_time;

    public Interview(String interviewId, LocalDate date, LocalTime start_time) {
        this.interviewId = interviewId;
        this.date = date;
        this.start_time = start_time;
    }

    public String getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(String interviewId) {
        this.interviewId = interviewId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Interview interview = (Interview) obj;
        return interviewId.equals(interview.interviewId);
    }

    @Override
    public int hashCode() {
        return interviewId.hashCode();
    }

    public Interview deepCopy() {
        return new Interview(
                this.interviewId,
                this.date,
                this.start_time
        );
    }

}
