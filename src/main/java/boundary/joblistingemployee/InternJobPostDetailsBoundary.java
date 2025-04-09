package boundary.joblistingemployee;

import adt.ArrayList;
import atlantafx.base.theme.Styles;
import control.joblistingemployee.InternJobPostDetailsControl;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @Raymond
 */
public class InternJobPostDetailsBoundary implements Initializable {

    @FXML private Button modifyBtn;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField descTextField;
    @FXML private Button experienceAddBtn;
    @FXML private HBox experienceBtnHBox;
    @FXML private Button experienceRemoveBtn;
    @FXML private TextArea fullAddressTextArea;
    @FXML private TextField maxSalaryTextField;
    @FXML private TextField minSalaryTextField;
    @FXML private Button qualificationAddBtn;
    @FXML private HBox qualificationBtnHBox;
    @FXML private Button qualificationRemoveBtn;
    @FXML private ListView<Experience> requiredExperienceListView;
    @FXML private ListView<Qualification> requiredQualificationListView;
    @FXML private ListView<Skill> requiredSkillListView;
    @FXML private Button skillAddBtn;
    @FXML private HBox skillBtnHBox;
    @FXML private Button skillRemoveBtn;
    @FXML private ComboBox<Location.MalaysianRegion> stateComboBox;
    @FXML private TextField titleTextField;
    @FXML private Button addBtn;

    private InternJobPostDetailsControl control;

