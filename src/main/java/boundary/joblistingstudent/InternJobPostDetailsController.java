package boundary.joblistingstudent;

import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.QualificationChecker;

/**
 *
 * @author rttz159
 */
public class InternJobPostDetailsController {

    @FXML
    private Button applyBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private ListView<Experience> requiredExperienceListView;

    @FXML
    private ListView<Qualification> requiredQualificationListView;

    @FXML
    private ListView<Skill> requiredSkillListView;

    @FXML
    private TextField salaryRangeTextField;

    @FXML
    private TextField titleTextField;

    private InternPost internpost;

    public void setInternPost(InternPost internpost) {
        this.internpost = internpost;
        setUp();
    }

    private void setUp() {
        this.titleTextField.setText(this.internpost.getTitle().toUpperCase());
        this.descTextField.setText(this.internpost.getDesc());
        this.locationTextField.setText("State: " + this.internpost.getLocation().getState() + " , Full Address: " + this.internpost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", this.internpost.getMinMaxSalary().getX(), this.internpost.getMinMaxSalary().getY()));
        requiredExperienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Industry Type: %s, Duration: %d", item.getIndustryType().toString(), item.getDuration()));
            }
        });
        requiredQualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Qualification Type: %s, Level: %d", item.getQualificationType().toString(), item.getLevel()));
            }
        });
        requiredSkillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Skill Type: %s, Proficiency Level: %d", item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        for (var x : internpost.getInterPostExperiences()) {
            requiredExperienceListView.getItems().add(x);
        }
        for (var x : internpost.getInternPostQualifications()) {
            requiredQualificationListView.getItems().add(x);
        }
        for (var x : internpost.getInternPostSkills()) {
            requiredSkillListView.getItems().add(x);
        }

        if (!checkQualification()) {
            applyBtn.setDisable(true);
            applyBtn.setText("Not Qualified");
        }
        
        if(!notApplyBefore()){
            applyBtn.setDisable(true);
            applyBtn.setText("Pending");
        }

        applyBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("JobListingStudent/interviewStudentScheduler.fxml"));
            Node node = null;
            InterviewStudentSchedulerController controller = null;
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

        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });

        ApplicationSharedState.getInstance().addAppliedListener((obs, oldValue, newValue) -> {
            if (newValue) {
                ((Stage) cancelBtn.getScene().getWindow()).close();
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
