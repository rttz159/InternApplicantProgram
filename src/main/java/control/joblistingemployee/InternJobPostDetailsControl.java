package control.joblistingemployee;

import adt.ArrayList;
import adt.ListInterface;
import adt.OrderPair;
import atlantafx.base.theme.Styles;
import boundary.ExperienceDetailBoundary;
import static boundary.PredefinedDialog.showConfirmationDialog;
import static boundary.PredefinedDialog.showErrorDialog;
import boundary.QualificationDetailBoundary;
import boundary.SkillDetailBoundary;
import boundary.joblistingemployee.InternJobPostDetailsBoundary;
import com.rttz.assignment.App;
import dao.CompanyDAO;
import dao.MainControlClass;
import entity.Company;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import utils.Validation;
import utils.builders.InternPostBuilder;

/**
 *
 * @author Raymond
 */
public class InternJobPostDetailsControl {

    private InternPost currentInternPost;
    private InternPost tempInternPost;

    private ListInterface<Experience> tempAddExperiences;
    private ListInterface<Qualification> tempAddQualifications;
    private ListInterface<Skill> tempAddSkills;
    private ListInterface<Experience> tempRemoveExperiences;
    private ListInterface<Qualification> tempRemoveQualifications;
    private ListInterface<Skill> tempRemoveSkills;

    private InternJobPostDetailsBoundary boundary;

    public ListInterface<Experience> getTempAddExperiences() {
        return tempAddExperiences;
    }

    public ListInterface<Qualification> getTempAddQualifications() {
        return tempAddQualifications;
    }

    public ListInterface<Skill> getTempAddSkills() {
        return tempAddSkills;
    }

    public ListInterface<Experience> getTempRemoveExperiences() {
        return tempRemoveExperiences;
    }

    public ListInterface<Qualification> getTempRemoveQualifications() {
        return tempRemoveQualifications;
    }

    public ListInterface<Skill> getTempRemoveSkills() {
        return tempRemoveSkills;
    }

    public InternPost getCurrentInternPost() {
        return currentInternPost;
    }

    public InternPost getTempInternPost() {
        return tempInternPost;
    }

    public InternJobPostDetailsControl(InternJobPostDetailsBoundary boundary) {
        this.boundary = boundary;
        tempAddExperiences = new ArrayList<>();
        tempAddQualifications = new ArrayList<>();
        tempAddSkills = new ArrayList<>();
        tempRemoveExperiences = new ArrayList<>();
        tempRemoveQualifications = new ArrayList<>();
        tempRemoveSkills = new ArrayList<>();
    }