    public Button getModifyBtn() {
        return modifyBtn;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public TextField getDescTextField() {
        return descTextField;
    }

    public Button getExperienceAddBtn() {
        return experienceAddBtn;
    }

    public HBox getExperienceBtnHBox() {
        return experienceBtnHBox;
    }

    public Button getExperienceRemoveBtn() {
        return experienceRemoveBtn;
    }

    public TextArea getFullAddressTextArea() {
        return fullAddressTextArea;
    }

    public TextField getMaxSalaryTextField() {
        return maxSalaryTextField;
    }

    public TextField getMinSalaryTextField() {
        return minSalaryTextField;
    }

    public Button getQualificationAddBtn() {
        return qualificationAddBtn;
    }

    public HBox getQualificationBtnHBox() {
        return qualificationBtnHBox;
    }

    public Button getQualificationRemoveBtn() {
        return qualificationRemoveBtn;
    }

    public ListView<Experience> getRequiredExperienceListView() {
        return requiredExperienceListView;
    }

    public ListView<Qualification> getRequiredQualificationListView() {
        return requiredQualificationListView;
    }

    public ListView<Skill> getRequiredSkillListView() {
        return requiredSkillListView;
    }

    public Button getSkillAddBtn() {
        return skillAddBtn;
    }

    public HBox getSkillBtnHBox() {
        return skillBtnHBox;
    }

    public Button getSkillRemoveBtn() {
        return skillRemoveBtn;
    }

    public ComboBox<Location.MalaysianRegion> getStateComboBox() {
        return stateComboBox;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requiredExperienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Industry Type: %s, Duration: %d", item.getDesc(), item.getIndustryType().toString(), item.getDuration()));
            }
        });
        Styles.toggleStyleClass(requiredExperienceListView, Styles.STRIPED);
        experienceAddBtn.setOnAction(eh -> control.addExperience());
        experienceRemoveBtn.setOnAction(eh -> control.removeExperience());

        requiredQualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Qualification Type: %s, Level: %d", item.getDesc(), item.getQualificationType().toString(), item.getLevel()));
            }
        });
        Styles.toggleStyleClass(requiredQualificationListView, Styles.STRIPED);
        qualificationAddBtn.setOnAction(eh -> control.addQualification());
        qualificationRemoveBtn.setOnAction(eh -> control.removeQualification());

        requiredSkillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Name: %s, Skill Type: %s, Proficiency Level: %d", item.getName(), item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        Styles.toggleStyleClass(requiredSkillListView, Styles.STRIPED);
        skillAddBtn.setOnAction(eh -> control.addSkill());
        skillRemoveBtn.setOnAction(eh -> control.removeSkill());

        modifyBtn.setOnAction(eh -> setUpForModify());

        saveBtn.setOnAction(eh -> control.saveJobPostDetails());

        addBtn.setOnAction(eh -> control.addJobPost());
    }

    public void setInternPost(InternPost internpost) {
        control = new InternJobPostDetailsControl(this);
        control.setInternPost(internpost);
    }

    public InternPost getInternPost() {
        return control.getCurrentInternPost();
    }
    
    public void setUpForReadOnly() {
        experienceBtnHBox.setVisible(false);
        experienceBtnHBox.setManaged(false);
        qualificationBtnHBox.setVisible(false);
        qualificationBtnHBox.setManaged(false);
        skillBtnHBox.setVisible(false);
        skillBtnHBox.setManaged(false);
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);

        modifyBtn.setVisible(true);
        modifyBtn.setManaged(true);

        fullAddressTextArea.setEditable(false);
        titleTextField.setEditable(false);
        maxSalaryTextField.setEditable(false);
        minSalaryTextField.setEditable(false);
        descTextField.setEditable(false);
        stateComboBox.setDisable(true);

        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });
    }

    public void setUpForModify() {
        experienceBtnHBox.setVisible(true);
        experienceBtnHBox.setManaged(true);
        qualificationBtnHBox.setVisible(true);
        qualificationBtnHBox.setManaged(true);
        skillBtnHBox.setVisible(true);
        skillBtnHBox.setManaged(true);
        saveBtn.setVisible(true);
        saveBtn.setManaged(true);

        modifyBtn.setVisible(false);
        modifyBtn.setManaged(false);

        addBtn.setVisible(false);
        addBtn.setManaged(false);

        fullAddressTextArea.setEditable(true);
        titleTextField.setEditable(true);
        maxSalaryTextField.setEditable(true);
        minSalaryTextField.setEditable(true);
        descTextField.setEditable(true);
        stateComboBox.setDisable(false);

        cancelBtn.setOnAction(eh -> {
            if (control.getCurrentInternPost() == null) {
                enrichFieldsNull();
            } else {
                enrichFieldsNotNull();
            }
            setUpForReadOnly();
        });
    }

    public void enrichFieldsNotNull() {
        InternPost tempTempInternPost = null;
        if (control.getTempInternPost() == null) {
            tempTempInternPost = control.getCurrentInternPost();
            addBtn.setVisible(false);
            addBtn.setManaged(false);
        } else {
            tempTempInternPost = control.getTempInternPost();
            addBtn.setVisible(true);
            addBtn.setManaged(true);
        }
        fullAddressTextArea.setText(tempTempInternPost.getLocation().getFullAddress());
        titleTextField.setText(tempTempInternPost.getTitle());
        descTextField.setText(tempTempInternPost.getDesc());
        minSalaryTextField.setText(tempTempInternPost.getMinMaxSalary().getX() + "");
        maxSalaryTextField.setText(tempTempInternPost.getMinMaxSalary().getY() + "");
        stateComboBox.getItems().addAll(Location.MalaysianRegion.values());
        stateComboBox.getSelectionModel().select(Location.MalaysianRegion.valueOf(tempTempInternPost.getLocation().getState()));

        requiredExperienceListView.getItems().clear();
        for (var x : tempTempInternPost.getInterPostExperiences()) {
            requiredExperienceListView.getItems().add(x);
        }

        requiredQualificationListView.getItems().clear();
        for (var x : tempTempInternPost.getInternPostQualifications()) {
            requiredQualificationListView.getItems().add(x);
        }

        requiredSkillListView.getItems().clear();
        for (var x : tempTempInternPost.getInternPostSkills()) {
            requiredSkillListView.getItems().add(x);
        }

    }

    public void resetTextFieldPseudoClass() {
        titleTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        titleTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        descTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        descTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        minSalaryTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        minSalaryTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        maxSalaryTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        maxSalaryTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        fullAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        fullAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
    }

    public void enrichFieldsNull() {
        fullAddressTextArea.setText("");
        titleTextField.setText("");
        descTextField.setText("");
        minSalaryTextField.setText("");
        maxSalaryTextField.setText("");
        stateComboBox.getItems().addAll(Location.MalaysianRegion.values());
        stateComboBox.getSelectionModel().selectFirst();

        addBtn.setVisible(true);
        addBtn.setManaged(true);

        requiredExperienceListView.getItems().clear();
        requiredQualificationListView.getItems().clear();
        requiredSkillListView.getItems().clear();
    }

}
