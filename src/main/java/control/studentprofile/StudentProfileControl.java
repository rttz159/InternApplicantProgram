package control.studentprofile;

import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import boundary.studentprofile.StudentProfileManagementBoundary;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Experience;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import utils.Validation;
import static boundary.PredefinedDialog.showConfirmationDialog;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Raymond
 */
public class StudentProfileControl {
    private final StudentProfileManagementBoundary boundary;
    private Student currentStudent;
    
    private ListInterface<Experience> tempAddExperiences;
    private ListInterface<Qualification> tempAddQualifications;
    private ListInterface<Skill> tempAddSkills;
    private ListInterface<Experience> tempRemoveExperiences;
    private ListInterface<Qualification> tempRemoveQualifications;
    private ListInterface<Skill> tempRemoveSkills;

    public StudentProfileControl(StudentProfileManagementBoundary boundary) {
        this.boundary = boundary;
        this.currentStudent = (Student) MainControlClass.getCurrentUser();
    }

    public void initialize() {
        setupListViews();
        setupEventHandlers();
        enrichFields();
        setUpForReadOnly();
    }

    private void setupListViews() {
        boundary.getExperienceListView().setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : 
                    String.format("Desc: %s, Industry Type: %s, Duration: %d", 
                        item.getDesc(), item.getIndustryType().toString(), item.getDuration()));
            }
        });
        Styles.toggleStyleClass(boundary.getExperienceListView(), Styles.STRIPED);

        boundary.getQualificationListView().setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : 
                    String.format("Desc: %s, Qualification Type: %s, Level: %d", 
                        item.getDesc(), item.getQualificationType().toString(), item.getLevel()));
            }
        });
        Styles.toggleStyleClass(boundary.getQualificationListView(), Styles.STRIPED);

        boundary.getSkillListView().setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : 
                    String.format("Name: %s, Skill Type: %s, Proficiency Level: %d", 
                        item.getName(), item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        Styles.toggleStyleClass(boundary.getSkillListView(), Styles.STRIPED);
    }

    private void setupEventHandlers() {
        boundary.getExperienceAddBtn().setOnAction(eh -> handleAddExperience());
        boundary.getExperienceRemoveBtn().setOnAction(eh -> handleRemoveExperience());
        
        boundary.getQualificationAddBtn().setOnAction(eh -> handleAddQualification());
        boundary.getQualificationRemoveBtn().setOnAction(eh -> handleRemoveQualification());
        
        boundary.getSkillAddBtn().setOnAction(eh -> handleAddSkill());
        boundary.getSkillRemoveBtn().setOnAction(eh -> handleRemoveSkill());
        
        boundary.getModifyBtn().setOnAction(eh -> setUpForModify());
        boundary.getSaveBtn().setOnAction(eh -> handleSave());
        boundary.getCancelBtn().setOnAction(eh -> {
            enrichFields();
            setUpForReadOnly();
        });
    }

    private void handleAddExperience() {
        try {
            Experience tempExperience = boundary.showExperienceDialog();
            if (tempExperience != null) {
                tempAddExperiences.append(tempExperience);
                boundary.getExperienceListView().getItems().add(tempExperience);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleRemoveExperience() {
        Experience selectedExperience = boundary.getExperienceListView().getSelectionModel().getSelectedItem();
        if (selectedExperience == null) {
            boundary.showErrorDialog("Select Experience", "No experience selected.", 
                "Please select an experience before proceed.");
            return;
        }

        tempRemoveExperiences.append(selectedExperience);
        boundary.getExperienceListView().getItems().remove(selectedExperience);
    }

    private void handleAddQualification() {
        try {
            Qualification tempQualification = boundary.showQualificationDialog();
            if (tempQualification != null) {
                tempAddQualifications.append(tempQualification);
                boundary.getQualificationListView().getItems().add(tempQualification);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleRemoveQualification() {
        Qualification selectedQualification = boundary.getQualificationListView().getSelectionModel().getSelectedItem();
        if (selectedQualification == null) {
            boundary.showErrorDialog("Select Qualification", "No qualification selected.", 
                "Please select a qualification before proceed.");
            return;
        }

        tempRemoveQualifications.append(selectedQualification);
        boundary.getQualificationListView().getItems().remove(selectedQualification);
    }

    private void handleAddSkill() {
        try {
            Skill tempSkill = boundary.showSkillDialog();
            if (tempSkill != null) {
                tempAddSkills.append(tempSkill);
                boundary.getSkillListView().getItems().add(tempSkill);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleRemoveSkill() {
        Skill selectedSkill = boundary.getSkillListView().getSelectionModel().getSelectedItem();
        if (selectedSkill == null) {
            boundary.showErrorDialog("Select Skill", "No skill selected.", 
                "Please select a skill before proceed.");
            return;
        }

        tempRemoveSkills.append(selectedSkill);
        boundary.getSkillListView().getItems().remove(selectedSkill);
    }

    private void handleSave() {
        boolean valid = validateFields();

        if (valid) {
            Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");

            if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
                saveStudentData();
                enrichFields();
                setUpForReadOnly();
                resetFieldStyles();
            }
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        boolean nameValid = Validation.validateText(boundary.getNameTextField().getText());
        boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
        boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
        valid &= nameValid;

        boolean ageValid = Validation.validateDigit(boundary.getAgeTextField().getText());
        boundary.getAgeTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !ageValid);
        boundary.getAgeTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, ageValid);
        valid &= ageValid;

        boolean contactNoValid = Validation.isValidPhoneNumber(boundary.getContactNoTextField().getText());
        boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !contactNoValid);
        boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, contactNoValid);
        valid &= contactNoValid;

        boolean emailValid = Validation.isValidEmail(boundary.getEmailTextField().getText());
        boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
        boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
        valid &= emailValid;

        boolean addressValid = Validation.validateText(boundary.getAddressTextArea().getText());
        boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
        boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
        valid &= addressValid;

        return valid;
    }

    private void saveStudentData() {
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

        currentStudent.setName(boundary.getNameTextField().getText());
        currentStudent.setAge(Integer.parseInt(boundary.getAgeTextField().getText()));
        currentStudent.setContactno(boundary.getContactNoTextField().getText());
        currentStudent.setEmail(boundary.getEmailTextField().getText());
        currentStudent.setLocation(new Location(
            boundary.getStateComboBox().getSelectionModel().getSelectedItem().toString(), 
            boundary.getAddressTextArea().getText()
        ));

        StudentDAO.updateStudentById(currentStudent);
    }

    private void resetFieldStyles() {
        boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
        boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        boundary.getAgeTextField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
        boundary.getAgeTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
        boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
        boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_DANGER, false);
        boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
    }

    private void setUpForReadOnly() {
        boundary.getExperienceBtnHBox().setVisible(false);
        boundary.getExperienceBtnHBox().setManaged(false);
        boundary.getQualificationBtnHBox().setVisible(false);
        boundary.getQualificationBtnHBox().setManaged(false);
        boundary.getSkillBtnHBox().setVisible(false);
        boundary.getSkillBtnHBox().setManaged(false);
        boundary.getCancelBtn().setVisible(false);
        boundary.getCancelBtn().setManaged(false);
        boundary.getSaveBtn().setVisible(false);
        boundary.getSaveBtn().setManaged(false);

        boundary.getModifyBtn().setVisible(true);
        boundary.getModifyBtn().setManaged(true);

        boundary.getAddressTextArea().setEditable(false);
        boundary.getAgeTextField().setEditable(false);
        boundary.getContactNoTextField().setEditable(false);
        boundary.getEmailTextField().setEditable(false);
        boundary.getNameTextField().setEditable(false);
        boundary.getStateComboBox().setDisable(true);
    }

    private void setUpForModify() {
        tempAddExperiences = new ArrayList<>();
        tempAddQualifications = new ArrayList<>();
        tempAddSkills = new ArrayList<>();

        tempRemoveExperiences = new ArrayList<>();
        tempRemoveQualifications = new ArrayList<>();
        tempRemoveSkills = new ArrayList<>();

        boundary.getExperienceBtnHBox().setVisible(true);
        boundary.getExperienceBtnHBox().setManaged(true);
        boundary.getQualificationBtnHBox().setVisible(true);
        boundary.getQualificationBtnHBox().setManaged(true);
        boundary.getSkillBtnHBox().setVisible(true);
        boundary.getSkillBtnHBox().setManaged(true);
        boundary.getCancelBtn().setVisible(true);
        boundary.getCancelBtn().setManaged(true);
        boundary.getSaveBtn().setVisible(true);
        boundary.getSaveBtn().setManaged(true);

        boundary.getModifyBtn().setVisible(false);
        boundary.getModifyBtn().setManaged(false);

        boundary.getAddressTextArea().setEditable(true);
        boundary.getAgeTextField().setEditable(true);
        boundary.getContactNoTextField().setEditable(true);
        boundary.getEmailTextField().setEditable(true);
        boundary.getNameTextField().setEditable(true);
        boundary.getStateComboBox().setDisable(false);
    }

    private void enrichFields() {
        boundary.getAddressTextArea().setText(currentStudent.getLocation().getFullAddress());
        boundary.getAgeTextField().setText(currentStudent.getAge() + "");
        boundary.getContactNoTextField().setText(currentStudent.getContactno());
        boundary.getEmailTextField().setText(currentStudent.getEmail());
        boundary.getNameTextField().setText(currentStudent.getName());
        boundary.getStateComboBox().getItems().addAll(Location.MalaysianRegion.values());
        boundary.getStateComboBox().getSelectionModel().select(Location.MalaysianRegion.valueOf(currentStudent.getLocation().getState()));

        boundary.getExperienceListView().getItems().clear();
        for (var x : currentStudent.getStudentExperiences()) {
            boundary.getExperienceListView().getItems().add(x);
        }

        boundary.getQualificationListView().getItems().clear();
        for (var x : currentStudent.getStudentQualifications()) {
            boundary.getQualificationListView().getItems().add(x);
        }

        boundary.getSkillListView().getItems().clear();
        for (var x : currentStudent.getStudentSkills()) {
            boundary.getSkillListView().getItems().add(x);
        }
    }
}