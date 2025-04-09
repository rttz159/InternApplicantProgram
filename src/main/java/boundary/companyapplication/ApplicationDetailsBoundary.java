package boundary.companyapplication;

import boundary.PredefinedDialog;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class ApplicationDetailsBoundary {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button statusBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField salaryRangeTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField interviewDateTextField;

    @FXML
    private TextField interviewTimeTextField;

    @FXML
    private ListView<Qualification> qualificationListView;

    @FXML
    private ListView<Skill> skillListView;

    @FXML
    private ListView<Experience> experienceListView;

    @FXML
    private ComboBox<Application.Status> statusComboBox;

    @FXML
    private TextField studentAgeTextField;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField studentEmailTextField;

    private Application application;

    private Student tempStud;

    private Application studApplication;

    private InternPost internPost;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public void setApplication(Application application, Application studApplication) {
        this.application = application;
        this.studApplication = studApplication;
        this.tempStud = MainControlClass.getStudentsIdMap().get(application.getApplicantId());
        this.internPost = MainControlClass.getInternPostMap().get(application.getInternPostId());
        this.titleTextField.setEditable(false);
        this.descTextField.setEditable(false);
        this.studentAgeTextField.setEditable(false);
        this.studentNameTextField.setEditable(false);
        this.studentEmailTextField.setEditable(false);
        this.locationTextField.setEditable(false);
        this.salaryRangeTextField.setEditable(false);
        this.statusComboBox.setEditable(false);
        this.interviewDateTextField.setEditable(false);
        this.interviewTimeTextField.setEditable(false);
        experienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Industry Type: %s, Duration: %d", item.getDesc(), item.getIndustryType().toString(), item.getDuration()));
            }
        });
        qualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Qualification Type: %s, Level: %d", item.getDesc(), item.getQualificationType().toString(), item.getLevel()));
            }
        });
        skillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Name: %s, Skill Type: %s, Proficiency Level: %d", item.getName(), item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        saveBtn.setOnAction(eh -> {
            Optional<ButtonType> result = PredefinedDialog.showConfirmationDialog("The action is irreversible");
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                Application.Status tempStatus = statusComboBox.getValue();
                Application.Status prevStatus = application.getStatus();
                application.setStatus(tempStatus);
                studApplication.setStatus(tempStatus);
                StudentDAO.updateStudentById(tempStud);

                Company tempComp = (Company) MainControlClass.getCurrentUser();

                if (tempStatus.equals(Application.Status.CANCELLED) && !prevStatus.equals(Application.Status.CANCELLED)) {
                    tempComp.getInterviewManager().interviewCancelled(studApplication.getInterview().getDate(), studApplication.getInterview().getStart_time());
                }

                CompanyDAO.updateCompanyById(tempComp);
                setUpforReadOnly();
                CompanyApplicationShareState.getInstance().refresh();
            }
        });
        statusBtn.setOnAction(eh -> {
            setUpforModify();
        });
        setUpforReadOnly();
        enrichField();
    }

    private void setUpforReadOnly() {
        this.statusComboBox.setDisable(true);
        this.saveBtn.setVisible(false);
        this.saveBtn.setManaged(false);
        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });
    }

    private void setUpforModify() {
        this.statusComboBox.setDisable(false);
        this.saveBtn.setVisible(true);
        this.saveBtn.setManaged(true);
        cancelBtn.setOnAction(eh -> {
            enrichField();
            setUpforReadOnly();
        });
    }

    private void enrichField() {
        this.titleTextField.setText(this.internPost.getTitle().toUpperCase());
        this.descTextField.setText(this.internPost.getDesc());
        this.locationTextField.setText("State: " + this.internPost.getLocation().getState() + " , Full Address: " + this.internPost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", this.internPost.getMinMaxSalary().getX(), this.internPost.getMinMaxSalary().getY()));
        this.studentAgeTextField.setText(tempStud.getAge() + "");
        this.studentNameTextField.setText(tempStud.getName());
        this.studentEmailTextField.setText(tempStud.getEmail());

        statusComboBox.getItems().clear();
        statusComboBox.getItems().addAll(Application.Status.values());
        statusComboBox.getSelectionModel().select(this.application.getStatus());

        interviewDateTextField.setText(formatter.format(studApplication.getInterview().getDate()));
        interviewTimeTextField.setText(timeFormatter.format(studApplication.getInterview().getStart_time()));

        experienceListView.getItems().clear();
        for (var x : tempStud.getStudentExperiences()) {
            experienceListView.getItems().add(x);
        }
        qualificationListView.getItems().clear();
        for (var x : tempStud.getStudentQualifications()) {
            qualificationListView.getItems().add(x);
        }
        skillListView.getItems().clear();
        for (var x : tempStud.getStudentSkills()) {
            skillListView.getItems().add(x);
        }
    }

}
