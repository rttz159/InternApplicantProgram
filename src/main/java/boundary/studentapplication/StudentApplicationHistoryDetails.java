package boundary.studentapplication;

import boundary.joblisting.*;
import control.MainControlClass;
import dao.CompanyDAO;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.Experience;
import entity.InternPost;
import entity.Interview;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.builders.ApplicationBuilder;

/**
 *
 * @author rttz159
 */
public class StudentApplicationHistoryDetails {

    @FXML
    private Button okBtn;

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

    @FXML
    private TextField interviewDateTextField;

    @FXML
    private TextField interviewTimeTextField;

    @FXML
    private TextField statusTextField;

    private Application application;

    private InternPost internPost;

    private Company company;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public void setApplication(Application application) {
        this.application = application;
        this.internPost = MainControlClass.getInternPostMap().get(application.getInternPostId());
        setUp();
    }

    private void setUp() {
        this.titleTextField.setText(this.internPost.getTitle().toUpperCase());
        this.descTextField.setText(this.internPost.getDesc());
        this.locationTextField.setText("State: " + this.internPost.getLocation().getState() + " , Full Address: " + this.internPost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", this.internPost.getMinMaxSalary().getX(), this.internPost.getMinMaxSalary().getY()));
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
        for (var x : internPost.getInterPostExperiences()) {
            requiredExperienceListView.getItems().add(x);
        }
        for (var x : internPost.getInternPostQualifications()) {
            requiredQualificationListView.getItems().add(x);
        }
        for (var x : internPost.getInternPostSkills()) {
            requiredSkillListView.getItems().add(x);
        }

        interviewDateTextField.setText(formatter.format(application.getInterview().getDate()));
        interviewTimeTextField.setText(timeFormatter.format(application.getInterview().getStart_time()));
        statusTextField.setText(application.getStatus().toString());

        okBtn.setOnAction(eh -> {
            ((Stage) okBtn.getScene().getWindow()).close();
        });

        if (this.application.getStatus().equals(Application.Status.CANCELLED)) {
            cancelBtn.setDisable(true);
        }

        cancelBtn.setOnAction(eh -> {
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
                    cancelBtn.setDisable(true);
                    
                    StudentDAO.updateStudentById(tempStudent);
                    CompanyDAO.updateCompanyById(company);

                    this.statusTextField.setText(application.getStatus().toString());
                }
            }
        });
    }

    public Optional<ButtonType> showConfirmationDialog(String message) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(message);

        ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelBtn = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(yesBtn, cancelBtn);
        return alert.showAndWait();
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
