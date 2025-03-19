package boundary.joblistingemployee;

import adt.ArrayList;
import adt.ListInterface;
import adt.OrderPair;
import atlantafx.base.theme.Styles;
import boundary.QualificationDetailController;
import boundary.SkillDetailController;
import boundary.ExperienceDetailController;
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
import utils.builders.InternPostBuilder;

/**
 *
 * @author rttz159
 */
public class InternJobPostDetailsController implements Initializable {

    @FXML
    private Button modifyBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private Button experienceAddBtn;

    @FXML
    private HBox experienceBtnHBox;

    @FXML
    private Button experienceRemoveBtn;

    @FXML
    private TextArea fullAddressTextArea;

    @FXML
    private TextField maxSalaryTextField;

    @FXML
    private TextField minSalaryTextField;

    @FXML
    private Button qualificationAddBtn;

    @FXML
    private HBox qualificationBtnHBox;

    @FXML
    private Button qualificationRemoveBtn;

    @FXML
    private ListView<Experience> requiredExperienceListView;

    @FXML
    private ListView<Qualification> requiredQualificationListView;

    @FXML
    private ListView<Skill> requiredSkillListView;

    @FXML
    private Button skillAddBtn;

    @FXML
    private HBox skillBtnHBox;

    @FXML
    private Button skillRemoveBtn;

    @FXML
    private ComboBox<Location.MalaysianRegion> stateComboBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button addBtn;

    private InternPost currentInternPost;
    private InternPost tempInternPost;

