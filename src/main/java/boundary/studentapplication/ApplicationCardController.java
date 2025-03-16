package boundary.studentapplication;

import com.rttz.assignment.App;
import control.MainControlClass;
import entity.Application;
import entity.InternPost;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author rttz159
 */
public class ApplicationCardController {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private Label titleLabel;

    private Application application;

    private InternPost post;
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void setApplication(Application app) {
        this.application = app;
        this.post = MainControlClass.getInternPostMap().get(app.getInternPostId());
        setUp();
    }

    private void setUp() {
        this.titleLabel.setText(post.getTitle() + String.format(" [%s]", formatter.format(application.getInterview().getDate())) + String.format(" [%s]", post.getLocation().getState()) + String.format(" [%s]", application.getStatus().toString()));
        this.descriptionLabel.setText(post.getDesc());
        this.detailsButton.setOnAction(eh -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentapplication/StudentApplicationHistoryDetails.fxml"));
            Node node = null;
            StudentApplicationHistoryDetailsController controller = null;
            try {
                node = fxmlLoader.load();
                controller = fxmlLoader.getController();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            controller.setApplication(application);
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

}
