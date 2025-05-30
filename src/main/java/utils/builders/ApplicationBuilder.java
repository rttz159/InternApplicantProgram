package utils.builders;

import com.fasterxml.uuid.Generators;
import entity.Application;
import entity.Interview;

/**
 *
 * @author Raymond
 */
public class ApplicationBuilder {
    private String applicationId;
    private String internPostId;
    private String applicantId;
    private Application.Status status;
    private Interview interview;

    public ApplicationBuilder() {
        this.applicationId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public ApplicationBuilder internPostId(String internPostId) {
        this.internPostId = internPostId;
        return this;
    }

    public ApplicationBuilder applicantId(String applicantId) {
        this.applicantId = applicantId;
        return this;
    }

    public ApplicationBuilder status(Application.Status status) {
        this.status = status;
        return this;
    }

    public ApplicationBuilder interview(Interview interview) {
        this.interview = interview;
        return this;
    }

    public Application build() {
        return new Application(applicationId, internPostId, applicantId, status, interview);
    }
}
