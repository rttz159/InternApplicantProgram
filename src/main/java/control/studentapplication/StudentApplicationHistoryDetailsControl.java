package control.studentapplication;

import dao.CompanyDAO;
import dao.StudentDAO;
import entity.Application;
import entity.InternPost;
import entity.Interview;
import entity.Student;
import static boundary.PredefinedDialog.showConfirmationDialog;
import boundary.studentapplication.StudentApplicationHistoryDetailsController;
import boundary.studentapplication.StudentApplicationShareState;
import dao.MainControlClass;
import entity.Company;
import entity.Experience;
import entity.Qualification;
import entity.Skill;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class StudentApplicationHistoryDetailsControl {
    
    private Application application;
    private InternPost internPost;
    private Company company;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    
    private StudentApplicationHistoryDetailsController boundary;

    public StudentApplicationHistoryDetailsControl(StudentApplicationHistoryDetailsController boundary, Application application){
        this.boundary = boundary;
        this.application = application;
        this.internPost = MainControlClass.getInternPostMap().get(this.application.getInternPostId());
    }
    
    public void setUp() {
        boundary.getTitleTextField().setText(this.internPost.getTitle().toUpperCase());
        boundary.getDescTextField().setText(this.internPost.getDesc());
        boundary.getLocationTextField().setText("State: " + this.internPost.getLocation().getState() + " , Full Address: " + this.internPost.getLocation().getFullAddress());
        boundary.getSalaryRangeTextField().setText(String.format("RM %.2f - RM %.2f", this.internPost.getMinMaxSalary().getX(), this.internPost.getMinMaxSalary().getY()));
        boundary.getRequiredExperienceListView().setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Industry Type: %s, Duration: %d", item.getIndustryType().toString(), item.getDuration()));
            }
        });
        boundary.getRequiredQualificationListView().setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Qualification Type: %s, Level: %d", item.getQualificationType().toString(), item.getLevel()));
            }
        });
        boundary.getRequiredSkillListView().setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Skill Type: %s, Proficiency Level: %d", item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        for (var x : internPost.getInterPostExperiences()) {
            boundary.getRequiredExperienceListView().getItems().add(x);
        }
        for (var x : internPost.getInternPostQualifications()) {
            boundary.getRequiredQualificationListView().getItems().add(x);
        }
        for (var x : internPost.getInternPostSkills()) {
            boundary.getRequiredSkillListView().getItems().add(x);
        }

        boundary.getInterviewDateTextField().setText(formatter.format(application.getInterview().getDate()));
        boundary.getInterviewTimeTextField().setText(timeFormatter.format(application.getInterview().getStart_time()));
        boundary.getStatusTextField().setText(application.getStatus().toString());

        boundary.getOkBtn().setOnAction(eh -> {
            ((Stage) boundary.getOkBtn().getScene().getWindow()).close();
        });

        if (!this.application.getStatus().equals(Application.Status.PENDING)) {
            boundary.getCancelBtn().setDisable(true);
        }

        boundary.getCancelBtn().setOnAction(eh -> {
            Optional<ButtonType> result = showConfirmationDialog("This application will be cancelled and the action is irreversible.");

            if (result.isPresent()) {
                if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                    Student tempStudent = (Student) MainControlClass.getCurrentUser();
                    Interview tempInterview = application.getInterview();
                    findCompany(this.internPost);

                    company.getInterviewManager().interviewCancelled(tempInterview.getDate(), tempInterview.getStart_time());
                    for (var x : internPost.getInternPostApplications()) {
                        if (x.getApplicationId().equals(application.getApplicationId())) {
                            x.setStatus(Application.Status.CANCELLED);
                            break;
                        }
                    }
                    application.setStatus(Application.Status.CANCELLED);
                    boundary.getCancelBtn().setDisable(true);
                    
                    StudentDAO.updateStudentById(tempStudent);
                    CompanyDAO.updateCompanyById(company);

                    boundary.getStatusTextField().setText(application.getStatus().toString());
                    StudentApplicationShareState.getInstance().refresh();
                }
            }
        });
    }

    public void findCompany(InternPost internPost) {
        for (var x : MainControlClass.getCompanies()) {
            if (x.getInternPosts().contains(internPost)) {
                this.company = x;
                break;
            }
        }
    }
}
