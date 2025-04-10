package control.companyapplication;

import boundary.PredefinedDialog;
import boundary.companyapplication.ApplicationDetailsBoundary;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Raymond
 */
public class ApplicationDetailsControl {

    private Application application;
    private Student tempStud;
    private Application studApplication;
    private InternPost internPost;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private ApplicationDetailsBoundary boundary;

    public Application getApplication() {
        return application;
    }

    public Student getTempStud() {
        return tempStud;
    }

    public Application getStudApplication() {
        return studApplication;
    }

    public InternPost getInternPost() {
        return internPost;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public DateTimeFormatter getTimeFormatter() {
        return timeFormatter;
    }

    public ApplicationDetailsControl(ApplicationDetailsBoundary boundary, Application application, Application studApplication) {
        this.boundary = boundary;
        this.application = application;
        this.studApplication = studApplication;
        this.tempStud = MainControlClass.getStudentsIdMap().get(application.getApplicantId());
        this.internPost = MainControlClass.getInternPostMap().get(application.getInternPostId());
    }

    public void saveApplication() {
        Optional<ButtonType> result = PredefinedDialog.showConfirmationDialog("The action is irreversible");
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
            Application.Status tempStatus = boundary.getStatusComboBox().getValue();
            Application.Status prevStatus = application.getStatus();
            application.setStatus(tempStatus);
            studApplication.setStatus(tempStatus);
            StudentDAO.updateStudentById(tempStud);

            Company tempComp = (Company) MainControlClass.getCurrentUser();

            if (tempStatus.equals(Application.Status.CANCELLED) && !prevStatus.equals(Application.Status.CANCELLED)) {
                tempComp.getInterviewManager().interviewCancelled(studApplication.getInterview().getDate(), studApplication.getInterview().getStart_time());
            }

            CompanyDAO.updateCompanyById(tempComp);
            boundary.setUpforReadOnly();
            CompanyApplicationShareState.getInstance().refresh();
        }
    }
}
