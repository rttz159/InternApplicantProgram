package boundary.companyapplication;

import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.InternPost;
import entity.Student;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.SimilarityCalculator;

/**
 *
 * @author Raymond
 */
public class ApplicationCardBoundary {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private CheckBox selectedCheckBox;

    @FXML
    private Label titleLabel;

    @FXML
    private HBox parentHBox;

    private Application application;
    
    private Application studApplication;

    private Student applicant;

    private InternPost post;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void setApplication(Application app) {
        this.application = app;
        this.studApplication = MainControlClass.getStudentApplicationMap().get(app.getApplicationId());
        this.post = MainControlClass.getInternPostMap().get(app.getInternPostId());
        this.applicant = MainControlClass.getStudentsIdMap().get(application.getApplicantId());
        setUp();
    }

    private void setUp() {
        this.titleLabel.setText(post.getTitle() + String.format(" [%s] [%s]", application.getStatus().toString(),formatter.format(studApplication.getInterview().getDate())));
        this.descriptionLabel.setText(applicant.getName() + String.format(", Similarity Score: [%.2f]", SimilarityCalculator.calculateSimilarity(applicant, post) * 100));
        this.selectedCheckBox.setSelected(false);
        this.selectedCheckBox.setMouseTransparent(true);
        this.detailsButton.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("companyapplication/ApplicationDetails.fxml"));
            Node node = null;
            ApplicationDetailsBoundary controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            controller.setApplication(application,studApplication);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Applcation Details");
            alert.setHeaderText("");
            alert.getDialogPane().setContent(node);
            alert.getButtonTypes().clear();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setOnCloseRequest(event -> stage.close());
            alert.showAndWait();

        });
    }

    public void setIsSelected() {
        this.selectedCheckBox.setSelected(true); 
        parentHBox.setStyle("-fx-background-color: #F2C873;"); 
        detailsButton.getStyleClass().add("custom_button");
    }

    public void setNotSelected() {
        this.selectedCheckBox.setSelected(false);
        parentHBox.setStyle(""); 
        detailsButton.getStyleClass().remove("custom_button");
    }

}
