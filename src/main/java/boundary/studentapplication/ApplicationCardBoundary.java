package boundary.studentapplication;

import com.rttz.assignment.App;
import control.studentapplication.ApplicationCardControl;
import entity.Application;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class ApplicationCardBoundary {
    @FXML private Label descriptionLabel;
    @FXML private Button detailsButton;
    @FXML private Label titleLabel;

    private ApplicationCardControl control;

    public ApplicationCardBoundary() {
        this.control = new ApplicationCardControl(this);
    }

    public Label getDescriptionLabel() { return descriptionLabel; }
    public Button getDetailsButton() { return detailsButton; }
    public Label getTitleLabel() { return titleLabel; }

    public void showDetailsDialog(Application application) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studentapplication/StudentApplicationHistoryDetails.fxml"));
        Node node = fxmlLoader.load();
        StudentApplicationHistoryDetailsBoundary controller = fxmlLoader.getController();
        controller.setApplication(application);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Application Details");
        alert.getDialogPane().setContent(node);
        alert.getButtonTypes().clear();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> stage.close());
        alert.showAndWait();
    }

    public void initialize() {
        control.initialize();
    }

    public void setApplication(Application application) {
        control.setApplication(application);
    }
}