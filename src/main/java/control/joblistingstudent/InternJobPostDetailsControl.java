package control.joblistingstudent;

import boundary.joblistingstudent.InternJobPostDetailsBoundary;
import boundary.joblistingstudent.InterviewStudentSchedulerBoundary;
import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.Company;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utils.QualificationChecker;

/**
 *
 * @author Raymond
 */
public class InternJobPostDetailsControl {
    
    private InternPost internpost;
    private Company tempCompany;
    private InternJobPostDetailsBoundary boundary;
    
    public InternJobPostDetailsControl(InternJobPostDetailsBoundary boundary, InternPost internpost){
        this.boundary = boundary;
        this.internpost = internpost;
    }

    public void setUp() {
        for (var x : MainControlClass.getCompanies()) {
            if (x.getInternPosts().contains(internpost)) {
                tempCompany = x;
                break;
            }
        }
        boundary.getCompnayNameTextField().setText(tempCompany.getCompanyName());
        boundary.getTitleTextField().setText(this.internpost.getTitle().toUpperCase());
        boundary.getDescTextField().setText(this.internpost.getDesc());
        boundary.getLocationTextField().setText("State: " + this.internpost.getLocation().getState() + " , Full Address: " + this.internpost.getLocation().getFullAddress());
        boundary.getSalaryRangeTextField().setText(String.format("RM %.2f - RM %.2f", this.internpost.getMinMaxSalary().getX(), this.internpost.getMinMaxSalary().getY()));
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
        for (var x : internpost.getInterPostExperiences()) {
            boundary.getRequiredExperienceListView().getItems().add(x);
        }
        for (var x : internpost.getInternPostQualifications()) {
            boundary.getRequiredQualificationListView().getItems().add(x);
        }
        for (var x : internpost.getInternPostSkills()) {
            boundary.getRequiredSkillListView().getItems().add(x);
        }

        if (!checkQualification()) {
            boundary.getApplyBtn().setDisable(true);
            boundary.getApplyBtn().setText("Not Qualified");
        }

        if (!notApplyBefore()) {
            boundary.getApplyBtn().setDisable(true);
            boundary.getApplyBtn().setText("Pending");
        }

        boundary.getApplyBtn().setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("JobListingStudent/interviewStudentScheduler.fxml"));
            Node node = null;
            InterviewStudentSchedulerBoundary controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            controller.setInternPost(internpost);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Internship Applcation");
            alert.setHeaderText("");
            alert.getDialogPane().setContent(node);
            alert.getButtonTypes().clear();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setOnCloseRequest(event -> stage.close());
            alert.showAndWait();
        });

        boundary.getCancelBtn().setOnAction(eh -> {
            ((Stage) boundary.getCancelBtn().getScene().getWindow()).close();
        });

        ApplicationSharedState.getInstance().addAppliedListener((obs, oldValue, newValue) -> {
            if (newValue) {
                ((Stage) boundary.getCancelBtn().getScene().getWindow()).close();
            }
        });
    }

    private boolean checkQualification() {
        Student stud = (Student) MainControlClass.getCurrentUser();
        boolean qualification = QualificationChecker.checkQualification(stud.getStudentQualifications(), internpost.getInternPostQualifications());
        boolean experience = QualificationChecker.checkExperience(stud.getStudentExperiences(), internpost.getInterPostExperiences());
        boolean skill = QualificationChecker.checkSkill(stud.getStudentSkills(), internpost.getInternPostSkills());

        return qualification && experience && skill;
    }

    private boolean notApplyBefore() {
        boolean valid = true;
        Student stud = (Student) MainControlClass.getCurrentUser();
        for (var x : stud.getStudentApplications()) {
            if (x.getInternPostId().equals(internpost.getInterPostId()) && x.getStatus().equals(Application.Status.PENDING)) {
                valid = false;
                break;
            }
        }
        return valid;
    }
}
