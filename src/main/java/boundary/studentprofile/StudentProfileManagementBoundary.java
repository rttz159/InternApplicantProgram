package boundary.studentprofile;

import boundary.ExperienceDetailBoundary;
import boundary.QualificationDetailBoundary;
import boundary.SkillDetailBoundary;
import com.rttz.assignment.App;
import control.studentprofile.StudentProfileControl;
import entity.Experience;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class StudentProfileManagementBoundary implements Initializable {

    @FXML private TextArea addressTextArea;
    @FXML private TextField ageTextField;
    @FXML private Button cancelBtn;
    @FXML private TextField contactNoTextField;
    @FXML private TextField emailTextField;
    @FXML private Button experienceAddBtn;
    @FXML private HBox experienceBtnHBox;
    @FXML private ListView<Experience> experienceListView;
    @FXML private Button experienceRemoveBtn;
    @FXML private Button modifyBtn;
    @FXML private TextField nameTextField;
    @FXML private Button qualificationAddBtn;
    @FXML private HBox qualificationBtnHBox;
    @FXML private ListView<Qualification> qualificationListView;
    @FXML private Button qualificationRemoveBtn;
    @FXML private Button saveBtn;
    @FXML private Button skillAddBtn;
    @FXML private HBox skillBtnHBox;
    @FXML private ListView<Skill> skillListView;
    @FXML private Button skillRemoveBtn;
    @FXML private ComboBox<Location.MalaysianRegion> stateComboBox;

    private StudentProfileControl control;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new StudentProfileControl(this);
        control.initialize();
    }

    public TextArea getAddressTextArea() { return addressTextArea; }
    public TextField getAgeTextField() { return ageTextField; }
    public Button getCancelBtn() { return cancelBtn; }
    public TextField getContactNoTextField() { return contactNoTextField; }
    public TextField getEmailTextField() { return emailTextField; }
    public Button getExperienceAddBtn() { return experienceAddBtn; }
    public HBox getExperienceBtnHBox() { return experienceBtnHBox; }
    public ListView<Experience> getExperienceListView() { return experienceListView; }
    public Button getExperienceRemoveBtn() { return experienceRemoveBtn; }
    public Button getModifyBtn() { return modifyBtn; }
    public TextField getNameTextField() { return nameTextField; }
    public Button getQualificationAddBtn() { return qualificationAddBtn; }
    public HBox getQualificationBtnHBox() { return qualificationBtnHBox; }
    public ListView<Qualification> getQualificationListView() { return qualificationListView; }
    public Button getQualificationRemoveBtn() { return qualificationRemoveBtn; }
    public Button getSaveBtn() { return saveBtn; }
    public Button getSkillAddBtn() { return skillAddBtn; }
    public HBox getSkillBtnHBox() { return skillBtnHBox; }
    public ListView<Skill> getSkillListView() { return skillListView; }
    public Button getSkillRemoveBtn() { return skillRemoveBtn; }
    public ComboBox<Location.MalaysianRegion> getStateComboBox() { return stateComboBox; }

    public Experience showExperienceDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileExperience.fxml"));
        Node node = fxmlLoader.load();
        ExperienceDetailBoundary controller = fxmlLoader.getController();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Experience Adder");
        alert.getDialogPane().setContent(node);
        alert.getButtonTypes().clear();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> stage.close());
        alert.showAndWait();
        
        return controller.getExperience();
    }

    public Qualification showQualificationDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileQualification.fxml"));
        Node node = fxmlLoader.load();
        QualificationDetailBoundary controller = fxmlLoader.getController();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Qualification Adder");
        alert.getDialogPane().setContent(node);
        alert.getButtonTypes().clear();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> stage.close());
        alert.showAndWait();
        
        return controller.getQualification();
    }

    public Skill showSkillDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentprofile/StudentProfileSkill.fxml"));
        Node node = fxmlLoader.load();
        SkillDetailBoundary controller = fxmlLoader.getController();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Skill Adder");
        alert.getDialogPane().setContent(node);
        alert.getButtonTypes().clear();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> stage.close());
        alert.showAndWait();
        
        return controller.getSkill();
    }

    public void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}