    public void addExperience() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostExperience.fxml"));
        Node node = null;
        ExperienceDetailBoundary controller = null;
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
            boundary.getRequiredExperienceListView().getItems().add(tempExperience);
        }
    }

    public void removeExperience() {
        Experience selectedExperience = boundary.getRequiredExperienceListView().getSelectionModel().getSelectedItem();
        if (selectedExperience == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Select Experience");
            alert.setHeaderText("No experience selected.");
            alert.setContentText("Please select an experience before proceed.");
            alert.showAndWait();
            return;
        }

        tempRemoveExperiences.append(selectedExperience);
        boundary.getRequiredExperienceListView().getItems().remove(selectedExperience);
    }

    public void addQualification() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostQualification.fxml"));
        Node node = null;
        QualificationDetailBoundary controller = null;
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
            boundary.getRequiredQualificationListView().getItems().add(tempQualification);
        }
    }

    public void removeQualification() {
        Qualification selectedQualification = boundary.getRequiredQualificationListView().getSelectionModel().getSelectedItem();
        if (selectedQualification == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Select Qualification");
            alert.setHeaderText("No qualification selected.");
            alert.setContentText("Please select a qualification before proceed.");
            alert.showAndWait();
            return;
        }

        tempRemoveQualifications.append(selectedQualification);
        boundary.getRequiredQualificationListView().getItems().remove(selectedQualification);
    }

    public void addSkill() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostSkill.fxml"));
        Node node = null;
        SkillDetailBoundary controller = null;
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
            boundary.getRequiredSkillListView().getItems().add(tempSkill);
        }
    }

    public void removeSkill() {
        Skill selectedSkill = boundary.getRequiredSkillListView().getSelectionModel().getSelectedItem();
        if (selectedSkill == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Select Skill");
            alert.setHeaderText("No skill selected.");
            alert.setContentText("Please select a skill before proceed.");
            alert.showAndWait();
            return;
        }

        tempRemoveSkills.append(selectedSkill);
        boundary.getRequiredSkillListView().getItems().remove(selectedSkill);
    }

    public void saveJobPostDetails() {
        boolean valid = true;

        boolean titleValid = Validation.validateText(boundary.getTitleTextField().getText());
        boundary.getTitleTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !titleValid);
        boundary.getTitleTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, titleValid);
        valid &= titleValid;

        boolean descValid = Validation.validateText(boundary.getDescTextField().getText());
        boundary.getDescTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !descValid);
        boundary.getDescTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, descValid);
        valid &= descValid;

        boolean minSalaryValid = Validation.isValidDouble(boundary.getMinSalaryTextField().getText());
        boundary.getMinSalaryTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !minSalaryValid);
        boundary.getMinSalaryTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, minSalaryValid);
        valid &= minSalaryValid;

        boolean maxSalaryValid = Validation.isValidDouble(boundary.getMaxSalaryTextField().getText());
        boundary.getMaxSalaryTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !maxSalaryValid);
        boundary.getMaxSalaryTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, maxSalaryValid);
        valid &= maxSalaryValid;

        boolean addressValid = Validation.validateText(boundary.getFullAddressTextArea().getText());
        boundary.getFullAddressTextArea().pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
        boundary.getFullAddressTextArea().pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
        valid &= addressValid;

        if (valid) {
            if (currentInternPost == null) {
                tempInternPost = new InternPostBuilder()
                        .title(boundary.getTitleTextField().getText())
                        .minMaxSalary(new OrderPair<>(Double.valueOf(boundary.getMinSalaryTextField().getText()), Double.valueOf(boundary.getMaxSalaryTextField().getText())))
                        .desc(boundary.getDescTextField().getText())
                        .location(new Location(boundary.getStateComboBox().getSelectionModel().getSelectedItem().toString(), boundary.getFullAddressTextArea().getText()))
                        .build();
                addNRemoveTempEntities(tempInternPost);
                boundary.enrichFieldsNotNull();
                boundary.setUpForReadOnly();
                boundary.resetTextFieldPseudoClass();

            } else {
                Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");
                InternPost tempTempInternPost = null;
                if (tempInternPost == null) {
                    tempTempInternPost = currentInternPost;
                } else {
                    tempTempInternPost = tempInternPost;
                }
                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                    addNRemoveTempEntities(tempTempInternPost);
                    tempTempInternPost.setTitle(boundary.getTitleTextField().getText());
                    tempTempInternPost.setDesc(boundary.getDescTextField().getText());
                    tempTempInternPost.setMinMaxSalary(new OrderPair<>(Double.valueOf(boundary.getMinSalaryTextField().getText()), Double.valueOf(boundary.getMaxSalaryTextField().getText())));
                    tempTempInternPost.setLocation(new Location(boundary.getStateComboBox().getSelectionModel().getSelectedItem().toString(), boundary.getFullAddressTextArea().getText()));

                    if (tempInternPost == null) {
                        CompanyDAO.updateCompanyById((Company) MainControlClass.getCurrentUser());
                    }

                    boundary.enrichFieldsNotNull();
                    boundary.setUpForReadOnly();
                    JobListingEmployeeShareState.getInstance().refresh();
                    boundary.resetTextFieldPseudoClass();
                }
            }
        }
    }

    private void addNRemoveTempEntities(InternPost tempInternPost) {
        for (var x : tempAddExperiences) {
            tempInternPost.getInterPostExperiences().add(x);
        }
        for (var x : tempRemoveExperiences) {
            tempInternPost.getInterPostExperiences().remove(x);
        }
        for (var x : tempAddQualifications) {
            tempInternPost.getInternPostQualifications().add(x);
        }
        for (var x : tempRemoveQualifications) {
            tempInternPost.getInternPostQualifications().remove(x);
        }
        for (var x : tempAddSkills) {
            tempInternPost.getInternPostSkills().add(x);
        }
        for (var x : tempRemoveSkills) {
            tempInternPost.getInternPostSkills().remove(x);
        }
    }

    public void addJobPost() {
        if (currentInternPost == null && tempInternPost != null) {
            Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                currentInternPost = tempInternPost;
                ((Stage) boundary.getAddBtn().getScene().getWindow()).close();
            }
        } else {
            showErrorDialog("Empty InternPost is not allowed");
        }
    }

    public void setInternPost(InternPost internpost) {
        this.currentInternPost = internpost;
        if (currentInternPost != null && !currentInternPost.getStatus()) {
            boundary.getModifyBtn().setDisable(true);
        }
        boundary.setUpForReadOnly();
        if (this.currentInternPost == null) {
            boundary.enrichFieldsNull();
        } else {
            boundary.enrichFieldsNotNull();
        }
    }

    public InternPost getInternPost() {
        return this.currentInternPost;
    }
}
