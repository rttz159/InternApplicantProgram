package boundary.companyapplication;

import dao.MainControlClass;
import entity.Application;
import entity.InternPost;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author rttz159
 */
public class ApplicationDetailsController {

    @FXML
    private Button okBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField salaryRangeTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField interviewDateTextField;

    @FXML
    private TextField interviewTimeTextField;

    @FXML
    private ComboBox<Application.Status> statusComboBox;

    private Application application;

    private Application studApplication;

    private InternPost internPost;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public void setApplication(Application application, Application studApplication) {
        this.application = application;
        this.studApplication = studApplication;
        this.internPost = MainControlClass.getInternPostMap().get(application.getInternPostId());
        this.titleTextField.setEditable(false);
        this.descTextField.setEditable(false);
        this.locationTextField.setEditable(false);
        this.salaryRangeTextField.setEditable(false);
        this.statusComboBox.setEditable(false);
        this.interviewDateTextField.setEditable(false);
        this.interviewTimeTextField.setEditable(false);
        setUpforReadOnly();
        enrichField();
    }

    private void setUpforReadOnly() {
        this.statusComboBox.setDisable(true);
    }

    private void setUpforModify() {
        this.titleTextField.setEditable(true);
        this.descTextField.setEditable(false);
        this.locationTextField.setEditable(false);
        this.salaryRangeTextField.setEditable(false);
    }

    private void enrichField() {
        this.titleTextField.setText(this.internPost.getTitle().toUpperCase());
        this.descTextField.setText(this.internPost.getDesc());
        this.locationTextField.setText("State: " + this.internPost.getLocation().getState() + " , Full Address: " + this.internPost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", this.internPost.getMinMaxSalary().getX(), this.internPost.getMinMaxSalary().getY()));

        statusComboBox.getItems().addAll(Application.Status.values());
        statusComboBox.getSelectionModel().select(this.application.getStatus());

        interviewDateTextField.setText(formatter.format(studApplication.getInterview().getDate()));
        interviewTimeTextField.setText(timeFormatter.format(studApplication.getInterview().getStart_time()));

        okBtn.setOnAction(eh -> {
            ((Stage) okBtn.getScene().getWindow()).close();
        });
    }

}
