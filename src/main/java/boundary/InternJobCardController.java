package boundary;

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

    public void setInternPost(InternPost post, double similarityScore) {
        this.internPost = post;
        setUp(similarityScore);
    }

    private void setUp(double similarityScore) {
        this.titleLabel.setText(internPost.getTitle() + " [ " + internPost.getLocation().getState() + " ]" + String.format(" [Similarity: %.2f%%]", similarityScore * 100));
        this.descriptionLabel.setWrapText(true);
        this.descriptionLabel.setText(internPost.getDesc());
        this.detailsButton.setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsController controller = fxmlLoader.getController();
                controller.setInternPost(internPost);

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
