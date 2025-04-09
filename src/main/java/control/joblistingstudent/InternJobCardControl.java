package control.joblistingstudent;

import boundary.joblistingstudent.InternJobPostDetailsController;
import boundary.joblistingstudent.ApplicationSharedState;
import boundary.joblistingstudent.InternJobCardController;
import com.rttz.assignment.App;
import entity.InternPost;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class InternJobCardControl {
    
    private InternPost internPost;
    private InternJobCardController boundary;
    
    public InternJobCardControl(InternJobCardController boundary, InternPost post) {
        this.boundary = boundary;
        this.internPost = post;
    }
    
    public void setUp(double similarityScore) {
        boundary.getTitleLabel().setText(internPost.getTitle() + " [ " + internPost.getLocation().getState() + " ]" + String.format(" [Similarity: %.2f%%]", similarityScore * 100));
        boundary.getDescriptionLabel().setWrapText(true);
        boundary.getDescriptionLabel().setText(internPost.getDesc());
        boundary.getDetailsButton().setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("JobListingStudent/InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsController controller = fxmlLoader.getController();
                controller.setInternPost(internPost);
                ApplicationSharedState.getInstance().setApplied(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Internship Details");
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
