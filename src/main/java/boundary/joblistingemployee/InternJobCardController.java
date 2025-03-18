package boundary.joblistingemployee;

import boundary.joblistingstudent.*;
import com.rttz.assignment.App;
import entity.InternPost;
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
 * @author rttz159
 */
public class InternJobCardController {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private Label titleLabel;

    private InternPost internPost;

    public void setInternPost(InternPost post) {
        this.internPost = post;
        setUp();
    }

    private void setUp() {
        this.titleLabel.setText(internPost.getTitle() + " [ " + internPost.getLocation().getState() + " ]" + " [ " + (internPost.getStatus()?"Active":"Archived") + " ]");
        this.descriptionLabel.setWrapText(true);
        this.descriptionLabel.setText(internPost.getDesc());
        this.detailsButton.setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsController controller = fxmlLoader.getController();
                controller.setInternPost(internPost);
                ApplicationSharedState.getInstance().setApplied(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Intern Post Details");
                alert.setHeaderText("");
                alert.getDialogPane().setContent(node);
                alert.getButtonTypes().clear();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> stage.close());
                alert.showAndWait();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

}
