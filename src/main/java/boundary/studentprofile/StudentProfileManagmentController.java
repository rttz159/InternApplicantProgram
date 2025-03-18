package boundary.studentprofile;

import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import com.rttz.assignment.App;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Experience;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.Validation;

/**
 *
 * @author rttz159
 */
public class StudentProfileManagmentController implements Initializable {

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField ageTextField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField contactNoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button experienceAddBtn;

    @FXML
    private HBox experienceBtnHBox;

    @FXML
    private ListView<Experience> experienceListView;

    @FXML
    private Button experienceRemoveBtn;

    @FXML
    private Button modifyBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button qualificationAddBtn;

    @FXML
    private HBox qualificationBtnHBox;

    @FXML
    private ListView<Qualification> qualificationListView;

    @FXML
    private Button qualificationRemoveBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button skillAddBtn;

    @FXML
    private HBox skillBtnHBox;

    @FXML
    private ListView<Skill> skillListView;

    @FXML
    private Button skillRemoveBtn;

    @FXML
    private ComboBox<Location.MalaysianRegion> stateComboBox;

    private Student currentStudent = (Student) MainControlClass.getCurrentUser();

    private ListInterface<Experience> tempAddExperiences;
    private ListInterface<Qualification> tempAddQualifications;
    private ListInterface<Skill> tempAddSkills;
    private ListInterface<Experience> tempRemoveExperiences;
    private ListInterface<Qualification> tempRemoveQualifications;
    private ListInterface<Skill> tempRemoveSkills;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enrichFields();
        setUpForReadOnly();

        experienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Industry Type: %s, Duration: %d", item.getIndustryType().toString(), item.getDuration()));
            }
        });
        Styles.toggleStyleClass(experienceListView, Styles.STRIPED);
        experienceAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileExperience.fxml"));
            Node node = null;
            ExperienceDetailController controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Experience Adder");
            alert.setHeaderText("");
            alert.getDialogPane().setContent(node);
            alert.getButtonTypes().clear();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setOnCloseRequest(event -> stage.close());
            alert.showAndWait();
            Experience tempExperience = controller.getExperience();
            if (tempExperience != null) {
                tempAddExperiences.append(tempExperience);
                experienceListView.getItems().add(tempExperience);
            }
        });
        experienceRemoveBtn.setOnAction(eh -> {
            Experience selectedExperience = experienceListView.getSelectionModel().getSelectedItem();
            if (selectedExperience == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Experience");
                alert.setHeaderText("No experience selected.");
                alert.setContentText("Please select an experience before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveExperiences.append(selectedExperience);
            experienceListView.getItems().remove(selectedExperience);
        });

        qualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Qualification Type: %s, Level: %d", item.getQualificationType().toString(), item.getLevel()));
            }
        });
        Styles.toggleStyleClass(qualificationListView, Styles.STRIPED);
        qualificationAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileQualification.fxml"));
            Node node = null;
            QualificationDetailController controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Qualification Adder");
            alert.setHeaderText("");
            alert.getDialogPane().setContent(node);
            alert.getButtonTypes().clear();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setOnCloseRequest(event -> stage.close());
            alert.showAndWait();
            Qualification tempQualification = controller.getQualification();
            if (tempQualification != null) {
                tempAddQualifications.append(tempQualification);
                qualificationListView.getItems().add(tempQualification);
            }
        });
        qualificationRemoveBtn.setOnAction(eh -> {
            Qualification selectedQualification = qualificationListView.getSelectionModel().getSelectedItem();
            if (selectedQualification == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Qualification");
                alert.setHeaderText("No qualification selected.");
                alert.setContentText("Please select a qualification before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveQualifications.append(selectedQualification);
            qualificationListView.getItems().remove(selectedQualification);
        });

        skillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Skill Type: %s, Proficiency Level: %d", item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        Styles.toggleStyleClass(skillListView, Styles.STRIPED);
        skillAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileSkill.fxml"));
            Node node = null;
            SkillDetailController controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Qualification Adder");
            alert.setHeaderText("");
            alert.getDialogPane().setContent(node);
            alert.getButtonTypes().clear();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setOnCloseRequest(event -> stage.close());
            alert.showAndWait();
            Skill tempSkill = controller.getSkill();
            if (tempSkill != null) {
                tempAddSkills.append(tempSkill);
                skillListView.getItems().add(tempSkill);
            }
        });
        skillRemoveBtn.setOnAction(eh -> {
            Skill selectedSkill = skillListView.getSelectionModel().getSelectedItem();
            if (selectedSkill == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Skill");
                alert.setHeaderText("No skill selected.");
                alert.setContentText("Please select a skill before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveSkills.append(selectedSkill);
            skillListView.getItems().remove(selectedSkill);
        });

        modifyBtn.setOnAction(eh -> {
            setUpForModify();
        });

        saveBtn.setOnAction(eh -> {
            boolean valid = true;

            boolean nameValid = Validation.validateText(nameTextField.getText());
            nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;

            boolean ageValid = Validation.validateDigit(ageTextField.getText());
            ageTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !ageValid);
            ageTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, ageValid);
            valid &= ageValid;

            boolean contactNoValid = Validation.isValidPhoneNumber(contactNoTextField.getText());
            contactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !contactNoValid);
            contactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, contactNoValid);
            valid &= contactNoValid;

            boolean emailValid = Validation.isValidEmail(emailTextField.getText());
            emailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
            emailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
            valid &= emailValid;

            boolean addressValid = Validation.validateText(addressTextArea.getText());
            addressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            addressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            if (valid) {
                Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");

                if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
                    for (var x : tempAddExperiences) {
                        currentStudent.getStudentExperiences().add(x);
                    }
                    for (var x : tempRemoveExperiences) {
                        currentStudent.getStudentExperiences().remove(x);
                    }
                    for (var x : tempAddQualifications) {
                        currentStudent.getStudentQualifications().add(x);
                    }
                    for (var x : tempRemoveQualifications) {
                        currentStudent.getStudentQualifications().remove(x);
                    }
                    for (var x : tempAddSkills) {
                        currentStudent.getStudentSkills().add(x);
                    }
                    for (var x : tempRemoveSkills) {
                        currentStudent.getStudentSkills().remove(x);
                    }
                    currentStudent.setName(nameTextField.getText());
                    currentStudent.setAge(Integer.parseInt(ageTextField.getText()));
                    currentStudent.setContactno(contactNoTextField.getText());
                    currentStudent.setEmail(emailTextField.getText());
                    currentStudent.setLocation(new Location(stateComboBox.getSelectionModel().getSelectedItem().toString(), addressTextArea.getText()));

                    StudentDAO.updateStudentById(currentStudent);
                    enrichFields();
                    setUpForReadOnly();
                    nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    ageTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    ageTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    contactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    contactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    emailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    emailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    addressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    addressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                }

            }
        });

        cancelBtn.setOnAction(eh -> {
            enrichFields();
            setUpForReadOnly();
        });
    }

    private Optional<ButtonType> showConfirmationDialog(String message) {
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

    private void setUpForReadOnly() {
        experienceBtnHBox.setVisible(false);
        experienceBtnHBox.setManaged(false);
        qualificationBtnHBox.setVisible(false);
        qualificationBtnHBox.setManaged(false);
        skillBtnHBox.setVisible(false);
        skillBtnHBox.setManaged(false);
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);

        modifyBtn.setVisible(true);
        modifyBtn.setManaged(true);

        addressTextArea.setEditable(false);
        ageTextField.setEditable(false);
        contactNoTextField.setEditable(false);
        emailTextField.setEditable(false);
        nameTextField.setEditable(false);
        stateComboBox.setDisable(true);

    }

    private void setUpForModify() {
        tempAddExperiences = new ArrayList<>();
        tempAddQualifications = new ArrayList<>();
        tempAddSkills = new ArrayList<>();

        tempRemoveExperiences = new ArrayList<>();
        tempRemoveQualifications = new ArrayList<>();
        tempRemoveSkills = new ArrayList<>();

        experienceBtnHBox.setVisible(true);
        experienceBtnHBox.setManaged(true);
        qualificationBtnHBox.setVisible(true);
        qualificationBtnHBox.setManaged(true);
        skillBtnHBox.setVisible(true);
        skillBtnHBox.setManaged(true);
        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
        saveBtn.setVisible(true);
        saveBtn.setManaged(true);

        modifyBtn.setVisible(false);
        modifyBtn.setManaged(false);

        addressTextArea.setEditable(true);
        ageTextField.setEditable(true);
        contactNoTextField.setEditable(true);
        emailTextField.setEditable(true);
        nameTextField.setEditable(true);
        stateComboBox.setDisable(false);

    }

    private void enrichFields() {
        addressTextArea.setText(currentStudent.getLocation().getFullAddress());
        ageTextField.setText(currentStudent.getAge() + "");
        contactNoTextField.setText(currentStudent.getContactno());
        emailTextField.setText(currentStudent.getEmail());
        nameTextField.setText(currentStudent.getName());
        stateComboBox.getItems().addAll(Location.MalaysianRegion.values());
        stateComboBox.getSelectionModel().select(Location.MalaysianRegion.valueOf(currentStudent.getLocation().getState()));

        experienceListView.getItems().clear();
        for (var x : currentStudent.getStudentExperiences()) {
            experienceListView.getItems().add(x);
        }

        qualificationListView.getItems().clear();
        for (var x : currentStudent.getStudentQualifications()) {
            qualificationListView.getItems().add(x);
        }

        skillListView.getItems().clear();
        for (var x : currentStudent.getStudentSkills()) {
            skillListView.getItems().add(x);
        }

    }

}
