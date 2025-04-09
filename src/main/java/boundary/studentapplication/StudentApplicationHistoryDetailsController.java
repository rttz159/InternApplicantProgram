package boundary.studentapplication;

import control.studentapplication.StudentApplicationHistoryDetailsControl;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @Raymond
 */
public class StudentApplicationHistoryDetailsController {

    @FXML private Button okBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField descTextField;
    @FXML private TextField locationTextField;
    @FXML private ListView<Experience> requiredExperienceListView;
    @FXML private ListView<Qualification> requiredQualificationListView;
    @FXML private ListView<Skill> requiredSkillListView;
    @FXML private TextField salaryRangeTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField interviewDateTextField;
    @FXML private TextField interviewTimeTextField;
    @FXML private TextField statusTextField;
    
    private StudentApplicationHistoryDetailsControl control;

    public Button getOkBtn() {
        return okBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public TextField getDescTextField() {
        return descTextField;
    }

    public TextField getLocationTextField() {
        return locationTextField;
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

    public TextField getStatusTextField() {
        return statusTextField;
    }
    
    public void setApplication(Application application) {
        control = new StudentApplicationHistoryDetailsControl(this,application);
        control.setUp();
    }

    public void findCompany(InternPost internPost) {
        control.findCompany(internPost);
    }

}