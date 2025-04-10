package boundary.companyapplication;

import control.companyapplication.ApplicationDetailsControl;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class ApplicationDetailsBoundary {

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;
    @FXML private Button statusBtn;
    @FXML private TextField descTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField salaryRangeTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField interviewDateTextField;
    @FXML private TextField interviewTimeTextField;
    @FXML private ListView<Qualification> qualificationListView;
    @FXML private ListView<Skill> skillListView;
    @FXML private ListView<Experience> experienceListView;
    @FXML private ComboBox<Application.Status> statusComboBox;
    @FXML private TextField studentAgeTextField;
    @FXML private TextField studentNameTextField;
    @FXML private TextField studentEmailTextField;

    private ApplicationDetailsControl control;

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getStatusBtn() {
        return statusBtn;
    }

    public TextField getDescTextField() {
        return descTextField;
    }

    public TextField getLocationTextField() {
        return locationTextField;
    }

    public TextField getSalaryRangeTextField() {
        return salaryRangeTextField;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public TextField getInterviewDateTextField() {
        return interviewDateTextField;
    }

    public TextField getInterviewTimeTextField() {
        return interviewTimeTextField;
    }

    public ListView<Qualification> getQualificationListView() {
        return qualificationListView;
    }

    public ListView<Skill> getSkillListView() {
        return skillListView;
    }

    public ListView<Experience> getExperienceListView() {
        return experienceListView;
    }

    public ComboBox<Application.Status> getStatusComboBox() {
        return statusComboBox;
    }

    public TextField getStudentAgeTextField() {
        return studentAgeTextField;
    }

    public TextField getStudentNameTextField() {
        return studentNameTextField;
    }

    public TextField getStudentEmailTextField() {
        return studentEmailTextField;
    }

    public void setApplication(Application application, Application studApplication) {
        control = new ApplicationDetailsControl(this,application,studApplication);
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
        saveBtn.setOnAction(eh -> control.saveApplication());
        statusBtn.setOnAction(eh -> {
            setUpforModify();
        });
        setUpforReadOnly();
        enrichField();
    }

    public void setUpforReadOnly() {
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
        InternPost internPost = control.getInternPost();
        Student tempStud = control.getTempStud();
        Application application = control.getApplication();
        Application studApplication = control.getStudApplication();
        
        this.titleTextField.setText(internPost.getTitle().toUpperCase());
        this.descTextField.setText(internPost.getDesc());
        this.locationTextField.setText("State: " + internPost.getLocation().getState() + " , Full Address: " + internPost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", internPost.getMinMaxSalary().getX(), internPost.getMinMaxSalary().getY()));
        this.studentAgeTextField.setText(tempStud.getAge() + "");
        this.studentNameTextField.setText(tempStud.getName());
        this.studentEmailTextField.setText(tempStud.getEmail());

        statusComboBox.getItems().clear();
        statusComboBox.getItems().addAll(Application.Status.values());
        statusComboBox.getSelectionModel().select(application.getStatus());

        interviewDateTextField.setText(control.getFormatter().format(studApplication.getInterview().getDate()));
        interviewTimeTextField.setText(control.getTimeFormatter().format(studApplication.getInterview().getStart_time()));

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