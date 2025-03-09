package entity;

/**
 *
 * @author rttz159
 */
public class Application {

    private String applicationId;
    private String internPostId;
    private String applicantId;
    private Status status;
    private Interview interview;

    public Application(String applicationId, String internPostId, String applicantId, Status status, Interview interview) {
        this.applicationId = applicationId;
        this.internPostId = internPostId;
        this.applicantId = applicantId;
        this.status = status;
        this.interview = interview;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getInternPostId() {
        return internPostId;
    }

    public void setInternPostId(String internPostId) {
        this.internPostId = internPostId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Application that = (Application) obj;
        return applicationId.equals(that.applicationId);
    }

    @Override
    public int hashCode() {
        return applicationId.hashCode();
    }

    public enum Status {
        SUCCESS,
        PENDING,
        REJECTED,
        CANCELLED
    }
}
