package boundary.joblistingstudent;

import control.joblistingstudent.InternJobPostDetailsControl;
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
 * @author Raymond
 */
public class InternJobPostDetailsController {

    @FXML private Button applyBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField descTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField compnayNameTextField;
    @FXML private ListView<Experience> requiredExperienceListView;
    @FXML private ListView<Qualification> requiredQualificationListView;
    @FXML private ListView<Skill> requiredSkillListView;
    @FXML private TextField salaryRangeTextField;
    @FXML private TextField titleTextField;
    
    private InternJobPostDetailsControl control;

    public Button getApplyBtn() {
        return applyBtn;
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

    public TextField getCompnayNameTextField() {
        return compnayNameTextField;
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

    public void setInternPost(InternPost internpost) {
        control = new InternJobPostDetailsControl(this, internpost);
        control.setUp();
    }

}