    private ListInterface<Experience> tempAddExperiences;
    private ListInterface<Qualification> tempAddQualifications;
    private ListInterface<Skill> tempAddSkills;
    private ListInterface<Experience> tempRemoveExperiences;
    private ListInterface<Qualification> tempRemoveQualifications;
    private ListInterface<Skill> tempRemoveSkills;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        requiredExperienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Industry Type: %s, Duration: %d", item.getDesc(),item.getIndustryType().toString(), item.getDuration()));
            }
        });
        Styles.toggleStyleClass(requiredExperienceListView, Styles.STRIPED);
        experienceAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostExperience.fxml"));
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
                requiredExperienceListView.getItems().add(tempExperience);
            }
        });
        experienceRemoveBtn.setOnAction(eh -> {
            Experience selectedExperience = requiredExperienceListView.getSelectionModel().getSelectedItem();
            if (selectedExperience == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Experience");
                alert.setHeaderText("No experience selected.");
                alert.setContentText("Please select an experience before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveExperiences.append(selectedExperience);
            requiredExperienceListView.getItems().remove(selectedExperience);
        });

        requiredQualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Desc: %s, Qualification Type: %s, Level: %d", item.getDesc(),item.getQualificationType().toString(), item.getLevel()));
            }
        });
        Styles.toggleStyleClass(requiredQualificationListView, Styles.STRIPED);
        qualificationAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostQualification.fxml"));
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
                requiredQualificationListView.getItems().add(tempQualification);
            }
        });
        qualificationRemoveBtn.setOnAction(eh -> {
            Qualification selectedQualification = requiredQualificationListView.getSelectionModel().getSelectedItem();
            if (selectedQualification == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Qualification");
                alert.setHeaderText("No qualification selected.");
                alert.setContentText("Please select a qualification before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveQualifications.append(selectedQualification);
            requiredQualificationListView.getItems().remove(selectedQualification);
        });

        requiredSkillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Name: %s, Skill Type: %s, Proficiency Level: %d", item.getName(),item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        Styles.toggleStyleClass(requiredSkillListView, Styles.STRIPED);
        skillAddBtn.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternPostSkill.fxml"));
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
                requiredSkillListView.getItems().add(tempSkill);
            }
        });
        skillRemoveBtn.setOnAction(eh -> {
            Skill selectedSkill = requiredSkillListView.getSelectionModel().getSelectedItem();
            if (selectedSkill == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Skill");
                alert.setHeaderText("No skill selected.");
                alert.setContentText("Please select a skill before proceed.");
                alert.showAndWait();
                return;
            }

            tempRemoveSkills.append(selectedSkill);
            requiredSkillListView.getItems().remove(selectedSkill);
        });

        modifyBtn.setOnAction(eh -> {
            setUpForModify();
        });

        saveBtn.setOnAction(eh -> {
            boolean valid = true;

            boolean titleValid = Validation.validateText(titleTextField.getText());
            titleTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !titleValid);
            titleTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, titleValid);
            valid &= titleValid;

            boolean descValid = Validation.validateText(descTextField.getText());
            descTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !descValid);
            descTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, descValid);
            valid &= descValid;

            boolean minSalaryValid = Validation.isValidDouble(minSalaryTextField.getText());
            minSalaryTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !minSalaryValid);
            minSalaryTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, minSalaryValid);
            valid &= minSalaryValid;

            boolean maxSalaryValid = Validation.isValidDouble(maxSalaryTextField.getText());
            maxSalaryTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !maxSalaryValid);
            maxSalaryTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, maxSalaryValid);
            valid &= maxSalaryValid;

            boolean addressValid = Validation.validateText(fullAddressTextArea.getText());
            fullAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            fullAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            if (valid) {
                if (currentInternPost == null) {
                    tempInternPost = new InternPostBuilder()
                            .title(titleTextField.getText())
                            .minMaxSalary(new OrderPair<>(Double.valueOf(minSalaryTextField.getText()), Double.valueOf(maxSalaryTextField.getText())))
                            .desc(descTextField.getText())
                            .location(new Location(stateComboBox.getSelectionModel().getSelectedItem().toString(), fullAddressTextArea.getText()))
                            .build();

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

                    enrichFieldsNotNull();
                    setUpForReadOnly();
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

                } else {
                    Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");
                    InternPost tempTempInternPost = null;
                    if (tempInternPost == null) {
                        tempTempInternPost = currentInternPost;
                    } else {
                        tempTempInternPost = tempInternPost;
                    }
                    if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
                        for (var x : tempAddExperiences) {
                            tempTempInternPost.getInterPostExperiences().add(x);
                        }
                        for (var x : tempRemoveExperiences) {
                            tempTempInternPost.getInterPostExperiences().remove(x);
                        }
                        for (var x : tempAddQualifications) {
                            tempTempInternPost.getInternPostQualifications().add(x);
                        }
                        for (var x : tempRemoveQualifications) {
                            tempTempInternPost.getInternPostQualifications().remove(x);
                        }
                        for (var x : tempAddSkills) {
                            tempTempInternPost.getInternPostSkills().add(x);
                        }
                        for (var x : tempRemoveSkills) {
                            tempTempInternPost.getInternPostSkills().remove(x);
                        }
                        tempTempInternPost.setTitle(titleTextField.getText());
                        tempTempInternPost.setDesc(descTextField.getText());
                        tempTempInternPost.setMinMaxSalary(new OrderPair<>(Double.valueOf(minSalaryTextField.getText()), Double.valueOf(maxSalaryTextField.getText())));
                        tempTempInternPost.setLocation(new Location(stateComboBox.getSelectionModel().getSelectedItem().toString(), fullAddressTextArea.getText()));

                        if (tempInternPost == null) {
                            CompanyDAO.updateCompanyById((Company) MainControlClass.getCurrentUser());
                        }

                        enrichFieldsNotNull();
                        setUpForReadOnly();
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
                }
            }
        });

        addBtn.setOnAction(eh -> {
            if (currentInternPost == null && tempInternPost != null) {
                Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");
                if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
                    currentInternPost = tempInternPost;
                    ((Stage) addBtn.getScene().getWindow()).close();
                }
            } else {
                showErrorDialog("Empty InternPost is not allowed");
            }
        });
    }

    public void setInternPost(InternPost internpost) {
        this.currentInternPost = internpost;
        setUpForReadOnly();
        if (this.currentInternPost == null) {
            enrichFieldsNull();
        } else {
            enrichFieldsNotNull();
        }
    }

    public InternPost getInternPost() {
        return this.currentInternPost;
    }

    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.setResizable(false);
        alert.showAndWait();
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
            if (this.currentInternPost == null) {
                enrichFieldsNull();
            } else {
                enrichFieldsNotNull();
            }
            setUpForReadOnly();
        });
    }

    private void enrichFieldsNotNull() {
        InternPost tempTempInternPost = null;
        if (tempInternPost == null) {
            tempTempInternPost = currentInternPost;
            addBtn.setVisible(false);
            addBtn.setManaged(false);
        } else {
            tempTempInternPost = tempInternPost;
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

    private void enrichFieldsNull() {
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
