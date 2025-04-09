package boundary.joblistingstudent;

import control.joblistingstudent.InterviewStudentSchedulerControl;
import entity.InternPost;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Raymond
 */
public class InterviewStudentSchedulerBoundary {

    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;
    @FXML private DatePicker interviewSchedulerDatePicker;
    @FXML private Pagination interviewSchedulerPagination;
    @FXML private Label jobtitleTextLabel;
    private ToggleGroup toggleGroup;
    
    private InterviewStudentSchedulerControl control;

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public Button getConfirmBtn() {
        return confirmBtn;
    }

    public DatePicker getInterviewSchedulerDatePicker() {
        return interviewSchedulerDatePicker;
    }

    public Pagination getInterviewSchedulerPagination() {
        return interviewSchedulerPagination;
    }

    public Label getJobtitleTextLabel() {
        return jobtitleTextLabel;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public void setInternPost(InternPost post) {
        control = new InterviewStudentSchedulerControl(this, post);
        toggleGroup = new ToggleGroup();
        control.setUp();
    }

}
