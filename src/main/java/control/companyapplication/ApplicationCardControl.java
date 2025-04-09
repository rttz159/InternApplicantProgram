package control.companyapplication;

import boundary.companyapplication.ApplicationCardBoundary;
import dao.MainControlClass;
import entity.Application;
import entity.InternPost;
import entity.Student;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Raymond
 */
public class ApplicationCardControl {

    private Application application;
    private Application studApplication;
    private Student applicant;
    private InternPost post;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    private ApplicationCardBoundary boundary;

    public Application getApplication() {
        return application;
    }

    public Application getStudApplication() {
        return studApplication;
    }

    public Student getApplicant() {
        return applicant;
    }

    public InternPost getPost() {
        return post;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
    
    public ApplicationCardControl(ApplicationCardBoundary boundary,Application app){
        this.boundary = boundary;
        this.application = app;
        this.studApplication = MainControlClass.getStudentApplicationMap().get(app.getApplicationId());
        this.post = MainControlClass.getInternPostMap().get(app.getInternPostId());
        this.applicant = MainControlClass.getStudentsIdMap().get(application.getApplicantId());
    }
}
